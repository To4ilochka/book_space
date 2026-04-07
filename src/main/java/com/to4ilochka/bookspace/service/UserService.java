package com.to4ilochka.bookspace.service;

import com.to4ilochka.bookspace.security.CustomUserDetails;

public interface UserService {
    void setLockStatus(Long userId, boolean isLocked, CustomUserDetails executor);
}