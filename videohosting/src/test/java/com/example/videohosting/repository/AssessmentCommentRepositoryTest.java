package com.example.videohosting.repository;

import com.example.videohosting.entity.AssessmentComment;
import com.example.videohosting.utils.AssessmentCommentUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Testcontainers
class AssessmentCommentRepositoryTest {

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
    private AssessmentCommentRepository assessmentCommentRepository;
    @Autowired
    private AssessmentCommentUtils assessmentCommentUtils;

    @AfterEach
    public void tearDown() {
        assessmentCommentUtils.tearDown();
    }

    @Test
    void countAssessmentCommentsByIdCommentAndLiked() {
        AssessmentComment savedAssessmentComment = assessmentCommentUtils.createAndSaveAssessmentComment();
        Long idComment = savedAssessmentComment.getIdComment();
        Long count =
                assessmentCommentRepository.countAssessmentCommentsByIdCommentAndLiked(idComment, true);
        assertEquals(1L, count);
    }

    @Test
    void countAssessmentCommentsByIdCommentAndLikedNegativeTest() {
        assessmentCommentUtils.createAndSaveAssessmentComment();
        Long count =
                assessmentCommentRepository.countAssessmentCommentsByIdCommentAndLiked(123L, true);
        assertEquals(0L, count);
    }


    @Test
    void getAssessmentCommentByIdCommentAndIdUser() {
        AssessmentComment savedAssessmentComment = assessmentCommentUtils.createAndSaveAssessmentComment();
        Long expected = savedAssessmentComment.getIdAssessmentComment();
        Long idComment = savedAssessmentComment.getIdComment();
        Long idUser = savedAssessmentComment.getIdUser();
        Long result =
                assessmentCommentRepository.getAssessmentCommentByIdCommentAndIdUser(idComment, idUser);
        assertEquals(expected, result);
    }

    @Test
    void getAssessmentCommentByIdCommentAndIdUserNegativeTest() {
        assessmentCommentUtils.createAndSaveAssessmentComment();
        Long result =
                assessmentCommentRepository.getAssessmentCommentByIdCommentAndIdUser(10L, 324L);
        assertNull(result);
    }

}