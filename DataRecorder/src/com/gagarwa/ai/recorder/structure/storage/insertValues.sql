INSERT INTO cells (cellID, name)
VALUES (1, 'Capital'), (2, 'Boston'), (3, 'Mass'),
(4, 'Raleigh'), (5, 'NC'), (6, 'Washington D.C.'),
(7, 'United States'), (8, 'Tokyo'), (9, 'Japan');

INSERT INTO links (linkID, linkType, cellLeft, cellLeftID, cellRight, cellRightID)
VALUES (1, 'DLINK', 'Capital', 1, 'Boston', 2), (2, 'DLINK', 'Capital', 1, 'Raleigh', 4),
(3, 'DLINK', 'Capital', 1, 'Washington D.C.', 6), (4, 'DLINK', 'Capital', 1, 'Tokyo', 8),
(5, 'LINK', 'Capital', 1, 'Mass', 3), (6, 'LINK', 'Capital', 1, 'NC', 5),
(7, 'LINK', 'Capital', 1, 'United States', 7), (8, 'LINK', 'Capital', 1, 'Japan', 9),
(9, 'LINK', 'Mass', 3, 'Boston', 2), (10, 'LINK', 'NC', 5, 'Raleigh', 4),
(11, 'LINK', 'United States', 7, 'Washington D.C.', 6), (12, 'LINK', 'Japan', 9, 'Tokyo', 8);