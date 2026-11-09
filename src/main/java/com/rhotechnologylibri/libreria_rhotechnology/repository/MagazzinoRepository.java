package com.rhotechnologylibri.libreria_rhotechnology.repository;

import com.rhotechnologylibri.libreria_rhotechnology.entity.Magazzino;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MagazzinoRepository extends JpaRepository<Magazzino, Integer> {

    boolean existsByCasaEditriceId(Integer idCasaEditrice);
}
