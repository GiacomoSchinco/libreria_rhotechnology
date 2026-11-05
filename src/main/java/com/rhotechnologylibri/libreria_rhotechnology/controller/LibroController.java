package com.rhotechnologylibri.libreria_rhotechnology.controller;

import com.rhotechnologylibri.libreria_rhotechnology.service.LibroService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/libri")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }
}
