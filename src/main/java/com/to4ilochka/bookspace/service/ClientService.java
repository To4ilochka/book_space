package com.to4ilochka.bookspace.service;

import com.to4ilochka.bookspace.dto.client.ClientResponse;
import com.to4ilochka.bookspace.dto.client.UpdateClientRequest;

import java.math.BigDecimal;
import java.util.List;

public interface ClientService {

    ClientResponse getMyProfile(Long userId);

    ClientResponse updateMyProfile(Long userId, UpdateClientRequest request);

    void addBalance(Long userId, BigDecimal amount);

    List<ClientResponse> getAllClients();
}
