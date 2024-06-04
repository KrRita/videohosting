package com.example.videohosting.repository;

import com.example.videohosting.entity.ViewedVideo;
import com.example.videohosting.utils.ViewedVideoUtils;
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
class ViewedVideoRepositoryTest {

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
    private ViewedVideoRepository repository;
    @Autowired
    private ViewedVideoUtils utils;

    @AfterEach
    public void tearDown() {
        utils.tearDown();
    }

    @Test
    @Transactional
    void getViewedVideosByIdUser() {
        ViewedVideo viewedVideo = utils.createAndSaveViewedVideo();
        Long idUser = viewedVideo.getIdUser();
        List<ViewedVideo> expected = List.of(viewedVideo);
        List<ViewedVideo> result = repository.getViewedVideosByIdUser(idUser);
        assertEquals(expected, result);
    }

    @Test
    void getViewedVideosByIdUserNegativeTest() {
        List<ViewedVideo> result = repository.getViewedVideosByIdUser(102L);
        assertEquals(List.of(), result);
    }

    @Test
    void countViewedVideosByVideo_IdVideo() {
        ViewedVideo viewedVideo = utils.createAndSaveViewedVideo();
        Long idVideo = viewedVideo.getVideo().getIdVideo();
        Long expected = repository.countViewedVideosByVideo_IdVideo(idVideo);
        assertEquals(1L, expected);
    }

    @Test
    void countViewedVideosByVideo_IdVideoNegativeTest() {
        Long expected = repository.countViewedVideosByVideo_IdVideo(202L);
        assertEquals(0L, expected);
    }
}