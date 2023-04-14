package com.bestseller.coffeestore.repository;

import com.bestseller.coffeestore.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
