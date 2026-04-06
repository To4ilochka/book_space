package com.to4ilochka.bookspace.dto.client;

import java.math.BigDecimal;

public record CreateClientRequest(
        String email,
        String password,
        String name,
        BigDecimal balance
) {
}
