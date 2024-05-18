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
@Table(name = "Assessment_video")
public class AssessmentVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_assessment_video")
    private Long idAssessmentVideo;
    @Column(name = "id_user")
    private Long idUser;
    @ManyToOne
    @JoinColumn(name = "id_video")
    private Video video;
    @Column(name = "date_of_assessment")
    private Timestamp dateOfAssessment;
    @Column(name = "is_like")
    private Boolean liked;

    public AssessmentVideo(Long idAssessmentVideo, Long idUser, Video video,
                           Timestamp dateOfAssessment, Boolean liked) {
        this.idAssessmentVideo = idAssessmentVideo;
        this.idUser = idUser;
        this.video = video;
        this.dateOfAssessment = dateOfAssessment;
        this.liked = liked;
    }

    public AssessmentVideo() {
    }

    public Long getIdAssessmentVideo() {
        return idAssessmentVideo;
    }

    public void setIdAssessmentVideo(Long idAssessmentVideo) {
        this.idAssessmentVideo = idAssessmentVideo;
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

    public Timestamp getDateOfAssessment() {
        return dateOfAssessment;
    }

    public void setDateOfAssessment(Timestamp dateOfAssessment) {
        this.dateOfAssessment = dateOfAssessment;
    }

    public Boolean isLiked() {
        return liked;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean like) {
        this.liked = like;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AssessmentVideo that = (AssessmentVideo) o;

        if (liked != that.liked) return false;
        if (!idAssessmentVideo.equals(that.idAssessmentVideo)) return false;
        if (!idUser.equals(that.idUser)) return false;
        if (!video.equals(that.video)) return false;
        return dateOfAssessment.equals(that.dateOfAssessment);
    }

    @Override
    public int hashCode() {
        int result = idAssessmentVideo.hashCode();
        result = 31 * result + idUser.hashCode();
        result = 31 * result + video.hashCode();
        result = 31 * result + dateOfAssessment.hashCode();
        result = 31 * result + (liked ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AssessmentVideo{" +
               "idAssessmentVideo=" + idAssessmentVideo +
               ", idUser=" + idUser +
               ", video=" + video +
               ", dateOfAssessment=" + dateOfAssessment +
               ", isLike=" + liked +
               '}';
    }
}
