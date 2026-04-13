package com.to4ilochka.bookspace.service.impl;

import com.to4ilochka.bookspace.dto.common.PagedResponse;
import com.to4ilochka.bookspace.dto.user.UserResponse;
import com.to4ilochka.bookspace.exception.BusinessValidationException;
import com.to4ilochka.bookspace.exception.ResourceNotFoundException;
import com.to4ilochka.bookspace.mapper.UserMapper;
import com.to4ilochka.bookspace.model.User;
import com.to4ilochka.bookspace.model.enums.Role;
import com.to4ilochka.bookspace.repo.UserRepository;
import com.to4ilochka.bookspace.security.CustomUserDetails;
import com.to4ilochka.bookspace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public void setLockStatus(Long userId, boolean isLocked, CustomUserDetails executor) {
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user.notfound"));

        boolean targetIsAdmin = targetUser.getRoles().contains(Role.ROLE_ADMIN);
        boolean targetIsEmployee = targetUser.getRoles().contains(Role.ROLE_EMPLOYEE);

        boolean executorIsAdmin = executor.getAuthorities().stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), Role.ROLE_ADMIN.name()));

        if (!executorIsAdmin && (targetIsAdmin || targetIsEmployee)) {
            throw new AccessDeniedException("user.employee.modify.denied");
        }

        if (targetIsAdmin && isLocked) {
            throw new BusinessValidationException("user.admin.lock.denied");
        }

        targetUser.setLocked(isLocked);
        userRepository.save(targetUser);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user.notfound"));

        return userMapper.toResponse(user);
    }

    @Override
    public PagedResponse<UserResponse> getAllUsers(String keyword, Pageable pageable) {
        Page<User> page;

        if (keyword != null && !keyword.isBlank()) {
            page = userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword, pageable);
        } else {
            page = userRepository.findAll(pageable);
        }

        List<UserResponse> content = page.stream()
                .map(userMapper::toResponse)
                .toList();

        return new PagedResponse<>(
                content,
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.isFirst(),
                page.isLast()
        );
    }

    @Transactional
    @Override
    public void grantAdminRole(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user.notfound"));

        user.getRoles().add(Role.ROLE_ADMIN);
        userRepository.save(user);
    }
}