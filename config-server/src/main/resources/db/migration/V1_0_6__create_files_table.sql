CREATE TABLE files
(
    uuid      VARCHAR(255) NOT NULL,
    filename  VARCHAR(255),
    user_id   BIGINT,
    data      BYTEA,
    CONSTRAINT pk_file PRIMARY KEY (uuid),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);