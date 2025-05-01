CREATE TABLE event_type (
    id                                       VARCHAR(36)                     PRIMARY KEY,
    scenario_id                              VARCHAR(36)                     NULL,
    name                                     VARCHAR(255)                    NOT NULL,
    description                              TEXT                            NOT NULL,
    icon                                     VARCHAR(255)                    NULL,
    created_at                               TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                               TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_event_type_scenario_id        (scenario_id),

    FOREIGN KEY (scenario_id) REFERENCES scenario(id) ON DELETE CASCADE
);