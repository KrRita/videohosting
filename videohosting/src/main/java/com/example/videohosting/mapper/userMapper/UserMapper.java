package com.example.videohosting.mapper.userMapper;

import com.example.videohosting.dto.userDto.CreateUserRequest;
import com.example.videohosting.dto.userDto.PreviewUserResponse;
import com.example.videohosting.dto.userDto.UpdateSubscriptionsRequest;
import com.example.videohosting.dto.userDto.UpdateUserRequest;
import com.example.videohosting.dto.userDto.UserResponse;
import com.example.videohosting.entity.User;
import com.example.videohosting.mapper.PlaylistMapper;
import com.example.videohosting.mapper.VideoMapper;
import com.example.videohosting.model.UserModel;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {VideoMapper.class, PlaylistMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class UserMapper {
    @Named("toUserModel")
    @Mapping(target = "subscriptions", qualifiedByName = "noSubscriptionsInUserModelList")
    @Mapping(target = "videos", qualifiedByName = "toVideoModelList")
    @Mapping(target = "playlists", qualifiedByName = "toPlaylistModelList")
    public abstract UserModel toModel(User entity);

    @Named("toUserEntity")
    @Mapping(target = "subscriptions", qualifiedByName = "noSubscriptionsInUserEntityList")
    @Mapping(target = "videos", qualifiedByName = "toVideoEntityList")
    @Mapping(target = "playlists", qualifiedByName = "toPlaylistEntityList")
    public abstract User toEntity(UserModel model);


    @Named("noSubscriptionsInUserModelList")
    public List<UserModel> toModelList(List<User> entities) {
        if (entities == null || entities.isEmpty()) {
            return new ArrayList<>();
        }
        return entities.stream().map(this::toModelWithoutSubscriptions).toList();
    }

    @Named("noSubscriptionsInUserEntityList")
    public List<User> toEntityList(List<UserModel> models) {
        if (models == null || models.isEmpty()) {
            return new ArrayList<>();
        }
        return models.stream().map(this::toEntityWithoutSubscriptions).toList();
    }

    @Mapping(target = "subscriptions", ignore = true)
    @Mapping(target = "videos", qualifiedByName = "toVideoModelList")
    public abstract UserModel toModelWithoutSubscriptions(User entity);

    @Mapping(target = "subscriptions", ignore = true)
    @Mapping(target = "videos", qualifiedByName = "toVideoEntityList")
    public abstract User toEntityWithoutSubscriptions(UserModel model);

    public abstract UserResponse toResponseFromModel(UserModel model);

    public abstract UserModel toModelFromCreateRequest(CreateUserRequest request);

    public abstract UserModel toModelFromUpdateRequest(UpdateUserRequest request);

    public UserModel toModelFromUpdateSubscriptionsRequest(UpdateSubscriptionsRequest request) {
        UserModel userModel = new UserModel();
        UserModel subscription = new UserModel();
        subscription.setIdUser(request.getIdSubscription());
        List<UserModel> subscriptions = List.of(subscription);
        userModel.setSubscriptions(subscriptions);
        return userModel;
    }

    public abstract PreviewUserResponse toPreviewUserResponseFromModel(UserModel userModel);

    public List<PreviewUserResponse> toPreviewUserResponseListFromModelUserList(List<UserModel> models) {
        if (models == null || models.isEmpty()) {
            return new ArrayList<>();
        }
        return models.stream().map(this::toPreviewUserResponseFromModel).toList();
    }

}
