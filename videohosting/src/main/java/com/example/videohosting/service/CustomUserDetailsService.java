package com.example.videohosting.service;

import com.example.videohosting.entity.User;
import com.example.videohosting.mapper.userMapper.UserMapper;
import com.example.videohosting.model.UserModel;
import com.example.videohosting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserModel loadUserByUsername(String email) {
        User user = userRepository.getUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User" + email + "not found");
        }
        return userMapper.toModel(user);
    }
}
