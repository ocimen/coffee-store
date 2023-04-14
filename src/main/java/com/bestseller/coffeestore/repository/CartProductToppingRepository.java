package com.bestseller.coffeestore.repository;

import com.bestseller.coffeestore.model.CartProductTopping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartProductToppingRepository extends JpaRepository<CartProductTopping, Long> {
    Optional<CartProductTopping> findById(Long id);
}
