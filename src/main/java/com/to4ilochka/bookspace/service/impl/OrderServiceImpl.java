package com.to4ilochka.bookspace.service.impl;

import com.to4ilochka.bookspace.dto.common.PagedResponse;
import com.to4ilochka.bookspace.dto.order.CreateOrderRequest;
import com.to4ilochka.bookspace.dto.order.OrderItemRequest;
import com.to4ilochka.bookspace.dto.order.OrderResponse;
import com.to4ilochka.bookspace.exception.InsufficientBalanceException;
import com.to4ilochka.bookspace.exception.ResourceNotFoundException;
import com.to4ilochka.bookspace.mapper.OrderMapper;
import com.to4ilochka.bookspace.model.*;
import com.to4ilochka.bookspace.model.enums.OrderStatus;
import com.to4ilochka.bookspace.repo.BookRepository;
import com.to4ilochka.bookspace.repo.ClientRepository;
import com.to4ilochka.bookspace.repo.EmployeeRepository;
import com.to4ilochka.bookspace.repo.OrderRepository;
import com.to4ilochka.bookspace.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final BookRepository bookRepository;
    private final OrderMapper orderMapper;

    @Transactional
    @Override
    public OrderResponse createOrder(Long clientId, CreateOrderRequest request) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        List<Long> bookIds = request.items().stream()
                .map(OrderItemRequest::bookId)
                .toList();

        List<Book> books = bookRepository.findAllById(bookIds);

        if (books.size() != bookIds.size()) {
            throw new ResourceNotFoundException("One or more books not found");
        }

        Map<Long, Book> bookMap = books.stream()
                .collect(Collectors.toMap(Book::getId, b -> b));

        Order order = new Order();
        order.setClient(client);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED);

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<BookItem> bookItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : request.items()) {
            Book book = bookMap.get(itemRequest.bookId());

            BookItem bookItem = new BookItem();
            bookItem.setOrder(order);
            bookItem.setBook(book);
            bookItem.setQuantity(itemRequest.quantity());
            bookItem.setPrice(book.getPrice());

            BigDecimal itemTotal = book.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity()));
            totalPrice = totalPrice.add(itemTotal);

            bookItems.add(bookItem);
        }

        if (client.getBalance().compareTo(totalPrice) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        client.setBalance(client.getBalance().subtract(totalPrice));
        clientRepository.save(client);

        order.setPrice(totalPrice);
        order.setBookItems(bookItems);

        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    public OrderResponse getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Override
    public PagedResponse<OrderResponse> getMyOrders(Long clientId, Pageable pageable) {
        Page<Order> orderPage = orderRepository.findByClientId(clientId, pageable);

        List<OrderResponse> content = orderMapper.toResponseList(orderPage.getContent());

        return new PagedResponse<>(
                content,
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                orderPage.getNumber(),
                orderPage.getSize(),
                orderPage.isFirst(),
                orderPage.isLast()
        );
    }

    @Override
    public PagedResponse<OrderResponse> getAllOrders(String keyword, Pageable pageable) {
        Page<Order> orderPage;

        if (keyword != null && !keyword.isBlank()) {
            orderPage = orderRepository.searchOrders(keyword, pageable);
        } else {
            orderPage = orderRepository.findAll(pageable);
        }

        List<OrderResponse> content = orderMapper.toResponseList(orderPage.getContent());

        return new PagedResponse<>(
                content,
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                orderPage.getNumber(),
                orderPage.getSize(),
                orderPage.isFirst(),
                orderPage.isLast()
        );
    }

    @Transactional
    @Override
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status, Long employeeId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        order.setStatus(status);
        order.setEmployee(employee);

        return orderMapper.toResponse(orderRepository.save(order));
    }
}