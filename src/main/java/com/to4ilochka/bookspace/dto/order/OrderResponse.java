package com.to4ilochka.bookspace.dto.order;

import com.to4ilochka.bookspace.model.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        Long clientId,
        String clientEmail,
        String employeeEmail,
        LocalDateTime orderDate,
        BigDecimal price,
        OrderStatus status,
        List<OrderItemResponse> bookItems
) {
}
