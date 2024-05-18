package com.example.videohosting.dto.assessmentCommentDto;

import jakarta.validation.constraints.NotNull;

public class CreateAssessmentCommentRequest {
    @NotNull
    private Long idUser;
    @NotNull
    private Boolean liked;

    public CreateAssessmentCommentRequest(Long idUser, Boolean liked) {
        this.idUser = idUser;
        this.liked = liked;
    }

    public CreateAssessmentCommentRequest() {
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
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

        CreateAssessmentCommentRequest that = (CreateAssessmentCommentRequest) o;

        if (!idUser.equals(that.idUser)) return false;
        return liked.equals(that.liked);
    }

    @Override
    public int hashCode() {
        int result = idUser.hashCode();
        result = 31 * result + liked.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CreateAssessmentCommentRequest{" +
               "idUser=" + idUser +
               ", liked=" + liked +
               '}';
    }
}
