INSERT INTO marker (
    id, event_id, image_id, name, location, description, contact_info, opening_hours, type, created_at, updated_at
) VALUES
      ('mk2001', NULL, NULL, 'Nordkapp Defibrillator',
       ST_PointFromText('POINT (25.7870 71.1715)', 4326),
       'Ved Nordkapphallen', NULL,
       '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-18:00,10:00-18:00',
       'Defibrillator', NOW(), NOW()
      ),
      ('mk2002', NULL, NULL, 'Kirkenes Legevakt',
       ST_PointFromText('POINT (30.0450 69.7250)', 4326),
       'Kirkenes community health center',
       'Dr. Lege,kirkenes@lege.no,+47 77700000',
       '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-18:00,10:00-18:00',
       'EmergencyClinic', NOW(), NOW()
      ),
      ('mk2003', NULL, NULL, 'Hammerfest Politistasjon',
       ST_PointFromText('POINT (23.6820 70.6600)', 4326),
       'Hammerfest politikammer',
       'Politiet,hammerfest@politi.no,+47 02800',
       '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-18:00,10:00-18:00',
       'PoliceStation', NOW(), NOW()
      ),
      ('mk2004', NULL, NULL, 'Røros Infopunkt',
       ST_PointFromText('POINT (11.3871 62.5740)', 4326),
       'Turist- og beredskapsinformasjon i Røros',
       NULL,
       '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-18:00,10:00-18:00',
       'General', NOW(), NOW()
      ),
      ('mk2005', NULL, NULL, 'Geiranger Distribusjonspunkt',
       ST_PointFromText('POINT (7.2059 62.1048)', 4326),
       'Mat- og vannutdeling ved Geiranger brygge',
       NULL,
       '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-18:00,10:00-18:00',
       'DistributionPoint', NOW(), NOW()
      ),
      ('mk2006', NULL, NULL, 'Trolltunga Shelter',
       ST_PointFromText('POINT (6.7125 60.1240)', 4326),
       'Fjellshelter ved Trolltunga – DNT',
       'DNT,post@dnt.no,+47 55544433',
       '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-18:00,10:00-18:00',
       'Shelter', NOW(), NOW()
      ),
      ('mk2007', NULL, NULL, 'Sognefjord Infopunkt',
       ST_PointFromText('POINT (7.1188 61.1347)', 4326),
       'Sognefjord turistinformasjon',
       NULL,
       '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-18:00,10:00-18:00',
       'General', NOW(), NOW()
      ),
      ('mk2008', NULL, NULL, 'Lofoten Infopunkt',
       ST_PointFromText('POINT (13.6283 68.2230)', 4326),
       'Beredskaps- og turistinfo i Lofoten',
       NULL,
       '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-18:00,10:00-18:00',
       'General', NOW(), NOW()
      ),
      ('mk2009', NULL, NULL, 'Kristiansand Hjertestarter',
       ST_PointFromText('POINT (8.1461 58.1467)', 4326),
       'Utenfor Kristiansand rådhus',
       NULL,
       '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-18:00,10:00-18:00',
       'Defibrillator', NOW(), NOW()
      ),
      ('mk2010', NULL, NULL, 'Stavanger Apotek 1',
       ST_PointFromText('POINT (5.7324 58.9690)', 4326),
       'Apotek 1 i Stavanger sentrum',
       'Apoteket,stavanger@apotek1.no,+47 51560000',
       '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-18:00,10:00-18:00',
       'Pharmacy', NOW(), NOW()
      ),
      ('mk2011', NULL, NULL, 'Ålesund Matutdeling',
       ST_PointFromText('POINT (6.1598 62.4722)', 4326),
       'Gratis matutdeling ved Ålesund sentrum',
       NULL,
       '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-18:00,10:00-18:00',
       'DistributionPoint', NOW(), NOW()
      ),
      ('mk2012', NULL, NULL, 'Oslo Politistasjon',
       ST_PointFromText('POINT (10.7522 59.9127)', 4326),
       'Sentral politistasjon i Oslo sentrum',
       'Politiet,oslo@politi.no,+47 02800',
       '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-18:00,10:00-18:00',
       'PoliceStation', NOW(), NOW()
      ),
      ('mk2013', NULL, NULL, 'Vadsø Hjertestarter',
       ST_PointFromText('POINT (29.9260 70.0829)', 4326),
       'Ved Vadsø rådhus', NULL,
       '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-18:00,10:00-18:00',
       'Defibrillator', NOW(), NOW()
      ),
      ('mk2014', NULL, NULL, 'Mo i Rana Matutdeling',
       ST_PointFromText('POINT (14.1422 66.3123)', 4326),
       'Gratis matutdeling ved samfunnshuset', NULL,
       '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-18:00,10:00-18:00',
       'DistributionPoint', NOW(), NOW()
      ),
      ('mk2015', NULL, NULL, 'Bodø Legevakt',
       ST_PointFromText('POINT (14.4089 67.2804)', 4326),
       'Bodø akuttmottak', 'Bodø Lege,legevakt@bodo.no,+47 12345678',
       '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-18:00,10:00-18:00',
       'EmergencyClinic', NOW(), NOW()
      ),
      ('mk2016', NULL, NULL, 'St. Olavs Hospital',
       ST_PointFromText('POINT (10.4136 63.4061)', 4326),
       'Hjerteakutt ved St. Olavs Hospital', 'Sykehus,stolavs@ntnu.no,+47 98765432',
       '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-18:00,10:00-18:00',
       'EmergencyClinic', NOW(), NOW()
      ),
      ('mk2017', NULL, NULL, 'Hamar Politistasjon',
       ST_PointFromText('POINT (11.0669 60.7945)', 4326),
       'Hamar politihus', 'Politiet,hamar@politi.no,+47 02800',
       '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-18:00,10:00-18:00',
       'PoliceStation', NOW(), NOW()
      ),
      ('mk2018', NULL, NULL, 'Lillehammer Info',
       ST_PointFromText('POINT (10.4663 61.1153)', 4326),
       'Turist- og beredskapsinfo i Lillehammer', NULL,
       '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-18:00,10:00-18:00',
       'General', NOW(), NOW()
      ),
      ('mk2019', NULL, NULL, 'Drammen Apotek',
       ST_PointFromText('POINT (10.2040 59.7441)', 4326),
       'Apotek i Drammen sentrum', 'Apoteker,drammen@apotek.no,+47 11223344',
       '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-18:00,10:00-18:00',
       'Pharmacy', NOW(), NOW()
      ),
      ('mk2020', NULL, NULL, 'Skien Hjertestarter',
       ST_PointFromText('POINT (9.5981 59.2097)', 4326),
       'Hjertestarter ved Skien rådhus', NULL,
       '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-18:00,10:00-18:00',
       'Defibrillator', NOW(), NOW()
      ),
      ('mk2021', NULL, NULL, 'Narvik Shelter',
       ST_PointFromText('POINT (17.4273 68.4385)', 4326),
       'Fjellshelter i Narvik-regionen', 'DNT,post@dnt.no,+47 55544433',
       '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-18:00,10:00-18:00',
       'Shelter', NOW(), NOW()
      ),
      ('mk2022', NULL, NULL, 'Bergen Hjertestarter',
       ST_PointFromText('POINT (5.3221 60.3913)', 4326),
       'Hjertestarter ved Bergen stasjon', NULL,
       '08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,08:00-20:00,10:00-18:00,10:00-18:00',
       'Defibrillator', NOW(), NOW()
      );
