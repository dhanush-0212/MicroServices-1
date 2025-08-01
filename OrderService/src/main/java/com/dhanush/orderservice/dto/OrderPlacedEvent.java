package com.dhanush.orderservice.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlacedEvent {
    public String orderNumber;
}
