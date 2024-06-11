package com.example.videohosting.mapper;

import com.example.videohosting.dto.playlistDto.CreatePlaylistRequest;
import com.example.videohosting.dto.playlistDto.PlaylistResponse;
import com.example.videohosting.dto.playlistDto.UpdatePlaylistRequest;
import com.example.videohosting.entity.Playlist;
import com.example.videohosting.mapper.userMapper.UserWithoutListsMapper;
import com.example.videohosting.model.PlaylistModel;
import com.example.videohosting.model.UserModel;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = UserWithoutListsMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class PlaylistMapper {
    @Mapping(target = "user", qualifiedByName = "toUserEntityWithoutLists")
    public abstract Playlist toEntity(PlaylistModel model);

    @Mapping(target = "user", qualifiedByName = "toUserModelWithoutLists")
    public abstract PlaylistModel toModel(Playlist entity);

    @Named("toPlaylistEntityList")
    public abstract List<Playlist> toEntityList(List<PlaylistModel> models);

    @Named("toPlaylistModelList")
    public abstract List<PlaylistModel> toModelList(List<Playlist> entities);

    public PlaylistModel toModelFromCreateRequest(CreatePlaylistRequest request) {
        PlaylistModel playlistModel = new PlaylistModel();
        UserModel userModel = new UserModel();
        userModel.setIdUser(request.getIdUser());
        playlistModel.setUser(userModel);
        playlistModel.setNamePlaylist(request.getNamePlaylist());
        return playlistModel;
    }

    public abstract PlaylistModel toModelFromUpdateRequest(UpdatePlaylistRequest request);

    public abstract PlaylistResponse toResponseFromModel(PlaylistModel model);

    public List<PlaylistResponse> toPlaylistResponseListFromPlaylistModelList(List<PlaylistModel> models) {
        if (models == null || models.isEmpty()) {
            return new ArrayList<>();
        }
        return models.stream().map(this::toResponseFromModel).toList();
    }


}
