package com.to4ilochka.bookspace.dto.order;

public record OrderItemResponse(
        Long bookId,
        String bookName,
        Integer quantity
) {
}
