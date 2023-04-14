package com.bestseller.coffeestore.repository;

import com.bestseller.coffeestore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
