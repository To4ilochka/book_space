package com.to4ilochka.bookspace.dto.employee;

import java.time.LocalDate;

public record EmployeeResponse(
        Long id,
        String email,
        String name,
        String phone,
        LocalDate birthDate
) {
}
