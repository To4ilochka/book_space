package com.to4ilochka.bookspace.dto.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateEmployeeRequest(
        @NotBlank(message = "{employee.name.notblank}")
        String name,

        @NotBlank(message = "{employee.phone.notblank}")
        @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$", message = "{employee.phone.pattern}")
        String phone
) {
}