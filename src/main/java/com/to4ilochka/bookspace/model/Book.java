package com.to4ilochka.bookspace.model;

import com.to4ilochka.bookspace.model.enums.AgeGroup;
import com.to4ilochka.bookspace.model.enums.Language;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, updatable = false)
    private String name;
    @Column(nullable = false)
    private String genre;
    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(name = "publication_year", nullable = false)
    private LocalDate publicationDate;
    @Column(nullable = false)
    private String author;
    @Column(name = "number_of_pages", nullable = false)
    private Integer pages;
    @Column(nullable = false)
    private String characteristics;
    @Column()
    private String description;
    @Enumerated(EnumType.STRING)
    private Language language;
}
