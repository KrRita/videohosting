package com.example.videohosting.utils;

import com.example.videohosting.controller.JwtAuthorizationController;
import com.example.videohosting.dto.userDto.CreateUserRequest;
import com.example.videohosting.entity.User;
import com.example.videohosting.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserUtils {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtAuthorizationController jwtAuthorizationController;

    public User createAndSaveUser() {
        User subscription = new User();
        subscription.setEmail("user@example.com");
        subscription.setChannelName("Channel");
        subscription.setDescription("Description");
        subscription.setDateOfRegistration(new Timestamp(System.currentTimeMillis()));
        subscription.setPassword("password123");
        subscription.setSubscriptions(List.of());
        subscription.setVideos(List.of());
        subscription.setPlaylists(List.of());
        userRepository.save(subscription);

        User user = new User();
        user.setEmail("testuser@example.com");
        user.setChannelName("Test Channel");
        user.setDescription("Test Description");
        user.setDateOfRegistration(new Timestamp(System.currentTimeMillis()));
        user.setPassword("password");
        List<User> list = new ArrayList<>();
        list.add(subscription);
        user.setSubscriptions(list);
        user.setVideos(List.of());
        user.setPlaylists(List.of());
        userRepository.save(user);

        return subscription;
    }

    public CreateUserRequest signUp() throws JsonProcessingException {
        CreateUserRequest request = new CreateUserRequest("user123@example.com", "Channel523", "Test Description123", "password512");
        ObjectMapper objectMapper = new ObjectMapper();
        String stringRequest = objectMapper.writeValueAsString(request);
        jwtAuthorizationController.signUp(stringRequest,null,null);
        return request;
    }

    public void tearDown() {
        userRepository.deleteAll();
    }

}
