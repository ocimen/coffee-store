package com.bestseller.coffeestore.service;

import com.bestseller.coffeestore.dto.ToppingRequest;
import com.bestseller.coffeestore.model.Topping;
import com.bestseller.coffeestore.repository.ToppingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ToppingService {
    private final ToppingRepository toppingRepository;

    public Optional<Topping> getToppingById(Long id) {
        Optional<Topping> topping = toppingRepository.findById(id);
        if(topping.isEmpty()) {
            return Optional.empty();
        }
        return topping;
    }

    public Topping createTopping(ToppingRequest toppingRequest) {
        Topping topping = new Topping();
        topping.setName(toppingRequest.getName());
        topping.setPrice(toppingRequest.getPrice());
        topping =toppingRepository.save(topping);
        log.info("New topping {} is created", topping.getName());
        return topping;
    }

    public List<Topping> getAllToppings() {
        return toppingRepository.findAll();
    }

    public Optional<Topping> updateTopping(ToppingRequest toppingRequest, Long id) {
        Optional<Topping> existingTopping = toppingRepository.findById(id);
        if(existingTopping.isPresent()) {
            existingTopping.get().setName(toppingRequest.getName());
            existingTopping.get().setPrice(toppingRequest.getPrice());
            var updatedTopping = toppingRepository.save(existingTopping.get());
            log.info("Topping {} is updated", updatedTopping);
            return Optional.of(updatedTopping);
        }

        return Optional.empty();
    }

    public void deleteToppingById(Long id) {
        toppingRepository.deleteById(id);
        log.info("Topping with id {} is deleted", id);
    }
}
