package com.to4ilochka.bookspace.service.impl;

import com.to4ilochka.bookspace.dto.auth.AuthResponse;
import com.to4ilochka.bookspace.dto.auth.LoginRequest;
import com.to4ilochka.bookspace.dto.auth.RefreshRequest;
import com.to4ilochka.bookspace.dto.auth.RegisterRequest;
import com.to4ilochka.bookspace.exception.InvalidTokenException;
import com.to4ilochka.bookspace.exception.ResourceAlreadyExistsException;
import com.to4ilochka.bookspace.model.Client;
import com.to4ilochka.bookspace.model.User;
import com.to4ilochka.bookspace.model.enums.Role;
import com.to4ilochka.bookspace.repo.ClientRepository;
import com.to4ilochka.bookspace.repo.UserRepository;
import com.to4ilochka.bookspace.security.jwt.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_Success() {
        LoginRequest request = new LoginRequest("test@mail.com", "password");
        AuthResponse expectedResponse = new AuthResponse("accessToken", "refreshToken");

        when(jwtService.generateAuthToken(request.email())).thenReturn(expectedResponse);

        AuthResponse actualResponse = authService.login(request);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void registerClient_Success() {
        RegisterRequest request = new RegisterRequest("test@mail.com", "password", "Test Name");
        AuthResponse expectedResponse = new AuthResponse("token", "refresh");

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");
        when(jwtService.generateAuthToken(request.email())).thenReturn(expectedResponse);

        AuthResponse response = authService.registerClient(request);

        assertNotNull(response);
        assertEquals(expectedResponse, response);

        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(clientCaptor.capture());

        Client savedClient = clientCaptor.getValue();
        User savedUser = savedClient.getUser();

        assertEquals(request.email(), savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertTrue(savedUser.getRoles().contains(Role.ROLE_CLIENT));
        assertFalse(savedUser.isLocked());
    }

    @Test
    void registerClient_EmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest("test@mail.com", "password", "Test Name");

        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> authService.registerClient(request));
        verify(clientRepository, never()).save(any());
    }

    @Test
    void refresh_Success() {
        RefreshRequest request = new RefreshRequest("validRefreshToken");
        String email = "test@mail.com";
        AuthResponse expectedResponse = new AuthResponse("newAccessToken", "validRefreshToken");

        when(jwtService.validateJwtToken(request.refreshToken())).thenReturn(true);
        when(jwtService.getEmailFromToken(request.refreshToken())).thenReturn(email);
        when(jwtService.refreshBaseToken(email, request.refreshToken())).thenReturn(expectedResponse);

        AuthResponse response = authService.refresh(request);

        assertNotNull(response);
        assertEquals(expectedResponse, response);
    }

    @Test
    void refresh_InvalidToken() {
        RefreshRequest request = new RefreshRequest("invalidRefreshToken");

        when(jwtService.validateJwtToken(request.refreshToken())).thenReturn(false);

        assertThrows(InvalidTokenException.class, () -> authService.refresh(request));
        verify(jwtService, never()).getEmailFromToken(anyString());
        verify(jwtService, never()).refreshBaseToken(anyString(), anyString());
    }
}