package com.to4ilochka.bookspace.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @Column(nullable = false)
    private LocalDateTime orderDate;
    @Column(nullable = false)
    private BigDecimal price;
    @OneToMany(mappedBy = "order")
    private List<BookItem> bookItems;
}
