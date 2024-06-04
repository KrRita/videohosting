package com.example.videohosting.mapper;

import com.example.videohosting.dto.playlistWithVideosDto.CreatePlaylistWithVideosRequest;
import com.example.videohosting.dto.playlistWithVideosDto.PlaylistWithVideosResponse;
import com.example.videohosting.dto.videoDto.PreviewVideoResponse;
import com.example.videohosting.entity.PlaylistWithVideos;
import com.example.videohosting.model.PlaylistWithVideosModel;
import com.example.videohosting.model.VideoModel;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = VideoMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class PlaylistWithVideosMapper {
    @Mapping(target = "video", qualifiedByName = "toVideoEntity")
    public abstract PlaylistWithVideos toEntity(PlaylistWithVideosModel model);

    @Mapping(target = "video", qualifiedByName = "toVideoModel")
    public abstract PlaylistWithVideosModel toModel(PlaylistWithVideos entity);

    public abstract List<PlaylistWithVideos> toEntityList(List<PlaylistWithVideosModel> models);

    public abstract List<PlaylistWithVideosModel> toModelList(List<PlaylistWithVideos> entities);

    public PlaylistWithVideosResponse toResponseFromModel(PlaylistWithVideosModel model) {
        PreviewVideoResponse previewVideoResponse = new PreviewVideoResponse();
        PlaylistWithVideosResponse playlistWithVideosResponse = new PlaylistWithVideosResponse();
        previewVideoResponse.setIdUser(model.getVideo().getUser().getIdUser());
        previewVideoResponse.setUserName(model.getVideo().getUser().getChannelName());
        previewVideoResponse.setIdVideo(model.getVideo().getIdVideo());
        previewVideoResponse.setReleaseDateTime(model.getVideo().getReleaseDateTime());
        previewVideoResponse.setName(model.getVideo().getName());
        previewVideoResponse.setDescription(model.getVideo().getDescription());
        previewVideoResponse.setDuration(model.getVideo().getDuration());
        previewVideoResponse.setCountViewing(model.getVideo().getCountViewing());
        playlistWithVideosResponse.setPreviewVideoResponse(previewVideoResponse);
        playlistWithVideosResponse.setDateOfAddition(model.getDateOfAddition());
        return playlistWithVideosResponse;
    }

    public PlaylistWithVideosModel toModelFromCreateRequest(CreatePlaylistWithVideosRequest request) {
        PlaylistWithVideosModel playlistWithVideosModel = new PlaylistWithVideosModel();
        VideoModel videoModel = new VideoModel();
        videoModel.setIdVideo(request.getIdVideo());
        playlistWithVideosModel.setVideo(videoModel);
        return playlistWithVideosModel;
    }

    public List<PlaylistWithVideosResponse> toListResponseFromListModel(List<PlaylistWithVideosModel> models) {
        if (models == null || models.isEmpty()) {
            return new ArrayList<>();
        }
        return models.stream().map(this::toResponseFromModel).toList();
    }

}
