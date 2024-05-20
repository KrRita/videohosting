package com.example.videohosting.repository.utils;

import com.example.videohosting.entity.AssessmentComment;
import com.example.videohosting.entity.Comment;
import com.example.videohosting.entity.User;
import com.example.videohosting.entity.Video;
import com.example.videohosting.repository.AssessmentCommentRepository;
import com.example.videohosting.repository.CommentRepository;
import com.example.videohosting.repository.UserRepository;
import com.example.videohosting.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class AssessmentCommentUtils {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private AssessmentCommentRepository assessmentCommentRepository;

    public AssessmentComment createAndSaveAssessmentComment() {
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
        video.setDuration(100L);
        video.setDescription("Test Description");
        video.setReleaseDateTime(new Timestamp(System.currentTimeMillis()));
        video.setCategories(List.of());
        video = videoRepository.save(video);

        Comment comment = new Comment();
        comment.setIdVideo(video.getIdVideo());
        comment.setUser(user);
        comment.setText("Test Comment");
        comment.setReleaseDateTime(new Timestamp(System.currentTimeMillis()));
        comment = commentRepository.save(comment);

        AssessmentComment assessmentComment = new AssessmentComment();
        assessmentComment.setIdUser(user.getIdUser());
        assessmentComment.setIdComment(comment.getIdComment());
        assessmentComment.setLiked(true);
        return assessmentCommentRepository.save(assessmentComment);
    }

    public void tearDown() {
        assessmentCommentRepository.deleteAll();
        commentRepository.deleteAll();
        videoRepository.deleteAll();
        userRepository.deleteAll();
    }

}
