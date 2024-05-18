package com.example.videohosting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Assessment_comment")
public class AssessmentComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_assessment_comment")
    private Long idAssessmentComment;

    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "id_comment")
    private Long idComment;
    @Column(name = "is_like")
    private Boolean liked;


    public AssessmentComment() {
    }

    public AssessmentComment(Long idAssessmentComment, Long idUser, Long idComment, Boolean liked) {
        this.idAssessmentComment = idAssessmentComment;
        this.idUser = idUser;
        this.idComment = idComment;
        this.liked = liked;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdComment() {
        return idComment;
    }

    public void setIdComment(Long idComment) {
        this.idComment = idComment;
    }

    public Boolean isLiked() {
        return liked;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean like) {
        this.liked = like;
    }

    public Long getIdAssessmentComment() {
        return idAssessmentComment;
    }

    public void setIdAssessmentComment(Long idAssessmentComment) {
        this.idAssessmentComment = idAssessmentComment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AssessmentComment that = (AssessmentComment) o;

        if (liked != that.liked) return false;
        if (!idAssessmentComment.equals(that.idAssessmentComment)) return false;
        if (!idUser.equals(that.idUser)) return false;
        return idComment.equals(that.idComment);
    }

    @Override
    public int hashCode() {
        int result = idAssessmentComment.hashCode();
        result = 31 * result + idUser.hashCode();
        result = 31 * result + idComment.hashCode();
        result = 31 * result + (liked ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AssessmentComment{" +
               "idAssessmentComment=" + idAssessmentComment +
               ", idUser=" + idUser +
               ", idComment=" + idComment +
               ", isLike=" + liked +
               '}';
    }
}
