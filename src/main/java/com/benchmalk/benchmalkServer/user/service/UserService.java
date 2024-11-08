package com.benchmalk.benchmalkServer.user.service;

import com.benchmalk.benchmalkServer.user.domain.User;
import com.benchmalk.benchmalkServer.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User create(String userid, String username, String password) {
        User user = new User(userid, username, passwordEncoder.encode(password));
        this.userRepository.save(user);
        return user;
    }
}