package com.rhotechnologylibri.libreria_rhotechnology.repository;

import com.rhotechnologylibri.libreria_rhotechnology.entity.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Integer> {

    // Solo relazioni ManyToOne nell'EntityGraph: le collezioni (autori) sono LAZY e
    // vengono caricate in batch, cosi' la paginazione resta fatta dal database
    @Override
    @EntityGraph(attributePaths = "genere")
    Page<Libro> findAll(Pageable pageable);

    // Ricerca: nessun @EntityGraph, il DTO di sintesi usa solo id e titolo
    List<Libro> findByTitoloContainingIgnoreCaseOrderByTitoloAsc(String titolo);

    boolean existsByGenereId(Integer idGenere);

    List<Libro> findByGenereId(Integer idGenere);
}
