CREATE TABLE contact_info (
    marker_id       VARCHAR(12)         PRIMARY KEY REFERENCES marker(id),
    name            VARCHAR(50),
    phone           VARCHAR(20),
    email           VARCHAR(50)
);