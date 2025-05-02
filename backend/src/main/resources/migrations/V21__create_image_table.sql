CREATE TABLE image (
    id              VARCHAR(12)              PRIMARY KEY,
    name            VARCHAR(100)             NOT NULL,
    description     TEXT                     NULL,
    created_at       TIMESTAMP               DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);