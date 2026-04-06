package com.to4ilochka.bookspace.service.impl;

import com.to4ilochka.bookspace.model.User;
import com.to4ilochka.bookspace.repo.UserRepository;
import com.to4ilochka.bookspace.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional
    @Override
    public void toggleLock(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setLocked(!user.isLocked());
        userRepository.save(user);
    }
}
