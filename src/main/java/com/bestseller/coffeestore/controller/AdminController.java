package com.bestseller.coffeestore.controller;

import com.bestseller.coffeestore.dto.ToppingUsageDto;
import com.bestseller.coffeestore.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("toppingusage")
    @ResponseStatus(HttpStatus.OK)
    public List<ToppingUsageDto> getToppingUsage() {
        return adminService.getToppingUsage();
    }
}
