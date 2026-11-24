-- Magazzino: una sola riga per coppia (libro, casa editrice).
-- Da eseguire una volta sul database esistente prima di avviare la nuova versione.
-- Le quantita' delle righe duplicate vengono sommate sulla riga con id minore.

-- 1) somma le quantita' dei duplicati sulla riga che resta
UPDATE magazzino m
JOIN (
    SELECT MIN(id) AS keep_id, SUM(quantita) AS totale
    FROM magazzino
    GROUP BY id_libro, id_casa_editrice
    HAVING COUNT(*) > 1
) d ON d.keep_id = m.id
SET m.quantita = d.totale;

-- 2) elimina le altre righe duplicate (<=> confronta anche i NULL)
DELETE m
FROM magazzino m
JOIN magazzino k
    ON k.id_libro = m.id_libro
   AND k.id_casa_editrice <=> m.id_casa_editrice
   AND k.id < m.id;

-- 3) impedisce nuovi duplicati
-- Nota: MySQL considera i NULL tutti distinti in un indice UNIQUE, quindi le righe
-- con id_casa_editrice NULL restano duplicabili a livello di DB; il servizio le
-- unisce comunque a livello applicativo.
ALTER TABLE magazzino
    ADD UNIQUE KEY uk_magazzino_libro_casa (id_libro, id_casa_editrice);
