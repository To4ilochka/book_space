package com.to4ilochka.bookspace.mapper;

import com.to4ilochka.bookspace.dto.user.UserResponse;
import com.to4ilochka.bookspace.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "locked", source = "locked")
    UserResponse toResponse(User user);
}