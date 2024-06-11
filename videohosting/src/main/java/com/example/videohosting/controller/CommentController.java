package com.example.videohosting.controller;

import com.example.videohosting.dto.assessmentCommentDto.CreateAssessmentCommentRequest;
import com.example.videohosting.dto.assessmentCommentDto.DeleteAssessmentCommentRequest;
import com.example.videohosting.dto.commentDto.CommentResponse;
import com.example.videohosting.dto.commentDto.CreateCommentRequest;
import com.example.videohosting.dto.commentDto.UpdateCommentRequest;
import com.example.videohosting.mapper.AssessmentCommentMapper;
import com.example.videohosting.mapper.CommentMapper;
import com.example.videohosting.model.AssessmentCommentModel;
import com.example.videohosting.model.CommentModel;
import com.example.videohosting.model.UserModel;
import com.example.videohosting.service.CommentService;
import com.example.videohosting.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final AssessmentCommentMapper assessmentCommentMapper;
    private final UserService userService;

    @Autowired
    public CommentController(CommentService commentService, CommentMapper commentMapper,
                             AssessmentCommentMapper assessmentCommentMapper, UserService userService) {
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.assessmentCommentMapper = assessmentCommentMapper;
        this.userService = userService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<CommentResponse> postComment(@Valid @RequestBody CreateCommentRequest request) {
        CommentModel commentModel = commentMapper.toModelFromCreateRequest(request);
        UserModel userModel = userService.findUserById(request.getIdUser());
        commentModel.setUser(userModel);
        CommentModel result = commentService.insertComment(commentModel);
        CommentResponse response = commentMapper.toResponseFromModel(result);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> putComment(@PathVariable Long id,
                                                      @Valid @RequestBody UpdateCommentRequest request) {
        CommentModel commentModel = commentMapper.toModelFromUpdateRequest(request);
        commentModel.setIdComment(id);
        CommentModel model = commentService.updateComment(commentModel);
        CommentResponse response = commentMapper.toResponseFromModel(model);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable Long id) {
        CommentModel model = commentService.findCommentById(id);
        CommentResponse response = commentMapper.toResponseFromModel(model);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{id}/assessment")
    public ResponseEntity<CommentResponse> postAssessmentComment(
            @PathVariable Long id, @Valid @RequestBody CreateAssessmentCommentRequest request) {
        AssessmentCommentModel assessmentCommentModel = assessmentCommentMapper.toModelFromCreateRequest(request);
        assessmentCommentModel.setIdComment(id);
        CommentModel commentModel = commentService.insertAssessmentComment(assessmentCommentModel);
        CommentResponse response = commentMapper.toResponseFromModel(commentModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}/assessment")
    public ResponseEntity<CommentResponse> deleteAssessmentComment(
            @PathVariable Long id, @Valid @RequestBody DeleteAssessmentCommentRequest request) {
        CommentModel commentModel = commentService.deleteAssessmentComment(request.getIdUser(), id);
        CommentResponse response = commentMapper.toResponseFromModel(commentModel);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
