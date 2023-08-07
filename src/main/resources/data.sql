-- Dodawanie skrytek
INSERT INTO skrytki (id,version, data_utworzenia, status, typ_skrytki)
VALUES
    (1,1, '2023-07-15 12:00:00', 'Zajęta', 'Duża'),
    (2,1, '2023-07-15 12:00:00', 'Wolna', 'Duża'),
    (3,1, '2023-07-15 12:00:00', 'Wolna', 'Duża'),
    (4,1, '2023-07-15 12:00:00', 'Wolna', 'Średnia'),
    (5,1, '2023-07-15 12:00:00', 'Wolna', 'Średnia'),
    (6,1, '2023-07-15 12:00:00', 'Wolna', 'Średnia'),
    (7,1, '2023-07-15 12:00:00', 'Wolna', 'Mała'),
    (8,1, '2023-07-15 12:00:00', 'Wolna', 'Mała'),
    (9,1, '2023-07-15 12:00:00', 'Wolna', 'Mała'),
    (10,1, '2023-07-15 12:00:00', 'Wolna', 'Mała'),
    (11,1, '2023-07-15 12:00:00', 'Wolna', 'Mała');

-- Dodawanie paczki
INSERT INTO paczka (version, id, nadawca, odbiorca_email, rozmiar_paczki, data_stworzenia, data_do_odbioru, status, kod_odbioru, skrytka_id)
VALUES (1, 1, 'Eula Lane', 'eula.lane@jigrormo.ye', 'XL', '2023-07-15 12:00:00', '2023-07-20 12:00:00', 'Oczekująca', 123456, 1);
