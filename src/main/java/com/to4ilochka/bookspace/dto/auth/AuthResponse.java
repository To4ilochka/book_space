package com.to4ilochka.bookspace.dto.auth;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}
