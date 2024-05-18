package com.example.videohosting.model;

import java.sql.Timestamp;

public class PlaylistWithVideosModel {
    private Long idPlaylistWithVideos;
    private Long idPlaylist;
    private VideoModel video;
    private Timestamp dateOfAddition;

    public PlaylistWithVideosModel(Long idPlaylistWithVideos, Long idPlaylist, VideoModel video, Timestamp dateOfAddition) {
        this.idPlaylistWithVideos = idPlaylistWithVideos;
        this.idPlaylist = idPlaylist;
        this.video = video;
        this.dateOfAddition = dateOfAddition;
    }

    public PlaylistWithVideosModel() {
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

    public VideoModel getVideo() {
        return video;
    }

    public void setVideo(VideoModel video) {
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

        PlaylistWithVideosModel that = (PlaylistWithVideosModel) o;

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
        return "PlaylistWithVideosModel{" +
               "idPlaylistWithVideos=" + idPlaylistWithVideos +
               ", idPlaylist=" + idPlaylist +
               ", videoModel=" + video +
               ", dateOfAddition=" + dateOfAddition +
               '}';
    }
}
