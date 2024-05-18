package com.example.videohosting.dto.playlistDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.core.io.Resource;


import java.sql.Timestamp;
import java.util.Objects;

public class PlaylistResponse {
    @NotNull
    private Long idPlaylist;
    @NotBlank
    @Size(max = 100)
    private String namePlaylist;
    @NotNull
    @Past
    private Timestamp dateCreation;
    private Resource imageIcon;
    @NotNull
    @PositiveOrZero
    private Long countVideos;

    public PlaylistResponse(Long idPlaylist, String namePlaylist, Timestamp dateCreation,
                            Resource imageIcon, Long countVideos) {
        this.idPlaylist = idPlaylist;
        this.namePlaylist = namePlaylist;
        this.dateCreation = dateCreation;
        this.imageIcon = imageIcon;
        this.countVideos = countVideos;
    }

    public PlaylistResponse() {
    }

    public Long getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(Long idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public String getNamePlaylist() {
        return namePlaylist;
    }

    public void setNamePlaylist(String namePlaylist) {
        this.namePlaylist = namePlaylist;
    }

    public Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Resource getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(Resource imageIcon) {
        this.imageIcon = imageIcon;
    }

    public Long getCountVideos() {
        return countVideos;
    }

    public void setCountVideos(Long countVideos) {
        this.countVideos = countVideos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaylistResponse that = (PlaylistResponse) o;

        if (!idPlaylist.equals(that.idPlaylist)) return false;
        if (!namePlaylist.equals(that.namePlaylist)) return false;
        if (!dateCreation.equals(that.dateCreation)) return false;
        if (!Objects.equals(imageIcon, that.imageIcon)) return false;
        return countVideos.equals(that.countVideos);
    }

    @Override
    public int hashCode() {
        int result = idPlaylist.hashCode();
        result = 31 * result + namePlaylist.hashCode();
        result = 31 * result + dateCreation.hashCode();
        result = 31 * result + (imageIcon != null ? imageIcon.hashCode() : 0);
        result = 31 * result + countVideos.hashCode();
        return result;
    }
    @Override
    public String toString() {
        return "PlaylistResponse{" +
               "idPlaylist=" + idPlaylist +
               ", namePlaylist='" + namePlaylist + '\'' +
               ", dateCreation=" + dateCreation +
               ", imageIcon='" + imageIcon + '\'' +
               ", countVideos=" + countVideos +
               '}';
    }

}
