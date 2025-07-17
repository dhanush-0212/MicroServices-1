package com.dhanush.inventoryservice.service;

import com.dhanush.inventoryservice.model.Inventory;
import com.dhanush.inventoryservice.repository.InventoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepo inventoryRepo;

    @Transactional(readOnly = true)
    public boolean IsInStock(String code) {
        return inventoryRepo.findByOrdercode(code).isPresent();
    }
}
