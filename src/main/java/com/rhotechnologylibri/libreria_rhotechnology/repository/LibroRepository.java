package com.rhotechnologylibri.libreria_rhotechnology.repository;

import com.rhotechnologylibri.libreria_rhotechnology.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LibroRepository extends JpaRepository<Libro, UUID> {

}
