package com.rhotechnologylibri.libreria_rhotechnology.service;

import com.rhotechnologylibri.libreria_rhotechnology.repository.MagazzinoRepository;
import org.springframework.stereotype.Service;

@Service
public class MagazzinoService {

    private final MagazzinoRepository magazzinoRepository;

    public MagazzinoService(MagazzinoRepository magazzinoRepository) {
        this.magazzinoRepository = magazzinoRepository;
    }
}
