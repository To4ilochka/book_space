package com.to4ilochka.bookspace.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderRequest(
        @NotEmpty(message = "{order.items.notempty}")
        List<@Valid OrderItemRequest> items
) {
}