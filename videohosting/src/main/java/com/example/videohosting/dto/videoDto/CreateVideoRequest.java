package com.example.videohosting.dto.videoDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class CreateVideoRequest {
    @NotNull
    private Long idUser;
    @NotBlank
    @Size(max = 70)
    private String name;
    @NotBlank
    @Size(max = 1000)
    private String description;
    private MultipartFile videoFile;
    private MultipartFile previewImage;
    @NotEmpty
    private List<String> categories;

    public CreateVideoRequest(Long idUser, String name, String description, MultipartFile videoFile,
                              MultipartFile previewImage, List<String> categories) {
        this.idUser = idUser;
        this.name = name;
        this.description = description;
        this.videoFile = videoFile;
        this.previewImage = previewImage;
        this.categories = categories;
    }

    public CreateVideoRequest() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(MultipartFile videoFile) {
        this.videoFile = videoFile;
    }

    public MultipartFile getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(MultipartFile previewImage) {
        this.previewImage = previewImage;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreateVideoRequest that = (CreateVideoRequest) o;

        if (!Objects.equals(idUser, that.idUser)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(description, that.description)) return false;
        if (!Objects.equals(videoFile, that.videoFile)) return false;
        if (!Objects.equals(previewImage, that.previewImage)) return false;
        return Objects.equals(categories, that.categories);
    }

    @Override
    public int hashCode() {
        int result = idUser != null ? idUser.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (videoFile != null ? videoFile.hashCode() : 0);
        result = 31 * result + (previewImage != null ? previewImage.hashCode() : 0);
        result = 31 * result + (categories != null ? categories.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CreateVideoRequest{" +
               "idUser=" + idUser +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", url='" + videoFile + '\'' +
               ", previewImage='" + previewImage + '\'' +
               ", categories=" + categories +
               '}';
    }
}
