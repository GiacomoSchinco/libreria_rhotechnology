-- libreria_db.Case_Editrici definition

-- NOTA: gli UUID sono salvati in layout standard (uuid_to_bin(uuid(),0)),
-- lo stesso usato da Hibernate. Non usare il flag 1 (swap): mescolare i due
-- formati produce byte diversi per lo stesso UUID.

CREATE TABLE `Case_Editrici` (
  `id_casa_editrice` binary(16) NOT NULL DEFAULT (uuid_to_bin(uuid(),0)),
  `nome` varchar(150) NOT NULL,
  PRIMARY KEY (`id_casa_editrice`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- libreria_db.Generi definition

CREATE TABLE `Generi` (
  `id_genere` binary(16) NOT NULL DEFAULT (uuid_to_bin(uuid(),0)),
  `nome` varchar(100) NOT NULL,
  PRIMARY KEY (`id_genere`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- libreria_db.Nazionalita definition

CREATE TABLE `Nazionalita` (
  `id_nazionalita` binary(16) NOT NULL DEFAULT (uuid_to_bin(uuid(),0)),
  `nome` varchar(100) NOT NULL,
  PRIMARY KEY (`id_nazionalita`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- libreria_db.Autori definition

CREATE TABLE `Autori` (
  `id_autore` binary(16) NOT NULL DEFAULT (uuid_to_bin(uuid(),0)),
  `nome` varchar(100) NOT NULL,
  `cognome` varchar(100) NOT NULL,
  `data_nascita` date DEFAULT NULL,
  `id_nazionalita` binary(16) DEFAULT NULL,
  PRIMARY KEY (`id_autore`),
  KEY `idx_autori_nazionalita` (`id_nazionalita`),
  CONSTRAINT `Autori_ibfk_1` FOREIGN KEY (`id_nazionalita`) REFERENCES `Nazionalita` (`id_nazionalita`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- libreria_db.Libri definition

CREATE TABLE `Libri` (
  `id_libro` binary(16) NOT NULL DEFAULT (uuid_to_bin(uuid(),0)),
  `titolo` varchar(255) NOT NULL,
  `anno` int DEFAULT NULL,
  `id_genere` binary(16) DEFAULT NULL,
  PRIMARY KEY (`id_libro`),
  KEY `idx_libri_genere` (`id_genere`),
  CONSTRAINT `Libri_ibfk_1` FOREIGN KEY (`id_genere`) REFERENCES `Generi` (`id_genere`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- libreria_db.Libri_Autori definition

CREATE TABLE `Libri_Autori` (
  `id_libro` binary(16) NOT NULL,
  `id_autore` binary(16) NOT NULL,
  PRIMARY KEY (`id_libro`,`id_autore`),
  KEY `id_autore` (`id_autore`),
  CONSTRAINT `Libri_Autori_ibfk_1` FOREIGN KEY (`id_libro`) REFERENCES `Libri` (`id_libro`) ON DELETE CASCADE,
  CONSTRAINT `Libri_Autori_ibfk_2` FOREIGN KEY (`id_autore`) REFERENCES `Autori` (`id_autore`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- libreria_db.Magazzino definition

CREATE TABLE `Magazzino` (
  `id_copia` binary(16) NOT NULL DEFAULT (uuid_to_bin(uuid(),0)),
  `id_libro` binary(16) NOT NULL,
  `quantita` int NOT NULL DEFAULT '1',
  `id_casa_editrice` binary(16) DEFAULT NULL,
  PRIMARY KEY (`id_copia`),
  KEY `idx_copie_libro` (`id_libro`),
  KEY `idx_copie_casa_editrice` (`id_casa_editrice`),
  CONSTRAINT `Magazzino_ibfk_1` FOREIGN KEY (`id_libro`) REFERENCES `Libri` (`id_libro`) ON DELETE CASCADE,
  CONSTRAINT `Magazzino_ibfk_2` FOREIGN KEY (`id_casa_editrice`) REFERENCES `Case_Editrici` (`id_casa_editrice`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;