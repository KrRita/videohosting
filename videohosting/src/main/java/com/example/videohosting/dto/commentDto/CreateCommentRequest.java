package com.example.videohosting.dto.commentDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateCommentRequest {
    @NotNull
    private Long idVideo;
    @NotNull
    private Long idUser;
    @NotBlank
    private String text;

    public CreateCommentRequest(Long idVideo, Long idUser, String text) {
        this.idVideo = idVideo;
        this.idUser = idUser;
        this.text = text;
    }

    public CreateCommentRequest() {
    }

    public Long getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(Long idVideo) {
        this.idVideo = idVideo;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreateCommentRequest that = (CreateCommentRequest) o;

        if (!idVideo.equals(that.idVideo)) return false;
        if (!idUser.equals(that.idUser)) return false;
        return text.equals(that.text);
    }

    @Override
    public int hashCode() {
        int result = idVideo.hashCode();
        result = 31 * result + idUser.hashCode();
        result = 31 * result + text.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CreateCommentRequest{" +
               "idVideo=" + idVideo +
               ", idUser=" + idUser +
               ", text='" + text + '\'' +
               '}';
    }
}
