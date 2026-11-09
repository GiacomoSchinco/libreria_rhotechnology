package com.rhotechnologylibri.libreria_rhotechnology.service;

import com.rhotechnologylibri.libreria_rhotechnology.repository.GenereRepository;
import org.springframework.stereotype.Service;

@Service
public class GenereService {

    private final GenereRepository genereRepository;

    public GenereService(GenereRepository genereRepository) {
        this.genereRepository = genereRepository;
    }
}
