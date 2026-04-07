package com.to4ilochka.bookspace.dto.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateEmployeeRequest(
        @NotBlank String name,
        @NotBlank @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$") String phone
) {
}