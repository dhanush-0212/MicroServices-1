package com.dhanush.orderservice.service;

import com.dhanush.orderservice.dto.InventoryResponse;
import com.dhanush.orderservice.dto.OrderLineItemsDto;
import com.dhanush.orderservice.dto.OrderPlacedEvent;
import com.dhanush.orderservice.dto.OrderRequest;
import com.dhanush.orderservice.model.order;
import com.dhanush.orderservice.model.orderLineItems;
import com.dhanush.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
    public void placeOrder(com.dhanush.orderservice.dto.OrderRequest orderRequest) {

        List<OrderLineItemsDto> itemDtos = orderRequest.getOrderLineitemsDto();
        if (itemDtos == null || itemDtos.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item.");
        }

        // Validate all requested quantities > 0
        boolean hasInvalidQty = itemDtos.stream()
                .anyMatch(item -> item.getQuantity() == null || item.getQuantity() <= 0);

        if (hasInvalidQty) {
            throw new IllegalArgumentException("Invalid order: quantity must be greater than 0 for all items.");
        }

        order o = new order();
        o.setOrderNumber(UUID.randomUUID().toString());

        List<orderLineItems> ordercodes = getOrderLineEntities(orderRequest.getOrderLineitemsDto());
        o.setOrderlineitems(ordercodes);

        // Extract and clean order codes
        List<String> codes = o.getOrderlineitems().stream()
                .map(orderLineItems::getOrdercode)
                .filter(Objects::nonNull)
                .filter(code -> !code.isBlank())
                .toList();

        System.out.println("Fetching inventory for codes: " + codes);

        // Call Inventory Service
        InventoryResponse[] result = webClientBuilder.build()
                .get()
                .uri("lb://INVENTORYSERVICE/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("code", codes.toArray()).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        if (result == null) throw new AssertionError("Inventory service returned null");

        // Build sets for validation
        Set<String> expectedCodes = new HashSet<>(codes);
        Set<String> receivedCodes = Arrays.stream(result)
                .map(InventoryResponse::getOrdercode)
                .collect(Collectors.toSet());

        boolean allRequestedCodesReturned = receivedCodes.containsAll(expectedCodes);
        boolean allInStock = Arrays.stream(result).allMatch(InventoryResponse::getIsInStock);

        if (allRequestedCodesReturned && allInStock) {
            orderRepository.save(o);
            kafkaTemplate.send("notificationTopic",new OrderPlacedEvent(o.getOrderNumber()));
            System.out.println("✅ Order placed successfully: " + o.getOrderNumber());
        } else {
            System.err.println("❌ Order failed: Out of stock or invalid codes");
            throw new IllegalArgumentException("Order not placed: some items are out of stock or do not exist.");
        }
    }

    private List<orderLineItems> getOrderLineEntities(List<OrderLineItemsDto> orderLineList) {
        if (orderLineList == null || orderLineList.isEmpty()) {
            throw new IllegalArgumentException("Order line items cannot be null or empty");
        }

        return orderLineList.stream()
                .map(dto -> modelMapper.map(dto, orderLineItems.class))
                .collect(Collectors.toList());
    }
}
