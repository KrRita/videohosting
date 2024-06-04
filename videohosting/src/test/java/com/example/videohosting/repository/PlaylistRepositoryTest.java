package com.example.videohosting.repository;

import com.example.videohosting.entity.Playlist;
import com.example.videohosting.utils.PlaylistUtils;
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
class PlaylistRepositoryTest {

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
    private PlaylistRepository repository;
    @Autowired
    private PlaylistUtils utils;

    @Test
    @Transactional
    void getPlaylistsByUser_IdUser() {
        Playlist playlist = utils.createAndSavePlaylist();
        Long idUser = playlist.getUser().getIdUser();
        List<Playlist> expected = List.of(playlist);
        List<Playlist> result = repository.getPlaylistsByUser_IdUser(idUser);
        assertEquals(expected, result);
    }

    @Test
    @Transactional
    void getPlaylistsByUser_IdUserNegativeTest() {
        List<Playlist> result = repository.getPlaylistsByUser_IdUser(237L);
        assertEquals(List.of(), result);
    }

}