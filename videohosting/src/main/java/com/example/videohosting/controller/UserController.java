package com.example.videohosting.controller;

import com.example.videohosting.dto.playlistDto.PlaylistResponse;
import com.example.videohosting.dto.userDto.PreviewUserResponse;
import com.example.videohosting.dto.userDto.UpdateSubscriptionsRequest;
import com.example.videohosting.dto.userDto.UpdateUserRequest;
import com.example.videohosting.dto.userDto.UserResponse;
import com.example.videohosting.dto.videoDto.PreviewVideoResponse;
import com.example.videohosting.dto.viewedVideoDto.ViewedVideoResponse;
import com.example.videohosting.mapper.PlaylistMapper;
import com.example.videohosting.mapper.VideoMapper;
import com.example.videohosting.mapper.ViewedVideoMapper;
import com.example.videohosting.mapper.userMapper.UserMapper;
import com.example.videohosting.model.PlaylistModel;
import com.example.videohosting.model.UserModel;
import com.example.videohosting.model.VideoModel;
import com.example.videohosting.model.ViewedVideoModel;
import com.example.videohosting.service.UserService;
import com.example.videohosting.service.VideoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final VideoService videoService;
    private final VideoMapper videoMapper;
    private final ViewedVideoMapper viewedVideoMapper;
    private final PlaylistMapper playlistMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper, VideoService videoService,
                          VideoMapper videoMapper, ViewedVideoMapper viewedVideoMapper,
                          PlaylistMapper playlistMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.videoService = videoService;
        this.videoMapper = videoMapper;
        this.viewedVideoMapper = viewedVideoMapper;
        this.playlistMapper = playlistMapper;
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public ResponseEntity<UserResponse> putUser(
            @Valid @RequestPart(value = "request", required = true) String request,
            @RequestPart(value = "icon", required = true) MultipartFile icon,
            @RequestPart(value = "header", required = true) MultipartFile header ,
            Authentication authentication) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        UpdateUserRequest updateUserRequest = objectMapper.readValue(request, UpdateUserRequest.class);
        UserModel model = userMapper.toModelFromUpdateRequest(updateUserRequest);
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser();
        model.setIdUser(id);
        UserModel userModel = userService.update(model, header, icon);
        UserResponse response = userMapper.toResponseFromModel(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/subscription")
    @Transactional
    public ResponseEntity<UserResponse> postSubscription
            (@Valid @RequestBody UpdateSubscriptionsRequest request, Authentication authentication) {
        UserModel model = userMapper.toModelFromUpdateSubscriptionsRequest(request);
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser();
        model.setIdUser(id);
        UserModel subscription = userService.findUserById(request.getIdSubscription());
        List<UserModel> subscriptions = new ArrayList<>();
        subscriptions.add(subscription);
        model.setSubscriptions(subscriptions);
        UserModel userModel = userService.addSubscription(model);
        UserResponse response = userMapper.toResponseFromModel(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/subscription")
    @Transactional
    public ResponseEntity<UserResponse> deleteSubscription
            (@Valid @RequestBody UpdateSubscriptionsRequest request, Authentication authentication) {
        UserModel model = userMapper.toModelFromUpdateSubscriptionsRequest(request);
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser();
        model.setIdUser(id);
        UserModel userModel = userService.deleteSubscription(model);
        UserResponse response = userMapper.toResponseFromModel(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteUser(Authentication authentication) {
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser();
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserModel model = userService.findUserById(id);
        UserResponse response = userMapper.toResponseFromModel(model);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/subscriptions")
    @Transactional
    public ResponseEntity<List<PreviewUserResponse>> getUserSubscriptions(Authentication authentication) {
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser();
        UserModel user = userService.findUserById(id);
        List<UserModel> subscriptions = user.getSubscriptions();
        List<PreviewUserResponse> responses = userMapper.toPreviewUserResponseListFromModelUserList(subscriptions);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/videos")
    @Transactional
    public ResponseEntity<List<PreviewVideoResponse>> getUserVideos(Authentication authentication) {
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser();
        UserModel user = userService.findUserById(id);
        List<VideoModel> videos = user.getVideos();
        List<PreviewVideoResponse> responses = videoMapper.toListPreviewVideoResponseFromListModel(videos);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/subscriptions_videos")
    @Transactional
    public ResponseEntity<List<PreviewVideoResponse>> getSubscriptionsVideos(Authentication authentication) {
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser();
        List<VideoModel> videos = videoService.getSubscriptionsVideos(id);
        List<PreviewVideoResponse> responses = videoMapper.toListPreviewVideoResponseFromListModel(videos);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/viewed_videos")
    public ResponseEntity<List<ViewedVideoResponse>> getViewedVideos(Authentication authentication) {
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser();
        List<ViewedVideoModel> models = videoService.getViewedVideos(id);
        List<ViewedVideoResponse> responses = viewedVideoMapper.toResponseListFromModelList(models);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/liked_videos")
    public ResponseEntity<List<PreviewVideoResponse>> getLikedVideos(Authentication authentication) {
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser();
        List<VideoModel> models = videoService.getLikedVideos(id);
        List<PreviewVideoResponse> responses = videoMapper.toListPreviewVideoResponseFromListModel(models);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/playlists")
    public ResponseEntity<List<PlaylistResponse>> getUserPlaylists(Authentication authentication) {
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser();
        UserModel user = userService.findUserById(id);
        List<PlaylistModel> models = user.getPlaylists();
        List<PlaylistResponse> responses = playlistMapper.toPlaylistResponseListFromPlaylistModelList(models);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

}
