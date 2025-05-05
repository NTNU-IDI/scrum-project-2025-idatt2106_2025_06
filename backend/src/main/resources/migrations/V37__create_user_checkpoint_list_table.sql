CREATE TABLE user_checkpoint_list (
    id                          VARCHAR(36)             PRIMARY KEY,
    user_id                     VARCHAR(36)             NOT NULL,
    checkpoint_list_id          VARCHAR(36)             NOT NULL,

    FOREIGN KEY (user_id)       REFERENCES users(id),
    FOREIGN KEY (checkpoint_list_id)    REFERENCES checkpoint_list(id)
);