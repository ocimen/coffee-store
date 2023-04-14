package com.bestseller.coffeestore.service;

import com.bestseller.coffeestore.dto.ProductRequest;
import com.bestseller.coffeestore.model.Product;
import com.bestseller.coffeestore.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public Optional<Product> getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()) {
            return Optional.empty();
        }
        return product;
    }

    public Product createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .build();

        product = productRepository.save(product);
        log.info("New product {} is created", product.getName());
        return product;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> updateProduct(ProductRequest product, Long id) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if(existingProduct.isPresent()) {
            existingProduct.get().setName(product.getName());
            existingProduct.get().setPrice(product.getPrice());
            var updatedProduct= productRepository.save(existingProduct.get());
            log.info("Product {} is updated", updatedProduct);
            return Optional.of(updatedProduct);
        }

        return Optional.empty();
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
        log.info("Product with id {} is deleted", id);
    }
}
