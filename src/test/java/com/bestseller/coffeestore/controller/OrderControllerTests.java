package com.bestseller.coffeestore.controller;

import com.bestseller.coffeestore.model.Cart;
import com.bestseller.coffeestore.model.CartProduct;
import com.bestseller.coffeestore.model.Product;
import com.bestseller.coffeestore.model.Topping;
import com.bestseller.coffeestore.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class OrderControllerTests {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ToppingRepository toppingRepository;
    @Autowired
    private CartProductRepository cartProductRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    public void setUp() {
        productRepository.deleteAll();
        toppingRepository.deleteAll();

        Product latte = productRepository.save(Product.builder().name("Latte").price(5.0).build());
        Product tea = productRepository.save(Product.builder().name("Tea").price(3.0).build());

        Topping hazelnut = toppingRepository.save(Topping.builder().name("Hazelnut syrup").price(3.0).build());
        Topping lemon = toppingRepository.save(Topping.builder().name("Lemon").price(2.0).build());

        CartProduct latteWithHazelnut = cartProductRepository.save(CartProduct.builder().product(latte).toppings(List.of(hazelnut)).build());
        CartProduct teaWithLemon = cartProductRepository.save(CartProduct.builder().product(tea).toppings(List.of(lemon)).build());

        cartRepository.save(Cart.builder().userId(1L).price(8.0).cartProduct(latteWithHazelnut).build());
        cartRepository.save(Cart.builder().userId(1L).price(5.0).cartProduct(teaWithLemon).build());
    }

    @AfterEach
    public void tearDown() {
        orderDetailRepository.deleteAll();
        orderRepository.deleteAll();
        orderItemRepository.deleteAll();
        cartRepository.deleteAll();
        cartProductRepository.deleteAll();
        productRepository.deleteAll();
        toppingRepository.deleteAll();
    }

    @Test
    void testPlaceOrder() throws Exception {
        mockMvc.perform(post("/api/order")
                        .param("userId", "1")
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.totalPrice", is(13.0)))
                .andExpect(jsonPath("$.discountedPrice", is(9.75)));
    }

    @Test
    void testPlaceOrderNoItemsInTheCart() throws Exception {
        cartRepository.deleteAll();
        mockMvc.perform(post("/api/order")
                        .param("userId", "1")
                )
                .andExpect(status().isBadRequest());
    }
}
