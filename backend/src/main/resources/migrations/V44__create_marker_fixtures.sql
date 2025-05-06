INSERT INTO marker (
    id, event_id, image_id, name, location, description, contact_info, opening_hours, type, created_at, updated_at
) VALUES

('mk1001', null, null, 'Hjertestarter Solsiden', ST_PointFromText('POINT (10.4142 63.4343)', 4326), 'Ved inngangen til Solsiden kjøpesenter', null, null, 'Defibrillator', NOW(), NOW()),
('mk1002', null, null, 'Hjertestarter Bakklandet', ST_PointFromText('POINT (10.4065 63.4281)', 4326), 'Montert på vegg ved Bakklandstorget', null, null, 'Defibrillator', NOW(), NOW()),
('mk1003', null, null, 'Trondheim Legevakt', ST_PointFromText('POINT (10.4020 63.4274)', 4326), 'Legevakt åpen hele døgnet', 'Dr. Hjelp,legevakt@trd.no,+47 11122233', null, 'EmergencyClinic', NOW(), NOW()),
('mk1004', null, null, 'Midtbyen Apotek', ST_PointFromText('POINT (10.3985 63.4302)', 4326), 'Apotek med bredt utvalg og reseptservice', 'Apoteker,apoteker@trd.no,+47 22233344', null, 'Pharmacy', NOW(), NOW()),
('mk1005', null, null, 'Matstasjon Ila', ST_PointFromText('POINT (10.3856 63.4308)', 4326), 'Distribusjon av basisvarer til trengende', 'Hjelpesentral,ila@mat.no,+47 33344455', null, 'DistributionPoint', NOW(), NOW()),
('mk1006', null, null, 'Trondheim Politistasjon', ST_PointFromText('POINT (10.3970 63.4301)', 4326), 'Politistasjon med beredskap 24/7', 'Politi,politiet@trd.no,+47 02800', null, 'PoliceStation', NOW(), NOW()),
('mk1007', null, null, 'Nidelven Informasjonspunkt', ST_PointFromText('POINT (10.4032 63.4260)', 4326), 'Turist- og beredskapsinformasjon', null, null, 'General', NOW(), NOW()),
('mk1008', null, null, 'Hjertestarter Moholt', ST_PointFromText('POINT (10.4412 63.4170)', 4326), 'Utenfor treningssenter', null, null, 'Defibrillator', NOW(), NOW()),
('mk1009', null, null, 'Studentklinikken Legevakt', ST_PointFromText('POINT (10.4100 63.4199)', 4326), 'For studenter med akutte behov', 'StudDr,student@legevakt.no,+47 99887766', null, 'EmergencyClinic', NOW(), NOW()),
('mk1010', null, null, 'Matutdeling Moholt', ST_PointFromText('POINT (10.4421 63.4175)', 4326), 'Midlertidig distribusjonspunkt for mat', null, null, 'DistributionPoint', NOW(), NOW()),
('mk1011', null, null, 'Hjertestarter Oslo S', ST_PointFromText('POINT (10.7530 59.9111)', 4326), 'Plassert ved hovedinngangen', null, null, 'Defibrillator', NOW(), NOW()),
('mk1012', null, null, 'Bergen Legevakt', ST_PointFromText('POINT (5.3250 60.3913)', 4326), 'Døgnåpen legevakt i Bergen sentrum', 'Bergen Lege,legevakt@bergen.no,+47 44556677', null, 'EmergencyClinic', NOW(), NOW()),
('mk1013', null, null, 'Matutdeling Ålesund', ST_PointFromText('POINT (6.1490 62.4722)', 4326), 'Åpen mandag-fredag', 'Ålesund Hjelp,alesund@mat.no,+47 55667788', null, 'DistributionPoint', NOW(), NOW()),
('mk1014', null, null, 'Politihuset Bodø', ST_PointFromText('POINT (14.3948 67.2804)', 4326), 'Politistasjon for Nordland', 'Politileder,bodo@politi.no,+47 02800', null, 'PoliceStation', NOW(), NOW()),
('mk1015', null, null, 'Hjertestarter Drammen Torg', ST_PointFromText('POINT (10.2062 59.7440)', 4326), 'Ved hovedinngangen til torget', null, null, 'Defibrillator', NOW(), NOW()),
('mk1016', null, null, 'Vitusapotek Kristiansand', ST_PointFromText('POINT (8.0001 58.1467)', 4326), 'Apotek med rådgivning og resept', 'Farmasøyt,farmasoyt@krs.no,+47 22334455', null, 'Pharmacy', NOW(), NOW()),
('mk1017', null, null, 'Beredskapspunkt Alta', ST_PointFromText('POINT (23.2700 69.9689)', 4326), 'Generell informasjon og hjelp', null, null, 'General', NOW(), NOW()),
('mk1018', null, null, 'Matpunkt Sandnes', ST_PointFromText('POINT (5.7352 58.8522)', 4326), 'Mat og vann i nødsituasjoner', null, null, 'DistributionPoint', NOW(), NOW()),
('mk1019', null, null, 'Legevakt Lillestrøm', ST_PointFromText('POINT (11.0457 59.9550)', 4326), 'Døgnåpent legevaktstilbud', 'Lillestrøm Med.,legevakt@ls.no,+47 66778899', null, 'EmergencyClinic', NOW(), NOW()),
('mk1020', null, null, 'Apotek1 Tromsø', ST_PointFromText('POINT (18.9553 69.6496)', 4326), 'Apotek i sentrum med lang åpningstid', 'Tromsø Apotek,apotek@tromso.no,+47 99887766', null, 'Pharmacy', NOW(), NOW());
