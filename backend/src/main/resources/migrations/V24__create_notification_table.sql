CREATE TABLE notification (
    id              VARCHAR(36)                                 PRIMARY KEY,
    event_id        VARCHAR(36)                                 NULL,
    marker_id       VARCHAR(36)                                 NULL,
    title           VARCHAR(255)                                NULL,
    type            ENUM('event', 'storage')                    NOT NULL,
    created_at      TIMESTAMP                                   DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP                                   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_notification_event_id (event_id),
    INDEX idx_notification_marker_id (marker_id),

    FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE,
    FOREIGN KEY (marker_id) REFERENCES marker(id) ON DELETE CASCADE
)