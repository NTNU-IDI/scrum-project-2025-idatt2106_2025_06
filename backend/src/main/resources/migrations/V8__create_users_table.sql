CREATE TABLE users (
    id          VARCHAR(36)         PRIMARY KEY,
    email       VARCHAR(255)        NOT NULL UNIQUE,
    name        VARCHAR(100)        NOT NULL,
    password    VARCHAR(255)        NOT NULL,
    created_at  TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    verified    BOOLEAN             NOT NULL DEFAULT FALSE,
    role_id     INT                 NOT NULL DEFAULT 1,

    INDEX idx_users_role_id (role_id),
    INDEX idx_users_email (email),

    FOREIGN KEY (role_id)   REFERENCES roles(id)
);