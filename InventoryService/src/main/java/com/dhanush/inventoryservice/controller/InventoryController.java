package com.dhanush.inventoryservice.controller;

import com.dhanush.inventoryservice.dto.InventoryResponse;
import com.dhanush.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    @GetMapping
    public ResponseEntity<List<InventoryResponse>> isInStock(@RequestParam List<String> code) {

        return ResponseEntity.ok(inventoryService.IsInStock(code));
    }
}
