package com.to4ilochka.bookspace.service;

import com.to4ilochka.bookspace.dto.client.ClientDTO;

import java.util.List;

public interface ClientService {
    List<ClientDTO> getAllClients();
    ClientDTO getClientByEmail(String email);
    ClientDTO updateClientByEmail(String email, ClientDTO client);
    void deleteClientByEmail(String email);
    ClientDTO  addClient(ClientDTO client);
}
