package com.to4ilochka.bookspace.controller;

import com.to4ilochka.bookspace.dto.common.PagedResponse;
import com.to4ilochka.bookspace.dto.book.BookDetailResponse;
import com.to4ilochka.bookspace.dto.book.BookShortResponse;
import com.to4ilochka.bookspace.dto.book.CreateBookRequest;
import com.to4ilochka.bookspace.dto.book.UpdateBookRequest;
import com.to4ilochka.bookspace.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BookController {

    private final BookService bookService;

    @GetMapping("/books")
    public PagedResponse<BookShortResponse> findBooks(@RequestParam(required = false) String keyword,
                                                      @PageableDefault(size = 6, page = 0,
                                                              sort = {"name", "author", "price"},
                                                              direction = Sort.Direction.DESC)
                                                      Pageable pageable) {
        if (keyword != null && !keyword.isBlank()) {
            return bookService.getBooksByKeyword(keyword, pageable);
        }
        return bookService.getBooks(pageable);
    }

    @GetMapping("/books/{id}")
    public BookDetailResponse getBookById(@PathVariable Long id) {
        return bookService.getBook(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/books")
    public BookDetailResponse addBook(@RequestBody @Valid CreateBookRequest book) {
        return bookService.addBook(book);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PutMapping("/books/{id}")
    public BookDetailResponse updateBook(@PathVariable Long id,
                                         @RequestBody @Valid UpdateBookRequest book) {
        return bookService.updateBook(id, book);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }
}
