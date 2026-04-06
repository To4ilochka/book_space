package com.to4ilochka.bookspace.dto.auth;

public record LoginRequest(
        String email,
        String password
) {
}