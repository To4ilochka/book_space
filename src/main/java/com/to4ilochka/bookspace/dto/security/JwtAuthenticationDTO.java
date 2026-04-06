package com.to4ilochka.bookspace.dto.security;

public record JwtAuthenticationDTO(
        String token,
        String refreshToken
) {
}
