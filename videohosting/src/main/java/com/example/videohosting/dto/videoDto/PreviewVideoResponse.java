package com.example.videohosting.dto.videoDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.core.io.Resource;

import java.sql.Timestamp;
import java.util.Objects;

public class PreviewVideoResponse {
    @NotNull
    private Long idVideo;
    @NotBlank
    @Size(max = 70)
    private String name;
    private Long duration;
    @NotBlank
    @Size(max = 1000)
    private String description;

    @NotNull
    @Past
    private Timestamp releaseDateTime;
    @NotNull
    @PositiveOrZero
    private Long countViewing;
    @NotNull
    private Long idUser;
    @NotBlank
    @Size(max = 40)
    private String userName;

    public PreviewVideoResponse(Long idVideo, String name, Long duration, String description,
                                Timestamp releaseDateTime, Long countViewing, Long idUser, String userName) {
        this.idVideo = idVideo;
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.releaseDateTime = releaseDateTime;
        this.countViewing = countViewing;
        this.idUser = idUser;
        this.userName = userName;
    }

    public PreviewVideoResponse() {
    }

    public Long getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(Long idVideo) {
        this.idVideo = idVideo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
    public Timestamp getReleaseDateTime() {
        return releaseDateTime;
    }

    public void setReleaseDateTime(Timestamp releaseDateTime) {
        this.releaseDateTime = releaseDateTime;
    }

    public Long getCountViewing() {
        return countViewing;
    }

    public void setCountViewing(Long countViewing) {
        this.countViewing = countViewing;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PreviewVideoResponse that = (PreviewVideoResponse) o;

        if (!Objects.equals(idVideo, that.idVideo)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(duration, that.duration)) return false;
        if (!Objects.equals(description, that.description)) return false;
        if (!Objects.equals(releaseDateTime, that.releaseDateTime))
            return false;
        if (!Objects.equals(countViewing, that.countViewing)) return false;
        if (!Objects.equals(idUser, that.idUser)) return false;
        return Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        int result = idVideo != null ? idVideo.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (releaseDateTime != null ? releaseDateTime.hashCode() : 0);
        result = 31 * result + (countViewing != null ? countViewing.hashCode() : 0);
        result = 31 * result + (idUser != null ? idUser.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PreviewVideoResponse{" +
               "idVideo=" + idVideo +
               ", name='" + name + '\'' +
               ", duration=" + duration +
               ", description='" + description + '\'' +
               ", releaseDateTime=" + releaseDateTime +
               ", countViewing=" + countViewing +
               ", idUser=" + idUser +
               ", userName='" + userName + '\'' +
               '}';
    }
}
