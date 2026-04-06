package com.to4ilochka.bookspace.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private String clientEmail;
    private String employeeEmail;
    private LocalDateTime orderDate;
    private BigDecimal price;
    private List<BookItemDTO> bookItems = new ArrayList<>();
}
