package com.example.videohosting.model;

import java.sql.Timestamp;

public class AssessmentVideoModel {
    private Long idAssessmentVideo;
    private Long idUser;
    private VideoModel video;
    private Timestamp dateOfAssessment;
    private Boolean liked;

    public AssessmentVideoModel(Long idAssessmentVideo, Long idUser, VideoModel video,
                                Timestamp dateOfAssessment, Boolean liked) {
        this.idAssessmentVideo = idAssessmentVideo;
        this.idUser = idUser;
        this.video = video;
        this.dateOfAssessment = dateOfAssessment;
        this.liked = liked;
    }

    public AssessmentVideoModel() {
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

    public VideoModel getVideo() {
        return video;
    }

    public void setVideo(VideoModel video) {
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

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AssessmentVideoModel that = (AssessmentVideoModel) o;

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
        return "AssessmentVideoModel{" +
               "idAssessmentVideo=" + idAssessmentVideo +
               ", idUser=" + idUser +
               ", video=" + video +
               ", dateOfAssessment=" + dateOfAssessment +
               ", isLike=" + liked +
               '}';
    }
}
