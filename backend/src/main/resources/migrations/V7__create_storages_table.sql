CREATE TABLE storages (
    id          VARCHAR(36)         PRIMARY KEY,
    name        VARCHAR(255)        NOT NULL,
    token       INT                 NOT NULL,
    location    POINT,
    created_at  TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_storages_token (token)
);