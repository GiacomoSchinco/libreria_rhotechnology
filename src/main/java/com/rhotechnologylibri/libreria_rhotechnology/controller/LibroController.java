package com.rhotechnologylibri.libreria_rhotechnology.controller;

import com.rhotechnologylibri.libreria_rhotechnology.dto.request.LibroRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.LibroResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.LibroSintesiResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.pagination.Paging;
import com.rhotechnologylibri.libreria_rhotechnology.service.LibroService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<Page<LibroResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(libroService.findAll(Paging.of(page, size, Sort.by("titolo"))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(libroService.findById(id));
    }

    @GetMapping("/ricerca")
    public ResponseEntity<List<LibroSintesiResponseDTO>> searchByTitolo(
            @Parameter(description = "Parte del titolo (minimo 3 caratteri)")
            @RequestParam String titolo) {
        return ResponseEntity.ok(libroService.searchByTitolo(titolo));
    }

    @PostMapping
    public ResponseEntity<LibroResponseDTO> create(@Valid @RequestBody LibroRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(libroService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroResponseDTO> update(@PathVariable Integer id,
                                                   @Valid @RequestBody LibroRequestDTO request) {
        return ResponseEntity.ok(libroService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        libroService.delete(id);
    }
}
