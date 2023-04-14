package com.bestseller.coffeestore.controller;

import com.bestseller.coffeestore.model.Product;
import com.bestseller.coffeestore.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
class ProductControllerTests {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MockMvc mockMvc;

    private Product blackCoffee, latte, mocha, tea;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        blackCoffee = productRepository.save(Product.builder().name("Black Coffee").price(4.0).build());
        latte = productRepository.save(Product.builder().name("Latte").price(5.0).build());
        mocha = productRepository.save(Product.builder().name("Mocha").price(6.0).build());
        tea = productRepository.save(Product.builder().name("Tea").price(3.0).build());
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    void testGetProductById() throws Exception {
        mockMvc.perform(get("/api/admin/product/{id}", latte.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(latte.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Latte")))
                .andExpect(jsonPath("$.price", is(5.0)));
    }

    @Test
    void testGetProductByIdNotFound() throws Exception {
        productRepository.deleteById(mocha.getId());
        mockMvc.perform(get("/api/admin/product/{id}", mocha.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllDrinks() throws Exception {
        mockMvc.perform(get("/api/admin/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$.[0].name", is("Black Coffee")))
                .andExpect(jsonPath("$.[0].price", is(4.0)))
                .andExpect(jsonPath("$.[1].name", is("Latte")))
                .andExpect(jsonPath("$.[1].price", is(5.0)))
                .andExpect(jsonPath("$.[2].name", is("Mocha")))
                .andExpect(jsonPath("$.[2].price", is(6.0)))
                .andExpect(jsonPath("$.[3].name", is("Tea")))
                .andExpect(jsonPath("$.[3].price", is(3.0)));
    }


    @Test
    void testCreateProduct() throws Exception {
        mockMvc.perform(post("/api/admin/product")
                                .content("""
                        {
                            "name": "Ice Mocha",
                            "price": 6
                        }""")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Ice Mocha")))
                .andExpect(jsonPath("$.price", is(6.0)));
    }

    @Test
    void testCreateProductValidation() throws Exception {
        mockMvc.perform(post("/api/admin/product")
                                .content("""
                        {
                            "price": 2
                        }""")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateProduct() throws Exception {
        mockMvc.perform(put("/api/admin/product/{id}", tea.getId())
                                .content("""
                        {
                            "name": "Ice Tea",
                            "price": 3
                        }""")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Ice Tea")))
                .andExpect(jsonPath("$.price", is(3.0)));
    }

    @Test
    void testUpdateProductNotFound() throws Exception {
        productRepository.deleteById(mocha.getId());
        mockMvc.perform(put("/api/admin/product/{id}", mocha.getId())
                                .content("""
                        {
                            "name": "Ice Mocha"
                        }""")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteDrink() throws Exception {
        mockMvc.perform(delete("/api/admin/product/{id}", blackCoffee.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteDrinkNotFound() throws Exception {
        productRepository.deleteById(latte.getId());
        mockMvc.perform(delete("/api/admin/product/{id}", latte.getId()))
                .andExpect(status().isNotFound());
    }
}