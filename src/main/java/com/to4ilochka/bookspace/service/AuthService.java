package com.to4ilochka.bookspace.service;

import com.to4ilochka.bookspace.dto.auth.AuthResponse;
import com.to4ilochka.bookspace.dto.auth.LoginRequest;
import com.to4ilochka.bookspace.dto.auth.RefreshRequest;
import com.to4ilochka.bookspace.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse registerClient(RegisterRequest request);

    AuthResponse refresh(RefreshRequest request);
}
