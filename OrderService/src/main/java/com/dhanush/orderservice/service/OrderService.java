package com.dhanush.orderservice.service;

import com.dhanush.orderservice.dto.InventoryResponse;
import com.dhanush.orderservice.dto.OrderLineItemsDto;
import com.dhanush.orderservice.dto.OrderRequest;
import com.dhanush.orderservice.model.order;
import com.dhanush.orderservice.model.orderLineItems;
import com.dhanush.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest) {
        order o = new order();
        o.setOrderNumber(UUID.randomUUID().toString());
        List<orderLineItems> ordercodes =getOrderLineEntities(orderRequest.getOrderLineitemsDto());
        o.setOrderlineitems(ordercodes);
        List<String> codes = o.getOrderlineitems().stream()
                .map(orderLineItems::getOrdercode).toList();


        InventoryResponse[] result = webClient.get()
                .uri("http://localhost:8082/api/inventory/",
                        uriBuilder -> uriBuilder.queryParam("codes",codes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                        .block();

        if (result == null) throw new AssertionError();

        boolean allMatch = Arrays.stream(result)
                .allMatch(InventoryResponse::getIsInStock);

        if(allMatch)
            orderRepository.save(o);
        else
            throw new IllegalArgumentException("Order not placed....product is not available or Out of Stock....please try again after some time");
    }

    private List<orderLineItems> getOrderLineEntities(List<OrderLineItemsDto> orderLineList) {
        if(orderLineList == null || orderLineList.isEmpty()){
            throw new IllegalArgumentException("orderLineList Cannot be  null or empty");
        }
        return orderLineList.stream()
                .map(orderLineItemsDto -> modelMapper.map(orderLineList,orderLineItems.class))
                .collect(Collectors.toList());
    }


}
