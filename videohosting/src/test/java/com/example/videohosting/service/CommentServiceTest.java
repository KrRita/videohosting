package com.example.videohosting.service;

import com.example.videohosting.entity.AssessmentComment;
import com.example.videohosting.entity.Comment;
import com.example.videohosting.entity.User;
import com.example.videohosting.exception.NotFoundException;
import com.example.videohosting.mapper.AssessmentCommentMapper;
import com.example.videohosting.mapper.CommentMapper;
import com.example.videohosting.model.AssessmentCommentModel;
import com.example.videohosting.model.CommentModel;
import com.example.videohosting.model.UserModel;
import com.example.videohosting.repository.AssessmentCommentRepository;
import com.example.videohosting.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private AssessmentCommentRepository assessmentCommentRepository;
    @Mock
    private AssessmentCommentMapper assessmentCommentMapper;
    @InjectMocks
    private CommentService commentService;

    private Comment comment;
    private CommentModel commentModel;
    private AssessmentComment assessmentComment;
    private AssessmentCommentModel assessmentCommentModel;

    @BeforeEach
    public void setUp() {
        //MockitoAnnotations.openMocks(this);
        Timestamp date = Timestamp.valueOf(LocalDateTime.now());

        User user = new User();
        user.setIdUser(1L);
        user.setEmail("testuser@example.com");
        user.setChannelName("Test Channel");
        user.setDescription("Test Description");
        user.setDateOfRegistration(date);
        user.setPassword("password");
        user.setSubscriptions(List.of());
        user.setVideos(List.of());
        user.setPlaylists(List.of());

        UserModel userModel = new UserModel();
        userModel.setIdUser(1L);
        userModel.setEmail("testuser@example.com");
        userModel.setChannelName("Test Channel");
        userModel.setDescription("Test Description");
        userModel.setDateOfRegistration(date);
        userModel.setPassword("password");
        userModel.setSubscriptions(List.of());
        userModel.setVideos(List.of());
        userModel.setPlaylists(List.of());
        userModel.setCountSubscribers(0L);

        comment = new Comment();
        comment.setIdComment(1L);
        comment.setIdVideo(1L);
        comment.setUser(user);
        comment.setText("Test comment");
        comment.setReleaseDateTime(date);

        commentModel = new CommentModel();
        commentModel.setIdComment(1L);
        commentModel.setIdVideo(1L);
        commentModel.setUser(userModel);
        commentModel.setText("Test comment");
        commentModel.setReleaseDateTime(date);
        commentModel.setCountLikes(0L);
        commentModel.setCountDislikes(0L);

        assessmentComment = new AssessmentComment();
        assessmentComment.setIdAssessmentComment(1L);
        assessmentComment.setIdUser(1L);
        assessmentComment.setIdComment(1L);
        assessmentComment.setLiked(true);

        assessmentCommentModel = new AssessmentCommentModel();
        assessmentCommentModel.setIdAssessmentComment(1L);
        assessmentCommentModel.setIdUser(1L);
        assessmentCommentModel.setIdComment(1L);
        assessmentCommentModel.setLiked(true);
    }

    @Test
    void insertComment() {
        when(commentMapper.toEntity(commentModel)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toModelFromEntity(comment)).thenReturn(commentModel);
        CommentModel result = commentService.insertComment(commentModel);
        assertEquals(commentModel, result);
    }

    @Test
    void updateComment() {
        Long id = comment.getIdComment();
        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toModelFromEntity(comment)).thenReturn(commentModel);
        when(assessmentCommentRepository.countAssessmentCommentsByIdCommentAndLiked(id, true)).thenReturn(0L);
        when(assessmentCommentRepository.countAssessmentCommentsByIdCommentAndLiked(id, false)).thenReturn(0L);

        CommentModel result = commentService.updateComment(commentModel);
        assertEquals(commentModel, result);
    }

    @Test
    void deleteComment() {
        Long id = 1L;
        commentService.deleteComment(id);
        verify(commentRepository).deleteById(id);
    }

    @Test
    void findCommentById() {
        Long id = comment.getIdComment();
        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));
        when(commentMapper.toModelFromEntity(comment)).thenReturn(commentModel);
        when(assessmentCommentRepository.countAssessmentCommentsByIdCommentAndLiked(id, true)).thenReturn(0L);
        when(assessmentCommentRepository.countAssessmentCommentsByIdCommentAndLiked(id, false)).thenReturn(0L);

        CommentModel result = commentService.findCommentById(id);
        assertEquals(commentModel, result);
    }

    @Test
    void findCommentByIdNegativeTest() {
        Long id = 3L;
        when(commentRepository.findById(id)).thenThrow(NotFoundException.class);

        verify(commentMapper, never()).toModelFromEntity(any(Comment.class));
        verify(assessmentCommentRepository, never()).countAssessmentCommentsByIdCommentAndLiked(id, true);
        verify(assessmentCommentRepository, never()).countAssessmentCommentsByIdCommentAndLiked(id, false);
        assertThrows(NotFoundException.class, () -> commentService.findCommentById(id));
    }


    @Test
    void getCommentsOnTheVideo() {
        Long id = comment.getIdVideo();
        List<Comment> comments = List.of(comment);
        List<CommentModel> commentModels = List.of(commentModel);
        when(commentRepository.getCommentsByIdVideo(id)).thenReturn(comments);
        when(commentMapper.toModelListFromEntityList(comments)).thenReturn(commentModels);
        when(assessmentCommentRepository.countAssessmentCommentsByIdCommentAndLiked(id, true)).thenReturn(0L);
        when(assessmentCommentRepository.countAssessmentCommentsByIdCommentAndLiked(id, false)).thenReturn(0L);

        List<CommentModel> result = commentService.getCommentsOnTheVideo(id);
        assertEquals(commentModels, result);
    }

    @Test
    void insertAssessmentComment() {
        Long idComment = assessmentComment.getIdComment();
        when(assessmentCommentMapper.toEntityFromModel(assessmentCommentModel)).thenReturn(assessmentComment);
        when(assessmentCommentRepository.save(assessmentComment)).thenReturn(assessmentComment);
        when(commentRepository.findById(idComment)).thenReturn(Optional.of(comment));
        when(commentMapper.toModelFromEntity(comment)).thenReturn(commentModel);
        when(assessmentCommentRepository.countAssessmentCommentsByIdCommentAndLiked(idComment, true)).thenReturn(0L);
        when(assessmentCommentRepository.countAssessmentCommentsByIdCommentAndLiked(idComment, false)).thenReturn(0L);

        CommentModel result = commentService.insertAssessmentComment(assessmentCommentModel);

        verify(assessmentCommentRepository).save(assessmentComment);
        assertEquals(commentModel, result);
    }

    @Test
    void deleteAssessmentComment() {
        Long idComment = assessmentCommentModel.getIdComment();
        Long idUser = assessmentCommentModel.getIdUser();
        Long id = assessmentCommentModel.getIdAssessmentComment();
        when(assessmentCommentRepository.getAssessmentCommentByIdCommentAndIdUser(idComment, idUser)).thenReturn(id);
        when(commentRepository.findById(idComment)).thenReturn(Optional.of(comment));
        when(commentMapper.toModelFromEntity(comment)).thenReturn(commentModel);
        when(assessmentCommentRepository.countAssessmentCommentsByIdCommentAndLiked(idComment, true)).thenReturn(0L);
        when(assessmentCommentRepository.countAssessmentCommentsByIdCommentAndLiked(idComment, false)).thenReturn(0L);

        CommentModel result = commentService.deleteAssessmentComment(idUser, idComment);

        verify(assessmentCommentRepository).deleteById(id);
        assertEquals(commentModel, result);
    }
}