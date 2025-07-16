package com.dhanush.orderservice.repository;

import com.dhanush.orderservice.model.order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<order, Long> {

}
