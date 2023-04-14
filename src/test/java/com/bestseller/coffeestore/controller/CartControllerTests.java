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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class CartControllerTests {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ToppingRepository toppingRepository;
    @Autowired
    private CartProductRepository cartProductRepository;
    @Autowired
    private CartProductToppingRepository cartProductToppingRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private MockMvc mockMvc;

    private Product blackCoffee, latte, mocha, tea;
    private Topping milk, hazelnut, chocolate, lemon;
    private CartProduct latteWithHazelnut, teaWithLemon;
    private Cart cartItem1, cartItem2;

    private static Long firstUserId = 1L;

    @BeforeEach
    public void setUpBeforeEach() {
        productRepository.deleteAll();
        toppingRepository.deleteAll();

        blackCoffee = productRepository.save(Product.builder().name("Black Coffee").price(4.0).build());
        latte = productRepository.save(Product.builder().name("Latte").price(5.0).build());
        mocha = productRepository.save(Product.builder().name("Mocha").price(6.0).build());
        tea = productRepository.save(Product.builder().name("Tea").price(3.0).build());

        milk = toppingRepository.save(Topping.builder().name("Milk").price(2.0).build());
        hazelnut = toppingRepository.save(Topping.builder().name("Hazelnut syrup").price(3.0).build());
        chocolate = toppingRepository.save(Topping.builder().name("Chocolate sauce").price(5.0).build());
        lemon = toppingRepository.save(Topping.builder().name("Lemon").price(2.0).build());

        latteWithHazelnut = cartProductRepository.save(CartProduct.builder().product(latte).toppings(List.of(hazelnut)).build());
        teaWithLemon = cartProductRepository.save(CartProduct.builder().product(tea).toppings(List.of(lemon)).build());

        cartItem1 = cartRepository.save(Cart.builder().userId(firstUserId).price(8.0).cartProduct(latteWithHazelnut).build());
        cartItem2 = cartRepository.save(Cart.builder().userId(firstUserId).price(5.0).cartProduct(teaWithLemon).build());
    }

    @AfterEach
    public void tearDown() {
        cartRepository.deleteAll();
        cartProductRepository.deleteAll();
        productRepository.deleteAll();
        toppingRepository.deleteAll();
    }

    @Test
    void testGetAllCartItems() throws Exception {
        mockMvc.perform(get("/api/cart")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice", is(13.0)))
                .andExpect(jsonPath("$.discountedPrice", is(9.75)))
                .andExpect(jsonPath("$.cartItems[0].userId", is(1)))
                .andExpect(jsonPath("$.cartItems[0].price", is(8.0)))
                .andExpect(jsonPath("$.cartItems[0].cartProduct.product.name", is("Latte")))
                .andExpect(jsonPath("$.cartItems[0].cartProduct.toppings[0].name", is("Hazelnut syrup")))
                .andExpect(jsonPath("$.cartItems[1].userId", is(1)))
                .andExpect(jsonPath("$.cartItems[1].price", is(5.0)))
                .andExpect(jsonPath("$.cartItems[1].cartProduct.product.name", is("Tea")))
                .andExpect(jsonPath("$.cartItems[1].cartProduct.toppings[0].name", is("Lemon")));
    }

    @Test
    void testAddCartItem() throws Exception {
        mockMvc.perform(post("/api/cart")
                        .param("userId", "1")
                        .content("{\n" +
                                "\"productId\": " + blackCoffee.getId() + ",\n" +
                                "\"toppingIds\": [" + milk.getId() + "]\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId", is(1)))
                .andExpect(jsonPath("$.price", is(6.0)))
                .andExpect(jsonPath("$.cartProduct.product.name", is("Black Coffee")))
                .andExpect(jsonPath("$.cartProduct.product.price", is(4.0)))
                .andExpect(jsonPath("$.cartProduct.toppings[0].name", is("Milk")))
                .andExpect(jsonPath("$.cartProduct.toppings[0].price", is(2.0)));
    }

    @Test
    void testAddCartItemEmptyRequest() throws Exception {
        mockMvc.perform(post("/api/cart")
                        .param("userId", "1")
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteCartItem() throws Exception {
        mockMvc.perform(delete("/api/cart/" + cartItem1.getId())
                        .param("userId", "1")
                )
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteCartItemNotFound() throws Exception {
        cartRepository.deleteById(cartItem2.getId());
        mockMvc.perform(delete("/api/cart/" + cartItem2.getId())
                        .param("userId", "1")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void testClearCart() throws Exception {
        mockMvc.perform(delete("/api/cart")
                        .param("userId", "1")
                )
                .andExpect(status().isNoContent());
    }
}
