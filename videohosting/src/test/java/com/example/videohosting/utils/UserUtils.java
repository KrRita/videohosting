package com.example.videohosting.utils;

import com.example.videohosting.controller.JwtAuthorizationController;
import com.example.videohosting.dto.jwtResponse.JwtResponse;
import com.example.videohosting.dto.userDto.CreateUserRequest;
import com.example.videohosting.entity.User;
import com.example.videohosting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    public CreateUserRequest signUp() {
        CreateUserRequest request = new CreateUserRequest("user123@example.com", "Channel523",
                null, null, "Test Description123", "password512");
        jwtAuthorizationController.signUp(request);
        return request;
    }

    public void tearDown() {
        userRepository.deleteAll();
    }

}
