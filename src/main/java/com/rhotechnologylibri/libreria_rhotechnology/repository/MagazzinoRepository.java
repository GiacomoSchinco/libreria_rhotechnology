package com.rhotechnologylibri.libreria_rhotechnology.repository;

import com.rhotechnologylibri.libreria_rhotechnology.entity.Magazzino;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MagazzinoRepository extends JpaRepository<Magazzino, Integer> {

    @Override
    @EntityGraph(attributePaths = { "libro", "casaEditrice" })
    Page<Magazzino> findAll(Pageable pageable);

    boolean existsByCasaEditriceId(Integer idCasaEditrice);
}
