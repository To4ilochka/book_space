package com.to4ilochka.bookspace.service.impl;

import com.to4ilochka.bookspace.dto.client.ClientResponse;
import com.to4ilochka.bookspace.dto.client.UpdateClientRequest;
import com.to4ilochka.bookspace.exception.BusinessValidationException;
import com.to4ilochka.bookspace.exception.ResourceNotFoundException;
import com.to4ilochka.bookspace.mapper.ClientMapper;
import com.to4ilochka.bookspace.model.Client;
import com.to4ilochka.bookspace.model.User;
import com.to4ilochka.bookspace.repo.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    void getMyProfile_Success() {
        Long userId = 1L;
        Client client = new Client();
        ClientResponse expectedResponse = mock(ClientResponse.class);

        when(clientRepository.findById(userId)).thenReturn(Optional.of(client));
        when(clientMapper.toResponse(client)).thenReturn(expectedResponse);

        ClientResponse result = clientService.getMyProfile(userId);

        assertEquals(expectedResponse, result);
    }

    @Test
    void getMyProfile_NotFound() {
        Long userId = 1L;

        when(clientRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.getMyProfile(userId));
    }

    @Test
    void updateMyProfile_Success() {
        Long userId = 1L;
        UpdateClientRequest request = new UpdateClientRequest("New Name");
        User user = new User();
        user.setName("Old Name");
        Client client = new Client();
        client.setId(userId);
        client.setUser(user);
        ClientResponse expectedResponse = mock(ClientResponse.class);

        when(clientRepository.findById(userId)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenAnswer(i -> i.getArguments()[0]);
        when(clientMapper.toResponse(client)).thenReturn(expectedResponse);

        ClientResponse result = clientService.updateMyProfile(userId, request);

        assertEquals("New Name", client.getUser().getName());
        assertEquals(expectedResponse, result);
        verify(clientRepository).save(client);
    }

    @Test
    void updateMyProfile_NotFound() {
        Long userId = 1L;
        UpdateClientRequest request = new UpdateClientRequest("New Name");

        when(clientRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.updateMyProfile(userId, request));
        verify(clientRepository, never()).save(any());
    }

    @Test
    void addBalance_Success() {
        Long userId = 1L;
        Client client = new Client();
        client.setId(userId);
        client.setBalance(BigDecimal.valueOf(100));

        when(clientRepository.findById(userId)).thenReturn(Optional.of(client));

        clientService.addBalance(userId, BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(150), client.getBalance());
        verify(clientRepository).save(client);
    }

    @Test
    void addBalance_NegativeAmount() {
        assertThrows(BusinessValidationException.class, () -> clientService.addBalance(1L, BigDecimal.valueOf(-10)));
        verify(clientRepository, never()).findById(any());
    }

    @Test
    void addBalance_NotFound() {
        Long userId = 1L;
        BigDecimal amount = BigDecimal.valueOf(50);

        when(clientRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clientService.addBalance(userId, amount));
        verify(clientRepository, never()).save(any());
    }

    @Test
    void getAllClients_Success() {
        List<Client> clients = List.of(new Client(), new Client());
        List<ClientResponse> expectedResponse = List.of(mock(ClientResponse.class), mock(ClientResponse.class));

        when(clientRepository.findAll()).thenReturn(clients);
        when(clientMapper.toResponseList(clients)).thenReturn(expectedResponse);

        List<ClientResponse> result = clientService.getAllClients();

        assertEquals(expectedResponse, result);
        assertEquals(2, result.size());
    }
}