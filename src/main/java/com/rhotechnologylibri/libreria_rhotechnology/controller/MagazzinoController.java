package com.rhotechnologylibri.libreria_rhotechnology.controller;

import com.rhotechnologylibri.libreria_rhotechnology.service.MagazzinoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/magazzino")
public class MagazzinoController {

    private final MagazzinoService magazzinoService;

    public MagazzinoController(MagazzinoService magazzinoService) {
        this.magazzinoService = magazzinoService;
    }
}
