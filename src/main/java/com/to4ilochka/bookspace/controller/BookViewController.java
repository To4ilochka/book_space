package com.to4ilochka.bookspace.controller;

import com.to4ilochka.bookspace.dto.book.BookShortResponse;
import com.to4ilochka.bookspace.dto.common.PagedResponse;
import com.to4ilochka.bookspace.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/ui/books")
public class BookViewController {

    private final BookService bookService;

    @GetMapping
    public String getBooksPage(@RequestParam(required = false) String keyword,
                               @PageableDefault(size = 12, sort = "name") Pageable pageable,
                               Model model) {
//        PagedResponse<BookShortResponse> response = (keyword != null && !keyword.isBlank())
//                ? bookService.getBooksByKeyword(keyword, pageable)
//                : bookService.getBooks(pageable);
//
//        model.addAttribute("books", response.content());
//        model.addAttribute("currentPage", pageable.getPageNumber());
//        model.addAttribute("totalPages", response.totalPages());
//        model.addAttribute("keyword", keyword);
//
//        String sortString = pageable.getSort().toString().replace(": ", ",").toLowerCase();
//        model.addAttribute("currentSort", sortString);

        return "books/books";
    }
}

