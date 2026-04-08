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
        @NotBlank(message = "{book.name.notblank}")
        String name,

        @NotBlank(message = "{book.genre.notblank}")
        String genre,

        @NotNull(message = "{book.agegroup.notnull}")
        AgeGroup ageGroup,

        @NotNull(message = "{book.price.notnull}")
        @Positive(message = "{book.price.positive}")
        BigDecimal price,

        @NotNull(message = "{book.date.notnull}")
        @PastOrPresent(message = "{book.date.past}")
        LocalDate publicationDate,

        @NotBlank(message = "{book.author.notblank}")
        String author,

        @NotNull(message = "{book.pages.notnull}")
        @Positive(message = "{book.pages.positive}")
        Integer pages,

        @Size(max = 2000, message = "{book.characteristics.size}")
        String characteristics,

        @Size(max = 5000, message = "{book.description.size}")
        String description,

        @NotNull(message = "{book.language.notnull}")
        Language language
) {
}