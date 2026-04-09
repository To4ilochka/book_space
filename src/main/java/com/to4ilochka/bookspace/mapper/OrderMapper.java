package com.to4ilochka.bookspace.mapper;

import com.to4ilochka.bookspace.dto.order.OrderItemResponse;
import com.to4ilochka.bookspace.dto.order.OrderResponse;
import com.to4ilochka.bookspace.model.BookItem;
import com.to4ilochka.bookspace.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "clientId", source = "client.id")
    @Mapping(target = "clientEmail", source = "client.user.email")
    @Mapping(target = "employeeEmail", source = "employee.user.email")
    OrderResponse toResponse(Order order);

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookName", source = "book.name")
    OrderItemResponse toItemResponse(BookItem bookItem);

    List<OrderResponse> toResponseList(List<Order> orders);
}
