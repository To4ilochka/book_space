package com.to4ilochka.bookspace.mapper;

import com.to4ilochka.bookspace.dto.client.CreateClientRequest;
import com.to4ilochka.bookspace.model.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {java.util.Set.class, com.to4ilochka.bookspace.model.enums.Role.class})
public interface ClientMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.email", source = "email")
    @Mapping(target = "user.name", source = "name")
    @Mapping(target = "user.password", ignore = true)
    @Mapping(target = "user.roles", expression = "java(Set.of(Role.ROLE_CLIENT))")
    @Mapping(target = "balance", defaultValue = "0")
//    TODO @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
    Client toEntity(CreateClientRequest createClientRequest);
}