package com.example.videohosting.service;

import com.example.videohosting.entity.User;
import com.example.videohosting.mapper.userMapper.UserMapper;
import com.example.videohosting.model.UserModel;
import com.example.videohosting.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserModel loadUserByUsername(String email) {
        logger.info("Attempting to load user by email: {}", email);
        User user = userRepository.getUserByEmail(email);
        if (user == null) {
            logger.error("User with email {} not found", email);
            throw new UsernameNotFoundException("User" + email + "not found");
        }
        logger.info("Successfully loaded user with email: {}", email);
        return userMapper.toModel(user);
    }
}
