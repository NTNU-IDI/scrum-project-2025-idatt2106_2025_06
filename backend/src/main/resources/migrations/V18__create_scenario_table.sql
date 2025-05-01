CREATE TABLE scenario (
    id                                       VARCHAR(36)                     PRIMARY KEY,
    title                                    VARCHAR(255)                    NOT NULL,
    url                                      VARCHAR(255)                    NOT NULL,
    description                              TEXT                            NOT NULL,
    content                                  TEXT                            NOT NULL,
    created_at                               TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                               TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);