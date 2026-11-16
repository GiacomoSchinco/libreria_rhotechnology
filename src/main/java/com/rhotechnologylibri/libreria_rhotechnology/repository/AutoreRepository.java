package com.rhotechnologylibri.libreria_rhotechnology.repository;

import com.rhotechnologylibri.libreria_rhotechnology.entity.Autore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AutoreRepository extends JpaRepository<Autore, Integer> {

    boolean existsByNazionalitaId(Integer idNazionalita);

    Optional<Autore> findFirstByNomeIgnoreCaseAndCognomeIgnoreCase(String nome, String cognome);
}
