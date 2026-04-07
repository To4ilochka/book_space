package com.to4ilochka.bookspace.dto.book;

import com.to4ilochka.bookspace.model.enums.AgeGroup;
import com.to4ilochka.bookspace.model.enums.Language;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateBookRequest(
        @NotBlank String name,
        @NotBlank String genre,
        @NotNull AgeGroup ageGroup,
        @NotNull @Positive BigDecimal price,
        @NotNull @PastOrPresent LocalDate publicationDate,
        @NotBlank String author,
        @NotNull @Positive Integer pages,
        @Size(max = 2000) String characteristics,
        @Size(max = 5000) String description,
        @NotNull Language language
) {
}