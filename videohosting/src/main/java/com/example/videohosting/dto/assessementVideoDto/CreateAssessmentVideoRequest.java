package com.example.videohosting.dto.assessementVideoDto;


import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class CreateAssessmentVideoRequest {
    @NotNull
    private Long idUser;
    @NotNull
    private Boolean liked;

    public CreateAssessmentVideoRequest(Long idUser, Boolean liked) {
        this.idUser = idUser;
        this.liked = liked;
    }

    public CreateAssessmentVideoRequest() {
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

        CreateAssessmentVideoRequest that = (CreateAssessmentVideoRequest) o;

        if (!Objects.equals(idUser, that.idUser)) return false;
        return Objects.equals(liked, that.liked);
    }

    @Override
    public int hashCode() {
        int result = idUser != null ? idUser.hashCode() : 0;
        result = 31 * result + (liked != null ? liked.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CreateAssessmentVideoRequest{" +
               "idUser=" + idUser +
               ", liked=" + liked +
               '}';
    }
}
