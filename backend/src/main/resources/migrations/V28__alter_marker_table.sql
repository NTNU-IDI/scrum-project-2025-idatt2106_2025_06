ALTER TABLE marker
    ADD COLUMN image_id VARCHAR(12) NULL,
    ADD CONSTRAINT fk_marker_image
        FOREIGN KEY (image_id) REFERENCES image(id)
            ON DELETE SET NULL
            ON UPDATE CASCADE;