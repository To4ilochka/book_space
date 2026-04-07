package com.to4ilochka.bookspace.dto.order;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderRequest(
        @NotEmpty List<OrderItemRequest> items
) {
}
