package com.dhanush.orderservice.service;

import com.dhanush.orderservice.dto.OrderRequest;
import com.dhanush.orderservice.model.order;
import com.dhanush.orderservice.model.orderLineItems;
import com.dhanush.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        order order = new order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<orderLineItems> orderitems = orderRequest.getOrderLineitemsDto()
                .stream()
                .map(orderLineItemsDto -> modelMapper.map(orderLineItemsDto, orderLineItems.class))
                .toList();

        order.setOrderlineitems(orderitems);
        orderRepository.save(order);
    }
}
