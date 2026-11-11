package com.rhotechnologylibri.libreria_rhotechnology.controller;

import com.rhotechnologylibri.libreria_rhotechnology.dto.request.GenereRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.GenereResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.LibroResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.service.GenereService;
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
@RequestMapping("/api/generi")
public class GenereController {

    private final GenereService genereService;

    public GenereController(GenereService genereService) {
        this.genereService = genereService;
    }

    @GetMapping
    public List<GenereResponseDTO> findAll() {
        return genereService.findAll();
    }

    @GetMapping("/{id}")
    public GenereResponseDTO findById(@PathVariable Integer id) {
        return genereService.findById(id);
    }

    @GetMapping("/{id}/libri")
    public List<LibroResponseDTO> getLibriByGenere(@PathVariable Integer id) {
        return genereService.getLibriByGenere(id);
    }

    @PostMapping
    public ResponseEntity<GenereResponseDTO> create(@Valid @RequestBody GenereRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(genereService.create(request));
    }

    @PutMapping("/{id}")
    public GenereResponseDTO update(@PathVariable Integer id,
            @Valid @RequestBody GenereRequestDTO request) {
        return genereService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        genereService.delete(id);
    }
}
