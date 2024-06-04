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

        PlaylistResponse response = (PlaylistResponse) o;

        if (!Objects.equals(idPlaylist, response.idPlaylist)) return false;
        if (!Objects.equals(namePlaylist, response.namePlaylist))
            return false;
        if (!Objects.equals(dateCreation, response.dateCreation))
            return false;
        if (!Objects.equals(imageIcon, response.imageIcon)) return false;
        return Objects.equals(countVideos, response.countVideos);
    }

    @Override
    public int hashCode() {
        int result = idPlaylist != null ? idPlaylist.hashCode() : 0;
        result = 31 * result + (namePlaylist != null ? namePlaylist.hashCode() : 0);
        result = 31 * result + (dateCreation != null ? dateCreation.hashCode() : 0);
        result = 31 * result + (imageIcon != null ? imageIcon.hashCode() : 0);
        result = 31 * result + (countVideos != null ? countVideos.hashCode() : 0);
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
