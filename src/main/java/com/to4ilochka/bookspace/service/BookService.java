package com.to4ilochka.bookspace.service;

import com.to4ilochka.bookspace.dto.common.PagedResponse;
import com.to4ilochka.bookspace.dto.book.BookDetailResponse;
import com.to4ilochka.bookspace.dto.book.BookShortResponse;
import com.to4ilochka.bookspace.dto.book.CreateBookRequest;
import com.to4ilochka.bookspace.dto.book.UpdateBookRequest;
import org.springframework.data.domain.Pageable;

public interface BookService {

    PagedResponse<BookShortResponse> getBooks(Pageable pageable);

    PagedResponse<BookShortResponse> getBooksByKeyword(String keyword, Pageable pageable);

    BookDetailResponse getBook(Long id);

    BookDetailResponse addBook(CreateBookRequest createBookRequest);

    BookDetailResponse updateBook(Long id, UpdateBookRequest updateBookRequest);

    void delete(Long id);
}
