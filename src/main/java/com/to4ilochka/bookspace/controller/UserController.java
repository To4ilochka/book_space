package com.to4ilochka.bookspace.controller;

import com.to4ilochka.bookspace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PatchMapping("/{id}/lock")
    public ResponseEntity<Void> toggleLock(@PathVariable Long id) {
        userService.toggleLock(id);
        return ResponseEntity.noContent().build();
    }
}
