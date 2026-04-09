package com.to4ilochka.bookspace.service.impl;

import com.to4ilochka.bookspace.dto.common.PagedResponse;
import com.to4ilochka.bookspace.dto.order.CreateOrderRequest;
import com.to4ilochka.bookspace.dto.order.OrderItemRequest;
import com.to4ilochka.bookspace.dto.order.OrderResponse;
import com.to4ilochka.bookspace.exception.InsufficientBalanceException;
import com.to4ilochka.bookspace.exception.ResourceNotFoundException;
import com.to4ilochka.bookspace.mapper.OrderMapper;
import com.to4ilochka.bookspace.model.Book;
import com.to4ilochka.bookspace.model.Client;
import com.to4ilochka.bookspace.model.Employee;
import com.to4ilochka.bookspace.model.Order;
import com.to4ilochka.bookspace.model.enums.OrderStatus;
import com.to4ilochka.bookspace.repo.BookRepository;
import com.to4ilochka.bookspace.repo.ClientRepository;
import com.to4ilochka.bookspace.repo.EmployeeRepository;
import com.to4ilochka.bookspace.repo.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void createOrder_Success() {
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);
        client.setBalance(BigDecimal.valueOf(100.0));

        Book book1 = new Book();
        book1.setId(10L);
        book1.setPrice(BigDecimal.valueOf(20.0));

        Book book2 = new Book();
        book2.setId(20L);
        book2.setPrice(BigDecimal.valueOf(15.0));

        CreateOrderRequest request = new CreateOrderRequest(List.of(
                new OrderItemRequest(10L, 2),
                new OrderItemRequest(20L, 1)
        ));

        OrderResponse expectedResponse = mock(OrderResponse.class);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(bookRepository.findAllById(List.of(10L, 20L))).thenReturn(List.of(book1, book2));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArguments()[0]);
        when(orderMapper.toResponse(any(Order.class))).thenReturn(expectedResponse);

        OrderResponse result = orderService.createOrder(clientId, request);

        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(clientCaptor.capture());
        assertEquals(0, BigDecimal.valueOf(45.0).compareTo(clientCaptor.getValue().getBalance()));

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository).save(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();
        assertEquals(0, BigDecimal.valueOf(55.0).compareTo(savedOrder.getPrice()));
        assertEquals(2, savedOrder.getBookItems().size());
        assertEquals(expectedResponse, result);
    }

    @Test
    void createOrder_ClientNotFound() {
        Long clientId = 1L;
        CreateOrderRequest request = new CreateOrderRequest(List.of(new OrderItemRequest(10L, 2)));

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(clientId, request));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_BookNotFound() {
        Long clientId = 1L;
        Client client = new Client();
        CreateOrderRequest request = new CreateOrderRequest(List.of(new OrderItemRequest(10L, 2)));

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(bookRepository.findAllById(List.of(10L))).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(clientId, request));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_InsufficientBalance() {
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);
        client.setBalance(BigDecimal.valueOf(10.0));

        Book book = new Book();
        book.setId(10L);
        book.setPrice(BigDecimal.valueOf(20.0));

        CreateOrderRequest request = new CreateOrderRequest(List.of(
                new OrderItemRequest(10L, 1)
        ));

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(bookRepository.findAllById(List.of(10L))).thenReturn(List.of(book));

        assertThrows(InsufficientBalanceException.class, () -> orderService.createOrder(clientId, request));
        verify(clientRepository, never()).save(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void getOrderById_Success() {
        Long orderId = 1L;
        Order order = new Order();
        OrderResponse expectedResponse = mock(OrderResponse.class);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.toResponse(order)).thenReturn(expectedResponse);

        OrderResponse result = orderService.getOrderById(orderId);

        assertEquals(expectedResponse, result);
    }

    @Test
    void getOrderById_NotFound() {
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(orderId));
    }

    @Test
    void getMyOrders_Success() {
        Long clientId = 1L;
        Pageable pageable = mock(Pageable.class);
        Page<Order> orderPage = mock(Page.class);
        List<Order> orders = List.of(new Order(), new Order());
        List<OrderResponse> responseList = List.of(mock(OrderResponse.class), mock(OrderResponse.class));

        when(orderRepository.findByClientId(clientId, pageable)).thenReturn(orderPage);
        when(orderPage.getContent()).thenReturn(orders);
        when(orderPage.getTotalElements()).thenReturn(2L);
        when(orderPage.getTotalPages()).thenReturn(1);
        when(orderMapper.toResponseList(orders)).thenReturn(responseList);

        PagedResponse<OrderResponse> result = orderService.getMyOrders(clientId, pageable);

        assertNotNull(result);
        assertEquals(2, result.content().size());
        assertEquals(2L, result.totalElements());
        assertEquals(1, result.totalPages());
    }

    @Test
    void getAllOrders_WithKeyword() {
        String keyword = "Test";
        Pageable pageable = mock(Pageable.class);
        Page<Order> orderPage = mock(Page.class);
        List<Order> orders = List.of(new Order());
        List<OrderResponse> responseList = List.of(mock(OrderResponse.class));

        when(orderRepository.searchOrders(keyword, pageable)).thenReturn(orderPage);
        when(orderPage.getContent()).thenReturn(orders);
        when(orderPage.getTotalElements()).thenReturn(1L);
        when(orderPage.getTotalPages()).thenReturn(1);
        when(orderMapper.toResponseList(orders)).thenReturn(responseList);

        PagedResponse<OrderResponse> result = orderService.getAllOrders(keyword, pageable);

        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals(1L, result.totalElements());
        verify(orderRepository).searchOrders(keyword, pageable);
        verify(orderRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    void getAllOrders_WithBlankKeyword() {
        String keyword = "   ";
        Pageable pageable = mock(Pageable.class);
        Page<Order> orderPage = mock(Page.class);
        List<Order> orders = List.of(new Order());
        List<OrderResponse> responseList = List.of(mock(OrderResponse.class));

        when(orderRepository.findAll(pageable)).thenReturn(orderPage);
        when(orderPage.getContent()).thenReturn(orders);
        when(orderMapper.toResponseList(orders)).thenReturn(responseList);

        PagedResponse<OrderResponse> result = orderService.getAllOrders(keyword, pageable);

        assertNotNull(result);
        assertEquals(1, result.content().size());
        verify(orderRepository).findAll(pageable);
        verify(orderRepository, never()).searchOrders(anyString(), any(Pageable.class));
    }

    @Test
    void getAllOrders_WithNullKeyword() {
        Pageable pageable = mock(Pageable.class);
        Page<Order> orderPage = mock(Page.class);
        List<Order> orders = List.of(new Order());
        List<OrderResponse> responseList = List.of(mock(OrderResponse.class));

        when(orderRepository.findAll(pageable)).thenReturn(orderPage);
        when(orderPage.getContent()).thenReturn(orders);
        when(orderMapper.toResponseList(orders)).thenReturn(responseList);

        PagedResponse<OrderResponse> result = orderService.getAllOrders(null, pageable);

        assertNotNull(result);
        assertEquals(1, result.content().size());
        verify(orderRepository).findAll(pageable);
        verify(orderRepository, never()).searchOrders(anyString(), any(Pageable.class));
    }

    @Test
    void updateOrderStatus_Success() {
        Long orderId = 1L;
        Long employeeId = 2L;
        OrderStatus newStatus = OrderStatus.COMPLETED;

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.CREATED);

        Employee employee = new Employee();
        employee.setId(employeeId);

        OrderResponse expectedResponse = mock(OrderResponse.class);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toResponse(order)).thenReturn(expectedResponse);

        OrderResponse result = orderService.updateOrderStatus(orderId, newStatus, employeeId);

        assertEquals(newStatus, order.getStatus());
        assertEquals(employee, order.getEmployee());
        assertEquals(expectedResponse, result);
        verify(orderRepository).save(order);
    }

    @Test
    void updateOrderStatus_OrderNotFound() {
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                orderService.updateOrderStatus(orderId, OrderStatus.COMPLETED, 2L));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void updateOrderStatus_EmployeeNotFound() {
        Long orderId = 1L;
        Long employeeId = 2L;
        Order order = new Order();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                orderService.updateOrderStatus(orderId, OrderStatus.COMPLETED, employeeId));
        verify(orderRepository, never()).save(any());
    }
}