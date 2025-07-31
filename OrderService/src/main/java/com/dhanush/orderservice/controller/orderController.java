package com.dhanush.orderservice.controller;

import com.dhanush.orderservice.dto.OrderRequest;
import com.dhanush.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class orderController {

    private final OrderService orderService;

    @PostMapping("/place")
    @CircuitBreaker(name = "inventory" , fallbackMethod = "fallbackmethod")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest){

        orderService.placeOrder(orderRequest);
        return new ResponseEntity<>("Order placed", HttpStatus.CREATED);
    }
    public ResponseEntity<String> fallbackmethod(@RequestBody OrderRequest orderRequest,RuntimeException runtimeException){
        return new ResponseEntity<>("oops something wrong try after sometime", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
