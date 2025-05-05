CREATE TABLE opening_hours (
        marker_id       VARCHAR(12)         PRIMARY KEY REFERENCES marker(id),
        monday          VARCHAR(20),
        tuesday         VARCHAR(20),
        wednesday       VARCHAR(20),
        thursday        VARCHAR(20),
        friday          VARCHAR(20),
        saturday        VARCHAR(20),
        sunday          VARCHAR(20)
);