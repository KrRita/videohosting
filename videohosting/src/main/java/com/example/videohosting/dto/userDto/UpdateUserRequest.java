package com.example.videohosting.dto.userDto;

import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class UpdateUserRequest {
    @Size(max = 40)
    private String channelName;
    private MultipartFile imageIcon;
    private MultipartFile imageHeader;
    @Size(max = 1000)
    private String  description;
    private String password;

    public UpdateUserRequest(String channelName, MultipartFile imageIcon, MultipartFile imageHeader,
                             String description, String password) {
        this.channelName = channelName;
        this.imageIcon = imageIcon;
        this.imageHeader = imageHeader;
        this.description = description;
        this.password = password;
    }

    public UpdateUserRequest() {
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public MultipartFile getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(MultipartFile imageIcon) {
        this.imageIcon = imageIcon;
    }

    public MultipartFile getImageHeader() {
        return imageHeader;
    }

    public void setImageHeader(MultipartFile imageHeader) {
        this.imageHeader = imageHeader;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UpdateUserRequest that = (UpdateUserRequest) o;

        if (!channelName.equals(that.channelName)) return false;
        if (!Objects.equals(imageIcon, that.imageIcon)) return false;
        if (!Objects.equals(imageHeader, that.imageHeader)) return false;
        if (!description.equals(that.description)) return false;
        return password.equals(that.password);
    }

    @Override
    public int hashCode() {
        int result = channelName.hashCode();
        result = 31 * result + (imageIcon != null ? imageIcon.hashCode() : 0);
        result = 31 * result + (imageHeader != null ? imageHeader.hashCode() : 0);
        result = 31 * result + description.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UpdateUserRequest{" +
               ", name='" + channelName + '\'' +
               ", imageIcon='" + imageIcon + '\'' +
               ", imageHeader='" + imageHeader + '\'' +
               ", description='" + description + '\'' +
               ", password='" + password + '\'' +
               '}';
    }
}
