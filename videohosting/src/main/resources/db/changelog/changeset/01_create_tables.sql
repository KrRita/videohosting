CREATE TABLE User_
(
    id_user serial,
    e_mail text,
    channel_name text,
    image_header text,
    image_icon text,
    description text,
    date_of_registration timestamp NOT NULL,
    password text,
    CONSTRAINT user_id_user_pk PRIMARY KEY (id_user),
    CONSTRAINT user_e_mail_uq UNIQUE (e_mail),
    CONSTRAINT user_channel_name_uq UNIQUE (channel_name)
);

CREATE TABLE Video
(
    id_video serial,
    id_user bigint NOT NULL,
    video_name text,
    duration interval NOT NULL,
    description text,
    release_date_time timestamp NOT NULL,
    video_url text NOT NULL,
    preview_image text,
    CONSTRAINT video_id_video_pk PRIMARY KEY (id_video),
    CONSTRAINT video_id_user_fk FOREIGN KEY (id_user) REFERENCES User_ (id_user) ON DELETE CASCADE,
    CONSTRAINT video_video_url_uq UNIQUE (video_url)
);

CREATE TABLE Comment
(
    id_comment serial,
    id_video bigint NOT NULL,
    id_user bigint NOT NULL,
    text text,
    release_date_time timestamp NOT NULL,
    CONSTRAINT comment_id_comment_pk PRIMARY KEY (id_comment),
    CONSTRAINT comment_id_video_fk FOREIGN KEY (id_video) REFERENCES Video (id_video) ON DELETE CASCADE,
    CONSTRAINT comment_id_user_fk FOREIGN KEY (id_user) REFERENCES User_ (id_user) ON DELETE CASCADE
);

CREATE TABLE Assessment_comment
(
    id_user bigint,
    id_comment bigint,
    is_like boolean NOT NULL,
    CONSTRAINT assessment_comment_id_user_pk_id_comment_pk PRIMARY KEY (id_user, id_comment),
    CONSTRAINT assessment_comment_id_user_fk FOREIGN KEY (id_user) REFERENCES User_ (id_user) ON DELETE CASCADE,
    CONSTRAINT assessment_comment_id_comment_fk FOREIGN KEY (id_comment) REFERENCES Comment (id_comment) ON DELETE CASCADE
);

CREATE TABLE Viewed_videos
(
    id_viewed_videos serial,
    id_user bigint NOT NULL,
    id_video bigint NOT NULL,
    date_of_viewing timestamp NOT NULL,
    CONSTRAINT viewed_videos_id_viewed_videos_pk PRIMARY KEY (id_viewed_videos),
    CONSTRAINT viewed_videos_id_user_fk FOREIGN KEY (id_user) REFERENCES User_ (id_user) ON DELETE CASCADE,
    CONSTRAINT viewed_videos_id_video_fk FOREIGN KEY (id_video) REFERENCES Video (id_video) ON DELETE CASCADE
);

CREATE TABLE Category
(
    id_category serial,
    name_category text,
    CONSTRAINT category_id_category_pk PRIMARY KEY (id_category),
    CONSTRAINT category_name_category_uq UNIQUE (name_category)
);

CREATE TABLE Videos_categories
(
    id_video bigint,
    id_category bigint,
    CONSTRAINT video_in_categories_id_video_id_category_pk PRIMARY KEY (id_video,id_category),
    CONSTRAINT video_in_categories_id_video_fk FOREIGN KEY (id_video) REFERENCES Video (id_video) ON DELETE CASCADE,
    CONSTRAINT video_in_categories_id_category_fk FOREIGN KEY (id_category) REFERENCES Category (id_category) ON DELETE CASCADE
);

CREATE TABLE Assessment_video
(
    id_user bigint,
    id_video bigint,
    date_of_assessment timestamp NOT NULL,
    is_like boolean NOT NULL,
    CONSTRAINT assessment_video_id_user_id_video_pk PRIMARY KEY (id_user, id_video),
    CONSTRAINT assessment_video_id_user_fk FOREIGN KEY (id_user) REFERENCES User_(id_user) ON DELETE CASCADE,
    CONSTRAINT assessment_video_id_video_fk FOREIGN KEY (id_video) REFERENCES Video (id_video) ON DELETE CASCADE
);

CREATE TABLE Subscription
(
    id_user_channel bigint,
    id_user_subscriber bigint,
    subscription_date timestamp NOT NULL,
    CONSTRAINT subscription_id_user_channel_id_user_subscriber_pk PRIMARY KEY (id_user_channel, id_user_subscriber),
    CONSTRAINT subscription_id_user_channel_fk FOREIGN KEY (id_user_channel) REFERENCES User_(id_user) ON DELETE CASCADE,
    CONSTRAINT subscription_id_user_subscriber_fk FOREIGN KEY (id_user_subscriber) REFERENCES User_(id_user) ON DELETE CASCADE
);

CREATE TABLE Playlist
(
    id_playlist serial,
    id_user bigint NOT NULL,
    name_playlist text,
    date_creation timestamp NOT NULL,
    image_icon path,
    CONSTRAINT playlist_id_playlist_pk PRIMARY KEY (id_playlist),
    CONSTRAINT playlist_id_user_name_playlist_uq UNIQUE (id_user, name_playlist),
    CONSTRAINT playlist_id_user_fk FOREIGN KEY (id_user) REFERENCES User_ (id_user) ON DELETE CASCADE
);

CREATE TABLE Playlist_with_videos
(
    id_playlist bigint,
    id_video bigint,
    date_of_addition timestamp NOT NULL,
    CONSTRAINT playlist_with_videos_id_playlist_id_video_pk PRIMARY KEY (id_playlist, id_video),
    CONSTRAINT playlist_with_videos_id_playlist_fk FOREIGN KEY (id_playlist) REFERENCES Playlist (id_playlist) ON DELETE CASCADE,
    CONSTRAINT playlist_with_videos_id_video_fk FOREIGN KEY (id_video) REFERENCES Video (id_video) ON DELETE CASCADE
);
