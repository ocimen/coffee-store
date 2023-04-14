package com.bestseller.coffeestore.service;

import com.bestseller.coffeestore.dto.CartDto;
import com.bestseller.coffeestore.model.*;
import com.bestseller.coffeestore.repository.CartProductRepository;
import com.bestseller.coffeestore.repository.OrderDetailRepository;
import com.bestseller.coffeestore.repository.OrderItemRepository;
import com.bestseller.coffeestore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final CartProductService cartProductService;

    private final OrderDetailRepository orderDetailRepository;

    private final CartProductRepository cartProductRepository;

    public Optional<Order> createOrder(Long userId) {
        var cart = cartProductService.getCartProducts(userId);
        if (cart.getCartItems().isEmpty()) {
            log.warn("User's {} cart is empty. Order can not  be created", userId);
            return Optional.empty();
        }

        Order order = saveOrder(cart, userId);
        log.info("User {} created an order", userId);

        var orderItems = saveOrderItems(cart, order);
        log.info("Order {} items are saved", order.getId());

        saveOrderDetail(order, orderItems);

        cartProductService.clearCart(userId);
        log.info("User {}'s cart is cleared", userId);

        return Optional.of(order);
    }

    private Order saveOrder(CartDto cartDto, Long userId) {
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(cartDto.getTotalPrice());
        order.setDiscountedPrice(cartDto.getDiscountedPrice());
        return orderRepository.save(order);
    }

    private List<OrderItem> saveOrderItems(CartDto cart, Order order) {
        var cartProductList = cart.getCartItems();
        List<OrderItem> orderItems = new ArrayList<>();
        for (Cart cartItem : cartProductList) {
            var orderItem = OrderItem.builder()
                    .orderId(order.getId())
                    .price(cartItem.getPrice())
                    .cartProductId(cartItem.getCartProduct().getId())
                    .build();
            orderItem = orderItemRepository.save(orderItem);
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    private void saveOrderDetail(Order order, List<OrderItem> orderItems) {
        var cartProductIdList = orderItems.stream().map(OrderItem::getCartProductId).toList();
        var cartProductMap = cartProductRepository.findAllById(cartProductIdList).stream().collect(Collectors.toMap(CartProduct::getId, Function.identity()));

        for (OrderItem item : orderItems) {
            var cartProduct = cartProductMap.get(item.getCartProductId());
            if (cartProduct.getProduct() != null) {
                var product = cartProduct.getProduct();
                createOrderDetail(order, cartProduct.getId(), "product", product.getName());
            }

            cartProduct.getToppings().forEach(topping -> createOrderDetail(order, cartProduct.getId(), "topping", topping.getName()));
        }
    }

    private OrderDetail createOrderDetail(Order order,
                                          Long cartProductId,
                                          String productType,
                                          String productName) {
        OrderDetail orderDetail = OrderDetail.builder()
                .orderId(order.getId())
                .productType(productType)
                .productId(cartProductId)
                .productName(productName)
                .build();
        return orderDetailRepository.save(orderDetail);
    }
}
