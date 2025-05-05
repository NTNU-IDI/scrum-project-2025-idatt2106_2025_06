CREATE TABLE event_items(
    id          VARCHAR(36)      PRIMARY KEY,
    event_id    VARCHAR(36)      NOT NULL,
    item_id     VARCHAR(36)      NOT NULL,
    quantity    DOUBLE           DEFAULT 0,
    note        TEXT             NULL,
    created_at  TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_event_items_event_id (event_id),
    INDEX idx_event_items_item_id (item_id),

    FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE,
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
);