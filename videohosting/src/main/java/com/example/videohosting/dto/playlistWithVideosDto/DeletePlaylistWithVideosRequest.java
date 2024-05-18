package com.example.videohosting.dto.playlistWithVideosDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DeletePlaylistWithVideosRequest {
    @NotNull
    private Long idVideo;

    public DeletePlaylistWithVideosRequest(Long idVideo) {
        this.idVideo = idVideo;
    }

    public DeletePlaylistWithVideosRequest() {
    }

    public Long getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(Long idVideo) {
        this.idVideo = idVideo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeletePlaylistWithVideosRequest that = (DeletePlaylistWithVideosRequest) o;

        return idVideo.equals(that.idVideo);
    }

    @Override
    public int hashCode() {
        return idVideo.hashCode();
    }

    @Override
    public String toString() {
        return "DeletePlaylistWithVideosRequest{" +
               "idVideo=" + idVideo +
               '}';
    }
}
