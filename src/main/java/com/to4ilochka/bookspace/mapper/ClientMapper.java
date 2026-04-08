package com.to4ilochka.bookspace.mapper;

import com.to4ilochka.bookspace.dto.client.ClientResponse;
import com.to4ilochka.bookspace.model.Client;
import com.to4ilochka.bookspace.model.enums.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", imports = {Set.class, Role.class})
public interface ClientMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "balance", source = "balance")
    ClientResponse toResponse(Client client);

    List<ClientResponse> toResponseList(List<Client> clients);
}