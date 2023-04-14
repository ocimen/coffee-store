package com.bestseller.coffeestore.controller;

import com.bestseller.coffeestore.dto.CartDto;
import com.bestseller.coffeestore.dto.CartProductRequest;
import com.bestseller.coffeestore.model.Cart;
import com.bestseller.coffeestore.service.CartProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RequestMapping("/api/cart")
@RestController
@RequiredArgsConstructor
public class CartController {
    private final CartProductService cartProductService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Cart addItemToCart(@RequestParam Long userId, @RequestBody CartProductRequest addRequest) {
        return cartProductService.addProductToCart(addRequest, userId);
    }

    @GetMapping
    public CartDto getCartProducts(@RequestParam Long userId) {
        return cartProductService.getCartProducts(userId);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartProduct(@PathVariable Long id, @RequestParam Long userId) {
        if(cartProductService.cartProductExists(id, userId)) {
            cartProductService.deleteCartProduct(id, userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Transactional
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@RequestParam Long userId) {
        cartProductService.clearCart(userId);
    }
}
