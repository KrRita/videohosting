package com.example.videohosting.repository;

import com.example.videohosting.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> getPlaylistsByUser_IdUser(Long idUser);
}

