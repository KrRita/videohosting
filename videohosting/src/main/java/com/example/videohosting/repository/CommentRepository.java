package com.example.videohosting.repository;

import com.example.videohosting.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> getCommentsByIdVideo(Long idVideo);
}
