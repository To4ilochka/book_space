package com.to4ilochka.bookspace.dto.client;

import jakarta.validation.constraints.NotBlank;

public record UpdateClientRequest(
        @NotBlank String name
) {
}