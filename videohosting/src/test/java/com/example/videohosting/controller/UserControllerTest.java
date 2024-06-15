package com.example.videohosting.controller;

import com.example.videohosting.dto.playlistDto.PlaylistResponse;
import com.example.videohosting.dto.userDto.CreateUserRequest;
import com.example.videohosting.dto.userDto.PreviewUserResponse;
import com.example.videohosting.dto.userDto.UpdateSubscriptionsRequest;
import com.example.videohosting.dto.userDto.UpdateUserRequest;
import com.example.videohosting.dto.userDto.UserResponse;
import com.example.videohosting.dto.videoDto.PreviewVideoResponse;
import com.example.videohosting.dto.viewedVideoDto.ViewedVideoResponse;
import com.example.videohosting.entity.AssessmentVideo;
import com.example.videohosting.entity.Category;
import com.example.videohosting.entity.Playlist;
import com.example.videohosting.entity.User;
import com.example.videohosting.entity.Video;
import com.example.videohosting.entity.ViewedVideo;
import com.example.videohosting.exception.NotFoundException;
import com.example.videohosting.model.UserModel;
import com.example.videohosting.repository.AssessmentVideoRepository;
import com.example.videohosting.repository.PlaylistRepository;
import com.example.videohosting.repository.UserRepository;
import com.example.videohosting.repository.VideoRepository;
import com.example.videohosting.repository.ViewedVideoRepository;
import com.example.videohosting.utils.UserUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
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
class UserControllerTest {
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
    private UserController userController;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private ViewedVideoRepository viewedVideoRepository;
    @Autowired
    private AssessmentVideoRepository assessmentVideoRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @AfterEach
    public void tearDown() {
        userUtils.tearDown();
    }

    @Test
    void putUser() throws JsonProcessingException {
        CreateUserRequest createUserRequest = userUtils.signUp();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(createUserRequest.getEmail(), createUserRequest.getPassword()));
        String channelName = "cat";
        UpdateUserRequest request = new UpdateUserRequest(channelName,  null, null);
        String stringRequest = objectMapper.writeValueAsString(request);
        UserResponse response = userController.putUser(stringRequest, null,null, authentication).getBody();
        assert response != null;
        UserResponse expected = new UserResponse(response.getIdUser(), createUserRequest.getEmail(),
                channelName, createUserRequest.getDescription(), response.getDateOfRegistration(),
                response.getCountSubscribers());
        assertEquals(expected, response);
    }


    @Test
    void postSubscription() throws JsonProcessingException {
        CreateUserRequest createUserRequest = userUtils.signUp();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(createUserRequest.getEmail(), createUserRequest.getPassword()));
        User subscription = userUtils.createAndSaveUser();
        UpdateSubscriptionsRequest request = new UpdateSubscriptionsRequest(subscription.getIdUser());
        userController.postSubscription(request, authentication);
        UserResponse result = userController.getUserById(subscription.getIdUser()).getBody();
        assertEquals(2L, result.getCountSubscribers());
    }

    @Test
    void deleteSubscription() throws JsonProcessingException {
        CreateUserRequest createUserRequest = userUtils.signUp();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(createUserRequest.getEmail(), createUserRequest.getPassword()));
        User subscription = userUtils.createAndSaveUser();
        UpdateSubscriptionsRequest request = new UpdateSubscriptionsRequest(subscription.getIdUser());
        userController.postSubscription(request, authentication);
        userController.deleteSubscription(request, authentication);
        UserResponse result = userController.getUserById(subscription.getIdUser()).getBody();
        assertEquals(1L, result.getCountSubscribers());
    }

    @Test
    void deleteUser() throws JsonProcessingException {
        CreateUserRequest createUserRequest = userUtils.signUp();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(createUserRequest.getEmail(), createUserRequest.getPassword()));
        ResponseEntity<?> response = userController.deleteUser(authentication);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getUserById() throws JsonProcessingException {
        CreateUserRequest createUserRequest = userUtils.signUp();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(createUserRequest.getEmail(), createUserRequest.getPassword()));
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser();
        UserResponse response = userController.getUserById(id).getBody();
        UserResponse expected = new UserResponse(response.getIdUser(), createUserRequest.getEmail(),
                createUserRequest.getChannelName(), createUserRequest.getDescription(), response.getDateOfRegistration(),
                response.getCountSubscribers());
        assertEquals(expected, response);
    }

    @Test
    void getUserByIdNegativeTest() throws JsonProcessingException {
        CreateUserRequest createUserRequest = userUtils.signUp();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(createUserRequest.getEmail(), createUserRequest.getPassword()));
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser() + 1;
        assertThrows(NotFoundException.class, () -> userController.getUserById(id));
    }


    @Test
    void getUserSubscriptions() throws JsonProcessingException {
        CreateUserRequest createUserRequest = userUtils.signUp();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(createUserRequest.getEmail(), createUserRequest.getPassword()));
        User subscription = userUtils.createAndSaveUser();
        UpdateSubscriptionsRequest request = new UpdateSubscriptionsRequest(subscription.getIdUser());
        userController.postSubscription(request, authentication);
        List<PreviewUserResponse> responses = userController.getUserSubscriptions(authentication).getBody();
        PreviewUserResponse expectedUser = new PreviewUserResponse(
                subscription.getIdUser(), subscription.getChannelName(), 2L);
        List<PreviewUserResponse> expectedList = new ArrayList<>();
        expectedList.add(expectedUser);
        assertEquals(expectedList, responses);
    }

    @Test
    void getUserSubscriptionsEmptyList() throws JsonProcessingException {
        CreateUserRequest createUserRequest = userUtils.signUp();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(createUserRequest.getEmail(), createUserRequest.getPassword()));
        List<PreviewUserResponse> responses = userController.getUserSubscriptions(authentication).getBody();
        assertEquals(List.of(), responses);
    }

    @Test
    @Transactional
    void getSubscriptionsVideos() throws JsonProcessingException {
        CreateUserRequest createUserRequest = userUtils.signUp();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(createUserRequest.getEmail(), createUserRequest.getPassword()));
        User subscription = userUtils.createAndSaveUser();
        UpdateSubscriptionsRequest request = new UpdateSubscriptionsRequest(subscription.getIdUser());
        userController.postSubscription(request, authentication);
        Video video = createAndSaveVideoForUser(subscription.getIdUser());
        List<PreviewVideoResponse> response = userController.getSubscriptionsVideos(authentication).getBody();
        PreviewVideoResponse previewVideoResponse = new PreviewVideoResponse(video.getIdVideo(), video.getName(),
                video.getDuration(), video.getDescription(), video.getReleaseDateTime(),
                0L, subscription.getIdUser(), subscription.getChannelName());
        List<PreviewVideoResponse> expected = new ArrayList<>();
        expected.add(previewVideoResponse);
        assertEquals(expected, response);
    }

    @Test
    @Transactional
    void getViewedVideos() throws JsonProcessingException {
        CreateUserRequest createUserRequest = userUtils.signUp();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(createUserRequest.getEmail(), createUserRequest.getPassword()));
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser();
        Video video = createAndSaveVideoForUser(id);
        createAndSaveViewedVideo(id, video);
        List<ViewedVideoResponse> responses = userController.getViewedVideos(authentication).getBody();
        PreviewVideoResponse previewVideoResponse = new PreviewVideoResponse(video.getIdVideo(), video.getName(),
                video.getDuration(), video.getDescription(), video.getReleaseDateTime(),
                1L, id, createUserRequest.getChannelName());
        ViewedVideoResponse expected = new ViewedVideoResponse(previewVideoResponse, responses.get(0).getDateOfViewing());
        assertEquals(expected, responses.get(0));
    }

    @Test
    @Transactional
    void getLikedVideos() throws JsonProcessingException {
        CreateUserRequest createUserRequest = userUtils.signUp();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(createUserRequest.getEmail(), createUserRequest.getPassword()));
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser();
        Video video = createAndSaveVideoForUser(id);
        createAndSaveAssessmentOnVideo(id, video);
        List<PreviewVideoResponse> response = userController.getLikedVideos(authentication).getBody();
        PreviewVideoResponse expected = new PreviewVideoResponse(video.getIdVideo(), video.getName(),
                video.getDuration(), video.getDescription(), video.getReleaseDateTime(),
                0L, id, createUserRequest.getChannelName());
        assertEquals(expected, response.get(0));
    }

    @Test
    void getUserPlaylists() throws JsonProcessingException {
        CreateUserRequest createUserRequest = userUtils.signUp();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(createUserRequest.getEmail(), createUserRequest.getPassword()));
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser();
        Playlist playlist = createAndSavePlaylistForUser(id);
        List<PlaylistResponse> responses = userController.getUserPlaylists(authentication).getBody();
        PlaylistResponse expected = new PlaylistResponse(playlist.getIdPlaylist(), playlist.getNamePlaylist(),
                playlist.getDateCreation(), 0L);
        assertEquals(expected, responses.get(0));
    }

    @Transactional
    public Video createAndSaveVideoForUser(Long idUser) {
        User user = userRepository.findById(idUser).orElse(null);
        Video video = new Video();
        video.setUser(user);
        video.setName("Test Video123");
        video.setDuration(630L);
        video.setDescription("Test Description123");
        video.setReleaseDateTime(new Timestamp(System.currentTimeMillis()));
        List<Category> list = new ArrayList<>();
        video.setCategories(list);
        return videoRepository.save(video);
    }

    @Transactional
    public void createAndSaveViewedVideo(Long idUser, Video video) {
        ViewedVideo viewedVideo = new ViewedVideo();
        viewedVideo.setIdUser(idUser);
        viewedVideo.setVideo(video);
        viewedVideo.setDateOfViewing(new Timestamp(System.currentTimeMillis()));
        viewedVideoRepository.save(viewedVideo);
    }

    public void createAndSaveAssessmentOnVideo(Long idUser, Video video) {
        AssessmentVideo assessmentVideo = new AssessmentVideo();
        assessmentVideo.setIdUser(idUser);
        assessmentVideo.setVideo(video);
        assessmentVideo.setLiked(true);
        assessmentVideo.setDateOfAssessment(new Timestamp(System.currentTimeMillis()));
        assessmentVideoRepository.save(assessmentVideo);
    }

    public Playlist createAndSavePlaylistForUser(Long idUser) {
        User user = userRepository.findById(idUser).orElse(null);
        Playlist playlist = new Playlist();
        playlist.setUser(user);
        playlist.setNamePlaylist("Test Playlist123");
        playlist.setDateCreation(new Timestamp(System.currentTimeMillis()));
        return playlistRepository.save(playlist);
    }
}