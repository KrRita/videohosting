package com.example.videohosting.service;

import com.example.videohosting.entity.Playlist;
import com.example.videohosting.exception.NotFoundException;
import com.example.videohosting.mapper.PlaylistMapper;
import com.example.videohosting.model.PlaylistModel;
import com.example.videohosting.repository.PlaylistRepository;
import com.example.videohosting.repository.PlaylistWithVideosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final PlaylistMapper playlistMapper;
    private final PlaylistWithVideosRepository playlistWithVideosRepository;
    private final MediaService mediaService;
    private final Logger logger = LoggerFactory.getLogger(PlaylistService.class);


    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository, PlaylistMapper playlistMapper,
                           PlaylistWithVideosRepository playlistWithVideosRepository, MediaService mediaService) {
        this.playlistRepository = playlistRepository;
        this.playlistMapper = playlistMapper;
        this.playlistWithVideosRepository = playlistWithVideosRepository;
        this.mediaService = mediaService;
    }

    public PlaylistModel insert(PlaylistModel playlistModel, MultipartFile imageIcon) {
        logger.info("Inserting new playlist: {}", playlistModel.getNamePlaylist());
        playlistModel.setDateCreation(Timestamp.valueOf(LocalDateTime.now()));
        Playlist playlist = playlistMapper.toEntity(playlistModel);
        Playlist savedPlaylist = playlistRepository.save(playlist);
        String path = "imageIconPlaylist\\" + savedPlaylist.getIdPlaylist() + ".jpeg";
        if (imageIcon != null) {
            mediaService.saveMedia(imageIcon, path);
            logger.info("Saved image icon for playlist at path: {}", path);
        }
        PlaylistModel savedPlaylistModel = playlistMapper.toModel(savedPlaylist);
        savedPlaylistModel.setCountVideos(0L);
        logger.info("Playlist inserted successfully with id: {}", savedPlaylist.getIdPlaylist());
        return savedPlaylistModel;
    }

    public PlaylistModel update(PlaylistModel playlistModel, MultipartFile imageIcon) {
        logger.info("Updating playlist with id: {}", playlistModel.getIdPlaylist());
        Playlist newPlaylist = playlistMapper.toEntity(playlistModel);
        Playlist oldPlaylist = playlistRepository.findById(playlistModel.getIdPlaylist())
                .orElseThrow(() -> {
                    logger.error("Playlist with id {} not found", playlistModel.getIdPlaylist());
                    return new NotFoundException("Playlist not found");
                });
        if (newPlaylist.getNamePlaylist() != null) {
            oldPlaylist.setNamePlaylist(newPlaylist.getNamePlaylist());
            logger.info("Updated playlist name to: {}", newPlaylist.getNamePlaylist());
        }
        if (imageIcon != null) {
            String path = "imageIconPlaylist\\" + oldPlaylist.getIdPlaylist() + ".jpeg";
            mediaService.saveMedia(imageIcon, path);
            logger.info("Saved image icon for playlist at path: {}", path);
        }
        Playlist savedPlaylist = playlistRepository.save(oldPlaylist);
        PlaylistModel savedPlaylistModel = playlistMapper.toModel(savedPlaylist);
        Long countVideos = playlistWithVideosRepository.countPlaylistWithVideosByIdPlaylist(savedPlaylist.getIdPlaylist());
        savedPlaylistModel.setCountVideos(countVideos);
        logger.info("Playlist updated successfully with id: {}", savedPlaylist.getIdPlaylist());
        return savedPlaylistModel;
    }

    public void delete(Long id) {
        logger.info("Deleting playlist with id: {}", id);
        String path = "imageIconPlaylist\\" + id + ".jpeg";
        playlistRepository.deleteById(id);
        mediaService.deleteMedia(path);
        logger.info("Deleted playlist and associated media with id: {}", id);
    }

    public PlaylistModel findPlaylistById(Long id) {
        logger.info("Finding playlist with id: {}", id);
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Playlist with id {} not found", id);
                    return new NotFoundException("Playlist not found");
                });
        PlaylistModel playlistModel = playlistMapper.toModel(playlist);
        Long countVideos = playlistWithVideosRepository.countPlaylistWithVideosByIdPlaylist(id);
        playlistModel.setCountVideos(countVideos);
        logger.info("Found playlist with id: {}", id);
        return playlistModel;
    }
}
