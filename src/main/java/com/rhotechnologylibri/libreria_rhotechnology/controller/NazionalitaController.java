package com.rhotechnologylibri.libreria_rhotechnology.controller;

import com.rhotechnologylibri.libreria_rhotechnology.dto.request.NazionalitaRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.NazionalitaResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.service.NazionalitaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    public List<NazionalitaResponseDTO> findAll() {
        return nazionalitaService.findAll();
    }

    @GetMapping("/{id}")
    public NazionalitaResponseDTO findById(@PathVariable Integer id) {
        return nazionalitaService.findById(id);
    }

    @PostMapping
    public ResponseEntity<NazionalitaResponseDTO> create(@Valid @RequestBody NazionalitaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(nazionalitaService.create(request));
    }

    @PutMapping("/{id}")
    public NazionalitaResponseDTO update(@PathVariable Integer id,
                                        @Valid @RequestBody NazionalitaRequestDTO request) {
        return nazionalitaService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        nazionalitaService.delete(id);
    }
}
