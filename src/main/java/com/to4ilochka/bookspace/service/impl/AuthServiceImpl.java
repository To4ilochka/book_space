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
import com.to4ilochka.bookspace.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        return jwtService.generateAuthToken(request.email());
    }

    @Transactional
    @Override
    public AuthResponse registerClient(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ResourceAlreadyExistsException("auth.email.exists");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setName(request.name());
        user.setLocked(false);
        user.setRoles(Set.of(Role.ROLE_CLIENT));

        Client client = new Client();
        client.setUser(user);

        clientRepository.save(client);

        return jwtService.generateAuthToken(user.getEmail());
    }

    @Override
    public AuthResponse refresh(RefreshRequest request) {
        if (!jwtService.validateJwtToken(request.refreshToken())) {
            throw new InvalidTokenException("auth.token.invalid");
        }

        String email = jwtService.getEmailFromToken(request.refreshToken());
        return jwtService.refreshBaseToken(email, request.refreshToken());
    }
}
