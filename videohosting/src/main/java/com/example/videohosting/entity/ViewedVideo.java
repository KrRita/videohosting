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
@Table(name="Viewed_videos")
public class ViewedVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_viewed_videos")
    private Long idViewedVideo;
    @Column(name="id_user")
    private Long idUser;
    @ManyToOne
    @JoinColumn(name="id_video")
    private Video video;
    @Column(name="date_of_viewing")
    private Timestamp dateOfViewing;

    public ViewedVideo(Long idViewedVideo, Long idUser, Video video, Timestamp dateOfViewing) {
        this.idViewedVideo = idViewedVideo;
        this.idUser = idUser;
        this.video = video;
        this.dateOfViewing = dateOfViewing;
    }

    public ViewedVideo() {
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

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Timestamp getDateOfViewing() {
        return dateOfViewing;
    }

    public void setDateOfViewing(Timestamp dateOfViewing) {
        this.dateOfViewing = dateOfViewing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ViewedVideo that = (ViewedVideo) o;

        if (!idViewedVideo.equals(that.idViewedVideo)) return false;
        if (!idUser.equals(that.idUser)) return false;
        if (!video.equals(that.video)) return false;
        return dateOfViewing.equals(that.dateOfViewing);
    }

    @Override
    public int hashCode() {
        int result = idViewedVideo.hashCode();
        result = 31 * result + idUser.hashCode();
        result = 31 * result + video.hashCode();
        result = 31 * result + dateOfViewing.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ViewedVideo{" +
               "idViewedVideo=" + idViewedVideo +
               ", idUser=" + idUser +
               ", video=" + video +
               ", dateOfViewing=" + dateOfViewing +
               '}';
    }
}
