package com.example.videohosting.controller;

import com.example.videohosting.dto.assessmentCommentDto.CreateAssessmentCommentRequest;
import com.example.videohosting.dto.assessmentCommentDto.DeleteAssessmentCommentRequest;
import com.example.videohosting.dto.commentDto.CommentResponse;
import com.example.videohosting.dto.commentDto.CreateCommentRequest;
import com.example.videohosting.dto.commentDto.UpdateCommentRequest;
import com.example.videohosting.entity.Comment;
import com.example.videohosting.entity.Video;
import com.example.videohosting.exception.NotFoundException;
import com.example.videohosting.utils.CommentUtils;
import com.example.videohosting.utils.VideoUtils;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class CommentControllerTest {
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
    private CommentController commentController;

    @Autowired
    private VideoUtils videoUtils;
    @Autowired
    private CommentUtils commentUtils;

    @AfterEach
    public void tearDown() {
        videoUtils.tearDown();
        commentUtils.tearDown();
    }

    @Test
    void postComment() {
        Video video = videoUtils.createAndSaveVideo();
        Long idUser = video.getUser().getIdUser();
        Long idVideo = video.getIdVideo();
        String text = "cat";
        CreateCommentRequest request = new CreateCommentRequest(idVideo, idUser, text);
        ResponseEntity<CommentResponse> response = commentController.postComment(request);
        response.getBody().setIdComment(null);
        response.getBody().setReleaseDateTime(null);
        CommentResponse expected = new CommentResponse(null, idUser, video.getUser().getChannelName(), text,
                null, 0L, 0L);
        assertEquals(expected, response.getBody());
    }

    @Test
    void putComment() {
        Comment comment = commentUtils.createAndSaveComment();
        Long idComment = comment.getIdComment();
        String text = "New text";
        UpdateCommentRequest request = new UpdateCommentRequest(text);
        ResponseEntity<CommentResponse> response = commentController.putComment(idComment, request);
        response.getBody().setReleaseDateTime(null);
        response.getBody().setCountDislikes(null);
        response.getBody().setCountLikes(null);
        CommentResponse expected = new CommentResponse(idComment, comment.getUser().getIdUser(),
                comment.getUser().getChannelName(), text, null, null, null);
        assertEquals(expected, response.getBody());
    }

    @Test
    void deleteComment() {
        Comment comment = commentUtils.createAndSaveComment();
        Long idComment = comment.getIdComment();
        ResponseEntity<?> response = commentController.deleteComment(idComment);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getCommentById() {
        Comment comment = commentUtils.createAndSaveComment();
        Long idComment = comment.getIdComment();
        ResponseEntity<CommentResponse> response = commentController.getCommentById(idComment);
        response.getBody().setCountLikes(null);
        response.getBody().setCountDislikes(null);
        CommentResponse expected = new CommentResponse(idComment, comment.getUser().getIdUser(),
                comment.getUser().getChannelName(), comment.getText(), comment.getReleaseDateTime(),
                null, null);
        assertEquals(expected, response.getBody());
    }

    @Test
    void getCommentByIdNegativeTest() {
        Comment comment = commentUtils.createAndSaveComment();
        Long idComment = comment.getIdComment() + 1;
        assertThrows(NotFoundException.class,() -> commentController.getCommentById(idComment));
    }

    @Test
    void postAssessmentComment() {
        Comment comment = commentUtils.createAndSaveComment();
        Long idComment = comment.getIdComment();
        CreateAssessmentCommentRequest request =
                new CreateAssessmentCommentRequest(comment.getUser().getIdUser(), true);
        ResponseEntity<CommentResponse> response = commentController.postAssessmentComment(idComment, request);
        response.getBody().setCountDislikes(null);
        CommentResponse expected = new CommentResponse(idComment, comment.getUser().getIdUser(),
                comment.getUser().getChannelName(), comment.getText(), comment.getReleaseDateTime(),
                1L, null);
        assertEquals(expected, response.getBody());
    }

    @Test
    void deleteAssessmentComment() {
        Comment comment = commentUtils.createAndSaveComment();
        Long idComment = comment.getIdComment();
        CreateAssessmentCommentRequest createRequest =
                new CreateAssessmentCommentRequest(comment.getUser().getIdUser(), false);
        commentController.postAssessmentComment(idComment, createRequest);
        DeleteAssessmentCommentRequest request = new DeleteAssessmentCommentRequest(comment.getUser().getIdUser());
        ResponseEntity<CommentResponse> response = commentController.deleteAssessmentComment(idComment, request);
        response.getBody().setCountLikes(null);
        CommentResponse expected = new CommentResponse(idComment, comment.getUser().getIdUser(),
                comment.getUser().getChannelName(), comment.getText(), comment.getReleaseDateTime(),
                null, 0L);
        assertEquals(expected, response.getBody());

    }
}