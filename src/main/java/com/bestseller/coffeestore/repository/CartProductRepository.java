package com.bestseller.coffeestore.repository;

import com.bestseller.coffeestore.model.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
    Optional<CartProduct> findById(Long userId);
}
