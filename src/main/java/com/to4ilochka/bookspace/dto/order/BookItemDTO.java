package com.to4ilochka.bookspace.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookItemDTO {
    private String bookName;
    private Integer quantity;
}
