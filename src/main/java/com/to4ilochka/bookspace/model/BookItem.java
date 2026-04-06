package com.to4ilochka.bookspace.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book_items")
public class BookItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    TODO FetchType.LAZY
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private Order order;
//    TODO FetchType.LAZY
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @Column(nullable = false)
    private Integer quantity;
}
