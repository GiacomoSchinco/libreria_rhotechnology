package com.rhotechnologylibri.libreria_rhotechnology.repository;

import com.rhotechnologylibri.libreria_rhotechnology.entity.Genere;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenereRepository extends JpaRepository<Genere, Integer> {

    Optional<Genere> findByNomeIgnoreCase(String nome);
}
