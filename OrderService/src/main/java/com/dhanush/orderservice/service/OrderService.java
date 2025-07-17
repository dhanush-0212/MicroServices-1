package com.dhanush.orderservice.service;

import com.dhanush.orderservice.dto.OrderLineItemsDto;
import com.dhanush.orderservice.dto.OrderRequest;
import com.dhanush.orderservice.model.order;
import com.dhanush.orderservice.model.orderLineItems;
import com.dhanush.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final ModelMapper modelMapper;
    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        order o = new order();
        o.setOrderNumber(UUID.randomUUID().toString());
        o.setOrderlineitems(getOrderLineEntities(orderRequest.getOrderLineitemsDto()));
        orderRepository.save(o);
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
