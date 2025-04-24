CREATE TABLE user_storages (
    user_id         VARCHAR(36)     NOT NULL,
    storage_id      VARCHAR(36)     NOT NULL,

    INDEX idx_user_storages_user_id (user_id),
    INDEX idx_user_storages_storage_id (storage_id),

    PRIMARY KEY (user_id, storage_id),
    FOREIGN KEY (user_id)               REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (storage_id)            REFERENCES storages(id) ON DELETE CASCADE
);