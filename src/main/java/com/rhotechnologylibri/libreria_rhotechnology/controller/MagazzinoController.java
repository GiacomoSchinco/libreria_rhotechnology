package com.rhotechnologylibri.libreria_rhotechnology.controller;

import com.rhotechnologylibri.libreria_rhotechnology.dto.request.MagazzinoRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.MagazzinoResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.service.MagazzinoService;
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
@RequestMapping("/api/magazzino")
public class MagazzinoController {

    private final MagazzinoService magazzinoService;

    public MagazzinoController(MagazzinoService magazzinoService) {
        this.magazzinoService = magazzinoService;
    }

    @GetMapping
    public List<MagazzinoResponseDTO> findAll() {
        return magazzinoService.findAll();
    }

    @GetMapping("/{id}")
    public MagazzinoResponseDTO findById(@PathVariable Integer id) {
        return magazzinoService.findById(id);
    }

    @PostMapping
    public ResponseEntity<MagazzinoResponseDTO> create(@Valid @RequestBody MagazzinoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(magazzinoService.create(request));
    }

    @PutMapping("/{id}")
    public MagazzinoResponseDTO update(@PathVariable Integer id,
                                       @Valid @RequestBody MagazzinoRequestDTO request) {
        return magazzinoService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        magazzinoService.delete(id);
    }
}
