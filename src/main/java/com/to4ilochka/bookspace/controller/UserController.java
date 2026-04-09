package com.to4ilochka.bookspace.controller;

import com.to4ilochka.bookspace.dto.common.PagedResponse;
import com.to4ilochka.bookspace.dto.user.UserResponse;
import com.to4ilochka.bookspace.security.CustomUserDetails;
import com.to4ilochka.bookspace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/{id}/lock")
    public ResponseEntity<Void> lockUser(@PathVariable Long id,
                                         @AuthenticationPrincipal CustomUserDetails executor) {
        userService.setLockStatus(id, true, executor);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/unlock")
    public ResponseEntity<Void> unlockUser(@PathVariable Long id,
                                           @AuthenticationPrincipal CustomUserDetails executor) {
        userService.setLockStatus(id, false, executor);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(userService.getUserById(user.id()));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<UserResponse>> getAllUsers(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(keyword, pageable));
    }

    @PostMapping("/{id}/roles/admin")
    public ResponseEntity<Void> grantAdminRole(@PathVariable Long id) {
        userService.grantAdminRole(id);
        return ResponseEntity.ok().build();
    }
}