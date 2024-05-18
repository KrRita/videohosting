package com.example.videohosting.dto.playlistDto;

import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class UpdatePlaylistRequest {
    @Size(max = 100)
    private String namePlaylist;
    private MultipartFile imageIcon;

    public UpdatePlaylistRequest(String namePlaylist, MultipartFile imageIcon) {
        this.namePlaylist = namePlaylist;
        this.imageIcon = imageIcon;
    }

    public UpdatePlaylistRequest() {
    }
    public String getNamePlaylist() {
        return namePlaylist;
    }

    public void setNamePlaylist(String namePlaylist) {
        this.namePlaylist = namePlaylist;
    }

    public MultipartFile getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(MultipartFile imageIcon) {
        this.imageIcon = imageIcon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpdatePlaylistRequest that = (UpdatePlaylistRequest) o;

        if (!Objects.equals(namePlaylist, that.namePlaylist)) return false;
        return Objects.equals(imageIcon, that.imageIcon);
    }

    @Override
    public int hashCode() {
        int result = namePlaylist != null ? namePlaylist.hashCode() : 0;
        result = 31 * result + (imageIcon != null ? imageIcon.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UpdatePlaylistRequest{" +
               "namePlaylist='" + namePlaylist + '\'' +
               ", imageIcon='" + imageIcon + '\'' +
               '}';
    }
}
