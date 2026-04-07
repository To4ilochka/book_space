package com.to4ilochka.bookspace.controller;

import com.to4ilochka.bookspace.security.CustomUserDetails;
import com.to4ilochka.bookspace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PostMapping("/{id}/lock")
    public ResponseEntity<Void> lockUser(@PathVariable Long id,
                                         @AuthenticationPrincipal CustomUserDetails executor) {
        userService.setLockStatus(id, true, executor);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PostMapping("/{id}/unlock")
    public ResponseEntity<Void> unlockUser(@PathVariable Long id,
                                           @AuthenticationPrincipal CustomUserDetails executor) {
        userService.setLockStatus(id, false, executor);
        return ResponseEntity.noContent().build();
    }
}