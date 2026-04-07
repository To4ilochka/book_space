package com.to4ilochka.bookspace.service.impl;

import com.to4ilochka.bookspace.model.User;
import com.to4ilochka.bookspace.model.enums.Role;
import com.to4ilochka.bookspace.repo.UserRepository;
import com.to4ilochka.bookspace.security.CustomUserDetails;
import com.to4ilochka.bookspace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public void setLockStatus(Long userId, boolean isLocked, CustomUserDetails executor) {
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        boolean targetIsAdmin = targetUser.getRoles().contains(Role.ROLE_ADMIN);
        boolean executorIsAdmin = executor.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Role.ROLE_ADMIN.name()));

        if (targetIsAdmin && !executorIsAdmin) {
            throw new AccessDeniedException("Employees cannot modify admin accounts");
        }

        if (targetIsAdmin && isLocked) {
            throw new IllegalArgumentException("Cannot lock an admin account");
        }

        targetUser.setLocked(isLocked);
        userRepository.save(targetUser);
    }
}