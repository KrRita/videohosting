package com.example.videohosting.mapper;

import com.example.videohosting.dto.assessementVideoDto.CreateAssessmentVideoRequest;
import com.example.videohosting.entity.AssessmentVideo;
import com.example.videohosting.model.AssessmentVideoModel;
import com.example.videohosting.model.VideoModel;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = VideoMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class AssessmentVideoMapper {
    @Mapping(target = "video", qualifiedByName = "toVideoEntity")
    public abstract AssessmentVideo toEntity(AssessmentVideoModel model);

    @Mapping(target = "video", qualifiedByName = "toVideoModel")
    public abstract AssessmentVideoModel toModel(AssessmentVideo entity);

    public abstract List<AssessmentVideoModel> toModelList(List<AssessmentVideo> entities);

    public abstract List<AssessmentVideo> toEntityList(List<AssessmentVideoModel> models);

    public AssessmentVideoModel toModelFromCreateRequest(CreateAssessmentVideoRequest request) {
        AssessmentVideoModel assessmentVideoModel = new AssessmentVideoModel();
        VideoModel videoModel = new VideoModel();
        assessmentVideoModel.setIdUser(request.getIdUser());
        assessmentVideoModel.setVideo(videoModel);
        assessmentVideoModel.setLiked(request.getLiked());
        return assessmentVideoModel;
    }

}
