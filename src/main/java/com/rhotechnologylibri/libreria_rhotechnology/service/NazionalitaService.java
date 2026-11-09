package com.rhotechnologylibri.libreria_rhotechnology.service;

import com.rhotechnologylibri.libreria_rhotechnology.repository.NazionalitaRepository;
import org.springframework.stereotype.Service;

@Service
public class NazionalitaService {

    private final NazionalitaRepository nazionalitaRepository;

    public NazionalitaService(NazionalitaRepository nazionalitaRepository) {
        this.nazionalitaRepository = nazionalitaRepository;
    }
}
