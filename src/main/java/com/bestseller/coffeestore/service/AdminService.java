package com.bestseller.coffeestore.service;

import com.bestseller.coffeestore.dto.ToppingUsageDto;
import com.bestseller.coffeestore.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final OrderDetailRepository orderDetailRepository;

    public List<ToppingUsageDto> getToppingUsage() {
        return orderDetailRepository.getToppingUsage();
    }
}
