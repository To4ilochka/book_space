package com.to4ilochka.bookspace.dto.book;

import com.to4ilochka.bookspace.model.enums.AgeGroup;
import com.to4ilochka.bookspace.model.enums.Language;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateBookRequest(
        String genre,
        AgeGroup ageGroup,
        BigDecimal price,
        LocalDate publicationDate,
        String author,
        Integer pages,
        String characteristics,
        String description,
        Language language
) {
}
