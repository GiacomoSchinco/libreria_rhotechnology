package com.rhotechnologylibri.libreria_rhotechnology.service;

import com.rhotechnologylibri.libreria_rhotechnology.repository.AutoreRepository;
import org.springframework.stereotype.Service;

@Service
public class AutoreService {

    private final AutoreRepository autoreRepository;

    public AutoreService(AutoreRepository autoreRepository) {
        this.autoreRepository = autoreRepository;
    }
}
