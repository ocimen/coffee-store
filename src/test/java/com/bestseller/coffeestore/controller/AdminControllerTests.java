package com.bestseller.coffeestore.controller;

import com.bestseller.coffeestore.model.OrderDetail;
import com.bestseller.coffeestore.repository.OrderDetailRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AdminControllerTests {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        orderDetailRepository.save(OrderDetail.builder().orderId(1L).productId(1L).productType("drink").productName("Black Coffee").build());
        orderDetailRepository.save(OrderDetail.builder().orderId(1L).productId(1L).productType("topping").productName("Milk").build());
        orderDetailRepository.save(OrderDetail.builder().orderId(1L).productId(2L).productType("topping").productName("Hazelnut syrup").build());
        orderDetailRepository.save(OrderDetail.builder().orderId(2L).productId(2L).productType("drink").productName("Latte").build());
        orderDetailRepository.save(OrderDetail.builder().orderId(2L).productId(1L).productType("topping").productName("Milk").build());
        orderDetailRepository.save(OrderDetail.builder().orderId(2L).productId(3L).productType("topping").productName("Chocolate syrup").build());
        orderDetailRepository.save(OrderDetail.builder().orderId(3L).productId(1L).productType("drink").productName("Black Coffee").build());
        orderDetailRepository.save(OrderDetail.builder().orderId(3L).productId(1L).productType("topping").productName("Milk").build());
        orderDetailRepository.save(OrderDetail.builder().orderId(3L).productId(3L).productType("topping").productName("Chocolate syrup").build());
    }

    @AfterEach
    public void tearDown() {
        orderDetailRepository.deleteAll();
    }

    @Test
    void testGetMostUsedToppings() throws Exception {
        mockMvc.perform(get("/api/admin/toppingusage"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].usageCount", is(3)))
                .andExpect(jsonPath("$[0].toppingName", is("Milk")))
                .andExpect(jsonPath("$[1].usageCount", is(2)))
                .andExpect(jsonPath("$[1].toppingName", is("Chocolate syrup")))
                .andExpect(jsonPath("$[2].usageCount", is(1)))
                .andExpect(jsonPath("$[2].toppingName", is("Hazelnut syrup")));
    }
}
