package com.to4ilochka.bookspace.dto.auth;

public record RegisterRequest(
        String email,
        String password,
        String firstName,
        String lastName
) {
}