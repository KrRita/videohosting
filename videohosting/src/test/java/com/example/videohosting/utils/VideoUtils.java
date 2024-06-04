package com.example.videohosting.utils;

import com.example.videohosting.entity.Category;
import com.example.videohosting.entity.User;
import com.example.videohosting.entity.Video;
import com.example.videohosting.repository.CategoryRepository;
import com.example.videohosting.repository.UserRepository;
import com.example.videohosting.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Component
public class VideoUtils {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public Video createAndSaveVideo() {
        Category category = new Category();
        category.setName("Cats");
        category = categoryRepository.save(category);

        User subscription = new User();
        subscription.setEmail("user@example.com");
        subscription.setChannelName("Channel");
        subscription.setDescription("Description");
        subscription.setDateOfRegistration(new Timestamp(System.currentTimeMillis()));
        subscription.setPassword("password123");
        subscription.setSubscriptions(new ArrayList<>());
        subscription.setVideos(new ArrayList<>());
        subscription.setPlaylists(new ArrayList<>());
        userRepository.save(subscription);

        User user = new User();
        user.setEmail("testuser@example.com");
        user.setChannelName("Test Channel");
        user.setDescription("Test Description");
        user.setDateOfRegistration(new Timestamp(System.currentTimeMillis()));
        user.setPassword("password");
        user.setSubscriptions(List.of(subscription));
        user.setVideos(new ArrayList<>());
        user.setPlaylists(new ArrayList<>());
        user = userRepository.save(user);

        Video video = new Video();
        video.setUser(user);
        video.setName("Test Video");
        video.setDuration(600L);
        video.setDescription("Test Description");
        video.setReleaseDateTime(new Timestamp(System.currentTimeMillis()));
        List<Category> list = new ArrayList<>();
        list.add(category);
        video.setCategories(list);
        return videoRepository.save(video);
    }

    public void tearDown() {
        videoRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();
    }
}
