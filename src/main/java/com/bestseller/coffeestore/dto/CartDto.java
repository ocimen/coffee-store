package com.bestseller.coffeestore.dto;

import com.bestseller.coffeestore.model.Cart;
import lombok.Data;

import java.util.List;

@Data
public class CartDto {
    private List<Cart> cartItems;
    private Double totalPrice;
    private Double discountedPrice;
}
