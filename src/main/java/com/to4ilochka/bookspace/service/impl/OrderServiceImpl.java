package com.to4ilochka.bookspace.service.impl;

import com.to4ilochka.bookspace.dto.order.OrderDTO;
import com.to4ilochka.bookspace.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public List<OrderDTO> getAllOrdersByClient(String email) {
        return List.of();
    }

    @Override
    public List<OrderDTO> getAllOrdersByEmployee(String email) {
        return List.of();
    }

    @Override
    public OrderDTO addOrder(OrderDTO order) {
        return null;
    }
}
