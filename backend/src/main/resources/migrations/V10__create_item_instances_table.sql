CREATE TABLE item_instances (
    id          VARCHAR(36)         PRIMARY KEY,
    item_id     VARCHAR(36)         NOT NULL,
    storage_id  VARCHAR(36)         NOT NULL,
    expiry_date DATE                NULL,
    amount      DECIMAL(10, 2)               NOT NULL,
    created_at  TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_item_instances_item_id (item_id),
    INDEX idx_item_instances_storage_id (storage_id),

    FOREIGN KEY (item_id)       REFERENCES items(id),
    FOREIGN KEY (storage_id)    REFERENCES storages(id)
);