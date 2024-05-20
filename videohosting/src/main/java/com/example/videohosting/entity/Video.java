package com.example.videohosting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;

@Entity
@Table(name = "Video")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_video")
    private Long idVideo;
    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
    @Column(name = "video_name")
    private String name;
    @Column(name = "duration")
    private Long duration;
    @Column(name = "description")
    private String description;
    @Column(name = "release_date_time")
    private Timestamp releaseDateTime;
    @ManyToMany
    @JoinTable(name = "Videos_categories",
            joinColumns = @JoinColumn(name = "id_video"),
            inverseJoinColumns = @JoinColumn(name = "id_category"))
    private List<Category> categories;

    public Video(Long idVideo, User user, String name, Long duration, String description,
                 Timestamp releaseDateTime, List<Category> categories) {
        this.idVideo = idVideo;
        this.user = user;
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.releaseDateTime = releaseDateTime;
        this.categories = categories;
    }

    public Video() {
    }
    public void addCategory(Category category){
        categories.add(category);
    }
    public void deleteCategory(Category category){
        categories.remove(category);
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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Video video = (Video) o;

        if (!idVideo.equals(video.idVideo)) return false;
        if (!user.equals(video.user)) return false;
        if (!name.equals(video.name)) return false;
        if (!duration.equals(video.duration)) return false;
        if (!description.equals(video.description)) return false;
        if (!releaseDateTime.equals(video.releaseDateTime)) return false;
        return categories.equals(video.categories);
    }

    @Override
    public int hashCode() {
        int result = idVideo.hashCode();
        result = 31 * result + user.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + duration.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + releaseDateTime.hashCode();
        result = 31 * result + categories.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Video{" +
               "idVideo=" + idVideo +
               ", user=" + user +
               ", name='" + name + '\'' +
               ", duration=" + duration +
               ", description='" + description + '\'' +
               ", releaseDateTime=" + releaseDateTime +
               ", categories=" + categories +
               '}';
    }
}
