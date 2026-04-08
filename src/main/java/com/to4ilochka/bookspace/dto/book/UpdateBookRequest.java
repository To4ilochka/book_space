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

        @Positive(message = "{book.price.positive}")
        BigDecimal price,

        @PastOrPresent(message = "{book.date.past}")
        LocalDate publicationDate,

        String author,

        @Positive(message = "{book.pages.positive}")
        Integer pages,

        @Size(max = 2000, message = "{book.characteristics.size}")
        String characteristics,

        @Size(max = 5000, message = "{book.description.size}")
        String description,

        Language language
) {
}