package com.example.videohosting.mapper;

import com.example.videohosting.dto.videoDto.PreviewVideoResponse;
import com.example.videohosting.dto.viewedVideoDto.CreateViewedVideoRequest;
import com.example.videohosting.dto.viewedVideoDto.ViewedVideoResponse;
import com.example.videohosting.entity.ViewedVideo;
import com.example.videohosting.model.VideoModel;
import com.example.videohosting.model.ViewedVideoModel;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = VideoMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class ViewedVideoMapper {
    @Mapping(target = "video", qualifiedByName = "toVideoEntity")
    public abstract ViewedVideo toEntity(ViewedVideoModel model);

    @Mapping(target = "video", qualifiedByName = "toVideoModel")
    public abstract ViewedVideoModel toModel(ViewedVideo entity);

    public abstract List<ViewedVideo> toEntityList(List<ViewedVideoModel> models);

    public abstract List<ViewedVideoModel> toModelList(List<ViewedVideo> entities);

    public ViewedVideoModel toModelFromCreateRequest(CreateViewedVideoRequest request) {
        ViewedVideoModel viewedVideoModel = new ViewedVideoModel();
        VideoModel videoModel = new VideoModel();
        viewedVideoModel.setVideo(videoModel);
        viewedVideoModel.setIdUser(request.getIdUser());
        return viewedVideoModel;
    }

    public ViewedVideoResponse toResponseFromModel(ViewedVideoModel model) {
        PreviewVideoResponse previewVideoResponse = new PreviewVideoResponse();
        previewVideoResponse.setIdVideo(model.getVideo().getIdVideo());
        previewVideoResponse.setName(model.getVideo().getName());
        previewVideoResponse.setDuration(model.getVideo().getDuration());
        previewVideoResponse.setReleaseDateTime(model.getVideo().getReleaseDateTime());
        previewVideoResponse.setCountViewing(model.getVideo().getCountViewing());
        previewVideoResponse.setIdUser(model.getIdUser());
        previewVideoResponse.setUserName(model.getVideo().getUser().getChannelName());
        ViewedVideoResponse viewedVideoResponse = new ViewedVideoResponse();
        viewedVideoResponse.setPreviewVideoResponse(previewVideoResponse);
        viewedVideoResponse.setDateOfViewing(model.getDateOfViewing());
        return viewedVideoResponse;
    }

    public List<ViewedVideoResponse> toResponseListFromModelList(List<ViewedVideoModel> models) {
        if (models == null || models.isEmpty()) {
            return new ArrayList<>();
        }
        return models.stream().map(this::toResponseFromModel).toList();
    }


}
