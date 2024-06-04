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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AssessmentCommentRepository assessmentCommentRepository;
    private final AssessmentCommentMapper assessmentCommentMapper;
    private final Logger logger = LoggerFactory.getLogger(CommentService.class);

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
        logger.info("Inserting a new comment: {}", commentModel);
        commentModel.setReleaseDateTime(Timestamp.valueOf(LocalDateTime.now()));
        Comment comment = commentMapper.toEntity(commentModel);
        Comment savedComment = commentRepository.save(comment);
        CommentModel savedCommentModel = commentMapper.toModelFromEntity(savedComment);
        Long count = 0L;
        savedCommentModel.setCountDislikes(count);
        savedCommentModel.setCountLikes(count);
        logger.info("Inserted comment successfully: {}", savedCommentModel);
        return savedCommentModel;
    }

    public CommentModel updateComment(CommentModel commentModel) {
        logger.info("Updating comment with ID: {}", commentModel.getIdComment());
        String text = commentModel.getText();
        Comment oldComment = commentRepository.findById(commentModel.getIdComment())
                .orElseThrow(() -> {
                    logger.error("Comment not found with ID: {}", commentModel.getIdComment());
                    return new NotFoundException("Comment not found");
                });
        oldComment.setText(text);
        oldComment.setReleaseDateTime(Timestamp.valueOf(LocalDateTime.now()));
        Comment savedComment = commentRepository.save(oldComment);
        CommentModel savedCommentModel = commentMapper.toModelFromEntity(savedComment);
        Long countLikes = assessmentCommentRepository.countAssessmentCommentsByIdCommentAndLiked(savedComment.getIdComment(), true);
        Long countDislikes = assessmentCommentRepository.countAssessmentCommentsByIdCommentAndLiked(savedComment.getIdComment(), false);
        savedCommentModel.setCountLikes(countLikes);
        savedCommentModel.setCountDislikes(countDislikes);
        logger.info("Updated comment successfully: {}", savedCommentModel);
        return savedCommentModel;
    }

    public void deleteComment(Long id) {
        logger.info("Deleting comment with ID: {}", id);
        commentRepository.deleteById(id);
        logger.info("Deleted comment with ID: {}", id);
    }

    public CommentModel findCommentById(Long idComment) {
        logger.info("Finding comment by ID: {}", idComment);
        Comment comment = commentRepository.findById(idComment)
                .orElseThrow(() -> {
                    logger.error("Comment not found with ID: {}", idComment);
                    return new NotFoundException("Comment not found");
                });
        CommentModel commentModel = commentMapper.toModelFromEntity(comment);
        Long countLikes = assessmentCommentRepository.countAssessmentCommentsByIdCommentAndLiked(idComment, true);
        Long countDislikes = assessmentCommentRepository.countAssessmentCommentsByIdCommentAndLiked(idComment, false);
        commentModel.setCountLikes(countLikes);
        commentModel.setCountDislikes(countDislikes);
        logger.info("Found comment: {}", commentModel);
        return commentModel;
    }

    @Transactional
    public List<CommentModel> getCommentsOnTheVideo(Long idVideo) {
        logger.info("Getting comments for video with ID: {}", idVideo);
        List<Comment> comments = commentRepository.getCommentsByIdVideo(idVideo);
        List<CommentModel> commentModels = commentMapper.toModelListFromEntityList(comments);
        for (CommentModel commentModel : commentModels) {
            Long countLikes = assessmentCommentRepository.countAssessmentCommentsByIdCommentAndLiked(commentModel.getIdComment(), true);
            Long countDislikes = assessmentCommentRepository.countAssessmentCommentsByIdCommentAndLiked(commentModel.getIdComment(), false);
            commentModel.setCountLikes(countLikes);
            commentModel.setCountDislikes(countDislikes);
        }
        logger.info("Retrieved {} comments for video with ID: {}", commentModels.size(), idVideo);
        return commentModels;
    }

    public CommentModel insertAssessmentComment(AssessmentCommentModel assessmentCommentModel) {
        logger.info("Inserting assessment comment: {}", assessmentCommentModel);
        AssessmentComment assessmentComment = assessmentCommentMapper.toEntityFromModel(assessmentCommentModel);
        assessmentCommentRepository.save(assessmentComment);
        logger.info("Inserted assessment comment successfully: {}", assessmentCommentModel);
        return findCommentById(assessmentCommentModel.getIdComment());
    }

    public CommentModel deleteAssessmentComment(Long idUser, Long idComment) {
        logger.info("Deleting assessment comment for user ID: {} and comment ID: {}", idUser, idComment);
        Long id = assessmentCommentRepository.getAssessmentCommentByIdCommentAndIdUser(idComment, idUser);
        assessmentCommentRepository.deleteById(id);
        logger.info("Deleted assessment comment with ID: {} successfully", id);
        return findCommentById(idComment);
    }
}
