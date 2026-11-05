package com.rhotechnologylibri.libreria_rhotechnology.repository;

import com.rhotechnologylibri.libreria_rhotechnology.entity.Genere;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface GenereRepository extends JpaRepository<Genere, UUID> {

}
