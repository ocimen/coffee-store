package com.bestseller.coffeestore.repository;

import com.bestseller.coffeestore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
