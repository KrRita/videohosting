package com.example.videohosting.model;

public class AssessmentCommentModel {
    private Long idAssessmentComment;
    private Long idUser;
    private Long idComment;
    private Boolean liked;


    public AssessmentCommentModel(Long idAssessmentComment, Long idUser, Long idComment, Boolean liked) {
        this.idAssessmentComment = idAssessmentComment;
        this.idUser = idUser;
        this.idComment = idComment;
        this.liked = liked;
    }

    public AssessmentCommentModel() {
    }

    public Long getIdAssessmentComment() {
        return idAssessmentComment;
    }

    public void setIdAssessmentComment(Long idAssessmentComment) {
        this.idAssessmentComment = idAssessmentComment;
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

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AssessmentCommentModel that = (AssessmentCommentModel) o;

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
        return "AssessmentCommentModel{" +
               "idAssessmentComment=" + idAssessmentComment +
               ", idUser=" + idUser +
               ", idComment=" + idComment +
               ", isLike=" + liked +
               '}';
    }
}
