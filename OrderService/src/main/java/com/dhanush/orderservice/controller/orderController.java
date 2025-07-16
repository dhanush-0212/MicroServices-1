package com.dhanush.orderservice.controller;

import com.dhanush.orderservice.dto.OrderRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class orderController {

    @PostMapping("/place")
    public ResponseEntity<String> placeOrder(OrderRequest orderRequest){

        return new ResponseEntity<>("Order Placed", HttpStatus.CREATED);
    }
}
