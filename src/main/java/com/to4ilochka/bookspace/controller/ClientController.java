package com.to4ilochka.bookspace.controller;

import com.to4ilochka.bookspace.dto.client.ClientResponse;
import com.to4ilochka.bookspace.dto.client.UpdateClientRequest;
import com.to4ilochka.bookspace.security.CustomUserDetails;
import com.to4ilochka.bookspace.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/me")
    public ResponseEntity<ClientResponse> getMyProfile(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(clientService.getMyProfile(user.id()));
    }

    @PutMapping("/me")
    public ResponseEntity<ClientResponse> updateMyProfile(@AuthenticationPrincipal CustomUserDetails user,
                                                          @RequestBody UpdateClientRequest request) {
        return ResponseEntity.ok(clientService.updateMyProfile(user.id(), request));
    }

    @PostMapping("/me/balance")
    public ResponseEntity<Void> addBalance(@AuthenticationPrincipal CustomUserDetails user,
                                           @RequestParam BigDecimal amount) {
        clientService.addBalance(user.id(), amount);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }
}