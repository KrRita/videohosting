package com.example.videohosting.repository;

import com.example.videohosting.entity.AssessmentComment;
import com.example.videohosting.entity.AssessmentVideo;
import com.example.videohosting.entity.User;
import com.example.videohosting.entity.Video;
import com.example.videohosting.repository.utils.AssessmentVideoUtils;
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

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class AssessmentVideoRepositoryTest {

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
    private AssessmentVideoRepository repository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoRepository videoRepository;


    @Autowired
    private AssessmentVideoUtils utils;

    @AfterEach
    public void tearDown() {
        utils.tearDown();
    }

    @Test
    @Transactional
    void getAssessmentVideoByIdUserAndLiked() {
        AssessmentVideo assessmentVideo = utils.createAndSaveAssessmentVideo();
        List<AssessmentVideo> expected = List.of(assessmentVideo);
        Long idUser = assessmentVideo.getIdUser();
        List<AssessmentVideo> result = repository.getAssessmentVideoByIdUserAndLiked(idUser, true);
        assertEquals(expected, result);
    }

    @Test
    @Transactional
    void getAssessmentVideoByIdUserAndLikedNegativeTest() {
        List<AssessmentVideo> result = repository.getAssessmentVideoByIdUserAndLiked(212L, true);
        assertEquals(List.of(),result);
    }


    @Test
    void countAssessmentVideosByVideo_IdVideoAndLiked() {
        AssessmentVideo assessmentVideo = utils.createAndSaveAssessmentVideo();
        Long idVideo = assessmentVideo.getVideo().getIdVideo();
        Long count = repository.countAssessmentVideosByVideo_IdVideoAndLiked(idVideo, true);
        assertEquals(1L, count);
    }

    @Test
    void countAssessmentVideosByVideo_IdVideoAndLikedNegativeTest() {
        Long count = repository.countAssessmentVideosByVideo_IdVideoAndLiked(787L, true);
        assertEquals(0L, count);
    }


    @Test
    void getAssessmentVideoByIdUserAndVideo_IdVideo() {
        Long id = repository.getAssessmentVideoByIdUserAndVideo_IdVideo(121L, 301L);
        assertNull(id);
    }
}