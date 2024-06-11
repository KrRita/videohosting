package com.example.videohosting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.sql.Timestamp;

@Entity
@Table(name="Playlist")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_playlist")
    private Long idPlaylist;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
    @Column(name="name_playlist")
    private String namePlaylist;
    @Column(name="date_creation")
    private Timestamp dateCreation;

    public Playlist(Long idPlaylist, User user, String namePlaylist, Timestamp dateCreation) {
        this.idPlaylist = idPlaylist;
        this.user = user;
        this.namePlaylist = namePlaylist;
        this.dateCreation = dateCreation;
    }

    public Playlist() {
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


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Playlist playlist = (Playlist) o;

        if (!idPlaylist.equals(playlist.idPlaylist)) return false;
        if (!user.equals(playlist.user)) return false;
        if (!namePlaylist.equals(playlist.namePlaylist)) return false;
        return dateCreation.equals(playlist.dateCreation);
    }

    @Override
    public int hashCode() {
        int result = idPlaylist.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + namePlaylist.hashCode();
        result = 31 * result + dateCreation.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Playlist{" +
               "idPlaylist=" + idPlaylist +
               ", user=" + user +
               ", namePlaylist='" + namePlaylist + '\'' +
               ", dateCreation=" + dateCreation +
               '}';
    }
}
