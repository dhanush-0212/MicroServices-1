package com.dhanush.inventoryservice.repository;

import com.dhanush.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepo  extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByOrdercode(String ordercode);

    List<Inventory> findByOrdercodeIn(List<String> ordercode);
}
