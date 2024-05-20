package com.example.videohosting.model;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class VideoModel {
    private Long idVideo;
    private UserModel user;
    private String name;
    private Long duration;
    private String description;
    private Timestamp releaseDateTime;
    private List<String> categories;
    private Long countViewing;
    private Long countLikes;
    private Long countDislikes;

    public VideoModel(Long idVideo, UserModel user, String name, Long duration,
                      String description, Timestamp releaseDateTime, List<String> categories,
                      Long countViewing, Long countLikes, Long countDislikes) {
        this.idVideo = idVideo;
        this.user = user;
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.releaseDateTime = releaseDateTime;
        this.categories = categories;
        this.countViewing = countViewing;
        this.countLikes = countLikes;
        this.countDislikes = countDislikes;
    }

    public VideoModel() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getReleaseDateTime() {
        return releaseDateTime;
    }

    public void setReleaseDateTime(Timestamp releaseDateTime) {
        this.releaseDateTime = releaseDateTime;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Long getCountViewing() {
        return countViewing;
    }

    public void setCountViewing(Long countViewing) {
        this.countViewing = countViewing;
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

        VideoModel that = (VideoModel) o;

        if (!idVideo.equals(that.idVideo)) return false;
        if (!user.equals(that.user)) return false;
        if (!name.equals(that.name)) return false;
        if (!duration.equals(that.duration)) return false;
        if (!description.equals(that.description)) return false;
        if (!releaseDateTime.equals(that.releaseDateTime)) return false;
        if (!Objects.equals(categories, that.categories))
            return false;
        if (!Objects.equals(countViewing, that.countViewing)) return false;
        if (!Objects.equals(countLikes, that.countLikes)) return false;
        return Objects.equals(countDislikes, that.countDislikes);
    }

    @Override
    public int hashCode() {
        int result = idVideo.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + duration.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + releaseDateTime.hashCode();
        result = 31 * result + (categories != null ? categories.hashCode() : 0);
        result = 31 * result + (countViewing != null ? countViewing.hashCode() : 0);
        result = 31 * result + (countLikes != null ? countLikes.hashCode() : 0);
        result = 31 * result + (countDislikes != null ? countDislikes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "VideoModel{" +
               "idVideo=" + idVideo +
               ", userModel=" + user +
               ", name='" + name + '\'' +
               ", duration=" + duration +
               ", description='" + description + '\'' +
               ", releaseDateTime=" + releaseDateTime +
               ", categoryModels=" + categories +
               ", countViewing=" + countViewing +
               ", countLikes=" + countLikes +
               ", countDislikes=" + countDislikes +
               '}';
    }
}
