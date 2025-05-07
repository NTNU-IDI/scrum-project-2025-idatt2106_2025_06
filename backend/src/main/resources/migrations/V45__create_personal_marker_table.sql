CREATE TABLE personal_marker(
    id                      VARCHAR(36)                     PRIMARY KEY,
    name                    VARCHAR(100)                    NOT NULL,
    description             TEXT,
    location                POINT                           NOT NULL,
    colour                  VARCHAR(50)                     NOT NULL,
    storage_id              VARCHAR(36)                     NOT NULL,

    FOREIGN KEY (storage_id) REFERENCES storages(id) ON DELETE CASCADE
    );