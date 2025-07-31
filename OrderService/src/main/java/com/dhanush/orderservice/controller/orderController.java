package com.dhanush.orderservice.controller;

import com.dhanush.orderservice.dto.OrderRequest;
import com.dhanush.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class orderController {

    private final OrderService orderService;

    @PostMapping("/place")
    @CircuitBreaker(name = "inventory" , fallbackMethod = "fallbackmethod")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest){
        try {
            orderService.placeOrder(orderRequest);
            return new ResponseEntity<>("✅ Order placed", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Business logic error
            return new ResponseEntity<>("❌ " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Unexpected failure — let fallback handle it
            throw e;
        }
    }

    public ResponseEntity<String> fallbackmethod(OrderRequest orderRequest, RuntimeException ex) {
        log.error("⚠️ Fallback triggered due to inventory service failure", ex);
        return new ResponseEntity<>("⚠️ Inventory service is down. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE);
    }

}
