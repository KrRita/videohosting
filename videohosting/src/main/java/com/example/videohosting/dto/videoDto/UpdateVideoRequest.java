package com.example.videohosting.dto.videoDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class UpdateVideoRequest {
    @NotBlank
    @Size(max = 70)
    private String name;
    @NotBlank
    @Size(max = 1000)
    private String description;
    private MultipartFile previewImage;

    public UpdateVideoRequest(String name, String description, MultipartFile previewImage) {
        this.name = name;
        this.description = description;
        this.previewImage = previewImage;
    }

    public UpdateVideoRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getPreviewImage() {
        return previewImage;
    }

    public void setPreviewImage(MultipartFile previewImage) {
        this.previewImage = previewImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpdateVideoRequest that = (UpdateVideoRequest) o;

        if (!name.equals(that.name)) return false;
        if (!description.equals(that.description)) return false;
        return Objects.equals(previewImage, that.previewImage);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + (previewImage != null ? previewImage.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UpdateVideoRequest{" +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", previewImage='" + previewImage + '\'' +
               '}';
    }
}
