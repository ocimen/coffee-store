package com.bestseller.coffeestore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToppingRequest {
    @NotNull
    @Size(max = 50)
    private String name;
    @NotNull
    private Double price;
}
