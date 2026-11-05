package com.rhotechnologylibri.libreria_rhotechnology.controller;

import com.rhotechnologylibri.libreria_rhotechnology.entity.Nazionalita;
import com.rhotechnologylibri.libreria_rhotechnology.service.NazionalitaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/nazionalita")
public class NazionalitaController {

    private final NazionalitaService nazionalitaService;

    public NazionalitaController(NazionalitaService nazionalitaService) {
        this.nazionalitaService = nazionalitaService;
    }

    @GetMapping
    public List<Nazionalita> getAll() {
        return nazionalitaService.getAll();
    }

    @GetMapping("/by-id")
    public Nazionalita getById(@RequestParam("id") Integer id) {
        return nazionalitaService.getById(id);
    }

}
