package com.bestseller.coffeestore.model;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "cart_product_toppings")
public class CartProductTopping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "cart_product_id")
    private Long productId;
    @Column(name = "topping_id")
    private Long toppingId;
}
