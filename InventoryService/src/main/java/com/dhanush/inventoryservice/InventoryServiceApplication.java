package com.dhanush.inventoryservice;

import com.dhanush.inventoryservice.model.Inventory;
import com.dhanush.inventoryservice.repository.InventoryRepo;
import com.dhanush.inventoryservice.service.InventoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    //@Bean
//    public CommandLineRunner init(InventoryRepo inventoryRepo) {
//        return (args) -> {
//            Inventory inventory = new Inventory();
//            inventory.setOrdercode("iphone_13");
//            inventory.setQuantity(3);
//
//            Inventory inventory2 = new Inventory();
//            inventory2.setOrdercode("iphone_12");
//            inventory2.setQuantity(3);
//
//            inventoryRepo.save(inventory);
//            inventoryRepo.save(inventory2);
//        };
//    }
}
