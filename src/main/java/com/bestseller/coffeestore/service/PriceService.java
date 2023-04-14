package com.bestseller.coffeestore.service;

import com.bestseller.coffeestore.model.Cart;
import com.bestseller.coffeestore.model.CartProduct;
import com.bestseller.coffeestore.model.Topping;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceService {

    private final static int MINIMUM_TOTAL_PRICE = 12;

    private final static int MINIMUM_ITEM_COUNT_IN_CART = 3;

    private final static double DISCOUNT_PERCENTAGE = 0.25;

    public Double calculateCartProductPrice(CartProduct cartProduct) {
        Double totalPrice = 0.0;
        totalPrice += (cartProduct.getProduct().getPrice());
        totalPrice += (cartProduct.getToppings().stream().mapToDouble(Topping::getPrice).sum());
        return totalPrice;
    }

    public Double calculateTotalPrice(List<Cart> cartItems) {
        return cartItems.stream().mapToDouble(Cart::getPrice).sum();
    }

    public Double calculateDiscountedPrice(List<Cart> cartItems, Double totalPrice) {
        Double moreThanTwelve = checkIfTotalPriceHigherThanTwelve(totalPrice);
        Double moreThanThreeItems = checkIfMoreThanThreeItems(cartItems, totalPrice);
        return moreThanTwelve <= moreThanThreeItems ? moreThanTwelve : moreThanThreeItems;
    }

    private Double checkIfTotalPriceHigherThanTwelve(Double totalPrice) {
        if(totalPrice > MINIMUM_TOTAL_PRICE) {
            return totalPrice - (totalPrice * DISCOUNT_PERCENTAGE);
        }
        return totalPrice;
    }

    private Double checkIfMoreThanThreeItems(List<Cart> cartItems, Double totalPrice) {
        if(cartItems.size() >= MINIMUM_ITEM_COUNT_IN_CART) {
            var min =cartItems.stream().mapToDouble(Cart::getPrice).min();
            return totalPrice - min.getAsDouble();
        }
        return totalPrice;
    }
 }
