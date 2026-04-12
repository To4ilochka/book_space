package com.to4ilochka.bookspace.service.impl;

import com.to4ilochka.bookspace.dto.client.ClientResponse;
import com.to4ilochka.bookspace.dto.client.UpdateClientRequest;
import com.to4ilochka.bookspace.exception.BusinessValidationException;
import com.to4ilochka.bookspace.exception.ResourceNotFoundException;
import com.to4ilochka.bookspace.mapper.ClientMapper;
import com.to4ilochka.bookspace.model.Client;
import com.to4ilochka.bookspace.repo.ClientRepository;
import com.to4ilochka.bookspace.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public ClientResponse getMyProfile(Long userId) {
        return clientRepository.findById(userId)
                .map(clientMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("client.notfound"));
    }

    @Transactional
    @Override
    public ClientResponse updateMyProfile(Long userId, UpdateClientRequest request) {
        Client client = clientRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("client.notfound"));

        client.getUser().setName(request.name());
        return clientMapper.toResponse(clientRepository.save(client));
    }

    @Transactional
    @Override
    public void addBalance(Long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessValidationException("client.balance.positive");
        }
        Client client = clientRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("client.notfound"));

        client.setBalance(client.getBalance().add(amount));
        clientRepository.save(client);
    }
}