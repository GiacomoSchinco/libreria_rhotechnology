package com.rhotechnologylibri.libreria_rhotechnology.service;

import com.rhotechnologylibri.libreria_rhotechnology.repository.LibroRepository;
import org.springframework.stereotype.Service;

@Service
public class LibroService {

    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }
}
