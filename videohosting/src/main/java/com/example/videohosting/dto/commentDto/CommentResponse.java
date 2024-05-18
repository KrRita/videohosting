package com.example.videohosting.dto.commentDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;

import java.sql.Timestamp;

public class CommentResponse {
    @NotNull
    private Long idComment;
    @NotNull
    private Long idUser;
    @NotBlank
    private String channelName;
    @NotBlank
    private String text;
    @NotNull
    @Past
    private Timestamp releaseDateTime;
    @NotNull
    @PositiveOrZero
    private Long countLikes;
    @NotNull
    @PositiveOrZero
    private Long countDislikes;

    public CommentResponse(Long idComment, Long idUser, String channelName, String text,
                           Timestamp releaseDateTime, Long countLikes, Long countDislikes) {
        this.idComment = idComment;
        this.idUser = idUser;
        this.channelName = channelName;
        this.text = text;
        this.releaseDateTime = releaseDateTime;
        this.countLikes = countLikes;
        this.countDislikes = countDislikes;
    }

    public CommentResponse() {
    }

    public Long getIdComment() {
        return idComment;
    }

    public void setIdComment(Long idComment) {
        this.idComment = idComment;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
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

        CommentResponse that = (CommentResponse) o;

        if (!idComment.equals(that.idComment)) return false;
        if (!idUser.equals(that.idUser)) return false;
        if (!channelName.equals(that.channelName)) return false;
        if (!text.equals(that.text)) return false;
        if (!releaseDateTime.equals(that.releaseDateTime)) return false;
        if (!countLikes.equals(that.countLikes)) return false;
        return countDislikes.equals(that.countDislikes);
    }

    @Override
    public int hashCode() {
        int result = idComment.hashCode();
        result = 31 * result + idUser.hashCode();
        result = 31 * result + channelName.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + releaseDateTime.hashCode();
        result = 31 * result + countLikes.hashCode();
        result = 31 * result + countDislikes.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CommentResponse{" +
               "idComment=" + idComment +
               ", idUser=" + idUser +
               ", userName='" + channelName + '\'' +
               ", text='" + text + '\'' +
               ", releaseDateTime=" + releaseDateTime +
               ", countLikes=" + countLikes +
               ", countDislikes=" + countDislikes +
               '}';
    }
}
