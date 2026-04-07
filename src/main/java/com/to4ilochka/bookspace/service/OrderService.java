package com.to4ilochka.bookspace.service;

import com.to4ilochka.bookspace.dto.order.CreateOrderRequest;
import com.to4ilochka.bookspace.dto.order.OrderResponse;
import com.to4ilochka.bookspace.model.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(Long clientId, CreateOrderRequest request);
    OrderResponse getOrderById(Long orderId);
    List<OrderResponse> getMyOrders(Long clientId);
    List<OrderResponse> getAllOrders();
    OrderResponse updateOrderStatus(Long orderId, OrderStatus status, Long employeeId);
}