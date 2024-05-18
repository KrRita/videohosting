package com.example.videohosting.dto.playlistWithVideosDto;

import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

public class CreatePlaylistWithVideosRequest {
    @NotNull
    private Long idVideo;

    public CreatePlaylistWithVideosRequest(Long idVideo) {
        this.idVideo = idVideo;
    }

    public CreatePlaylistWithVideosRequest() {
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

        CreatePlaylistWithVideosRequest that = (CreatePlaylistWithVideosRequest) o;

        return idVideo.equals(that.idVideo);
    }

    @Override
    public int hashCode() {
        return idVideo.hashCode();
    }

    @Override
    public String toString() {
        return "CreatePlaylistWithVideosRequest{" +
               ", idVideo=" + idVideo +
               '}';
    }
}
