package com.example.videohosting.service;

import com.example.videohosting.entity.AssessmentComment;
import com.example.videohosting.entity.Comment;
import com.example.videohosting.exception.NotFoundException;
import com.example.videohosting.mapper.AssessmentCommentMapper;
import com.example.videohosting.mapper.CommentMapper;
import com.example.videohosting.model.AssessmentCommentModel;
import com.example.videohosting.model.CommentModel;
import com.example.videohosting.repository.AssessmentCommentRepository;
import com.example.videohosting.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AssessmentCommentRepository assessmentCommentRepository;
    private final AssessmentCommentMapper assessmentCommentMapper;

    @Autowired
    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper,
                          AssessmentCommentRepository assessmentCommentRepository,
                          AssessmentCommentMapper assessmentCommentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.assessmentCommentRepository = assessmentCommentRepository;
        this.assessmentCommentMapper = assessmentCommentMapper;
    }

    public CommentModel insertComment(CommentModel commentModel) {
        commentModel.setReleaseDateTime(Timestamp.valueOf(LocalDateTime.now()));
        Comment comment = commentMapper.toEntity(commentModel);
        Comment savedComment = commentRepository.save(comment);
        CommentModel savedCommentModel = commentMapper.toModelFromEntity(savedComment);
        Long count = 0L;
        savedCommentModel.setCountDislikes(count);
        savedCommentModel.setCountLikes(count);
        return savedCommentModel;
    }

    public CommentModel updateComment(CommentModel commentModel) {
        String text = commentModel.getText();
        Comment oldComment = commentRepository.findById(commentModel.getIdComment()).orElseThrow(() -> new NotFoundException("Comment not found"));
        oldComment.setText(text);
        oldComment.setReleaseDateTime(Timestamp.valueOf(LocalDateTime.now()));
        Comment savedComment = commentRepository.save(oldComment);
        CommentModel savedCommentModel = commentMapper.toModelFromEntity(savedComment);
        Long countLikes = assessmentCommentRepository.countAssessmentCommentsByIdCommentAndLiked(savedComment.getIdComment(), true);
        Long countDislikes = assessmentCommentRepository.countAssessmentCommentsByIdCommentAndLiked(savedComment.getIdComment(), false);
        savedCommentModel.setCountLikes(countLikes);
        savedCommentModel.setCountDislikes(countDislikes);
        return savedCommentModel;
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    public CommentModel findCommentById(Long idComment) {
        Comment comment = commentRepository.findById(idComment).orElseThrow(() -> new NotFoundException("Comment not found"));
        CommentModel commentModel = commentMapper.toModelFromEntity(comment);
        Long countLikes = assessmentCommentRepository.countAssessmentCommentsByIdCommentAndLiked(idComment, true);
        Long countDislikes = assessmentCommentRepository.countAssessmentCommentsByIdCommentAndLiked(idComment, false);
        commentModel.setCountLikes(countLikes);
        commentModel.setCountDislikes(countDislikes);
        return commentModel;
    }

    public List<CommentModel> getCommentsOnTheVideo(Long idVideo) {
        List<Comment> comments = commentRepository.getCommentsByIdVideo(idVideo);
        List<CommentModel> commentModels = commentMapper.toModelListFromEntityList(comments);
        for (CommentModel commentModel : commentModels) {
            Long countLikes = assessmentCommentRepository.countAssessmentCommentsByIdCommentAndLiked(commentModel.getIdComment(), true);
            Long countDislikes = assessmentCommentRepository.countAssessmentCommentsByIdCommentAndLiked(commentModel.getIdComment(), false);
            commentModel.setCountLikes(countLikes);
            commentModel.setCountDislikes(countDislikes);
        }
        return commentModels;
    }

    public CommentModel insertAssessmentComment(AssessmentCommentModel assessmentCommentModel) {
        AssessmentComment assessmentComment = assessmentCommentMapper.toEntityFromModel(assessmentCommentModel);
        assessmentCommentRepository.save(assessmentComment);
        return findCommentById(assessmentCommentModel.getIdComment());
    }

    public CommentModel deleteAssessmentComment(Long idUser, Long idComment) {
        Long id = assessmentCommentRepository.getAssessmentCommentByIdCommentAndIdUser(idComment, idUser);
        assessmentCommentRepository.deleteById(id);
        return findCommentById(idComment);
    }
}
