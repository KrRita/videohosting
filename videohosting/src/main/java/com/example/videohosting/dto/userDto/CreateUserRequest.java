package com.example.videohosting.dto.userDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateUserRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Size(max = 40)
    private String channelName;
    @NotBlank
    @Size(max = 1000)
    private String description;
    @NotBlank
    private String password;

    public CreateUserRequest(String email, String channelName, String description, String password) {
        this.email = email;
        this.channelName = channelName;
        this.description = description;
        this.password = password;
    }

    public CreateUserRequest() {
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

        CreateUserRequest that = (CreateUserRequest) o;

        if (!email.equals(that.email)) return false;
        if (!channelName.equals(that.channelName)) return false;
        if (!description.equals(that.description)) return false;
        return password.equals(that.password);
    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + channelName.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CreateUserRequest{" +
               "email='" + email + '\'' +
               ", name='" + channelName + '\'' +
               ", description='" + description + '\'' +
               ", password='" + password + '\'' +
               '}';
    }
}
