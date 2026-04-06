package com.to4ilochka.bookspace.controller;

import com.to4ilochka.bookspace.dto.common.PagedResponse;
import com.to4ilochka.bookspace.dto.book.BookDetailResponse;
import com.to4ilochka.bookspace.dto.book.BookShortResponse;
import com.to4ilochka.bookspace.dto.book.CreateBookRequest;
import com.to4ilochka.bookspace.dto.book.UpdateBookRequest;
import com.to4ilochka.bookspace.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public BookDetailResponse getBookByName(@PathVariable Long id) {
        return bookService.getBook(id);
    }

    @PostMapping("/books")
    public BookDetailResponse addBook(@RequestBody CreateBookRequest book) {
        return bookService.addBook(book);
    }

    @PutMapping("/books/{id}")
    public BookDetailResponse updateBook(@PathVariable Long id,
                                         @RequestBody UpdateBookRequest book) {
        return bookService.updateBook(id, book);
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }

}
