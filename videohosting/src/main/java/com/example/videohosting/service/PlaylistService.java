package com.example.videohosting.service;

import com.example.videohosting.entity.Category;
import com.example.videohosting.entity.Playlist;
import com.example.videohosting.entity.PlaylistWithVideos;
import com.example.videohosting.exception.NotFoundException;
import com.example.videohosting.mapper.PlaylistMapper;
import com.example.videohosting.model.PlaylistModel;
import com.example.videohosting.model.PlaylistWithVideosModel;
import com.example.videohosting.model.VideoModel;
import com.example.videohosting.repository.PlaylistRepository;
import com.example.videohosting.repository.PlaylistWithVideosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final PlaylistMapper playlistMapper;
    private final PlaylistWithVideosRepository playlistWithVideosRepository;
    private final MediaService mediaService;

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository, PlaylistMapper playlistMapper,
                           PlaylistWithVideosRepository playlistWithVideosRepository, MediaService mediaService) {
        this.playlistRepository = playlistRepository;
        this.playlistMapper = playlistMapper;
        this.playlistWithVideosRepository = playlistWithVideosRepository;
        this.mediaService = mediaService;
    }

    public PlaylistModel insert(PlaylistModel playlistModel, MultipartFile imageIcon) {
        playlistModel.setDateCreation(Timestamp.valueOf(LocalDateTime.now()));
        Playlist playlist = playlistMapper.toEntity(playlistModel);
        Playlist savedPlaylist = playlistRepository.save(playlist);
        String path = "imageIconPlaylist\\" + savedPlaylist.getIdPlaylist() + ".jpeg";
        if (imageIcon != null) {
            mediaService.saveMedia(imageIcon, path);
        }
        PlaylistModel savedPlaylistModel = playlistMapper.toModel(savedPlaylist);
        savedPlaylistModel.setCountVideos(0L);
        return savedPlaylistModel;
    }

    public PlaylistModel update(PlaylistModel playlistModel, MultipartFile imageIcon) {
        Playlist newPlaylist = playlistMapper.toEntity(playlistModel);
        Playlist oldPlaylist = playlistRepository.findById(playlistModel.getIdPlaylist()).orElseThrow(() -> new NotFoundException("Playlist not found"));
        if (newPlaylist.getNamePlaylist() != null) {
            oldPlaylist.setNamePlaylist(newPlaylist.getNamePlaylist());
        }
        if (imageIcon != null) {
            String path = "imageIconPlaylist\\" + oldPlaylist.getIdPlaylist() + ".jpeg";
            mediaService.saveMedia(imageIcon, path);
        }
        Playlist savedPlaylist = playlistRepository.save(oldPlaylist);
        PlaylistModel savedPlaylistModel = playlistMapper.toModel(savedPlaylist);
        Long countVideos = playlistWithVideosRepository.countPlaylistWithVideosByIdPlaylist(savedPlaylist.getIdPlaylist());
        savedPlaylistModel.setCountVideos(countVideos);
        return savedPlaylistModel;
    }

    public void delete(Long id) {
        String path = "imageIconPlaylist\\" + id + ".jpeg";
        playlistRepository.deleteById(id);
        mediaService.deleteMedia(path);
    }

    public PlaylistModel findPlaylistById(Long id) {
        Playlist playlist = playlistRepository.findById(id).orElseThrow(() -> new NotFoundException("Playlist not found"));
        PlaylistModel playlistModel = playlistMapper.toModel(playlist);
        Long countVideos = playlistWithVideosRepository.countPlaylistWithVideosByIdPlaylist(id);
        playlistModel.setCountVideos(countVideos);
        return playlistModel;
    }
}
