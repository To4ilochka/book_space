package com.to4ilochka.bookspace.security;

import com.to4ilochka.bookspace.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("orderSecurity")
public class OrderSecurity {

    private final OrderRepository orderRepository;

    public boolean isOwner(Long orderId, Long userId) {
        return orderRepository.findById(orderId)
                .map(order -> order.getClient().getId().equals(userId))
                .orElse(false);
    }
}
