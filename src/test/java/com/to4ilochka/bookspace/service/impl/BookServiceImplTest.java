package com.to4ilochka.bookspace.service.impl;

import com.to4ilochka.bookspace.dto.book.BookDetailResponse;
import com.to4ilochka.bookspace.dto.book.BookShortResponse;
import com.to4ilochka.bookspace.dto.book.CreateBookRequest;
import com.to4ilochka.bookspace.dto.book.UpdateBookRequest;
import com.to4ilochka.bookspace.dto.common.PagedResponse;
import com.to4ilochka.bookspace.exception.ResourceAlreadyExistsException;
import com.to4ilochka.bookspace.exception.ResourceNotFoundException;
import com.to4ilochka.bookspace.mapper.BookMapper;
import com.to4ilochka.bookspace.model.Book;
import com.to4ilochka.bookspace.model.enums.AgeGroup;
import com.to4ilochka.bookspace.repo.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    BookRepository bookRepository;
    @Mock
    BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void getBooks_WithAgeGroup() {
        AgeGroup ageGroup = AgeGroup.ADULT;
        Pageable pageable = mock(Pageable.class);
        Page<Book> bookPage = mock(Page.class);
        PagedResponse<BookShortResponse> expectedResponse = mock(PagedResponse.class);

        when(bookRepository.findAllByAgeGroup(ageGroup, pageable)).thenReturn(bookPage);
        when(bookMapper.toPagedResponse(bookPage)).thenReturn(expectedResponse);

        PagedResponse<BookShortResponse> result = bookService.getBooks(ageGroup, pageable);

        assertEquals(expectedResponse, result);
        verify(bookRepository).findAllByAgeGroup(ageGroup, pageable);
        verify(bookRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    void getBooks_WithoutAgeGroup() {
        Pageable pageable = mock(Pageable.class);
        Page<Book> bookPage = mock(Page.class);
        PagedResponse<BookShortResponse> expectedResponse = mock(PagedResponse.class);

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toPagedResponse(bookPage)).thenReturn(expectedResponse);

        PagedResponse<BookShortResponse> result = bookService.getBooks(null, pageable);

        assertEquals(expectedResponse, result);
        verify(bookRepository).findAll(pageable);
    }

    @Test
    void getBooksByKeywordAndAgeGroup_WithAgeGroup() {
        String keyword = "Java";
        AgeGroup ageGroup = AgeGroup.TEEN;
        Pageable pageable = mock(Pageable.class);
        Page<Book> bookPage = mock(Page.class);
        PagedResponse<BookShortResponse> expectedResponse = mock(PagedResponse.class);

        when(bookRepository.findAllByAgeGroupAndNameContainingIgnoreCaseOrAgeGroupAndAuthorContainingIgnoreCase(
                ageGroup, keyword, ageGroup, keyword, pageable)).thenReturn(bookPage);
        when(bookMapper.toPagedResponse(bookPage)).thenReturn(expectedResponse);

        PagedResponse<BookShortResponse> result = bookService.getBooksByKeywordAndAgeGroup(keyword, ageGroup, pageable);

        assertEquals(expectedResponse, result);
        verify(bookRepository, never()).searchByKeyword(anyString(), any(Pageable.class));
    }

    @Test
    void getBooksByKeywordAndAgeGroup_WithoutAgeGroup() {
        String keyword = "Java";
        Pageable pageable = mock(Pageable.class);
        Page<Book> bookPage = mock(Page.class);
        PagedResponse<BookShortResponse> expectedResponse = mock(PagedResponse.class);

        when(bookRepository.searchByKeyword(keyword, pageable)).thenReturn(bookPage);
        when(bookMapper.toPagedResponse(bookPage)).thenReturn(expectedResponse);

        PagedResponse<BookShortResponse> result = bookService.getBooksByKeywordAndAgeGroup(keyword, null, pageable);

        assertEquals(expectedResponse, result);
    }

    @Test
    void getBook_Success() {
        Long bookId = 1L;
        Book book = new Book();
        BookDetailResponse expectedResponse = mock(BookDetailResponse.class);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDetailResponse(book)).thenReturn(expectedResponse);

        BookDetailResponse result = bookService.getBook(bookId);

        assertEquals(expectedResponse, result);
    }

    @Test
    void getBook_NotFound() {
        Long bookId = 99L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.getBook(bookId));
    }

    @Test
    void addBook_Success() {
        CreateBookRequest request = mock(CreateBookRequest.class);
        when(request.name()).thenReturn("Book Name");
        Book book = new Book();
        BookDetailResponse expectedResponse = mock(BookDetailResponse.class);

        when(bookRepository.existsByName(request.name())).thenReturn(false);
        when(bookMapper.toEntity(request)).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.toDetailResponse(book)).thenReturn(expectedResponse);

        BookDetailResponse result = bookService.addBook(request);

        assertEquals(expectedResponse, result);
        verify(bookRepository).save(book);
    }

    @Test
    void addBook_AlreadyExists() {
        CreateBookRequest request = mock(CreateBookRequest.class);
        when(request.name()).thenReturn("Book Name");

        when(bookRepository.existsByName(request.name())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> bookService.addBook(request));
        verify(bookRepository, never()).save(any());
    }

    @Test
    void updateBook_Success() {
        Long bookId = 1L;
        UpdateBookRequest request = mock(UpdateBookRequest.class);
        Book book = new Book();
        BookDetailResponse expectedResponse = mock(BookDetailResponse.class);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        doNothing().when(bookMapper).updateEntityFromDto(request, book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDetailResponse(book)).thenReturn(expectedResponse);

        BookDetailResponse result = bookService.updateBook(bookId, request);

        assertEquals(expectedResponse, result);
        verify(bookRepository).save(book);
        verify(bookMapper).updateEntityFromDto(request, book);
    }

    @Test
    void updateBook_NotFound() {
        Long bookId = 1L;
        UpdateBookRequest request = mock(UpdateBookRequest.class);

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(bookId, request));
        verify(bookRepository, never()).save(any());
    }

    @Test
    void delete_Success() {
        Long bookId = 1L;

        when(bookRepository.existsById(bookId)).thenReturn(true);

        bookService.delete(bookId);

        verify(bookRepository).deleteById(bookId);
    }

    @Test
    void delete_NotFound() {
        Long bookId = 1L;

        when(bookRepository.existsById(bookId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> bookService.delete(bookId));
        verify(bookRepository, never()).deleteById(any());
    }
}