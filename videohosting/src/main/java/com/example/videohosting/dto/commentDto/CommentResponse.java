package com.example.videohosting.dto.commentDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;

import java.sql.Timestamp;
import java.util.Objects;

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

        CommentResponse response = (CommentResponse) o;

        if (!Objects.equals(idComment, response.idComment)) return false;
        if (!Objects.equals(idUser, response.idUser)) return false;
        if (!Objects.equals(channelName, response.channelName))
            return false;
        if (!Objects.equals(text, response.text)) return false;
        if (!Objects.equals(releaseDateTime, response.releaseDateTime))
            return false;
        if (!Objects.equals(countLikes, response.countLikes)) return false;
        return Objects.equals(countDislikes, response.countDislikes);
    }

    @Override
    public int hashCode() {
        int result = idComment != null ? idComment.hashCode() : 0;
        result = 31 * result + (idUser != null ? idUser.hashCode() : 0);
        result = 31 * result + (channelName != null ? channelName.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (releaseDateTime != null ? releaseDateTime.hashCode() : 0);
        result = 31 * result + (countLikes != null ? countLikes.hashCode() : 0);
        result = 31 * result + (countDislikes != null ? countDislikes.hashCode() : 0);
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
