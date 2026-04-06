package com.to4ilochka.bookspace.dto.book;

import com.to4ilochka.bookspace.model.enums.AgeGroup;
import com.to4ilochka.bookspace.model.enums.Language;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BookShortResponse(
        String name,
        String genre,
        AgeGroup ageGroup,
        BigDecimal price,
        LocalDate publicationDate,
        String author,
        Language language
) {
}
