package com.dhanush.inventoryservice.controller;

import com.dhanush.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    @GetMapping("/{ordercode}")
    public ResponseEntity<Boolean> isInStock(@PathVariable("ordercode") String code) {

        return ResponseEntity.ok(inventoryService.IsInStock(code));
    }
}
