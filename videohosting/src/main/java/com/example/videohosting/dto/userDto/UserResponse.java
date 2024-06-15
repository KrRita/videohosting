package com.example.videohosting.dto.userDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.springframework.core.io.Resource;

import java.sql.Timestamp;
import java.util.Objects;

public class UserResponse {
    @NotNull
    private Long idUser;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Size(max = 40)
    private String channelName;
    @NotBlank
    @Size(max = 1000)
    private String description;
    @NotNull
    @Past
    private Timestamp dateOfRegistration;
    @NotNull
    @PositiveOrZero
    private Long countSubscribers;

    public UserResponse(Long idUser, String email, String channelName, String description,
                        Timestamp dateOfRegistration,
                        Long countSubscribers) {
        this.idUser = idUser;
        this.email = email;
        this.channelName = channelName;
        this.description = description;
        this.dateOfRegistration = dateOfRegistration;
        this.countSubscribers = countSubscribers;
    }

    public UserResponse() {
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDateOfRegistration() {
        return dateOfRegistration;
    }

    public void setDateOfRegistration(Timestamp dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
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

        UserResponse that = (UserResponse) o;

        if (!Objects.equals(idUser, that.idUser)) return false;
        if (!Objects.equals(email, that.email)) return false;
        if (!Objects.equals(channelName, that.channelName)) return false;
        if (!Objects.equals(description, that.description)) return false;
        if (!Objects.equals(dateOfRegistration, that.dateOfRegistration))
            return false;
        return Objects.equals(countSubscribers, that.countSubscribers);
    }

    @Override
    public int hashCode() {
        int result = idUser != null ? idUser.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (channelName != null ? channelName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (dateOfRegistration != null ? dateOfRegistration.hashCode() : 0);
        result = 31 * result + (countSubscribers != null ? countSubscribers.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
               "idUser=" + idUser +
               ", email='" + email + '\'' +
               ", channelName='" + channelName + '\'' +
               ", description='" + description + '\'' +
               ", dateOfRegistration=" + dateOfRegistration +
               ", countSubscribers=" + countSubscribers +
               '}';
    }
}
