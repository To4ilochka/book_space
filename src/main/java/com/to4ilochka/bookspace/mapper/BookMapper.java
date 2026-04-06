package com.to4ilochka.bookspace.mapper;

import com.to4ilochka.bookspace.dto.common.PagedResponse;
import com.to4ilochka.bookspace.dto.book.BookDetailResponse;
import com.to4ilochka.bookspace.dto.book.BookShortResponse;
import com.to4ilochka.bookspace.dto.book.CreateBookRequest;
import com.to4ilochka.bookspace.dto.book.UpdateBookRequest;
import com.to4ilochka.bookspace.model.Book;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface BookMapper {
    @Mapping(target = "id", ignore = true)
    Book toEntity(CreateBookRequest createBookRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", ignore = true)
    void updateEntityFromDto(UpdateBookRequest dto, @MappingTarget Book entity);

    BookShortResponse toShortResponse(Book book);

    PagedResponse<BookShortResponse> toPagedResponse(Page<Book> page);

    BookDetailResponse toDetailResponse(Book book);
}