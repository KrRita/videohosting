package com.example.videohosting.repository.utils;

import com.example.videohosting.entity.Comment;
import com.example.videohosting.entity.User;
import com.example.videohosting.entity.Video;
import com.example.videohosting.repository.CommentRepository;
import com.example.videohosting.repository.UserRepository;
import com.example.videohosting.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class CommentUtils {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private VideoRepository videoRepository;

    public Comment createAndSaveComment() {
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

        Comment comment = new Comment();
        comment.setIdVideo(video.getIdVideo());
        comment.setUser(user);
        comment.setText("Test Comment");
        comment.setReleaseDateTime(new Timestamp(System.currentTimeMillis()));
        return commentRepository.save(comment);
    }
    public void tearDown() {
        commentRepository.deleteAll();
        videoRepository.deleteAll();
        userRepository.deleteAll();
    }

}
