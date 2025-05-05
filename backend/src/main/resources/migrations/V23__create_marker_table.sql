CREATE TABLE marker (
  id                               VARCHAR(36)                     PRIMARY KEY,
  storage_id                       VARCHAR(36)                     NULL,
  event_id                         VARCHAR(36)                     NULL,
  name                             VARCHAR(255)                    NOT NULL,
  description                      TEXT                            NOT NULL,
  location                         POINT SRID 4326                 NOT NULL,
  type                             VARCHAR(255)                    NOT NULL,
  created_at                       TIMESTAMP                       DEFAULT CURRENT_TIMESTAMP,
  updated_at                       TIMESTAMP                       DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  INDEX idx_marker_storage_id       (storage_id),
  INDEX idx_marker_event_id         (event_id),

  FOREIGN KEY (storage_id)         REFERENCES storages(id) ON DELETE CASCADE,
  FOREIGN KEY (event_id)           REFERENCES event(id) ON DELETE CASCADE
);