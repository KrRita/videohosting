package com.example.videohosting.dto.assessementVideoDto;

import jakarta.validation.constraints.NotNull;

public class DeleteAssessmentVideoRequest {
    @NotNull
    private Long idUser;

    public DeleteAssessmentVideoRequest(Long idUser) {
        this.idUser = idUser;
    }

    public DeleteAssessmentVideoRequest() {
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

        DeleteAssessmentVideoRequest that = (DeleteAssessmentVideoRequest) o;

        return idUser.equals(that.idUser);
    }

    @Override
    public int hashCode() {
        return idUser.hashCode();
    }

    @Override
    public String toString() {
        return "DeleteAssessmentVideoRequest{" +
               "idUser=" + idUser +
               '}';
    }
}
