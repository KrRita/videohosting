package com.example.videohosting.repository;

import com.example.videohosting.entity.PlaylistWithVideos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlaylistWithVideosRepository extends JpaRepository<PlaylistWithVideos, Long> {
    List<PlaylistWithVideos> getPlaylistWithVideosByIdPlaylist(Long idPlaylist);
    Long countPlaylistWithVideosByIdPlaylist(Long idPlaylist);
    @Query("SELECT pv.idPlaylistWithVideos FROM PlaylistWithVideos pv WHERE pv.video.idVideo=:idVideo AND pv.idPlaylist=:idPlaylist" )
    Long getPlaylistWithVideosByIdPlaylistAndVideo_IdVideo(Long idPlaylist, Long idVideo);
}
