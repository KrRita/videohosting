package com.example.videohosting.dto.viewedVideoDto;

import jakarta.validation.constraints.NotNull;

public class CreateViewedVideoRequest {
    @NotNull
    private Long idUser;

    public CreateViewedVideoRequest(Long idUser) {
        this.idUser = idUser;
    }

    public CreateViewedVideoRequest() {
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

        CreateViewedVideoRequest that = (CreateViewedVideoRequest) o;

        return idUser.equals(that.idUser);
    }

    @Override
    public int hashCode() {
        return idUser.hashCode();
    }

    @Override
    public String toString() {
        return "CreateViewedVideoRequest{" +
               "idUser=" + idUser +
               '}';
    }
}
