package com.rhotechnologylibri.libreria_rhotechnology.repository;

import com.rhotechnologylibri.libreria_rhotechnology.entity.CasaEditrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CasaEditriceRepository extends JpaRepository<CasaEditrice, UUID> {

}
