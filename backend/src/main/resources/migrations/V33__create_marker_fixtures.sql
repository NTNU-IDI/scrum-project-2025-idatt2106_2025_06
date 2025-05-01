INSERT INTO marker (
    id, storage_id, event_id, name, description, location, type, created_at, updated_at, image_id, contact_info, opening_hours
) VALUES (
             'Xk9TqW7hN1',
             null,
             null,
             'Sykehus',
             'Trondheim sykehus',
             ST_PointFromText('POINT (10.4024 63.4196)', 4326),
             'hospital',
             '2025-05-01 18:30:00',
             '2025-05-01 18:30:00',
             null,
             'Dr-Amin,amin.elhady@medunit.org,+47 112 35 678',
             '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-18:00,10:00-18:00'
         ),
         ('Xk9TqW7hN2',
          null,
          null,
          'Sykehus',
          'Oslo sykehus',
          ST_PointFromText('POINT (10.4024 63.4196)', 4326),
          'hospital',
          '2025-05-01 18:30:00',
          '2025-05-01 18:30:00',
          null,
          'Dr-Amin, mashalla@mail.com,+47 112 35 678',
          '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-18:00,10:00-18:00'
         );


