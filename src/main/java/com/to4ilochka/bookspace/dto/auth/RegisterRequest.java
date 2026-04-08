package com.to4ilochka.bookspace.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "{auth.email.notblank}")
        @Email(message = "{auth.email.invalid}")
        String email,

        @NotBlank(message = "{auth.password.notblank}")
        @Size(min = 6, message = "{auth.password.size}")
        String password,

        @NotBlank(message = "{auth.name.notblank}")
        String name
) {
}