package com.to4ilochka.bookspace.service.impl;

import com.to4ilochka.bookspace.dto.book.BookDetailResponse;
import com.to4ilochka.bookspace.dto.book.BookShortResponse;
import com.to4ilochka.bookspace.dto.book.CreateBookRequest;
import com.to4ilochka.bookspace.dto.book.UpdateBookRequest;
import com.to4ilochka.bookspace.dto.common.PagedResponse;
import com.to4ilochka.bookspace.exception.ResourceNotFoundException;
import com.to4ilochka.bookspace.mapper.BookMapper;
import com.to4ilochka.bookspace.model.Book;
import com.to4ilochka.bookspace.repo.BookRepository;
import com.to4ilochka.bookspace.service.BookService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public PagedResponse<BookShortResponse> getBooks(Pageable pageable) {
        return bookMapper.toPagedResponse(bookRepository.findAll(pageable));
    }

    @Override
    public PagedResponse<BookShortResponse> getBooksByKeyword(String keyword, Pageable pageable) {
        return bookMapper.toPagedResponse(bookRepository.searchByKeyword(keyword, pageable));
    }

    @Override
    public BookDetailResponse getBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + id));

        return bookMapper.toDetailResponse(book);
    }

    @Transactional
    @Override
    public BookDetailResponse addBook(CreateBookRequest createBookRequest) {
        if (bookRepository.existsByName(createBookRequest.name())) {
            throw new EntityExistsException("Book with id " + createBookRequest.name() + " already exists");
        }

        Book book = bookRepository.save(bookMapper.toEntity(createBookRequest));
        return bookMapper.toDetailResponse(book);
    }

    @Transactional
    @Override
    public BookDetailResponse updateBook(Long id, UpdateBookRequest updateBookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + id));

//        Dirty Checking of @Transactional (without save())
        bookMapper.updateEntityFromDto(updateBookRequest, book);
        return bookMapper.toDetailResponse(book);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found: " + id);
        }
        bookRepository.deleteById(id);
    }
}