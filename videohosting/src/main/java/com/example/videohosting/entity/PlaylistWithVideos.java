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
@Table(name = "Playlist_with_videos")
public class PlaylistWithVideos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_playlist_with_videos")
    private Long idPlaylistWithVideos;
    @Column(name = "id_playlist")
    private Long idPlaylist;
    @ManyToOne
    @JoinColumn(name = "id_video")
    private Video video;
    @Column(name = "date_of_addition")
    private Timestamp dateOfAddition;

    public PlaylistWithVideos(Long idPlaylistWithVideos, Long idPlaylist, Video video,
                              Timestamp dateOfAddition) {
        this.idPlaylistWithVideos = idPlaylistWithVideos;
        this.idPlaylist = idPlaylist;
        this.video = video;
        this.dateOfAddition = dateOfAddition;
    }

    public PlaylistWithVideos() {
    }

    public Long getIdPlaylistWithVideos() {
        return idPlaylistWithVideos;
    }

    public void setIdPlaylistWithVideos(Long idPlaylistWithVideos) {
        this.idPlaylistWithVideos = idPlaylistWithVideos;
    }

    public Long getIdPlaylist() {
        return idPlaylist;
    }

    public void setIdPlaylist(Long idPlaylist) {
        this.idPlaylist = idPlaylist;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Timestamp getDateOfAddition() {
        return dateOfAddition;
    }

    public void setDateOfAddition(Timestamp dateOfAddition) {
        this.dateOfAddition = dateOfAddition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaylistWithVideos that = (PlaylistWithVideos) o;

        if (!idPlaylistWithVideos.equals(that.idPlaylistWithVideos)) return false;
        if (!idPlaylist.equals(that.idPlaylist)) return false;
        if (!video.equals(that.video)) return false;
        return dateOfAddition.equals(that.dateOfAddition);
    }

    @Override
    public int hashCode() {
        int result = idPlaylistWithVideos.hashCode();
        result = 31 * result + idPlaylist.hashCode();
        result = 31 * result + video.hashCode();
        result = 31 * result + dateOfAddition.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PlaylistWithVideos{" +
               "idPlaylistWithVideos=" + idPlaylistWithVideos +
               ", idPlaylist=" + idPlaylist +
               ", video=" + video +
               ", dateOfAddition=" + dateOfAddition +
               '}';
    }
}
