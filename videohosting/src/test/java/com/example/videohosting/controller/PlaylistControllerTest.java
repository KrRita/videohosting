package com.example.videohosting.controller;

import com.example.videohosting.dto.playlistDto.CreatePlaylistRequest;
import com.example.videohosting.dto.playlistDto.PlaylistResponse;
import com.example.videohosting.dto.playlistDto.UpdatePlaylistRequest;
import com.example.videohosting.dto.playlistWithVideosDto.CreatePlaylistWithVideosRequest;
import com.example.videohosting.dto.playlistWithVideosDto.DeletePlaylistWithVideosRequest;
import com.example.videohosting.dto.playlistWithVideosDto.PlaylistWithVideosResponse;
import com.example.videohosting.dto.videoDto.PreviewVideoResponse;
import com.example.videohosting.entity.Playlist;
import com.example.videohosting.entity.PlaylistWithVideos;
import com.example.videohosting.entity.User;
import com.example.videohosting.entity.Video;
import com.example.videohosting.exception.NotFoundException;
import com.example.videohosting.model.PlaylistWithVideosModel;
import com.example.videohosting.service.VideoService;
import com.example.videohosting.utils.PlaylistUtils;
import com.example.videohosting.utils.PlaylistWithVideosUtils;
import com.example.videohosting.utils.UserUtils;
import com.example.videohosting.utils.VideoUtils;
import org.jetbrains.annotations.NotNull;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class PlaylistControllerTest {

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
    private PlaylistController playlistController;
    @Autowired
    private PlaylistUtils playlistUtils;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private VideoUtils videoUtils;
    @Autowired
    private VideoService videoService;
    @Autowired
    private PlaylistWithVideosUtils playlistWithVideosUtils;

    @AfterEach
    public void tearDown() {
        userUtils.tearDown();
        playlistUtils.tearDown();
        videoUtils.tearDown();
    }

    @Test
    void postPlaylist() {
        User user = userUtils.createAndSaveUser();
        String name = "test";
        CreatePlaylistRequest request =
                new CreatePlaylistRequest(user.getIdUser(), name, null);
        ResponseEntity<PlaylistResponse> response = playlistController.postPlaylist(request);
        response.getBody().setIdPlaylist(null);
        response.getBody().setDateCreation(null);
        PlaylistResponse expected =
                new PlaylistResponse(null, name, null, null, 0L);
        assertEquals(expected, response.getBody());
    }

    @Test
    void putPlaylist() {
        Playlist playlist = playlistUtils.createAndSavePlaylist();
        Long idPlaylist = playlist.getIdPlaylist();
        String newName = "new playlist";
        UpdatePlaylistRequest request = new UpdatePlaylistRequest(newName, null);
        ResponseEntity<PlaylistResponse> response = playlistController.putPlaylist(idPlaylist, request);
        response.getBody().setCountVideos(null);
        PlaylistResponse expected = new PlaylistResponse(idPlaylist, newName, playlist.getDateCreation(),
                null, null);
        assertEquals(expected, response.getBody());
    }

    @Test
    void deletePlaylist() {
        Playlist playlist = playlistUtils.createAndSavePlaylist();
        Long idPlaylist = playlist.getIdPlaylist();
        ResponseEntity<?> response = playlistController.deletePlaylist(idPlaylist);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getPlaylistById() {
        Playlist playlist = playlistUtils.createAndSavePlaylist();
        Long idPlaylist = playlist.getIdPlaylist();
        PlaylistResponse expected = new PlaylistResponse(idPlaylist, playlist.getNamePlaylist(),
                playlist.getDateCreation(), null, null);
        ResponseEntity<PlaylistResponse> response = playlistController.getPlaylistById(idPlaylist);
        response.getBody().setCountVideos(null);
        assertEquals(expected, response.getBody());
    }

    @Test
    void getPlaylistByIdNegativeTest() {
        Long idPlaylist = 1L;
        assertThrows(NotFoundException.class, () -> playlistController.getPlaylistById(idPlaylist));
    }

    @Test
    void getVideosFromPlaylist() {
        PlaylistWithVideos playlistWithVideos = playlistWithVideosUtils.createAndSavePlaylistWithVideos();
        Video video = playlistWithVideos.getVideo();
        Long idPlaylist = playlistWithVideos.getIdPlaylist();
        List<PlaylistWithVideosResponse> expected =
                getPlaylistWithVideosResponses(video, playlistWithVideos.getDateOfAddition());
        ResponseEntity<List<PlaylistWithVideosResponse>> response =
                playlistController.getVideosFromPlaylist(idPlaylist);
        response.getBody().get(0).getPreviewVideoResponse().setCountViewing(null);
        assertEquals(expected, response.getBody());
    }

    @Test
    void addVideoInPlaylist() {
        Playlist playlist = playlistUtils.createAndSavePlaylist();
        Video video = videoUtils.createAndSaveVideo();
        CreatePlaylistWithVideosRequest request = new CreatePlaylistWithVideosRequest(video.getIdVideo());
        List<PlaylistWithVideosResponse> expected = getPlaylistWithVideosResponses(video, null);
        ResponseEntity<List<PlaylistWithVideosResponse>> response =
                playlistController.addVideoInPlaylist(playlist.getIdPlaylist(), request);
        response.getBody().get(0).getPreviewVideoResponse().setCountViewing(null);
        response.getBody().get(0).setDateOfAddition(null);
        assertEquals(expected, response.getBody());
    }

    @Test
    void deleteVideoFromPlaylist() {
        PlaylistWithVideos playlistWithVideos = playlistWithVideosUtils.createAndSavePlaylistWithVideos();
        Video video = playlistWithVideos.getVideo();
        Long idPlaylist = playlistWithVideos.getIdPlaylist();
        DeletePlaylistWithVideosRequest request = new DeletePlaylistWithVideosRequest(video.getIdVideo());
        ResponseEntity<List<PlaylistWithVideosResponse>> response =
                playlistController.deleteVideoFromPlaylist(idPlaylist, request);
        assertEquals(List.of(), response.getBody());
    }

    @NotNull
    private List<PlaylistWithVideosResponse> getPlaylistWithVideosResponses(Video video, Timestamp date) {
        PreviewVideoResponse previewVideoResponse = new PreviewVideoResponse(video.getIdVideo(), video.getName(),
                video.getDuration(), video.getDescription(), null, video.getReleaseDateTime(),
                null, video.getUser().getIdUser(), video.getUser().getChannelName());
        PlaylistWithVideosResponse playlistWithVideosResponse =
                new PlaylistWithVideosResponse(previewVideoResponse, date);
        return List.of(playlistWithVideosResponse);
    }

}