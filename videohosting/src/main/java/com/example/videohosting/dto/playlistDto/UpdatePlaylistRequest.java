package com.example.videohosting.dto.playlistDto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class UpdatePlaylistRequest {
    @NotNull
    @Positive
    private Long idPlaylist;
    @Size(max = 100)
    private String namePlaylist;

    public UpdatePlaylistRequest(Long idPlaylist, String namePlaylist) {
        this.idPlaylist = idPlaylist;
        this.namePlaylist = namePlaylist;
    }

    public UpdatePlaylistRequest() {
    }
    public String getNamePlaylist() {
        return namePlaylist;
    }

    public void setNamePlaylist(String namePlaylist) {
        this.namePlaylist = namePlaylist;
    }

    public Long getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(Long idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpdatePlaylistRequest request = (UpdatePlaylistRequest) o;

        if (!Objects.equals(idPlaylist, request.idPlaylist)) return false;
        return Objects.equals(namePlaylist, request.namePlaylist);
    }

    @Override
    public int hashCode() {
        int result = idPlaylist != null ? idPlaylist.hashCode() : 0;
        result = 31 * result + (namePlaylist != null ? namePlaylist.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UpdatePlaylistRequest{" +
               "idPlaylist=" + idPlaylist +
               ", namePlaylist='" + namePlaylist + '\'' +
               '}';
    }
}
