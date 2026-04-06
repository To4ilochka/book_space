package com.to4ilochka.bookspace.dto.common;

import java.util.List;

public record PagedResponse<T>(
        List<T> content,
        long totalElements,
        int totalPages,
        int number,
        int size,
        boolean first,
        boolean last
) {
}