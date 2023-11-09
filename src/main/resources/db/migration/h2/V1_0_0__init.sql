CREATE TABLE genre
(
    id      BIGINT NOT NULL,
    name    VARCHAR(255),
    imdb_id BIGINT,
    genre   BIGINT,
    CONSTRAINT pk_genre PRIMARY KEY (id)
);

CREATE TABLE movie_session
(
    id         BIGINT NULL,
    user_id    BIGINT,
    session_id BIGINT,
    liked      BOOLEAN,
    CONSTRAINT pk_movie_session PRIMARY KEY (id)
);

CREATE TABLE session
(
    id          BIGINT NOT NULL,
    creator_id  BIGINT,
    invite_code VARCHAR(255),
    CONSTRAINT pk_session PRIMARY KEY (id)
);

CREATE TABLE "user"
(
    id       BIGINT NOT NULL,
    username VARCHAR(255),
    password VARCHAR(255),
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE user_session
(
    id         BIGINT NOT NULL,
    user_id    BIGINT,
    session_id BIGINT,
    CONSTRAINT pk_user_session PRIMARY KEY (id)
);
