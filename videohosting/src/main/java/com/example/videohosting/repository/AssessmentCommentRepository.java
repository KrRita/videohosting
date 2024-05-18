package com.example.videohosting.repository;

import com.example.videohosting.entity.AssessmentComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface AssessmentCommentRepository extends JpaRepository<AssessmentComment, Long> {
    Long countAssessmentCommentsByIdCommentAndLiked(Long idComment, Boolean liked);
    @Query(value = "SELECT ac.idAssessmentComment FROM AssessmentComment ac WHERE ac.idUser=:idUser AND ac.idComment=:idComment")
    Long getAssessmentCommentByIdCommentAndIdUser(Long idComment, Long idUser);
}
