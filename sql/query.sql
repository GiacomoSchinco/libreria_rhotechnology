
SELECT BIN_TO_UUID(a.id_autore), CONCAT(a.nome, ' ', a.cognome)  from Autori a ORDER by a.nome;

SELECT BIN_TO_UUID(a.id_autore), CONCAT(a.nome, ' ', a.cognome)  from Autori a ORDER by a.cognome;

SELECT BIN_TO_UUID(l.id_libro), l.titolo , l.anno from Libri l where l.anno >2000;

SELECT BIN_TO_UUID(a.id_autore), a.nome , a.cognome, n.nome from Autori a join Nazionalita n on n.id_nazionalita = a.id_nazionalita where n.nome LIKE LOWER('Italia');

SELECT BIN_TO_UUID(l.id_libro), l.titolo AS Titolo, l.anno AS Anno FROM Libri l WHERE LOWER(l.titolo) LIKE '%x%';

SELECT BIN_TO_UUID(ce.id_casa_editrice), ce.nome from Case_Editrici ce ;

SELECT BIN_TO_UUID(l.id_libro), l.titolo , l.anno from Libri l ORDER BY l.anno DESC limit 5;

SELECT BIN_TO_UUID(l.id_libro), l.titolo , g.nome , l.anno from Libri l 
JOIN Generi g on g.id_genere = l.id_genere;

SELECT l.titolo AS Titolo, l.anno AS Anno, GROUP_CONCAT(CONCAT(a.nome, ' ', a.cognome) SEPARATOR ', ') AS Autori FROM Libri l
JOIN Libri_Autori la ON l.id_libro = la.id_libro
JOIN Autori a ON la.id_autore = a.id_autore
GROUP BY l.id_libro, l.titolo, l.anno ORDER BY l.titolo;

SELECT BIN_TO_UUID(l.id_libro), l.titolo , l.anno from Libri l 
JOIN Libri_Autori la ON l.id_libro = la.id_libro
JOIN Autori a ON la.id_autore = a.id_autore
where a.cognome LIKE LOWER('Eco');

SELECT BIN_TO_UUID(l.id_libro), l.titolo AS Titolo, l.anno AS Anno, m.quantita FROM Libri l
JOIN Generi g ON l.id_genere = g.id_genere
JOIN Magazzino m ON l.id_libro = m.id_libro
WHERE g.nome = 'Fantasy'  AND m.quantita > 0;

SELECT DISTINCT BIN_TO_UUID(a.id_autore), a.nome, a.cognome FROM Autori a
JOIN Libri_Autori la ON a.id_autore = la.id_autore
JOIN Libri l ON la.id_libro = l.id_libro
JOIN Generi g ON l.id_genere = g.id_genere
WHERE g.nome = 'Fantascienza'; 










