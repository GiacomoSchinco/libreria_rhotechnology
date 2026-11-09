package com.rhotechnologylibri.libreria_rhotechnology.controller;

import com.rhotechnologylibri.libreria_rhotechnology.dto.request.CasaEditriceRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.CasaEditriceResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.service.CasaEditriceService;
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
@RequestMapping("/api/case-editrici")
public class CasaEditriceController {

    private final CasaEditriceService casaEditriceService;

    public CasaEditriceController(CasaEditriceService casaEditriceService) {
        this.casaEditriceService = casaEditriceService;
    }

    @GetMapping
    public List<CasaEditriceResponseDTO> findAll() {
        return casaEditriceService.findAll();
    }

    @GetMapping("/{id}")
    public CasaEditriceResponseDTO findById(@PathVariable Integer id) {
        return casaEditriceService.findById(id);
    }

    @PostMapping
    public ResponseEntity<CasaEditriceResponseDTO> create(@Valid @RequestBody CasaEditriceRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(casaEditriceService.create(request));
    }

    @PutMapping("/{id}")
    public CasaEditriceResponseDTO update(@PathVariable Integer id,
                                          @Valid @RequestBody CasaEditriceRequestDTO request) {
        return casaEditriceService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        casaEditriceService.delete(id);
    }
}
