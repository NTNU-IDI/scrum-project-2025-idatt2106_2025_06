ALTER TABLE users
    ADD COLUMN location VARCHAR(255) NULL,
    ADD COLUMN tracking_enabled BOOLEAN NOT NULL DEFAULT FALSE;