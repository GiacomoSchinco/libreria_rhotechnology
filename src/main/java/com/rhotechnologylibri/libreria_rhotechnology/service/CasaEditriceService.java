package com.rhotechnologylibri.libreria_rhotechnology.service;

import com.rhotechnologylibri.libreria_rhotechnology.repository.CasaEditriceRepository;
import org.springframework.stereotype.Service;

@Service
public class CasaEditriceService {

    private final CasaEditriceRepository casaEditriceRepository;

    public CasaEditriceService(CasaEditriceRepository casaEditriceRepository) {
        this.casaEditriceRepository = casaEditriceRepository;
    }
}
