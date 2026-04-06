package com.to4ilochka.bookspace.service;

import com.to4ilochka.bookspace.dto.order.OrderDTO;

import java.util.List;

public interface OrderService {
    List<OrderDTO> getAllOrdersByClient(String email);
    List<OrderDTO> getAllOrdersByEmployee(String email);
    OrderDTO addOrder(OrderDTO order);
}
