package com.to4ilochka.bookspace.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(
        @NotNull Long bookId,
        @NotNull @Min(1) Integer quantity
) {
}
