package com.rhotechnologylibri.libreria_rhotechnology.controller;

import com.rhotechnologylibri.libreria_rhotechnology.service.CasaEditriceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/case-editrici")
public class CasaEditriceController {

    private final CasaEditriceService casaEditriceService;

    public CasaEditriceController(CasaEditriceService casaEditriceService) {
        this.casaEditriceService = casaEditriceService;
    }
}
