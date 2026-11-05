package com.rhotechnologylibri.libreria_rhotechnology.repository;

import com.rhotechnologylibri.libreria_rhotechnology.entity.Magazzino;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MagazzinoRepository extends JpaRepository<Magazzino, UUID> {

}
