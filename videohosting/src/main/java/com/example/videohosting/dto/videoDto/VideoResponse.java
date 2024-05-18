package com.example.videohosting.dto.videoDto;

import com.example.videohosting.dto.userDto.PreviewUserResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.core.io.Resource;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class VideoResponse {
    @NotNull
    private Long idVideo;
    @NotNull
    private PreviewUserResponse previewUserResponse;
    @NotBlank
    @Size(max = 70)
    private String name;
    private Duration duration;
    @NotBlank
    @Size(max = 1000)
    private String description;
    @NotNull
    @Past
    private Timestamp releaseDateTime;

    private Resource videoFile;
    @NotEmpty
    private List<String> categories;
    @NotNull
    @PositiveOrZero
    private Long countViewing;
    @NotNull
    @PositiveOrZero
    private Long countLikes;
    @NotNull
    @PositiveOrZero
    private Long countDislikes;

    public VideoResponse(Long idVideo, PreviewUserResponse previewUserResponse, String name,
                         Duration duration, String description, Timestamp releaseDateTime,
                         Resource videoFile, List<String> categories, Long countViewing, Long countLikes,
                         Long countDislikes) {
        this.idVideo = idVideo;
        this.previewUserResponse = previewUserResponse;
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.releaseDateTime = releaseDateTime;
        this.videoFile = videoFile;
        this.categories = categories;
        this.countViewing = countViewing;
        this.countLikes = countLikes;
        this.countDislikes = countDislikes;
    }

    public VideoResponse() {
    }

    public Long getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(Long idVideo) {
        this.idVideo = idVideo;
    }

    public PreviewUserResponse getPreviewUserResponse() {
        return previewUserResponse;
    }

    public void setPreviewUserResponse(PreviewUserResponse previewUserResponse) {
        this.previewUserResponse = previewUserResponse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
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

    public Resource getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(Resource videoFile) {
        this.videoFile = videoFile;
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

        VideoResponse that = (VideoResponse) o;

        if (!Objects.equals(idVideo, that.idVideo)) return false;
        if (!Objects.equals(previewUserResponse, that.previewUserResponse))
            return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(duration, that.duration)) return false;
        if (!Objects.equals(description, that.description)) return false;
        if (!Objects.equals(releaseDateTime, that.releaseDateTime))
            return false;
        if (!Objects.equals(videoFile, that.videoFile)) return false;
        if (!Objects.equals(categories, that.categories)) return false;
        if (!Objects.equals(countViewing, that.countViewing)) return false;
        if (!Objects.equals(countLikes, that.countLikes)) return false;
        return Objects.equals(countDislikes, that.countDislikes);
    }

    @Override
    public int hashCode() {
        int result = idVideo != null ? idVideo.hashCode() : 0;
        result = 31 * result + (previewUserResponse != null ? previewUserResponse.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (releaseDateTime != null ? releaseDateTime.hashCode() : 0);
        result = 31 * result + (videoFile != null ? videoFile.hashCode() : 0);
        result = 31 * result + (categories != null ? categories.hashCode() : 0);
        result = 31 * result + (countViewing != null ? countViewing.hashCode() : 0);
        result = 31 * result + (countLikes != null ? countLikes.hashCode() : 0);
        result = 31 * result + (countDislikes != null ? countDislikes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "VideoResponse{" +
               "idVideo=" + idVideo +
               ", previewUserResponse=" + previewUserResponse +
               ", name='" + name + '\'' +
               ", duration=" + duration +
               ", description='" + description + '\'' +
               ", releaseDateTime=" + releaseDateTime +
               ", url='" + videoFile + '\'' +
               ", categories=" + categories +
               ", countViewing=" + countViewing +
               ", countLikes=" + countLikes +
               ", countDislikes=" + countDislikes +
               '}';
    }
}
