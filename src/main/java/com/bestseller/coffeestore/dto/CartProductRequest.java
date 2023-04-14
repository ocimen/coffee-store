package com.bestseller.coffeestore.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartProductRequest {
    private Long productId;
    private List<Long> toppingIds;
}
