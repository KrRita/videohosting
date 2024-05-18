package com.example.videohosting.dto.assessmentCommentDto;

import jakarta.validation.constraints.NotNull;

public class DeleteAssessmentCommentRequest {
    @NotNull
    private Long idUser;

    public DeleteAssessmentCommentRequest(Long idUser) {
        this.idUser = idUser;
    }

    public DeleteAssessmentCommentRequest() {
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeleteAssessmentCommentRequest that = (DeleteAssessmentCommentRequest) o;

        return idUser.equals(that.idUser);
    }

    @Override
    public int hashCode() {
        return idUser.hashCode();
    }

    @Override
    public String toString() {
        return "DeleteAssessmentCommentRequest{" +
               "idUser=" + idUser +
               '}';
    }
}
