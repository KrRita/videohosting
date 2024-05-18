package com.example.videohosting.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class UserModel implements UserDetails {
    private Long idUser;
    private String email;
    private String channelName;
    private String description;
    private Timestamp dateOfRegistration;
    private String password;
    private List<UserModel> subscriptions;
    private List<VideoModel> videos;
    private List<PlaylistModel> playlists;
    private Long countSubscribers;

    public UserModel(Long idUser, String email, String channelName, String description,
                     Timestamp dateOfRegistration, String password, List<UserModel> subscriptions,
                     List<VideoModel> videos, List<PlaylistModel> playlists, Long countSubscribers) {
        this.idUser = idUser;
        this.email = email;
        this.channelName = channelName;
        this.description = description;
        this.dateOfRegistration = dateOfRegistration;
        this.password = password;
        this.subscriptions = subscriptions;
        this.videos = videos;
        this.playlists = playlists;
        this.countSubscribers = countSubscribers;
    }

    public UserModel() {
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<UserModel> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<UserModel> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<VideoModel> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoModel> videos) {
        this.videos = videos;
    }

    public List<PlaylistModel> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<PlaylistModel> playlists) {
        this.playlists = playlists;
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

        UserModel userModel = (UserModel) o;

        if (!idUser.equals(userModel.idUser)) return false;
        if (!email.equals(userModel.email)) return false;
        if (!channelName.equals(userModel.channelName)) return false;
        if (!description.equals(userModel.description)) return false;
        if (!dateOfRegistration.equals(userModel.dateOfRegistration)) return false;
        if (!password.equals(userModel.password)) return false;
        if (!Objects.equals(subscriptions, userModel.subscriptions))
            return false;
        if (!Objects.equals(videos, userModel.videos))
            return false;
        if (!Objects.equals(playlists, userModel.playlists))
            return false;
        return Objects.equals(countSubscribers, userModel.countSubscribers);
    }

    @Override
    public int hashCode() {
        int result = idUser.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + channelName.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + dateOfRegistration.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + (subscriptions != null ? subscriptions.hashCode() : 0);
        result = 31 * result + (videos != null ? videos.hashCode() : 0);
        result = 31 * result + (playlists != null ? playlists.hashCode() : 0);
        result = 31 * result + (countSubscribers != null ? countSubscribers.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserModel{" +
               "idUser=" + idUser +
               ", email='" + email + '\'' +
               ", channelName='" + channelName + '\'' +
               ", description='" + description + '\'' +
               ", dateOfRegistration=" + dateOfRegistration +
               ", password='" + password + '\'' +
               ", subscriptions=" + subscriptions +
               ", videoModels=" + videos +
               ", playlistModels=" + playlists +
               ", countSubscribers=" + countSubscribers +
               '}';
    }
}
