CREATE TABLE role_permissions (
    role_id         INT         NOT NULL,
    permission_id   INT         NOT NULL,

    INDEX idx_role_permissions_role_id (role_id),
    INDEX idx_role_permissions_permission_id (permission_id),

    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id)       REFERENCES roles(id)        ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(id)  ON DELETE CASCADE
);