package com.example.videohosting.utils;

import com.example.videohosting.entity.Playlist;
import com.example.videohosting.entity.User;
import com.example.videohosting.repository.PlaylistRepository;
import com.example.videohosting.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class PlaylistUtils {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlaylistRepository playlistRepository;

    public Playlist createAndSavePlaylist() {
        User user = new User();
        user.setEmail("testuser123@example.com");
        user.setChannelName("Test Channel123");
        user.setDescription("Test Description123");
        user.setDateOfRegistration(new Timestamp(System.currentTimeMillis()));
        user.setPassword("password123456");
        user.setSubscriptions(List.of());
        user.setVideos(List.of());
        user.setPlaylists(List.of());
        user = userRepository.save(user);

        Playlist playlist = new Playlist();
        playlist.setUser(user);
        playlist.setNamePlaylist("Test Playlist");
        playlist.setDateCreation(new Timestamp(System.currentTimeMillis()));
        return playlistRepository.save(playlist);
    }

    public void tearDown() {
        playlistRepository.deleteAll();
        userRepository.deleteAll();
    }
}
