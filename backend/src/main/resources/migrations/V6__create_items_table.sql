CREATE TABLE items (
    id              VARCHAR(36)     PRIMARY KEY,
    name            VARCHAR(100)    NOT NULL,
    type_id         INT             NOT NULL,
    unit_id         INT             NOT NULL,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_at       TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_items_type_id (type_id),
    INDEX idx_items_unit_id (unit_id),

    FOREIGN KEY (type_id)   REFERENCES item_types(id),
    FOREIGN KEY (unit_id)   REFERENCES item_units(id)
);