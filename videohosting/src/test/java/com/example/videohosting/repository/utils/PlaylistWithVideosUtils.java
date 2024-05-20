package com.example.videohosting.repository.utils;

import com.example.videohosting.entity.Playlist;
import com.example.videohosting.entity.PlaylistWithVideos;
import com.example.videohosting.entity.User;
import com.example.videohosting.entity.Video;
import com.example.videohosting.repository.PlaylistRepository;
import com.example.videohosting.repository.PlaylistWithVideosRepository;
import com.example.videohosting.repository.UserRepository;
import com.example.videohosting.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
public class PlaylistWithVideosUtils {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private VideoRepository videoRepository;
    @Autowired
    private PlaylistWithVideosRepository playlistWithVideosRepository;

    public PlaylistWithVideos createAndSavePlaylistWithVideos() {
        User user = new User();
        user.setEmail("testuser@example.com");
        user.setChannelName("Test Channel");
        user.setDescription("Test Description");
        user.setDateOfRegistration(new Timestamp(System.currentTimeMillis()));
        user.setPassword("password");
        user.setSubscriptions(List.of());
        user.setVideos(List.of());
        user.setPlaylists(List.of());
        user = userRepository.save(user);

        Video video = new Video();
        video.setUser(user);
        video.setName("Test Video");
        video.setDuration(600L);
        video.setDescription("Test Description");
        video.setReleaseDateTime(new Timestamp(System.currentTimeMillis()));
        video.setCategories(List.of());
        video = videoRepository.save(video);

        Playlist playlist = new Playlist();
        playlist.setUser(user);
        playlist.setNamePlaylist("Test Playlist");
        playlist.setDateCreation(new Timestamp(System.currentTimeMillis()));
        playlist = playlistRepository.save(playlist);

        PlaylistWithVideos playlistWithVideos = new PlaylistWithVideos();
        playlistWithVideos.setIdPlaylist(playlist.getIdPlaylist());
        playlistWithVideos.setVideo(video);
        playlistWithVideos.setDateOfAddition(new Timestamp(System.currentTimeMillis()));
        return playlistWithVideosRepository.save(playlistWithVideos);
    }
    public void tearDown() {
        playlistWithVideosRepository.deleteAll();
        playlistRepository.deleteAll();
        videoRepository.deleteAll();
        userRepository.deleteAll();
    }

}
