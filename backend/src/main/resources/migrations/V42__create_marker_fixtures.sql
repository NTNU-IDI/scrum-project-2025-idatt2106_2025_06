INSERT INTO marker (
    id, storage_id, event_id, name, description, location, type, created_at, updated_at, image_id, contact_info, opening_hours
) VALUES
-- Clinics
('Xk9TqW7hC1', null, null, 'Klinikk', 'Bergen helseklinikk',
 ST_PointFromText('POINT (5.3221 60.3929)', 4326), 'clinic',
 '2025-05-01 19:00:00', '2025-05-01 19:00:00', null,
 'Dr-Birgit, bergen.klinikk@helse.no,+47 113 45 123',
 '09:00-17:00,09:00-17:00,09:00-17:00,09:00-17:00,09:00-17:00,Closed,Closed'),

-- Pharmacies
('Xk9TqW7hP1', null, null, 'Apotek', 'Stavanger hovedapotek',
 ST_PointFromText('POINT (5.7331 58.9690)', 4326), 'pharmacy',
 '2025-05-01 19:30:00', '2025-05-01 19:30:00', null,
 'Apoteker-Kari, kari.apotek@norway.no,+47 114 56 789',
 '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-16:00,12:00-16:00'),

-- Food Banks
('Xk9TqW7hF1', null, null, 'Matsentral', 'Tromsø matsentral',
 ST_PointFromText('POINT (18.9553 69.6496)', 4326), 'food_bank',
 '2025-05-01 20:00:00', '2025-05-01 20:00:00', null,
 'Kontaktperson: Mats, tromso.food@help.no,+47 115 67 891',
 '10:00-14:00,10:00-14:00,10:00-14:00,10:00-14:00,10:00-14:00,Closed,Closed'),

-- Water Distribution
('Xk9TqW7hW1', null, null, 'Vannstasjon', 'Kristiansand vannfordeling',
 ST_PointFromText('POINT (8.0001 58.1467)', 4326), 'water_distribution',
 '2025-05-01 20:30:00', '2025-05-01 20:30:00', null,
 'Inger Vann, vann@krst.no,+47 116 78 912',
 '07:00-19:00,07:00-19:00,07:00-19:00,07:00-19:00,07:00-19:00,09:00-13:00,09:00-13:00'),

-- Food Banks
('Xk9TqW7hF2', null, null, 'Matsentral', 'Bodø matsentral',
 ST_PointFromText('POINT (14.3976 67.2804)', 4326), 'food_bank',
 '2025-05-02 08:00:00', '2025-05-02 08:00:00', null,
 'Lena Mat, bodo.food@help.no,+47 117 89 123',
 '09:00-15:00,09:00-15:00,09:00-15:00,09:00-15:00,09:00-15:00,Closed,Closed'),

('Xk9TqW7hF3', null, null, 'Matsentral', 'Ålesund matsentral',
 ST_PointFromText('POINT (6.1490 62.4722)', 4326), 'food_bank',
 '2025-05-02 08:10:00', '2025-05-02 08:10:00', null,
 'Per Åle, alesund.food@hjelp.no,+47 118 90 456',
 '10:00-14:00,10:00-14:00,10:00-14:00,10:00-14:00,10:00-14:00,Closed,Closed'),

-- Clinics
('Xk9TqW7hC2', null, null, 'Klinikk', 'Drammen helseklinikk',
 ST_PointFromText('POINT (10.2062 59.7440)', 4326), 'clinic',
 '2025-05-02 08:20:00', '2025-05-02 08:20:00', null,
 'Dr-Kaja, drammen.klinikk@helse.no,+47 119 91 321',
 '08:00-16:00,08:00-16:00,08:00-16:00,08:00-16:00,08:00-16:00,Closed,Closed'),

('Xk9TqW7hC3', null, null, 'Klinikk', 'Hamar helsesenter',
 ST_PointFromText('POINT (11.0789 60.7945)', 4326), 'clinic',
 '2025-05-02 08:30:00', '2025-05-02 08:30:00', null,
 'Dr-Tone, hamar.klinikk@innlandet.no,+47 120 92 654',
 '08:30-15:30,08:30-15:30,08:30-15:30,08:30-15:30,08:30-15:30,Closed,Closed'),

-- Pharmacies
('Xk9TqW7hP2', null, null, 'Apotek', 'Fredrikstad apotek',
 ST_PointFromText('POINT (10.9407 59.2180)', 4326), 'pharmacy',
 '2025-05-02 08:40:00', '2025-05-02 08:40:00', null,
 'Jens Apotek, jens.apotek@ostfold.no,+47 121 93 789',
 '09:00-18:00,09:00-18:00,09:00-18:00,09:00-18:00,09:00-18:00,10:00-14:00,Closed'),

('Xk9TqW7hP3', null, null, 'Apotek', 'Lillehammer apotek',
 ST_PointFromText('POINT (10.4885 61.1153)', 4326), 'pharmacy',
 '2025-05-02 08:50:00', '2025-05-02 08:50:00', null,
 'Nina A., nina.apotek@innlandet.no,+47 122 94 111',
 '08:00-16:00,08:00-16:00,08:00-16:00,08:00-16:00,08:00-16:00,09:00-13:00,Closed'),

-- Water Distribution
('Xk9TqW7hW2', null, null, 'Vannpost', 'Alta vannpost',
 ST_PointFromText('POINT (23.2700 69.9689)', 4326), 'water_distribution',
 '2025-05-02 09:00:00', '2025-05-02 09:00:00', null,
 'Iselin Vann, alta.vann@nord.no,+47 123 95 222',
 '07:30-17:00,07:30-17:00,07:30-17:00,07:30-17:00,07:30-17:00,08:00-12:00,Closed'),

('Xk9TqW7hW3', null, null, 'Vannstasjon', 'Lillestrøm vannstasjon',
 ST_PointFromText('POINT (11.0457 59.9550)', 4326), 'water_distribution',
 '2025-05-02 09:10:00', '2025-05-02 09:10:00', null,
 'Lars V., vann@lillestrom.no,+47 124 96 333',
 '08:00-18:00,08:00-18:00,08:00-18:00,08:00-18:00,08:00-18:00,09:00-14:00,09:00-14:00'),

-- Hospitals
('Xk9TqW7hN3', null, null, 'Sykehus', 'Harstad sykehus',
 ST_PointFromText('POINT (16.5416 68.7981)', 4326), 'hospital',
 '2025-05-02 09:20:00', '2025-05-02 09:20:00', null,
 'Dr-Nina, harstad@helse.no,+47 125 97 444',
 '00:00-23:59,00:00-23:59,00:00-23:59,00:00-23:59,00:00-23:59,00:00-23:59,00:00-23:59');
