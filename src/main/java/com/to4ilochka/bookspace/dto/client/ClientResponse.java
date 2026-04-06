package com.to4ilochka.bookspace.dto.client;

import java.math.BigDecimal;

public record ClientResponse(
        Long id,
        String email,
        String name,
        BigDecimal balance
) {
}