package com.example.videohosting.dto.playlistDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreatePlaylistRequest {
    @NotNull
    private Long idUser;
    @NotBlank
    @Size(max = 100)
    private String namePlaylist;

    public CreatePlaylistRequest(Long idUser, String namePlaylist) {
        this.idUser = idUser;
        this.namePlaylist = namePlaylist;
    }

    public CreatePlaylistRequest() {
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getNamePlaylist() {
        return namePlaylist;
    }

    public void setNamePlaylist(String namePlaylist) {
        this.namePlaylist = namePlaylist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreatePlaylistRequest that = (CreatePlaylistRequest) o;

        if (!idUser.equals(that.idUser)) return false;
        return namePlaylist.equals(that.namePlaylist);
    }

    @Override
    public int hashCode() {
        int result = idUser.hashCode();
        result = 31 * result + namePlaylist.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CreatePlaylistRequest{" +
               "idUser=" + idUser +
               ", namePlaylist='" + namePlaylist + '\'' +
               '}';
    }
}
