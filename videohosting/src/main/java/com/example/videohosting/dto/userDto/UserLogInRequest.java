package com.example.videohosting.dto.userDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserLogInRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;

    public UserLogInRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserLogInRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

        UserLogInRequest that = (UserLogInRequest) o;

        if (!email.equals(that.email)) return false;
        return password.equals(that.password);
    }

    @Override
    public int hashCode() {
        int result = email.hashCode();
        result = 31 * result + password.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "UserLogInRequest{" +
               "email='" + email + '\'' +
               ", password='" + password + '\'' +
               '}';
    }
}
