package com.rhotechnologylibri.libreria_rhotechnology.service;

import com.rhotechnologylibri.libreria_rhotechnology.entity.Nazionalita;
import com.rhotechnologylibri.libreria_rhotechnology.repository.NazionalitaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NazionalitaService {

    private final NazionalitaRepository nazionalitaRepository;

    public NazionalitaService(NazionalitaRepository nazionalitaRepository) {
        this.nazionalitaRepository = nazionalitaRepository;
    }

    public List<Nazionalita> getAll() {
        return nazionalitaRepository.findAll();
    }
}
