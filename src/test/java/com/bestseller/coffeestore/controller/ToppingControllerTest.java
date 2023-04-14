package com.bestseller.coffeestore.controller;

import com.bestseller.coffeestore.model.Topping;
import com.bestseller.coffeestore.repository.ToppingRepository;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class ToppingControllerTest {
    @Autowired
    private ToppingRepository toppingsRepository;
    @Autowired
    private MockMvc mockMvc;

    private Topping milk, hazelnut, chocolate, lemon;

    @BeforeEach
    public void setUp() {
        toppingsRepository.deleteAll();
        milk = toppingsRepository.save(Topping.builder().name("Milk").price(2.0).build());
        hazelnut = toppingsRepository.save(Topping.builder().name("Hazelnut syrup").price(3.0).build());
        chocolate = toppingsRepository.save(Topping.builder().name("Chocolate sauce").price(5.0).build());
        lemon = toppingsRepository.save(Topping.builder().name("Lemon").price(2.0).build());
    }

    @AfterEach
    public void tearDown() {
        toppingsRepository.deleteAll();
    }

    @Test
    void testGetToppingById() throws Exception {
        mockMvc.perform(get("/api/admin/topping/{id}", hazelnut.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(hazelnut.getId().intValue())))
                .andExpect(jsonPath("$.name", is("Hazelnut syrup")))
                .andExpect(jsonPath("$.price", is(3.0)));
    }

    @Test
    void testGetToppingByIdNotFound() throws Exception {
        toppingsRepository.deleteById(chocolate.getId());
        mockMvc.perform(get("/api/admin/topping/{id}", chocolate.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllToppings() throws Exception {
        mockMvc.perform(get("/api/admin/topping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$.[0].name", is("Milk")))
                .andExpect(jsonPath("$.[0].price", is(2.0)))
                .andExpect(jsonPath("$.[1].name", is("Hazelnut syrup")))
                .andExpect(jsonPath("$.[1].price", is(3.0)))
                .andExpect(jsonPath("$.[2].name", is("Chocolate sauce")))
                .andExpect(jsonPath("$.[2].price", is(5.0)))
                .andExpect(jsonPath("$.[3].name", is("Lemon")))
                .andExpect(jsonPath("$.[3].price", is(2.0)));
    }

    @Test
    void testCreateTopping() throws Exception {
        mockMvc.perform(post("/api/admin/topping")
                                .content("""
                        {
                            "name": "Caramel",
                            "price": 4
                        }""")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Caramel")))
                .andExpect(jsonPath("$.price", is(4.0)));
    }

    @Test
    void testCreateToppingValidation() throws Exception {
        mockMvc.perform(post("/api/admin/topping")
                                .content("""
                        {
                            "name": "New Topping"
                        }""")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateTopping() throws Exception {
        mockMvc.perform(put("/api/admin/topping/{id}", milk.getId())
                                .content("""
                        {
                            "name": "Barista Milk",
                            "price": 3
                        }""")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Barista Milk")))
                .andExpect(jsonPath("$.price", is(3.0)));
    }

    @Test
    void testUpdateToppingNotFound() throws Exception {
        toppingsRepository.deleteById(milk.getId());
        mockMvc.perform(put("/api/admin/topping/{id}", milk.getId())
                                .content("""
                        {
                            "name": "Barista Milk",
                            "price": 3
                        }""")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteTopping() throws Exception {
        mockMvc.perform(delete("/api/admin/topping/{id}", lemon.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteToppingNotFound() throws Exception {
        toppingsRepository.deleteById(milk.getId());
        mockMvc.perform(delete("/api/admin/topping/{id}", milk.getId()))
                .andExpect(status().isNotFound());
    }
}
