package com.rhotechnologylibri.libreria_rhotechnology.controller;

import com.rhotechnologylibri.libreria_rhotechnology.dto.request.AutoreRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.AutoreResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.service.AutoreService;
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
@RequestMapping("/api/autori")
public class AutoreController {

    private final AutoreService autoreService;

    public AutoreController(AutoreService autoreService) {
        this.autoreService = autoreService;
    }

    @GetMapping
    public List<AutoreResponseDTO> findAll() {
        return autoreService.findAll();
    }

    @GetMapping("/{id}")
    public AutoreResponseDTO findById(@PathVariable Integer id) {
        return autoreService.findById(id);
    }

    @PostMapping
    public ResponseEntity<AutoreResponseDTO> create(@Valid @RequestBody AutoreRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(autoreService.create(request));
    }

    @PutMapping("/{id}")
    public AutoreResponseDTO update(@PathVariable Integer id,
                                    @Valid @RequestBody AutoreRequestDTO request) {
        return autoreService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        autoreService.delete(id);
    }
}
