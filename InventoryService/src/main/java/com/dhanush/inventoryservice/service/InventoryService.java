package com.dhanush.inventoryservice.service;

import com.dhanush.inventoryservice.dto.InventoryResponse;
import com.dhanush.inventoryservice.model.Inventory;
import com.dhanush.inventoryservice.repository.InventoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepo inventoryRepo;

    @Transactional(readOnly = true)
    public List<InventoryResponse> IsInStock(List<String> code) {
        System.out.println("Inventory lookup for codes: " + code);

        return inventoryRepo.findByOrdercodeIn(code)
                .stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .ordercode(inventory.getOrdercode())
                                .IsInStock(inventory.getQuantity()>0)
                                .build()
                ).toList();
    }
}
