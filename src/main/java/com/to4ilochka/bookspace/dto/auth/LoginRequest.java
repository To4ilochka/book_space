package com.to4ilochka.bookspace.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "{auth.email.notblank}")
        @Email(message = "{auth.email.invalid}")
        String email,

        @NotBlank(message = "{auth.password.notblank}")
        String password
) {
}