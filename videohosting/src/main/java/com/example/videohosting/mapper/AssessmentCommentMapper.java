package com.example.videohosting.mapper;

import com.example.videohosting.dto.assessmentCommentDto.CreateAssessmentCommentRequest;
import com.example.videohosting.entity.AssessmentComment;
import com.example.videohosting.model.AssessmentCommentModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AssessmentCommentMapper {
    public abstract AssessmentComment toEntityFromModel(AssessmentCommentModel model);

    public abstract AssessmentCommentModel toModelFromEntity(AssessmentComment entity);

    public abstract AssessmentCommentModel toModelFromCreateRequest(CreateAssessmentCommentRequest request);

}
