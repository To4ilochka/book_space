package com.to4ilochka.bookspace.service;

import com.to4ilochka.bookspace.dto.common.PagedResponse;
import com.to4ilochka.bookspace.dto.order.CreateOrderRequest;
import com.to4ilochka.bookspace.dto.order.OrderResponse;
import com.to4ilochka.bookspace.model.enums.OrderStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(Long clientId, CreateOrderRequest request);

    OrderResponse getOrderById(Long orderId);

    PagedResponse<OrderResponse> getMyOrders(Long clientId, Pageable pageable);

    PagedResponse<OrderResponse> getAllOrders(String keyword, Pageable pageable);

    OrderResponse updateOrderStatus(Long orderId, OrderStatus status, Long employeeId);
}