INSERT INTO cells (name)
VALUES ('Capital'), ('Boston'), ('Mass'),
('Raleigh'), ('NC'), ('Washington D.C.'),
('United States'), ('Tokyo'), ('Japan');

INSERT INTO links (linkType, source, sourceID, target, targetID)
VALUES ('DLINK', 'Capital', 1, 'Boston', 2), ('DLINK', 'Capital', 1, 'Raleigh', 4),
('DLINK', 'Capital', 1, 'Washington D.C.', 6), ('DLINK', 'Capital', 1, 'Tokyo', 8),
('LINK', 'Capital', 1, 'Mass', 3), ('LINK', 'Capital', 1, 'NC', 5),
('LINK', 'Capital', 1, 'United States', 7), ('LINK', 'Capital', 1, 'Japan', 9),
('LINK', 'Mass', 3, 'Boston', 2), ('LINK', 'NC', 5, 'Raleigh', 4),
('LINK', 'United States', 7, 'Washington D.C.', 6), ('LINK', 'Japan', 9, 'Tokyo', 8);