-- NOTA: tabelle in minuscolo, chiavi primarie INT AUTO_INCREMENT.
-- Le chiavi esterne vanno create dopo le tabelle referenziate (ordine sotto rispettato).

-- libreria_db.case_editrici definition

CREATE TABLE `case_editrici` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(150) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- libreria_db.generi definition

CREATE TABLE `generi` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- libreria_db.nazionalita definition

CREATE TABLE `nazionalita` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- libreria_db.autori definition

CREATE TABLE `autori` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `cognome` varchar(100) NOT NULL,
  `data_nascita` date DEFAULT NULL,
  `id_nazionalita` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_autori_nazionalita` (`id_nazionalita`),
  CONSTRAINT `fk_autori_nazionalita` FOREIGN KEY (`id_nazionalita`) REFERENCES `nazionalita` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- libreria_db.libri definition

CREATE TABLE `libri` (
  `id` int NOT NULL AUTO_INCREMENT,
  `titolo` varchar(255) NOT NULL,
  `anno` int DEFAULT NULL,
  `id_genere` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_libri_genere` (`id_genere`),
  CONSTRAINT `fk_libri_genere` FOREIGN KEY (`id_genere`) REFERENCES `generi` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- libreria_db.libri_autori definition

CREATE TABLE `libri_autori` (
  `id_libro` int NOT NULL,
  `id_autore` int NOT NULL,
  PRIMARY KEY (`id_libro`,`id_autore`),
  KEY `idx_libri_autori_autore` (`id_autore`),
  CONSTRAINT `fk_libri_autori_libro` FOREIGN KEY (`id_libro`) REFERENCES `libri` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_libri_autori_autore` FOREIGN KEY (`id_autore`) REFERENCES `autori` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- libreria_db.magazzino definition

CREATE TABLE `magazzino` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_libro` int NOT NULL,
  `quantita` int NOT NULL DEFAULT '1',
  `id_casa_editrice` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_magazzino_libro` (`id_libro`),
  KEY `idx_magazzino_casa_editrice` (`id_casa_editrice`),
  CONSTRAINT `fk_magazzino_libro` FOREIGN KEY (`id_libro`) REFERENCES `libri` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_magazzino_casa_editrice` FOREIGN KEY (`id_casa_editrice`) REFERENCES `case_editrici` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
