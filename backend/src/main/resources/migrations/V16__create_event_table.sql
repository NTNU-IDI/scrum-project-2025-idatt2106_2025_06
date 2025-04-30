CREATE TABLE event (
    id                                       VARCHAR(36)                     PRIMARY KEY,
    name                                     VARCHAR(255)                    NOT NULL,
    description                              TEXT                            NOT NULL,
    content                                  TEXT                            NOT NULL,
    location                                 POINT SRID 4326                 NULL,
    type                                     ENUM ('natural_disaster', 'nuclear_attack', 'terror_attack', 'pandemic', 'war', 'other') NOT NULL,
    impact_area_radius_km                    Double                          NULL,
    mandatory_evacuation_area_radius_km      Double                          NULL,
    recommended_evacuation_area_radius_km    Double                          NULL,
    start_date                               TIMESTAMP                       NULL,
    end_date                                 TIMESTAMP                       NULL,
    eta                                      TIMESTAMP                       NULL,
    severity                                 ENUM ('low', 'medium', 'high')  NOT NULL DEFAULT 'low',
    created_at                               TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                               TIMESTAMP                       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);