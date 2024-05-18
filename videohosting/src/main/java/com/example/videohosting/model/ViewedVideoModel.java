package com.example.videohosting.model;

import java.sql.Timestamp;

public class ViewedVideoModel {
    private Long idViewedVideo;
    private Long idUser;
    private VideoModel video;
    private Timestamp dateOfViewing;

    public ViewedVideoModel(Long idViewedVideo, Long idUser, VideoModel video, Timestamp dateOfViewing) {
        this.idViewedVideo = idViewedVideo;
        this.idUser = idUser;
        this.video = video;
        this.dateOfViewing = dateOfViewing;
    }

    public ViewedVideoModel() {
    }

    public Long getIdViewedVideo() {
        return idViewedVideo;
    }

    public void setIdViewedVideo(Long idViewedVideo) {
        this.idViewedVideo = idViewedVideo;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public VideoModel getVideo() {
        return video;
    }

    public void setVideo(VideoModel video) {
        this.video = video;
    }

    public Timestamp getDateOfViewing() {
        return dateOfViewing;
    }

    public void setDateOfViewing(Timestamp dateOfViewing) {
        this.dateOfViewing = dateOfViewing;
    }

    @Override
    public String toString() {
        return "ViewedVideoModel{" +
               "idViewedVideo=" + idViewedVideo +
               ", idUser=" + idUser +
               ", videoModel=" + video +
               ", dateOfViewing=" + dateOfViewing +
               '}';
    }
}
