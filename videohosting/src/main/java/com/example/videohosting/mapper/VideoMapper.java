package com.example.videohosting.mapper;

import com.example.videohosting.dto.userDto.PreviewUserResponse;
import com.example.videohosting.dto.videoDto.CreateVideoRequest;
import com.example.videohosting.dto.videoDto.PreviewVideoResponse;
import com.example.videohosting.dto.videoDto.UpdateVideoRequest;
import com.example.videohosting.dto.videoDto.VideoResponse;
import com.example.videohosting.entity.Video;
import com.example.videohosting.mapper.userMapper.UserWithoutListsMapper;
import com.example.videohosting.model.UserModel;
import com.example.videohosting.model.VideoModel;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = UserWithoutListsMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class VideoMapper {
    @Named("toVideoModel")
    @Mapping(target = "user", qualifiedByName = "toUserModelWithoutLists")
    @Mapping(target = "categories", ignore = true)
    public abstract VideoModel toModel(Video entity);

    @Named("toVideoEntity")
    @Mapping(target = "user", qualifiedByName = "toUserEntityWithoutLists")
    @Mapping(target = "categories", ignore = true)
    public abstract Video toEntity(VideoModel model);

    @Named("toVideoModelList")
    public List<VideoModel> toModelList(List<Video> entities) {
        if (entities == null || entities.isEmpty()) {
            return new ArrayList<>();
        }
        return entities.stream().map(this::toModel).toList();
    }

    @Named("toVideoEntityList")
    public List<Video> toEntityList(List<VideoModel> models) {
        if (models == null || models.isEmpty()) {
            return new ArrayList<>();
        }
        return models.stream().map(this::toEntity).toList();
    }

    public VideoModel toModelFromCreateRequest(CreateVideoRequest request) {
        VideoModel videoModel = new VideoModel();
        UserModel userModel = new UserModel();
        userModel.setIdUser(request.getIdUser());
        videoModel.setUser(userModel);
        videoModel.setName(request.getName());
        videoModel.setDescription(request.getDescription());
        videoModel.setCategories(request.getCategories());
        return videoModel;
    }

    public abstract VideoModel toModelFromUpdateRequest(UpdateVideoRequest request);

    public VideoResponse toVideoResponseFromModel(VideoModel model) {
        VideoResponse videoResponse = new VideoResponse();
        PreviewUserResponse user = new PreviewUserResponse();
        user.setIdUser(model.getUser().getIdUser());
        user.setChannelName(model.getUser().getChannelName());
        user.setCountSubscribers(model.getUser().getCountSubscribers());
        videoResponse.setPreviewUserResponse(user);
        videoResponse.setIdVideo(model.getIdVideo());
        videoResponse.setName(model.getName());
        videoResponse.setDuration(model.getDuration());
        videoResponse.setDescription(model.getDescription());
        videoResponse.setReleaseDateTime(model.getReleaseDateTime());
        videoResponse.setCategories(model.getCategories());
        videoResponse.setCountViewing(model.getCountViewing());
        videoResponse.setCountLikes(model.getCountLikes());
        videoResponse.setCountDislikes(model.getCountDislikes());
        return videoResponse;
    }

    public PreviewVideoResponse toPreviewVideoResponseFromModel(VideoModel model) {
        PreviewVideoResponse previewVideoResponse = new PreviewVideoResponse();
        previewVideoResponse.setIdVideo(model.getIdVideo());
        previewVideoResponse.setName(model.getName());
        previewVideoResponse.setDuration(model.getDuration());
        previewVideoResponse.setDescription(model.getDescription());
        previewVideoResponse.setReleaseDateTime(model.getReleaseDateTime());
        previewVideoResponse.setCountViewing(model.getCountViewing());
        previewVideoResponse.setIdUser(model.getUser().getIdUser());
        previewVideoResponse.setUserName(model.getUser().getChannelName());
        return previewVideoResponse;
    }

    public List<PreviewVideoResponse> toListPreviewVideoResponseFromListModel(List<VideoModel> models) {
        if (models == null || models.isEmpty()) {
            return new ArrayList<>();
        }
        return models.stream().map(this::toPreviewVideoResponseFromModel).toList();
    }
}
