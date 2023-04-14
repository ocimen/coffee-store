package com.bestseller.coffeestore.repository;

import com.bestseller.coffeestore.model.Topping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToppingRepository extends JpaRepository<Topping, Long> {
}
