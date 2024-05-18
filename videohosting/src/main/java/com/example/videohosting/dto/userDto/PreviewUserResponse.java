package com.example.videohosting.dto.userDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.core.io.Resource;

import java.util.Objects;

public class PreviewUserResponse {
    @NotNull
    private Long idUser;
    @NotBlank
    @Size(max = 40)
    private String channelName;
    private Resource imageIcon;
    @NotNull
    @PositiveOrZero
    private Long countSubscribers;

    public PreviewUserResponse(Long idUser, String channelName, Resource imageIcon, Long countSubscribers) {
        this.idUser = idUser;
        this.channelName = channelName;
        this.imageIcon = imageIcon;
        this.countSubscribers = countSubscribers;
    }

    public PreviewUserResponse() {
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

    public Resource getImageIcon() {
        return imageIcon;
    }

    public void setImageIcon(Resource imageIcon) {
        this.imageIcon = imageIcon;
    }

    public Long getCountSubscribers() {
        return countSubscribers;
    }

    public void setCountSubscribers(Long countSubscribers) {
        this.countSubscribers = countSubscribers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PreviewUserResponse that = (PreviewUserResponse) o;

        if (!idUser.equals(that.idUser)) return false;
        if (!channelName.equals(that.channelName)) return false;
        if (!Objects.equals(imageIcon, that.imageIcon)) return false;
        return countSubscribers.equals(that.countSubscribers);
    }

    @Override
    public int hashCode() {
        int result = idUser.hashCode();
        result = 31 * result + channelName.hashCode();
        result = 31 * result + (imageIcon != null ? imageIcon.hashCode() : 0);
        result = 31 * result + countSubscribers.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "PreviewUserResponse{" +
               "idUser=" + idUser +
               ", name='" + channelName + '\'' +
               ", imageIcon='" + imageIcon + '\'' +
               ", countSubscribers=" + countSubscribers +
               '}';
    }
}
