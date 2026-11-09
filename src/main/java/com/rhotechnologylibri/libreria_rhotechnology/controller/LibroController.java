package com.rhotechnologylibri.libreria_rhotechnology.controller;

import com.rhotechnologylibri.libreria_rhotechnology.dto.request.LibroRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.LibroResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.service.LibroService;
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
@RequestMapping("/api/libri")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    public List<LibroResponseDTO> findAll() {
        return libroService.findAll();
    }

    @GetMapping("/{id}")
    public LibroResponseDTO findById(@PathVariable Integer id) {
        return libroService.findById(id);
    }

    @PostMapping
    public ResponseEntity<LibroResponseDTO> create(@Valid @RequestBody LibroRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(libroService.create(request));
    }

    @PutMapping("/{id}")
    public LibroResponseDTO update(@PathVariable Integer id,
                                   @Valid @RequestBody LibroRequestDTO request) {
        return libroService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        libroService.delete(id);
    }
}
