package com.bestseller.coffeestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan( basePackages = {"com.bestseller.coffeestore.model"} )
public class CoffeeStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoffeeStoreApplication.class, args);
    }
}
