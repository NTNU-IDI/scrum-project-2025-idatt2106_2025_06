CREATE TABLE item_units (
    id              INT             AUTO_INCREMENT PRIMARY KEY,
    unit_name       VARCHAR(100)    NOT NULL UNIQUE,
    description     TEXT,

    INDEX idx_item_units_unit_name (unit_name)
);