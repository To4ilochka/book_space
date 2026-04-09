package com.to4ilochka.bookspace.dto.user;

import com.to4ilochka.bookspace.model.enums.Role;

import java.util.Set;

public record UserResponse(
        Long id,
        String email,
        String name,
        Set<Role> roles,
        boolean locked
) {
}
