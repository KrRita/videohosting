package com.example.videohosting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "User_")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser;
    @Column(name = "e_mail")
    private String email;
    @Column(name = "channel_name")
    private String channelName;
    @Column(name = "description")
    private String description;
    @Column(name = "date_of_registration")
    private Timestamp dateOfRegistration;
    @Column(name = "password")
    private String password;
    @ManyToMany
    @JoinTable(name = "Subscription",
            joinColumns = @JoinColumn(name = "id_user_subscriber"),
            inverseJoinColumns = @JoinColumn(name = "id_user_channel"))
    private List<User> subscriptions;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Video> videos;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Playlist> playlists;

    public User() {
    }

    public User(Long idUser, String email, String channelName, String description, Timestamp dateOfRegistration,
                String password, List<User> subscriptions, List<Video> videos,
                List<Playlist> playlists) {
        this.idUser = idUser;
        this.email = email;
        this.channelName = channelName;
        this.description = description;
        this.dateOfRegistration = dateOfRegistration;
        this.password = password;
        this.subscriptions = subscriptions;
        this.videos = videos;
        this.playlists = playlists;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<User> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<User> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!idUser.equals(user.idUser)) return false;
        if (!email.equals(user.email)) return false;
        if (!channelName.equals(user.channelName)) return false;
        if (!description.equals(user.description)) return false;
        if (!dateOfRegistration.equals(user.dateOfRegistration)) return false;
        if (!password.equals(user.password)) return false;
        if (!Objects.equals(subscriptions, user.subscriptions))
            return false;
        if (!Objects.equals(videos, user.videos)) return false;
        return Objects.equals(playlists, user.playlists);
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
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
               "idUser=" + idUser +
               ", email='" + email + '\'' +
               ", channelName='" + channelName + '\'' +
               ", description='" + description + '\'' +
               ", dateOfRegistration=" + dateOfRegistration +
               '}';
    }
}
