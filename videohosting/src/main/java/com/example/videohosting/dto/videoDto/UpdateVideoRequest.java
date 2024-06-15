package com.example.videohosting.dto.videoDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class UpdateVideoRequest {
    @NotNull
    private Long idVideo;
    @NotBlank
    @Size(max = 70)
    private String name;
    @NotBlank
    @Size(max = 1000)
    private String description;

    public UpdateVideoRequest(Long idVideo, String name, String description) {
        this.idVideo = idVideo;
        this.name = name;
        this.description = description;
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

    public Long getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(Long idVideo) {
        this.idVideo = idVideo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpdateVideoRequest request = (UpdateVideoRequest) o;

        if (!Objects.equals(idVideo, request.idVideo)) return false;
        if (!Objects.equals(name, request.name)) return false;
        return Objects.equals(description, request.description);
    }

    @Override
    public int hashCode() {
        int result = idVideo != null ? idVideo.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UpdateVideoRequest{" +
               "idVideo=" + idVideo +
               ", name='" + name + '\'' +
               ", description='" + description + '\'' +
               '}';
    }
}
