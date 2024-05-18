package com.example.videohosting.mapper.userMapper;

import com.example.videohosting.entity.User;
import com.example.videohosting.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public abstract class UserWithoutListsMapper {
    @Named("toUserEntityWithoutLists")
    @Mapping(target = "subscriptions", ignore = true)
    @Mapping(target = "videos", ignore = true)
    @Mapping(target = "playlists", ignore = true)
    public abstract User toEntityWithoutLists(UserModel model);

    @Named("toUserModelWithoutLists")
    @Mapping(target = "subscriptions", ignore = true)
    @Mapping(target = "videos", ignore = true)
    @Mapping(target = "playlists", ignore = true)
    public abstract UserModel toModelWithoutLists(User entity);

}
