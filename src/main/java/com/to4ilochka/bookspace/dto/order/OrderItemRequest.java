package com.to4ilochka.bookspace.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(
        @NotNull(message = "{order.bookid.notnull}")
        Long bookId,

        @NotNull(message = "{order.quantity.notnull}")
        @Min(value = 1, message = "{order.quantity.min}")
        Integer quantity
) {
}