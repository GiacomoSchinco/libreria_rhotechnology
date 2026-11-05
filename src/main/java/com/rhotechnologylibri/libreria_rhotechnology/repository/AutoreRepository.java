package com.rhotechnologylibri.libreria_rhotechnology.repository;

import com.rhotechnologylibri.libreria_rhotechnology.entity.Autore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AutoreRepository extends JpaRepository<Autore, UUID> {

}
