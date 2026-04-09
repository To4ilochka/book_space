package com.to4ilochka.bookspace.service;

import com.to4ilochka.bookspace.dto.common.PagedResponse;
import com.to4ilochka.bookspace.dto.user.UserResponse;
import com.to4ilochka.bookspace.security.CustomUserDetails;
import org.springframework.data.domain.Pageable;

public interface UserService {

    void setLockStatus(Long userId, boolean isLocked, CustomUserDetails executor);

    UserResponse getUserById(Long id);

    PagedResponse<UserResponse> getAllUsers(String keyword, Pageable pageable);

    void grantAdminRole(Long userId);
}