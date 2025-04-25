CREATE TABLE permissions (
    id                  INT                 AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(100)        NOT NULL UNIQUE,
    description         TEXT,

    INDEX idx_permissions_name (name)
);