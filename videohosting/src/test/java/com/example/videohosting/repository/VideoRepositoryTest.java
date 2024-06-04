package com.example.videohosting.repository;

import com.example.videohosting.entity.Video;
import com.example.videohosting.utils.VideoUtils;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
class VideoRepositoryTest {
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
    private VideoRepository repository;
    @Autowired
    private VideoUtils utils;

    @AfterEach
    public void tearDown() {
        utils.tearDown();
    }

    @Test
    @Transactional
    void getVideosByUser_IdUser() {
        Video video = utils.createAndSaveVideo();
        Long idUser = video.getUser().getIdUser();
        List<Video> result = repository.getVideosByUser_IdUser(idUser);
        assertEquals(List.of(), result);
    }

    @Test
    void getVideosByUser_IdUserNegativeTest() {
        List<Video> result = repository.getVideosByUser_IdUser(421L);
        assertEquals(List.of(), result);
    }


    @Test
    @Transactional
    void findByNameContaining() {
        Video video = utils.createAndSaveVideo();
        String name = video.getName();
        List<Video> expected = List.of(video);
        List<Video> result = repository.findByNameContaining(name);
        assertEquals(expected, result);
    }

    @Test
    @Transactional
    void findByUser_ChannelNameContaining() {
        Video video = utils.createAndSaveVideo();
        String userName = video.getUser().getChannelName();
        List<Video> expected = List.of(video);
        List<Video> result = repository.findByUser_ChannelNameContaining(userName);
        assertEquals(expected, result);
    }

    @Test
    @Transactional
    void findDistinctByCategories_NameIn() {
        Video video = utils.createAndSaveVideo();
        List<Video> expected = List.of(video);
        String category = video.getCategories().get(0).getName();
        List<Video> result = repository.findDistinctByCategories_NameIn(List.of(category));
        assertEquals(expected, result);
    }

    @Test
    void findByNameContainingNegativeTest() {
        String name = "Cats";
        List<Video> result = repository.findByNameContaining(name);
        assertEquals(List.of(), result);
    }

    @Test
    void findByUser_ChannelNameContainingNegativeTest() {
        String userName = "Vasya";
        List<Video> result = repository.findByUser_ChannelNameContaining(userName);
        assertEquals(List.of(), result);
    }

    @Test
    void findDistinctByCategories_NameInNegativeTest() {
        String category = "Dogs";
        List<Video> result = repository.findDistinctByCategories_NameIn(List.of(category));
        assertEquals(List.of(), result);
    }

}