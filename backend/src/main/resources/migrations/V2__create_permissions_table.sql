CREATE TABLE permissions (
    id                  INT                 AUTO_INCREMENT PRIMARY KEY,
    permission_name     VARCHAR(100)        NOT NULL UNIQUE,
    description         TEXT,

    INDEX idx_permissions_permission_name (permission_name)
);