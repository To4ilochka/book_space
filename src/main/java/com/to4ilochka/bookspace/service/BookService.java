package com.to4ilochka.bookspace.service;

import com.to4ilochka.bookspace.dto.common.PagedResponse;
import com.to4ilochka.bookspace.dto.book.BookDetailResponse;
import com.to4ilochka.bookspace.dto.book.BookShortResponse;
import com.to4ilochka.bookspace.dto.book.CreateBookRequest;
import com.to4ilochka.bookspace.dto.book.UpdateBookRequest;
import com.to4ilochka.bookspace.model.enums.AgeGroup;
import org.springframework.data.domain.Pageable;

public interface BookService {

    PagedResponse<BookShortResponse> getBooks(AgeGroup ageGroup, Pageable pageable);

    PagedResponse<BookShortResponse> getBooksByKeywordAndAgeGroup(String keyword, AgeGroup ageGroup, Pageable pageable);

    BookDetailResponse getBook(Long id);

    BookDetailResponse addBook(CreateBookRequest createBookRequest);

    BookDetailResponse updateBook(Long id, UpdateBookRequest updateBookRequest);

    void delete(Long id);
}
