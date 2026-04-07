package com.to4ilochka.bookspace.dto.book;

import com.to4ilochka.bookspace.model.enums.AgeGroup;
import com.to4ilochka.bookspace.model.enums.Language;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateBookRequest(
        String genre,
        AgeGroup ageGroup,
        @Positive BigDecimal price,
        @PastOrPresent LocalDate publicationDate,
        String author,
        @Positive Integer pages,
        @Size(max = 2000) String characteristics,
        @Size(max = 5000) String description,
        Language language
) {
}