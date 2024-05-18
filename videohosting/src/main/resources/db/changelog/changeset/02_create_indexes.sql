CREATE INDEX ix_video_id_user ON Video (id_user);

CREATE INDEX ix_playlist_id_channel ON Playlist (id_user);

CREATE INDEX ix_comment_id_video ON Comment (id_video);

CREATE INDEX ix_viewed_videos_id_user ON Viewed_videos(id_user);

CREATE INDEX ix_viewed_videos_id_video ON Viewed_videos (id_video);

CREATE INDEX ix_assessment_video_is_like ON Assessment_video (is_like);

