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
@Table(name="Comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_comment")
    private Long idComment;
    @Column(name="id_video")
    private Long idVideo;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
    @Column(name="text")
    private String text;
    @Column(name="release_date_time")
    private Timestamp releaseDateTime;

    public Comment(Long idComment, Long idVideo, User user, String text, Timestamp releaseDateTime) {
        this.idComment = idComment;
        this.idVideo = idVideo;
        this.user = user;
        this.text = text;
        this.releaseDateTime = releaseDateTime;
    }

    public Comment() {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (!idComment.equals(comment.idComment)) return false;
        if (!idVideo.equals(comment.idVideo)) return false;
        if (!user.equals(comment.user)) return false;
        if (!text.equals(comment.text)) return false;
        return releaseDateTime.equals(comment.releaseDateTime);
    }

    @Override
    public int hashCode() {
        int result = idComment.hashCode();
        result = 31 * result + idVideo.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + releaseDateTime.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Comment{" +
               "idComment=" + idComment +
               ", idVideo=" + idVideo +
               ", user=" + user +
               ", text='" + text + '\'' +
               ", releaseDateTime=" + releaseDateTime +
               '}';
    }
}
