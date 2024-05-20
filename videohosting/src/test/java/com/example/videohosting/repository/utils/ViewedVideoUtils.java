package com.example.videohosting.repository.utils;

import com.example.videohosting.entity.User;
import com.example.videohosting.entity.Video;
import com.example.videohosting.entity.ViewedVideo;
import com.example.videohosting.repository.UserRepository;
import com.example.videohosting.repository.VideoRepository;
import com.example.videohosting.repository.ViewedVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class ViewedVideoUtils {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private ViewedVideoRepository viewedVideoRepository;

    public ViewedVideo createAndSaveViewedVideo() {
        User user = new User();
        user.setEmail("testuser@example.com");
        user.setChannelName("Test Channel");
        user.setDescription("Test Description");
        user.setDateOfRegistration(new Timestamp(System.currentTimeMillis()));
        user.setPassword("password");
        user.setSubscriptions(List.of());
        user.setVideos(List.of());
        user.setPlaylists(List.of());
        user = userRepository.save(user);

        Video video = new Video();
        video.setUser(user);
        video.setName("Test Video");
        video.setDuration(600L);
        video.setDescription("Test Description");
        video.setReleaseDateTime(new Timestamp(System.currentTimeMillis()));
        video.setCategories(List.of());
        video = videoRepository.save(video);

        ViewedVideo viewedVideo = new ViewedVideo();
        viewedVideo.setIdUser(user.getIdUser());
        viewedVideo.setVideo(video);
        viewedVideo.setDateOfViewing(new Timestamp(System.currentTimeMillis()));
        return viewedVideoRepository.save(viewedVideo);
    }

    public void tearDown() {
        viewedVideoRepository.deleteAll();
        videoRepository.deleteAll();
        userRepository.deleteAll();
    }
}
