ALTER TABLE playlist_with_videos DROP CONSTRAINT playlist_with_videos_id_playlist_id_video_pk;
ALTER TABLE playlist_with_videos ADD CONSTRAINT playlist_with_videos_id_playlist_with_videos PRIMARY KEY (id_playlist_with_videos);