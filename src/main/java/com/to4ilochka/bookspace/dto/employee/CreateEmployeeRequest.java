package com.to4ilochka.bookspace.dto.employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateEmployeeRequest(
        @NotBlank(message = "{auth.email.notblank}")
        @Email(message = "{auth.email.invalid}")
        String email,

        @NotBlank(message = "{employee.name.notblank}")
        String name,

        @NotBlank(message = "{employee.phone.notblank}")
        String phone,

        @NotNull(message = "{employee.birthdate.notnull}")
        @Past(message = "{employee.birthdate.past}")
        LocalDate birthDate
) {
}