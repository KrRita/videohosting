package com.example.videohosting.controller;

import com.example.videohosting.dto.playlistDto.CreatePlaylistRequest;
import com.example.videohosting.dto.playlistDto.PlaylistResponse;
import com.example.videohosting.dto.playlistDto.UpdatePlaylistRequest;
import com.example.videohosting.dto.playlistWithVideosDto.CreatePlaylistWithVideosRequest;
import com.example.videohosting.dto.playlistWithVideosDto.DeletePlaylistWithVideosRequest;
import com.example.videohosting.dto.playlistWithVideosDto.PlaylistWithVideosResponse;
import com.example.videohosting.dto.userDto.UpdateUserRequest;
import com.example.videohosting.exception.LoadFileException;
import com.example.videohosting.mapper.PlaylistMapper;
import com.example.videohosting.mapper.PlaylistWithVideosMapper;
import com.example.videohosting.model.PlaylistModel;
import com.example.videohosting.model.PlaylistWithVideosModel;
import com.example.videohosting.model.UserModel;
import com.example.videohosting.model.VideoModel;
import com.example.videohosting.service.MediaService;
import com.example.videohosting.service.PlaylistService;
import com.example.videohosting.service.UserService;
import com.example.videohosting.service.VideoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;
    private final PlaylistMapper playlistMapper;
    private final VideoService videoService;
    private final PlaylistWithVideosMapper playlistWithVideosMapper;
    private final UserService userService;

    @Autowired
    public PlaylistController(PlaylistService playlistService, PlaylistMapper playlistMapper,
                              VideoService videoService, PlaylistWithVideosMapper playlistWithVideosMapper,
                               UserService userService) {
        this.playlistService = playlistService;
        this.playlistMapper = playlistMapper;
        this.videoService = videoService;
        this.playlistWithVideosMapper = playlistWithVideosMapper;
        this.userService = userService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<PlaylistResponse> postPlaylist(
            @Valid @RequestPart(value = "request", required = true) String request,
            @RequestPart(value = "icon", required = true) MultipartFile icon) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        CreatePlaylistRequest createPlaylistRequest = objectMapper.readValue(request, CreatePlaylistRequest.class);
        PlaylistModel playlistModel = playlistMapper.toModelFromCreateRequest(createPlaylistRequest);
        UserModel userModel = userService.findUserById(playlistModel.getUser().getIdUser());
        playlistModel.setUser(userModel);
        PlaylistModel playlistResult = playlistService.insert(playlistModel, icon);
        PlaylistResponse response = playlistMapper.toResponseFromModel(playlistResult);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping(path="/updatePlaylist", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PlaylistResponse> putPlaylist(
            @Valid @RequestPart(value = "request", required = true) String request,
            @RequestPart(value = "icon", required = true) MultipartFile icon) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        UpdatePlaylistRequest updatePlaylistRequest = objectMapper.readValue(request, UpdatePlaylistRequest.class);
        PlaylistModel playlistModel = playlistMapper.toModelFromUpdateRequest(updatePlaylistRequest);
        PlaylistModel model = playlistService.update(playlistModel, icon);
        PlaylistResponse response = playlistMapper.toResponseFromModel(model);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable Long id) {
        playlistService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaylistResponse> getPlaylistById(@PathVariable Long id) {
        PlaylistModel model = playlistService.findPlaylistById(id);
        PlaylistResponse response = playlistMapper.toResponseFromModel(model);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{idPlaylist}/videos")
    public ResponseEntity<List<PlaylistWithVideosResponse>> getVideosFromPlaylist(@PathVariable Long idPlaylist) {
        List<PlaylistWithVideosModel> models = videoService.getVideosFromPlaylist(idPlaylist);
        List<PlaylistWithVideosResponse> responses = playlistWithVideosMapper.toListResponseFromListModel(models);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PostMapping("/{idPlaylist}/videos")
    @Transactional
    public ResponseEntity<List<PlaylistWithVideosResponse>> addVideoInPlaylist
            (@PathVariable Long idPlaylist, @Valid @RequestBody CreatePlaylistWithVideosRequest request) {
        PlaylistWithVideosModel model = playlistWithVideosMapper.toModelFromCreateRequest(request);
        VideoModel videoModel = videoService.findVideoById(request.getIdVideo());
        model.setIdPlaylist(idPlaylist);
        model.setVideo(videoModel);
        List<PlaylistWithVideosModel> models = videoService.insertPlaylistWithVideos(model);
        List<PlaylistWithVideosResponse> responses = playlistWithVideosMapper.toListResponseFromListModel(models);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @DeleteMapping("/{idPlaylist}/videos")
    public ResponseEntity<List<PlaylistWithVideosResponse>> deleteVideoFromPlaylist
            (@PathVariable Long idPlaylist, @Valid @RequestBody DeletePlaylistWithVideosRequest request) {
        List<PlaylistWithVideosModel> models = videoService.deletePlaylistWithVideos(idPlaylist, request.getIdVideo());
        List<PlaylistWithVideosResponse> responses = playlistWithVideosMapper.toListResponseFromListModel(models);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }
}
