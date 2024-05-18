package com.example.videohosting.model;

import java.sql.Timestamp;
import java.util.Objects;

public class CommentModel {
    private Long idComment;
    private Long idVideo;
    private UserModel user;
    private String text;
    private Timestamp releaseDateTime;
    private Long countLikes;
    private Long countDislikes;

    public CommentModel(Long idComment, Long idVideo, UserModel user, String text,
                        Timestamp releaseDateTime, Long countLikes, Long countDislikes) {
        this.idComment = idComment;
        this.idVideo = idVideo;
        this.user = user;
        this.text = text;
        this.releaseDateTime = releaseDateTime;
        this.countLikes = countLikes;
        this.countDislikes = countDislikes;
    }

    public CommentModel() {
    }


    public Long getIdComment() {
        return idComment;
    }

    public void setIdComment(Long idComment) {
        this.idComment = idComment;
    }

    public Long getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(Long idVideo) {
        this.idVideo = idVideo;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getReleaseDateTime() {
        return releaseDateTime;
    }

    public void setReleaseDateTime(Timestamp releaseDateTime) {
        this.releaseDateTime = releaseDateTime;
    }

    public Long getCountLikes() {
        return countLikes;
    }

    public void setCountLikes(Long countLikes) {
        this.countLikes = countLikes;
    }

    public Long getCountDislikes() {
        return countDislikes;
    }

    public void setCountDislikes(Long countDislikes) {
        this.countDislikes = countDislikes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommentModel that = (CommentModel) o;

        if (!idComment.equals(that.idComment)) return false;
        if (!idVideo.equals(that.idVideo)) return false;
        if (!user.equals(that.user)) return false;
        if (!text.equals(that.text)) return false;
        if (!releaseDateTime.equals(that.releaseDateTime)) return false;
        if (!Objects.equals(countLikes, that.countLikes)) return false;
        return Objects.equals(countDislikes, that.countDislikes);
    }

    @Override
    public int hashCode() {
        int result = idComment.hashCode();
        result = 31 * result + idVideo.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + releaseDateTime.hashCode();
        result = 31 * result + (countLikes != null ? countLikes.hashCode() : 0);
        result = 31 * result + (countDislikes != null ? countDislikes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CommentModel{" +
               "idComment=" + idComment +
               ", idVideo=" + idVideo +
               ", userModel=" + user +
               ", text='" + text + '\'' +
               ", releaseDateTime=" + releaseDateTime +
               ", countLikes=" + countLikes +
               ", countDislikes=" + countDislikes +
               '}';
    }
}
