package com.example.videohosting.controller;

import com.example.videohosting.dto.assessementVideoDto.CreateAssessmentVideoRequest;
import com.example.videohosting.dto.assessementVideoDto.DeleteAssessmentVideoRequest;
import com.example.videohosting.dto.commentDto.CommentResponse;
import com.example.videohosting.dto.videoDto.CreateVideoRequest;
import com.example.videohosting.dto.videoDto.PreviewVideoResponse;
import com.example.videohosting.dto.videoDto.UpdateVideoRequest;
import com.example.videohosting.dto.videoDto.VideoResponse;
import com.example.videohosting.dto.viewedVideoDto.CreateViewedVideoRequest;
import com.example.videohosting.mapper.AssessmentVideoMapper;
import com.example.videohosting.mapper.CommentMapper;
import com.example.videohosting.mapper.VideoMapper;
import com.example.videohosting.mapper.ViewedVideoMapper;
import com.example.videohosting.model.AssessmentVideoModel;
import com.example.videohosting.model.CommentModel;
import com.example.videohosting.model.VideoModel;
import com.example.videohosting.model.ViewedVideoModel;
import com.example.videohosting.service.CommentService;
import com.example.videohosting.service.VideoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.jcodec.api.JCodecException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    public VideoController(VideoService videoService, VideoMapper videoMapper,
                           AssessmentVideoMapper assessmentVideoMapper, ViewedVideoMapper viewedVideoMapper,
                           CommentService commentService, CommentMapper commentMapper) {
        this.videoService = videoService;
        this.videoMapper = videoMapper;
        this.assessmentVideoMapper = assessmentVideoMapper;
        this.viewedVideoMapper = viewedVideoMapper;
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VideoResponse> postVideo
            (@Valid @RequestPart(value = "request", required = true) String request,
             @RequestPart(value = "video", required = true) MultipartFile video,
             @RequestPart(value = "preview", required = true) MultipartFile preview) throws JCodecException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        CreateVideoRequest createVideoRequest = objectMapper.readValue(request, CreateVideoRequest.class);
        VideoModel model = videoMapper.toModelFromCreateRequest(createVideoRequest);
        VideoModel videoModel = videoService.insertVideo(model, video, preview);
        VideoResponse response = videoMapper.toVideoResponseFromModel(videoModel);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PutMapping(value = "/updateVideo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VideoResponse> putVideo(
            @Valid @RequestPart(value = "request", required = true) String request,
            @RequestPart(value = "preview", required = true) MultipartFile preview) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        UpdateVideoRequest updateRequest = objectMapper.readValue(request, UpdateVideoRequest.class);
        VideoModel model = videoMapper.toModelFromUpdateRequest(updateRequest);
        VideoModel videoModel = videoService.updateVideo(model, preview);
        VideoResponse response = videoMapper.toVideoResponseFromModel(videoModel);
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
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/by_name")
    public ResponseEntity<List<PreviewVideoResponse>> getVideosByName(@RequestParam String videoName) {
        List<VideoModel> models = videoService.getVideosByName(videoName);
        List<PreviewVideoResponse> responses = videoMapper.toListPreviewVideoResponseFromListModel(models);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/by_user_name")
    public ResponseEntity<List<PreviewVideoResponse>> getVideosByUserName(@RequestParam String userName) {
        List<VideoModel> models = videoService.getVideosByUserName(userName);
        List<PreviewVideoResponse> responses = videoMapper.toListPreviewVideoResponseFromListModel(models);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/by_categories")
    public ResponseEntity<List<PreviewVideoResponse>> getVideosByCategories(@RequestParam List<String> categories) {
        List<VideoModel> models = videoService.getVideosByCategories(categories);
        List<PreviewVideoResponse> responses = videoMapper.toListPreviewVideoResponseFromListModel(models);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PostMapping("/{idVideo}/assessment")
    public ResponseEntity<VideoResponse> createAssessmentVideo(
            @PathVariable Long idVideo, @Valid @RequestBody CreateAssessmentVideoRequest request) {
        AssessmentVideoModel model = assessmentVideoMapper.toModelFromCreateRequest(request);
        VideoModel video = videoService.findVideoById(idVideo);
        model.setVideo(video);
        VideoModel videoModel = videoService.insertAssessmentVideo(model);
        VideoResponse response = videoMapper.toVideoResponseFromModel(videoModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{idVideo}/assessment")
    public ResponseEntity<VideoResponse> deleteAssessmentVideo(
            @PathVariable Long idVideo, @Valid @RequestBody DeleteAssessmentVideoRequest request) {
        VideoModel model = videoService.deleteAssessmentVideo(request.getIdUser(), idVideo);
        VideoResponse response = videoMapper.toVideoResponseFromModel(model);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{idVideo}/viewed_video")
    public ResponseEntity<VideoResponse> createViewedVideo
            (@PathVariable Long idVideo, @Valid @RequestBody CreateViewedVideoRequest request) {
        ViewedVideoModel model = viewedVideoMapper.toModelFromCreateRequest(request);
        model.getVideo().setIdVideo(idVideo);
        VideoModel videoModel = videoService.insertViewedVideo(model);
        VideoResponse response = videoMapper.toVideoResponseFromModel(videoModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{idVideo}/comments")
    public ResponseEntity<List<CommentResponse>> getCommentsOnTheVideo(@PathVariable Long idVideo) {
        List<CommentModel> models = commentService.getCommentsOnTheVideo(idVideo);
        List<CommentResponse> responses = commentMapper.toListResponseFromListModel(models);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

}
