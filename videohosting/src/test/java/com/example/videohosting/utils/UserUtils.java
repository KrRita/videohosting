package com.example.videohosting.utils;

import com.example.videohosting.entity.User;
import com.example.videohosting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class UserUtils {
    @Autowired
    private UserRepository userRepository;

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
        user.setSubscriptions(List.of(subscription));
        user.setVideos(List.of());
        user.setPlaylists(List.of());
        userRepository.save(user);

        return subscription;
    }

    public void tearDown() {
        userRepository.deleteAll();
    }

}
