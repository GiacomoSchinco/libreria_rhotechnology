package com.rhotechnologylibri.libreria_rhotechnology.controller;

import com.rhotechnologylibri.libreria_rhotechnology.service.AutoreService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/autori")
public class AutoreController {

    private final AutoreService autoreService;

    public AutoreController(AutoreService autoreService) {
        this.autoreService = autoreService;
    }
}
