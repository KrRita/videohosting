package com.example.videohosting.repository;

import com.example.videohosting.entity.Comment;
import com.example.videohosting.repository.utils.CommentUtils;
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
class CommentRepositoryTest {
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
    private CommentUtils utils;
    @Autowired
    private CommentRepository repository;

    @Test
    @Transactional
    void getCommentsByIdVideo() {
        Comment comment = utils.createAndSaveComment();
        List<Comment> expected = List.of(comment);
        List<Comment> result = repository.getCommentsByIdVideo(comment.getIdVideo());
        assertEquals(expected, result);
    }
    @Test
    void getCommentsByIdVideoNegativeTest() {
        List<Comment> result = repository.getCommentsByIdVideo(345L);
        assertEquals(List.of(), result);
    }


}