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
import com.example.videohosting.service.MediaService;
import com.example.videohosting.service.UserService;
import com.example.videohosting.service.VideoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private final MediaService mediaService;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper, VideoService videoService,
                          VideoMapper videoMapper, ViewedVideoMapper viewedVideoMapper,
                          PlaylistMapper playlistMapper, MediaService mediaService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.videoService = videoService;
        this.videoMapper = videoMapper;
        this.viewedVideoMapper = viewedVideoMapper;
        this.playlistMapper = playlistMapper;
        this.mediaService = mediaService;
    }

    @PutMapping()
    public ResponseEntity<UserResponse> putUser(@Valid @RequestBody UpdateUserRequest request, Authentication authentication) {
        UserModel model = userMapper.toModelFromUpdateRequest(request);
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser();
        model.setIdUser(id);
        UserModel userModel = userService.update(model, request.getImageHeader(), request.getImageIcon());
        UserResponse response = userMapper.toResponseFromModel(userModel);
        addIconAndHeader(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping()
    public ResponseEntity<UserResponse> postSubscription
            (@Valid @RequestBody UpdateSubscriptionsRequest request, Authentication authentication) {
        UserModel model = userMapper.toModelFromUpdateSubscriptionsRequest(request);
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser();
        model.setIdUser(id);
        UserModel userModel = userService.addSubscription(model);
        UserResponse response = userMapper.toResponseFromModel(userModel);
        addIconAndHeader(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/subscription")
    public ResponseEntity<UserResponse> deleteSubscription
            (@Valid @RequestBody UpdateSubscriptionsRequest request, Authentication authentication) {
        UserModel model = userMapper.toModelFromUpdateSubscriptionsRequest(request);
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser();
        model.setIdUser(id);
        UserModel userModel = userService.deleteSubscription(model);
        UserResponse response = userMapper.toResponseFromModel(userModel);
        addIconAndHeader(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping()
    public ResponseEntity.BodyBuilder deleteUser(Authentication authentication) {
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser();
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserModel model = userService.findUserById(id);
        UserResponse response = userMapper.toResponseFromModel(model);
        addIconAndHeader(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<List<PreviewUserResponse>> getUserSubscriptions(Authentication authentication) {
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser();
        UserModel user = userService.findUserById(id);
        List<UserModel> subscriptions = user.getSubscriptions();
        List<PreviewUserResponse> responses = userMapper.toPreviewUserResponseListFromModelUserList(subscriptions);
        for (PreviewUserResponse response : responses) {
            String iconPath = "imageIconUser\\" + response.getIdUser() + ".jpeg";
            Resource icon = mediaService.getMedia(iconPath);
            response.setImageIcon(icon);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/videos")
    public ResponseEntity<List<PreviewVideoResponse>> getUserVideos(Authentication authentication) {
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser();
        UserModel user = userService.findUserById(id);
        List<VideoModel> videos = user.getVideos();
        List<PreviewVideoResponse> responses = videoMapper.toListPreviewVideoResponseFromListModel(videos);
        addVideoPreview(responses);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/subscriptions_videos")
    public ResponseEntity<List<PreviewVideoResponse>> getSubscriptionsVideos(Authentication authentication) {
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser();
        List<VideoModel> videos = videoService.getSubscriptionsVideos(id);
        List<PreviewVideoResponse> responses = videoMapper.toListPreviewVideoResponseFromListModel(videos);
        addVideoPreview(responses);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/viewed_videos")
    public ResponseEntity<List<ViewedVideoResponse>> getViewedVideos(Authentication authentication) {
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser();
        List<ViewedVideoModel> models = videoService.getViewedVideos(id);
        List<ViewedVideoResponse> responses = viewedVideoMapper.toResponseListFromModelList(models);
        for (ViewedVideoResponse response : responses) {
            String videoPreview = "previewVideo\\" + response.getPreviewVideoResponse().getIdVideo() + ".jpeg";
            Resource preview = mediaService.getMedia(videoPreview);
            response.getPreviewVideoResponse().setPreviewImage(preview);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/liked_videos")
    public ResponseEntity<List<PreviewVideoResponse>> getLikedVideos(Authentication authentication) {
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser();
        List<VideoModel> models = videoService.getLikedVideos(id);
        List<PreviewVideoResponse> responses = videoMapper.toListPreviewVideoResponseFromListModel(models);
        addVideoPreview(responses);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/playlists")
    public ResponseEntity<List<PlaylistResponse>> getUserPlaylists(Authentication authentication) {
        Long id = ((UserModel) authentication.getPrincipal()).getIdUser();
        UserModel user = userService.findUserById(id);
        List<PlaylistModel> models = user.getPlaylists();
        List<PlaylistResponse> responses = playlistMapper.toPlaylistResponseListFromPlaylistModelList(models);
        for (PlaylistResponse response : responses) {
            String iconPlaylist = "imageIconPlaylist\\" + response.getIdPlaylist() + ".jpeg";
            Resource icon = mediaService.getMedia(iconPlaylist);
            response.setImageIcon(icon);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    private void addIconAndHeader(UserResponse response) {
        String headerPath = "imageHeaderUser\\" + response.getIdUser() + ".jpeg";
        String iconPath = "imageIconUser\\" + response.getIdUser() + ".jpeg";
        Resource header = mediaService.getMedia(headerPath);
        Resource icon = mediaService.getMedia(iconPath);
        response.setImageHeader(header);
        response.setImageIcon(icon);
    }

    private void addVideoPreview(List<PreviewVideoResponse> responses) {
        for (PreviewVideoResponse response : responses) {
            String videoPreview = "previewVideo\\" + response.getIdVideo() + ".jpeg";
            Resource preview = mediaService.getMedia(videoPreview);
            response.setPreviewImage(preview);
        }
    }

}
