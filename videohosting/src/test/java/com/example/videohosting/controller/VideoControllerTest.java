package com.example.videohosting.controller;

import com.example.videohosting.dto.assessementVideoDto.CreateAssessmentVideoRequest;
import com.example.videohosting.dto.assessementVideoDto.DeleteAssessmentVideoRequest;
import com.example.videohosting.dto.commentDto.CommentResponse;
import com.example.videohosting.dto.userDto.PreviewUserResponse;
import com.example.videohosting.dto.videoDto.CreateVideoRequest;
import com.example.videohosting.dto.videoDto.PreviewVideoResponse;
import com.example.videohosting.dto.videoDto.UpdateVideoRequest;
import com.example.videohosting.dto.videoDto.VideoResponse;
import com.example.videohosting.dto.viewedVideoDto.CreateViewedVideoRequest;
import com.example.videohosting.entity.Comment;
import com.example.videohosting.entity.User;
import com.example.videohosting.entity.Video;
import com.example.videohosting.exception.LoadFileException;
import com.example.videohosting.exception.NotFoundException;
import com.example.videohosting.repository.CommentRepository;
import com.example.videohosting.repository.UserRepository;
import com.example.videohosting.utils.VideoUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
class VideoControllerTest {
    @Container
    public static PostgreSQLContainer<?> postgresqlContainer =
            new PostgreSQLContainer<>("postgres:16");

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }

    @Autowired
    private VideoController videoController;
    @Autowired
    private VideoUtils videoUtils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @AfterEach
    public void tearDown() {
        videoUtils.tearDown();
    }

    @Test
    void postVideo() throws JsonProcessingException {
        User user = new User();
        user.setEmail("testuser123@example.com");
        user.setChannelName("Test Channel123");
        user.setDescription("Test Description123");
        user.setDateOfRegistration(new Timestamp(System.currentTimeMillis()));
        user.setPassword("password123");
        user.setSubscriptions(List.of());
        user.setVideos(new ArrayList<>());
        user.setPlaylists(new ArrayList<>());
        user = userRepository.save(user);

        List<String> categories = new ArrayList<>();
        categories.add("Cats");

        CreateVideoRequest request = new CreateVideoRequest(user.getIdUser(), "Test Video123",
                "Test Description123", categories);

        String requestString = objectMapper.writeValueAsString(request);

        assertThrows(LoadFileException.class, () -> videoController.postVideo(requestString, null, null));
    }

    @Test
    void putVideo() throws JsonProcessingException {
        Video video = videoUtils.createAndSaveVideo();
        User user = video.getUser();
        String newName = "new video";
        String description = "aboba";
        UpdateVideoRequest request = new UpdateVideoRequest(video.getIdVideo(), newName, description);
        String requestString = objectMapper.writeValueAsString(request);
        VideoResponse response = videoController.putVideo(requestString, null).getBody();
        PreviewUserResponse previewUserResponse = new PreviewUserResponse(
                user.getIdUser(), user.getChannelName(), 0L);
        List<String> categories = new ArrayList<>();
        categories.add("Cats");
        VideoResponse expected = new VideoResponse(video.getIdVideo(), previewUserResponse, newName,
                video.getDuration(), description, video.getReleaseDateTime(),
                categories, 0L, 0L, 0L);
        assertEquals(expected, response);
    }

    @Test
    void deleteVideo() {
        Video video = videoUtils.createAndSaveVideo();
        ResponseEntity<?> response = videoController.deleteVideo(video.getIdVideo());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getVideoById() {
        Video video = videoUtils.createAndSaveVideo();
        User user = video.getUser();
        List<String> categories = new ArrayList<>();
        categories.add("Cats");

        PreviewUserResponse previewUserResponse = new PreviewUserResponse(
                user.getIdUser(), user.getChannelName(), 0L);
        VideoResponse response = videoController.getVideoById(video.getIdVideo()).getBody();
        VideoResponse expected = new VideoResponse(video.getIdVideo(), previewUserResponse, video.getName(),
                video.getDuration(), video.getDescription(), video.getReleaseDateTime(),
                categories, 0L, 0L, 0L);
        assertEquals(expected, response);
    }

    @Test
    void getVideoByIdNegativeTest() {
        Video video = videoUtils.createAndSaveVideo();
        Long idVideo = video.getIdVideo() + 1;
        assertThrows(NotFoundException.class, () -> videoController.getVideoById(idVideo));
    }

    @Test
    void getVideosByName() {
        Video video = videoUtils.createAndSaveVideo();
        String name = video.getName();
        List<PreviewVideoResponse> responses = videoController.getVideosByName(name).getBody();
        PreviewVideoResponse expected = new PreviewVideoResponse(video.getIdVideo(), name,
                video.getDuration(), video.getDescription(), video.getReleaseDateTime(),
                0L, video.getUser().getIdUser(), video.getUser().getChannelName());
        assert responses != null;
        assertEquals(expected, responses.get(0));
    }

    @Test
    void getVideosByNameNegativeTest() {
        Video video = videoUtils.createAndSaveVideo();
        String name = video.getName() + "aboba";
        List<PreviewVideoResponse> responses = videoController.getVideosByName(name).getBody();
        assertEquals(List.of(), responses);
    }


    @Test
    void getVideosByUserName() {
        Video video = videoUtils.createAndSaveVideo();
        String userName = video.getUser().getChannelName();
        List<PreviewVideoResponse> responses = videoController.getVideosByUserName(userName).getBody();
        PreviewVideoResponse expected = new PreviewVideoResponse(video.getIdVideo(), video.getName(),
                video.getDuration(), video.getDescription(), video.getReleaseDateTime(),
                0L, video.getUser().getIdUser(), userName);
        assert responses != null;
        assertEquals(expected, responses.get(0));

    }

    @Test
    void getVideosByUserNameNegativeTest() {
        Video video = videoUtils.createAndSaveVideo();
        String userName = video.getUser().getChannelName() + "cat";
        List<PreviewVideoResponse> responses = videoController.getVideosByUserName(userName).getBody();
        assertEquals(List.of(), responses);
    }


    @Test
    void getVideosByCategories() {
        Video video = videoUtils.createAndSaveVideo();
        List<String> categories = new ArrayList<>();
        categories.add("Cats");
        List<PreviewVideoResponse> responses = videoController.getVideosByCategories(categories).getBody();
        PreviewVideoResponse expected = new PreviewVideoResponse(video.getIdVideo(), video.getName(),
                video.getDuration(), video.getDescription(), video.getReleaseDateTime(),
                0L, video.getUser().getIdUser(), video.getUser().getChannelName());
        assert responses != null;
        assertEquals(expected, responses.get(0));
    }

    @Test
    void getVideosByCategoriesNegativeTest() {
        List<String> categories = new ArrayList<>();
        categories.add("Dogs");
        List<PreviewVideoResponse> responses = videoController.getVideosByCategories(categories).getBody();
        assertEquals(List.of(), responses);
    }


    @Test
    void createAssessmentVideo() {
        Video video = videoUtils.createAndSaveVideo();
        User user = video.getUser();
        List<String> categories = new ArrayList<>();
        categories.add("Cats");
        PreviewUserResponse previewUserResponse = new PreviewUserResponse(
                user.getIdUser(), user.getChannelName(), 0L);
        CreateAssessmentVideoRequest request = new CreateAssessmentVideoRequest(user.getIdUser(), true);
        VideoResponse response = videoController.createAssessmentVideo(video.getIdVideo(), request).getBody();
        VideoResponse expected = new VideoResponse(video.getIdVideo(), previewUserResponse, video.getName(),
                video.getDuration(), video.getDescription(), video.getReleaseDateTime(),
                categories, 0L, 1L, 0L);
        assertEquals(expected, response);
    }

    @Test
    void deleteAssessmentVideo() {
        Video video = videoUtils.createAndSaveVideo();
        User user = video.getUser();
        List<String> categories = new ArrayList<>();
        categories.add("Cats");
        PreviewUserResponse previewUserResponse = new PreviewUserResponse(
                user.getIdUser(), user.getChannelName(), 0L);
        CreateAssessmentVideoRequest request = new CreateAssessmentVideoRequest(user.getIdUser(), true);
        DeleteAssessmentVideoRequest deleteRequest = new DeleteAssessmentVideoRequest(user.getIdUser());
        videoController.createAssessmentVideo(video.getIdVideo(), request).getBody();
        VideoResponse response = videoController.deleteAssessmentVideo(video.getIdVideo(), deleteRequest).getBody();
        VideoResponse expected = new VideoResponse(video.getIdVideo(), previewUserResponse, video.getName(),
                video.getDuration(), video.getDescription(), video.getReleaseDateTime(),
                categories, 0L, 0L, 0L);
        assertEquals(expected, response);
    }

    @Test
    void createViewedVideo() {
        Video video = videoUtils.createAndSaveVideo();
        User user = video.getUser();
        List<String> categories = new ArrayList<>();
        categories.add("Cats");
        PreviewUserResponse previewUserResponse = new PreviewUserResponse(
                user.getIdUser(), user.getChannelName(), 0L);
        CreateViewedVideoRequest request = new CreateViewedVideoRequest(user.getIdUser());
        VideoResponse response = videoController.createViewedVideo(video.getIdVideo(), request).getBody();
        VideoResponse expected = new VideoResponse(video.getIdVideo(), previewUserResponse, video.getName(),
                video.getDuration(), video.getDescription(), video.getReleaseDateTime(),
                categories, 1L, 0L, 0L);
        assertEquals(expected, response);
    }

    @Test
    void getCommentsOnTheVideo() {
        Video video = videoUtils.createAndSaveVideo();
        User user = video.getUser();
        String text = "Test Comment123";
        Comment comment = new Comment();
        comment.setIdVideo(video.getIdVideo());
        comment.setUser(user);
        comment.setText(text);
        comment.setReleaseDateTime(new Timestamp(System.currentTimeMillis()));
        comment = commentRepository.save(comment);

        List<CommentResponse> responses = videoController.getCommentsOnTheVideo(video.getIdVideo()).getBody();
        CommentResponse expected = new CommentResponse(comment.getIdComment(), user.getIdUser(),
                user.getChannelName(), text, comment.getReleaseDateTime(), 0L, 0L);
        assert responses != null;
        assertEquals(expected, responses.get(0));
    }

    @Test
    void getCommentsOnTheVideoNegativeTest() {
        Video video = videoUtils.createAndSaveVideo();
        List<CommentResponse> responses = videoController.getCommentsOnTheVideo(video.getIdVideo()).getBody();
        assertEquals(List.of(), responses);

    }
}