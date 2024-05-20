package com.example.videohosting.repository;

import com.example.videohosting.entity.PlaylistWithVideos;
import com.example.videohosting.repository.utils.PlaylistWithVideosUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class PlaylistWithVideosRepositoryTest {

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
    private PlaylistWithVideosUtils utils;
    @Autowired
    private PlaylistWithVideosRepository repository;

    @AfterEach
    public void tearDown() {
        utils.tearDown();
    }

    @Test
    @Transactional
    void getPlaylistWithVideosByIdPlaylist() {
        PlaylistWithVideos playlistWithVideos = utils.createAndSavePlaylistWithVideos();
        List<PlaylistWithVideos> expected = List.of(playlistWithVideos);
        Long idPlaylist = playlistWithVideos.getIdPlaylist();
        List<PlaylistWithVideos> result =
                repository.getPlaylistWithVideosByIdPlaylist(idPlaylist);
        assertEquals(expected, result);
    }

    @Test
    void getPlaylistWithVideosByIdPlaylistNegativeTest() {
        List<PlaylistWithVideos> result =
                repository.getPlaylistWithVideosByIdPlaylist(0L);
        assertEquals(List.of(), result);
    }


    @Test
    void countPlaylistWithVideosByIdPlaylist() {
        PlaylistWithVideos playlistWithVideos = utils.createAndSavePlaylistWithVideos();
        Long idPlaylist = playlistWithVideos.getIdPlaylist();
        Long result = repository.countPlaylistWithVideosByIdPlaylist(idPlaylist);
        assertEquals(1L, result);
    }

    @Test
    void countPlaylistWithVideosByIdPlaylistNegativeTest() {
        Long result = repository.countPlaylistWithVideosByIdPlaylist(404L);
        assertEquals(0L, result);
    }


    @Test
    void getPlaylistWithVideosByIdPlaylistAndVideo_IdVideo() {
        PlaylistWithVideos playlistWithVideos = utils.createAndSavePlaylistWithVideos();
        Long expected = playlistWithVideos.getIdPlaylistWithVideos();
        Long idPlaylist = playlistWithVideos.getIdPlaylist();
        Long idVideo = playlistWithVideos.getVideo().getIdVideo();
        Long result =
                repository.getPlaylistWithVideosByIdPlaylistAndVideo_IdVideo(idPlaylist, idVideo);
        assertEquals(expected, result);
    }
    @Test
    void getPlaylistWithVideosByIdPlaylistAndVideo_IdVideoNegativeTest() {
        Long idPlaylist = 101L;
        Long idVideo = 664L;
        Long result =
                repository.getPlaylistWithVideosByIdPlaylistAndVideo_IdVideo(idPlaylist, idVideo);
        assertNull(result);
    }

}