package com.to4ilochka.bookspace.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequest(
        @NotBlank(message = "{auth.token.notblank}")
        String refreshToken
) {
}