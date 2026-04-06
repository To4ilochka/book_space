package com.to4ilochka.bookspace.dto.employee;

import java.time.LocalDate;

public record CreateEmployeeRequest(
        String email,
        String password,
        String name,
        String phone,
        LocalDate birthDate
) {
}
