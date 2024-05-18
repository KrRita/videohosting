package com.example.videohosting.model;

import java.sql.Timestamp;
import java.util.Objects;

public class PlaylistModel {
    private Long idPlaylist;
    private UserModel user;
    private String namePlaylist;
    private Timestamp dateCreation;
    private Long countVideos;

    public PlaylistModel(Long idPlaylist, UserModel user, String namePlaylist,
                         Timestamp dateCreation, Long countVideos) {
        this.idPlaylist = idPlaylist;
        this.user = user;
        this.namePlaylist = namePlaylist;
        this.dateCreation = dateCreation;
        this.countVideos = countVideos;
    }

    public PlaylistModel() {
    }

    public Long getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(Long idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
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

        PlaylistModel that = (PlaylistModel) o;

        if (!idPlaylist.equals(that.idPlaylist)) return false;
        if (!user.equals(that.user)) return false;
        if (!namePlaylist.equals(that.namePlaylist)) return false;
        if (!dateCreation.equals(that.dateCreation)) return false;
        return Objects.equals(countVideos, that.countVideos);
    }

    @Override
    public int hashCode() {
        int result = idPlaylist.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + namePlaylist.hashCode();
        result = 31 * result + dateCreation.hashCode();
        result = 31 * result + (countVideos != null ? countVideos.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PlaylistModel{" +
               "idPlaylist=" + idPlaylist +
               ", userModel=" + user +
               ", namePlaylist='" + namePlaylist + '\'' +
               ", dateCreation=" + dateCreation +
               ", countVideos=" + countVideos +
               '}';
    }
}
