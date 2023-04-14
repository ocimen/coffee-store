package com.bestseller.coffeestore.service;

import com.bestseller.coffeestore.dto.CartDto;
import com.bestseller.coffeestore.dto.CartProductRequest;
import com.bestseller.coffeestore.exception.InvalidRequestException;
import com.bestseller.coffeestore.model.Cart;
import com.bestseller.coffeestore.model.CartProduct;
import com.bestseller.coffeestore.model.Topping;
import com.bestseller.coffeestore.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartProductService {
    private final CartProductRepository cartProductRepository;

    private final ProductRepository productRepository;

    private final ToppingRepository toppingRepository;

    private final CartRepository cartRepository;

    private final PriceService priceService;


    public Cart addProductToCart(CartProductRequest cartProductRequest, Long userId) {
        CartProduct cartProduct = createCartProduct(cartProductRequest);

        Cart cart = new Cart();
        cart.setCartProduct(cartProduct);
        cart.setUserId(userId);
        cart.setPrice(priceService.calculateCartProductPrice(cartProduct));

        return cartRepository.save(cart);
    }

    public CartDto getCartProducts(Long userId) {
        List<Cart> cartProducts = cartRepository.getByUserId(userId);
        CartDto dto = new CartDto();
        dto.setCartItems(cartProducts);
        var totalPrice =priceService.calculateTotalPrice(cartProducts);
        dto.setTotalPrice(totalPrice);
        dto.setDiscountedPrice(priceService.calculateDiscountedPrice(cartProducts, totalPrice));
        return dto;
    }

    public void clearCart(Long userId) {
        List<Cart> cartItems = cartRepository.getByUserId(userId);
        for (Cart item : cartItems) {
            deleteCartProduct(item.getId(), userId);
        }
    }

    public void deleteCartProduct(Long id, Long userId) {
        List<Cart> cartItems = cartRepository.getByIdAndUserId(id, userId);
        cartRepository.deleteByIdAndUserId(id, userId);

        var cartProductIds = cartItems.stream().map(Cart::getId).toList();
        for (Long cartProductId : cartProductIds) {
            cartProductRepository.deleteById(cartProductId);
        }
    }

    public boolean cartProductExists(Long id, Long userId) {
        return cartRepository.existsByIdAndUserId(id, userId);
    }

    private CartProduct createCartProduct(CartProductRequest cartProductRequest) {
        CartProduct cartProduct = new CartProduct();

        if(cartProductRequest.getProductId() != null) {
            productRepository.findById(cartProductRequest.getProductId()).ifPresent(cartProduct::setProduct);
        }
        else {
            log.warn("Request does noy have product, {}", cartProductRequest);
            throw new InvalidRequestException("Request must have valid product");
        }

        List<Long> productToppings = cartProductRequest.getToppingIds();
        List<Topping> toppings = productToppings != null ? toppingRepository.findAllById(productToppings) : null;

        cartProduct.setToppings(toppings);
        return cartProductRepository.save(cartProduct);
    }
}
