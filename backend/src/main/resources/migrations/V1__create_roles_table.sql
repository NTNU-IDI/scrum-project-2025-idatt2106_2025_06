CREATE TABLE roles (
    id              INT             AUTO_INCREMENT PRIMARY KEY,
    role_name       VARCHAR(100)    NOT NULL UNIQUE,

    INDEX idx_roles_role_name (role_name)
);