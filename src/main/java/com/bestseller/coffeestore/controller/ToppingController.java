package com.bestseller.coffeestore.controller;

import com.bestseller.coffeestore.dto.ToppingRequest;
import com.bestseller.coffeestore.model.Topping;
import com.bestseller.coffeestore.service.ToppingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/topping")
@RequiredArgsConstructor
public class ToppingController {

    private final ToppingService toppingService;

    @GetMapping("/{id}")
    public ResponseEntity<Topping> getToppingById(@PathVariable Long id) {
        var topping = toppingService.getToppingById(id);
        return topping.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Topping>> getAllToppings(){
        List<Topping> toppings = toppingService.getAllToppings();
        return new ResponseEntity<>(toppings, HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Topping createTopping(@Valid @RequestBody ToppingRequest toppingRequest) {
        return toppingService.createTopping(toppingRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Topping> updateTopping(@PathVariable("id") Long id,
                                                 @RequestBody ToppingRequest toppingRequest){
        Optional<Topping> updatedTopping = toppingService.updateTopping(toppingRequest, id);
        return updatedTopping.map(topping -> new ResponseEntity<>(topping, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopping(@PathVariable("id") Long id) {
        if(toppingService.getToppingById(id).isPresent()) {
            toppingService.deleteToppingById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
