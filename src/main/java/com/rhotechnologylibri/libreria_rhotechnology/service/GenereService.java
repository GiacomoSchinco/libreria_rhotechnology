package com.rhotechnologylibri.libreria_rhotechnology.service;

import com.rhotechnologylibri.libreria_rhotechnology.entity.Genere;
import com.rhotechnologylibri.libreria_rhotechnology.repository.GenereRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenereService {

    private final GenereRepository genereRepository;

    public GenereService(GenereRepository genereRepository) {
        this.genereRepository = genereRepository;
    }

    public List<Genere> getAll() {
        return genereRepository.findAll();
    }
}
