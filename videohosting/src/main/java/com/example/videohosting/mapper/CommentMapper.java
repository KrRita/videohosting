package com.example.videohosting.mapper;

import com.example.videohosting.dto.commentDto.CommentResponse;
import com.example.videohosting.dto.commentDto.CreateCommentRequest;
import com.example.videohosting.dto.commentDto.UpdateCommentRequest;
import com.example.videohosting.entity.Comment;
import com.example.videohosting.mapper.userMapper.UserWithoutListsMapper;
import com.example.videohosting.model.CommentModel;
import com.example.videohosting.model.UserModel;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = UserWithoutListsMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class CommentMapper {
    @Mapping(target = "user", qualifiedByName = "toUserEntityWithoutLists")
    public abstract Comment toEntity(CommentModel model);

    @Mapping(target = "user", qualifiedByName = "toUserModelWithoutLists")
    public abstract CommentModel toModelFromEntity(Comment entity);

    public abstract List<Comment> toEntityList(List<CommentModel> models);

    public abstract List<CommentModel> toModelListFromEntityList(List<Comment> entities);

    public CommentModel toModelFromCreateRequest(CreateCommentRequest request) {
        CommentModel commentModel = new CommentModel();
        commentModel.setIdVideo(request.getIdVideo());
        UserModel userModel = new UserModel();
        userModel.setIdUser(request.getIdUser());
        commentModel.setUser(userModel);
        commentModel.setText(request.getText());
        return commentModel;
    }

    public abstract CommentModel toModelFromUpdateRequest(UpdateCommentRequest request);

    public CommentResponse toResponseFromModel(CommentModel model) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setIdComment(model.getIdComment());
        commentResponse.setIdUser(model.getUser().getIdUser());
        commentResponse.setChannelName(model.getUser().getChannelName());
        commentResponse.setText(model.getText());
        commentResponse.setReleaseDateTime(model.getReleaseDateTime());
        commentResponse.setCountLikes(model.getCountLikes());
        commentResponse.setCountDislikes(model.getCountDislikes());
        return commentResponse;
    }

    public List<CommentResponse> toListResponseFromListModel(List<CommentModel> models) {
        if (models == null || models.isEmpty()) {
            return new ArrayList<>();
        }
        return models.stream().map(this::toResponseFromModel).toList();
    }

}
