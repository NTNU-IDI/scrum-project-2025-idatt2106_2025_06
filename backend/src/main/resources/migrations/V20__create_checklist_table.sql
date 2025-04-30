CREATE TABLE checklist (
    id              VARCHAR(36)                PRIMARY KEY,
    scenario_id     VARCHAR(36)                NULL,
    name            VARCHAR(255)               NOT NULL,
    amount          DOUBLE                     NOT NULL DEFAULT 0,
    created_at      TIMESTAMP                  DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP                  DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_checklist_scenario_id (scenario_id),

    FOREIGN KEY (scenario_id) REFERENCES scenario(id) ON DELETE CASCADE
);