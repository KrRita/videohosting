package com.example.videohosting.dto.playlistDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class CreatePlaylistRequest {
    @NotNull
    private Long idUser;
    @NotBlank
    @Size(max = 100)
    private String namePlaylist;
    private MultipartFile imageIconFile;

    public CreatePlaylistRequest(Long idUser, String namePlaylist, MultipartFile imageIconFile) {
        this.idUser = idUser;
        this.namePlaylist = namePlaylist;
        this.imageIconFile = imageIconFile;
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

    public MultipartFile getImageIconFile() {
        return imageIconFile;
    }

    public void setImageIconFile(MultipartFile imageIconFile) {
        this.imageIconFile = imageIconFile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreatePlaylistRequest that = (CreatePlaylistRequest) o;

        if (!idUser.equals(that.idUser)) return false;
        if (!namePlaylist.equals(that.namePlaylist)) return false;
        return Objects.equals(imageIconFile, that.imageIconFile);
    }

    @Override
    public int hashCode() {
        int result = idUser.hashCode();
        result = 31 * result + namePlaylist.hashCode();
        result = 31 * result + (imageIconFile != null ? imageIconFile.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CreatePlaylistRequest{" +
               "idUser=" + idUser +
               ", namePlaylist='" + namePlaylist + '\'' +
               ", imageIcon='" + imageIconFile + '\'' +
               '}';
    }
}
