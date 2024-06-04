package com.example.videohosting.controller;

import com.example.videohosting.dto.assessementVideoDto.CreateAssessmentVideoRequest;
import com.example.videohosting.dto.assessementVideoDto.DeleteAssessmentVideoRequest;
import com.example.videohosting.dto.commentDto.CommentResponse;
import com.example.videohosting.dto.videoDto.CreateVideoRequest;
import com.example.videohosting.dto.videoDto.PreviewVideoResponse;
import com.example.videohosting.dto.videoDto.UpdateVideoRequest;
import com.example.videohosting.dto.videoDto.VideoResponse;
import com.example.videohosting.dto.viewedVideoDto.CreateViewedVideoRequest;
import com.example.videohosting.exception.LoadFileException;
import com.example.videohosting.mapper.AssessmentVideoMapper;
import com.example.videohosting.mapper.CommentMapper;
import com.example.videohosting.mapper.VideoMapper;
import com.example.videohosting.mapper.ViewedVideoMapper;
import com.example.videohosting.model.AssessmentVideoModel;
import com.example.videohosting.model.CommentModel;
import com.example.videohosting.model.VideoModel;
import com.example.videohosting.model.ViewedVideoModel;
import com.example.videohosting.service.CommentService;
import com.example.videohosting.service.MediaService;
import com.example.videohosting.service.VideoService;
import jakarta.validation.Valid;
import org.jcodec.api.JCodecException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/videos")
public class VideoController {
    private final VideoService videoService;
    private final VideoMapper videoMapper;
    private final AssessmentVideoMapper assessmentVideoMapper;
    private final ViewedVideoMapper viewedVideoMapper;
    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final MediaService mediaService;

    @Autowired
    public VideoController(VideoService videoService, VideoMapper videoMapper,
                           AssessmentVideoMapper assessmentVideoMapper, ViewedVideoMapper viewedVideoMapper,
                           CommentService commentService, CommentMapper commentMapper, MediaService mediaService) {
        this.videoService = videoService;
        this.videoMapper = videoMapper;
        this.assessmentVideoMapper = assessmentVideoMapper;
        this.viewedVideoMapper = viewedVideoMapper;
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.mediaService = mediaService;
    }

    @PostMapping()
    public ResponseEntity<VideoResponse> postVideo
            (@Valid @RequestBody CreateVideoRequest request) throws JCodecException, IOException {
        VideoModel model = videoMapper.toModelFromCreateRequest(request);
        VideoModel videoModel = videoService.insertVideo(model, request.getVideoFile(), request.getPreviewImage());
        VideoResponse response = videoMapper.toVideoResponseFromModel(videoModel);
        addVideoAndUserPreview(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VideoResponse> putVideo(@PathVariable Long id, @Valid @RequestBody UpdateVideoRequest request) {
        VideoModel model = videoMapper.toModelFromUpdateRequest(request);
        model.setIdVideo(id);
        VideoModel videoModel = videoService.updateVideo(model, request.getPreviewImage());
        VideoResponse response = videoMapper.toVideoResponseFromModel(videoModel);
        addVideoAndUserPreview(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVideo(@PathVariable Long id) {
        videoService.deleteVideo(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoResponse> getVideoById(@PathVariable Long id) {
        VideoModel videoModel = videoService.findVideoById(id);
        VideoResponse response = videoMapper.toVideoResponseFromModel(videoModel);
        addVideoAndUserPreview(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/by_name")
    public ResponseEntity<List<PreviewVideoResponse>> getVideosByName(@RequestParam String videoName) {
        List<VideoModel> models = videoService.getVideosByName(videoName);
        List<PreviewVideoResponse> responses = videoMapper.toListPreviewVideoResponseFromListModel(models);
        addVideoPreview(responses);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/by_user_name")
    public ResponseEntity<List<PreviewVideoResponse>> getVideosByUserName(@RequestParam String userName) {
        List<VideoModel> models = videoService.getVideosByUserName(userName);
        List<PreviewVideoResponse> responses = videoMapper.toListPreviewVideoResponseFromListModel(models);
        addVideoPreview(responses);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/by_categories")
    public ResponseEntity<List<PreviewVideoResponse>> getVideosByCategories(@RequestParam List<String> categories) {
        List<VideoModel> models = videoService.getVideosByCategories(categories);
        List<PreviewVideoResponse> responses = videoMapper.toListPreviewVideoResponseFromListModel(models);
        addVideoPreview(responses);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PostMapping("/{idVideo}/assessment")
    public ResponseEntity<VideoResponse> createAssessmentVideo(@PathVariable Long idVideo,
                                                               @Valid @RequestBody CreateAssessmentVideoRequest request) {
        AssessmentVideoModel model = assessmentVideoMapper.toModelFromCreateRequest(request);
        model.getVideo().setIdVideo(idVideo);
        VideoModel videoModel = videoService.insertAssessmentVideo(model);
        VideoResponse response = videoMapper.toVideoResponseFromModel(videoModel);
        addVideoAndUserPreview(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{idVideo}/assessment")
    public ResponseEntity<VideoResponse> deleteAssessmentVideo(@PathVariable Long idVideo,
                                                               @Valid @RequestBody DeleteAssessmentVideoRequest request) {
        VideoModel model = videoService.deleteAssessmentVideo(request.getIdUser(), idVideo);
        VideoResponse response = videoMapper.toVideoResponseFromModel(model);
        addVideoAndUserPreview(response);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{idVideo}/viewed_video")
    public ResponseEntity<VideoResponse> createViewedVideo(@PathVariable Long idVideo,
                                                           @Valid @RequestBody CreateViewedVideoRequest request) {
        ViewedVideoModel model = viewedVideoMapper.toModelFromCreateRequest(request);
        model.getVideo().setIdVideo(idVideo);
        VideoModel videoModel = videoService.insertViewedVideo(model);
        VideoResponse response = videoMapper.toVideoResponseFromModel(videoModel);
        addVideoAndUserPreview(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{idVideo}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsOnTheVideo(@PathVariable Long idVideo) {
        List<CommentModel> models = commentService.getCommentsOnTheVideo(idVideo);
        List<CommentResponse> responses = commentMapper.toListResponseFromListModel(models);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    private void addVideoAndUserPreview(VideoResponse response) {
        String videoPath = "video\\" + response.getIdVideo() + ".mp4";
        String userPreviewPath = "imageIconUser\\" + response.getPreviewUserResponse().getIdUser() + ".jpeg";
        Resource video = mediaService.getMedia(videoPath);
        response.setVideoFile(video);
        try {
            Resource preview = mediaService.getMedia(userPreviewPath);
            response.getPreviewUserResponse().setImageIcon(preview);
        } catch (LoadFileException ex) {
            response.getPreviewUserResponse().setImageIcon(null);
        }
    }

    private void addVideoPreview(List<PreviewVideoResponse> responses) {
        for (PreviewVideoResponse response : responses) {
            String videoPreview = "previewVideo\\" + response.getIdVideo() + ".jpeg";
            try {
                Resource preview = mediaService.getMedia(videoPreview);
                response.setPreviewImage(preview);
            } catch (LoadFileException ex) {
                response.setPreviewImage(null);
            }
        }
    }
}
