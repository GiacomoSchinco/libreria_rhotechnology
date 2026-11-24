package com.rhotechnologylibri.libreria_rhotechnology.controller;

import com.rhotechnologylibri.libreria_rhotechnology.dto.request.MagazzinoRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.MagazzinoResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.pagination.Paging;
import com.rhotechnologylibri.libreria_rhotechnology.service.MagazzinoService;
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

@RestController
@RequestMapping("/api/magazzino")
public class MagazzinoController {

    private final MagazzinoService magazzinoService;

    public MagazzinoController(MagazzinoService magazzinoService) {
        this.magazzinoService = magazzinoService;
    }

    @GetMapping
    public ResponseEntity<Page<MagazzinoResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(magazzinoService.findAll(Paging.of(page, size, Sort.by("id"))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MagazzinoResponseDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(magazzinoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<MagazzinoResponseDTO> create(@Valid @RequestBody MagazzinoRequestDTO request) {
        MagazzinoService.EsitoSalvataggio esito = magazzinoService.create(request);
        if (esito.creato()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(esito.magazzino());
        }
        // riga gia' esistente: quantita' sommata, nessuna nuova risorsa creata
        return ResponseEntity.ok(esito.magazzino());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MagazzinoResponseDTO> update(@PathVariable Integer id,
                                                       @Valid @RequestBody MagazzinoRequestDTO request) {
        return ResponseEntity.ok(magazzinoService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        magazzinoService.delete(id);
    }
}
