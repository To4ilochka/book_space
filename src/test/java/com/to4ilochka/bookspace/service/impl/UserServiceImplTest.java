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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void setLockStatus_EmployeeCannotLockAdmin() {
        Long adminId = 1L;
        User adminUser = new User();
        adminUser.setId(adminId);
        adminUser.setRoles(new HashSet<>(List.of(Role.ROLE_ADMIN)));

        CustomUserDetails employeeExecutor = new CustomUserDetails(
                2L,
                "emp@mail.com",
                "pass",
                false,
                List.of(new SimpleGrantedAuthority(Role.ROLE_EMPLOYEE.name()))
        );

        when(userRepository.findById(adminId)).thenReturn(Optional.of(adminUser));

        assertThrows(AccessDeniedException.class, () -> userService.setLockStatus(adminId, true, employeeExecutor));
        verify(userRepository, never()).save(any());
    }

    @Test
    void setLockStatus_AdminCannotLockAdmin() {
        Long adminId = 1L;
        User adminUser = new User();
        adminUser.setId(adminId);
        adminUser.setRoles(new HashSet<>(List.of(Role.ROLE_ADMIN)));

        CustomUserDetails adminExecutor = new CustomUserDetails(
                2L,
                "admin2@mail.com",
                "pass",
                false,
                List.of(new SimpleGrantedAuthority(Role.ROLE_ADMIN.name()))
        );

        when(userRepository.findById(adminId)).thenReturn(Optional.of(adminUser));

        assertThrows(BusinessValidationException.class, () -> userService.setLockStatus(adminId, true, adminExecutor));
        verify(userRepository, never()).save(any());
    }

    @Test
    void setLockStatus_UserNotFound() {
        Long userId = 1L;
        CustomUserDetails executor = new CustomUserDetails(
                2L, "emp@mail.com", "pass", false, List.of(new SimpleGrantedAuthority(Role.ROLE_ADMIN.name()))
        );

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.setLockStatus(userId, true, executor));
        verify(userRepository, never()).save(any());
    }

    @Test
    void setLockStatus_SuccessNonAdmin() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setRoles(new HashSet<>(List.of(Role.ROLE_CLIENT)));

        CustomUserDetails executor = new CustomUserDetails(
                2L, "emp@mail.com", "pass", false, List.of(new SimpleGrantedAuthority(Role.ROLE_EMPLOYEE.name()))
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.setLockStatus(userId, true, executor);

        assertTrue(user.isLocked());
        verify(userRepository).save(user);
    }

    @Test
    void setLockStatus_SuccessAdminUnlock() {
        Long adminId = 1L;
        User adminUser = new User();
        adminUser.setId(adminId);
        adminUser.setRoles(new HashSet<>(List.of(Role.ROLE_ADMIN)));

        CustomUserDetails adminExecutor = new CustomUserDetails(
                2L, "admin2@mail.com", "pass", false, List.of(new SimpleGrantedAuthority(Role.ROLE_ADMIN.name()))
        );

        when(userRepository.findById(adminId)).thenReturn(Optional.of(adminUser));

        userService.setLockStatus(adminId, false, adminExecutor);

        assertFalse(adminUser.isLocked());
        verify(userRepository).save(adminUser);
    }

    @Test
    void getUserById_Success() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        UserResponse expectedResponse = mock(UserResponse.class);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(expectedResponse);

        UserResponse result = userService.getUserById(userId);

        assertEquals(expectedResponse, result);
    }

    @Test
    void getUserById_NotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void getAllUsers_WithKeyword() {
        String keyword = "test";
        Pageable pageable = mock(Pageable.class);
        List<User> users = List.of(new User());
        Page<User> userPage = new PageImpl<>(users);
        UserResponse userResponse = mock(UserResponse.class);

        when(userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword, pageable)).thenReturn(userPage);
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

        PagedResponse<UserResponse> result = userService.getAllUsers(keyword, pageable);

        assertNotNull(result);
        assertEquals(1, result.content().size());
        verify(userRepository).findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword, pageable);
        verify(userRepository, never()).findAll(any(Pageable.class));
    }

    @Test
    void getAllUsers_WithoutKeyword() {
        Pageable pageable = mock(Pageable.class);
        List<User> users = List.of(new User());
        Page<User> userPage = new PageImpl<>(users);
        UserResponse userResponse = mock(UserResponse.class);

        when(userRepository.findAll(pageable)).thenReturn(userPage);
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

        PagedResponse<UserResponse> result = userService.getAllUsers("", pageable);

        assertNotNull(result);
        assertEquals(1, result.content().size());
        verify(userRepository).findAll(pageable);
        verify(userRepository, never()).findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(anyString(), anyString(), any(Pageable.class));
    }

    @Test
    void getAllUsers_WithBlankKeyword() {
        String keyword = "   ";
        Pageable pageable = mock(Pageable.class);
        List<User> users = List.of(new User());
        Page<User> userPage = new PageImpl<>(users);
        UserResponse userResponse = mock(UserResponse.class);

        when(userRepository.findAll(pageable)).thenReturn(userPage);
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

        PagedResponse<UserResponse> result = userService.getAllUsers(keyword, pageable);

        assertNotNull(result);
        assertEquals(1, result.content().size());
        verify(userRepository).findAll(pageable);
        verify(userRepository, never()).findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(anyString(), anyString(), any(Pageable.class));
    }

    @Test
    void getAllUsers_WithNullKeyword() {
        Pageable pageable = mock(Pageable.class);
        List<User> users = List.of(new User());
        Page<User> userPage = new PageImpl<>(users);
        UserResponse userResponse = mock(UserResponse.class);

        when(userRepository.findAll(pageable)).thenReturn(userPage);
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

        PagedResponse<UserResponse> result = userService.getAllUsers(null, pageable);

        assertNotNull(result);
        assertEquals(1, result.content().size());
        verify(userRepository).findAll(pageable);
        verify(userRepository, never()).findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(anyString(), anyString(), any(Pageable.class));
    }

    @Test
    void grantAdminRole_Success() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setRoles(new HashSet<>(List.of(Role.ROLE_CLIENT)));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.grantAdminRole(userId);

        assertTrue(user.getRoles().contains(Role.ROLE_ADMIN));
        verify(userRepository).save(user);
    }

    @Test
    void grantAdminRole_NotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.grantAdminRole(userId));
        verify(userRepository, never()).save(any());
    }
}