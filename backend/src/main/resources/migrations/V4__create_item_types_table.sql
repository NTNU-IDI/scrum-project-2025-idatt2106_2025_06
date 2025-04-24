CREATE TABLE item_types (
    id              INT             AUTO_INCREMENT PRIMARY KEY,
    type_name       VARCHAR(100)    NOT NULL UNIQUE,
    description     TEXT,

    INDEX idx_item_types_type_name (type_name)
);