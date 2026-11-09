package com.rhotechnologylibri.libreria_rhotechnology.service;

import com.rhotechnologylibri.libreria_rhotechnology.dto.request.MagazzinoRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.MagazzinoResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.entity.CasaEditrice;
import com.rhotechnologylibri.libreria_rhotechnology.entity.Libro;
import com.rhotechnologylibri.libreria_rhotechnology.entity.Magazzino;
import com.rhotechnologylibri.libreria_rhotechnology.exception.BadRequestException;
import com.rhotechnologylibri.libreria_rhotechnology.exception.ResourceNotFoundException;
import com.rhotechnologylibri.libreria_rhotechnology.mapper.MagazzinoMapper;
import com.rhotechnologylibri.libreria_rhotechnology.repository.CasaEditriceRepository;
import com.rhotechnologylibri.libreria_rhotechnology.repository.LibroRepository;
import com.rhotechnologylibri.libreria_rhotechnology.repository.MagazzinoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MagazzinoService {

    private final MagazzinoRepository magazzinoRepository;
    private final MagazzinoMapper magazzinoMapper;
    private final LibroRepository libroRepository;
    private final CasaEditriceRepository casaEditriceRepository;

    public MagazzinoService(MagazzinoRepository magazzinoRepository,
                            MagazzinoMapper magazzinoMapper,
                            LibroRepository libroRepository,
                            CasaEditriceRepository casaEditriceRepository) {
        this.magazzinoRepository = magazzinoRepository;
        this.magazzinoMapper = magazzinoMapper;
        this.libroRepository = libroRepository;
        this.casaEditriceRepository = casaEditriceRepository;
    }

    @Transactional(readOnly = true)
    public List<MagazzinoResponseDTO> findAll() {
        return magazzinoMapper.toResponseList(magazzinoRepository.findAll());
    }

    @Transactional(readOnly = true)
    public MagazzinoResponseDTO findById(Integer id) {
        Magazzino entity = magazzinoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Magazzino", id));
        return magazzinoMapper.toResponse(entity);
    }

    @Transactional
    public MagazzinoResponseDTO create(MagazzinoRequestDTO request) {
        Magazzino entity = magazzinoMapper.toEntity(request);
        entity.setLibro(resolveLibro(request.getIdLibro()));
        entity.setCasaEditrice(resolveCasaEditrice(request.getIdCasaEditrice()));
        return magazzinoMapper.toResponse(magazzinoRepository.save(entity));
    }

    @Transactional
    public MagazzinoResponseDTO update(Integer id, MagazzinoRequestDTO request) {
        Magazzino entity = magazzinoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Magazzino", id));
        magazzinoMapper.updateEntity(entity, request);
        entity.setLibro(resolveLibro(request.getIdLibro()));
        entity.setCasaEditrice(resolveCasaEditrice(request.getIdCasaEditrice()));
        return magazzinoMapper.toResponse(magazzinoRepository.save(entity));
    }

    @Transactional
    public void delete(Integer id) {
        if (!magazzinoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Magazzino", id);
        }
        magazzinoRepository.deleteById(id);
    }

    // Libro obbligatorio: la riga di magazzino non esiste senza libro
    private Libro resolveLibro(Integer idLibro) {
        if (idLibro == null) {
            throw new BadRequestException("idLibro e' obbligatorio");
        }
        return libroRepository.findById(idLibro)
                .orElseThrow(() -> new ResourceNotFoundException("Libro", idLibro));
    }

    // Casa editrice opzionale: null azzera la relazione
    private CasaEditrice resolveCasaEditrice(Integer idCasaEditrice) {
        if (idCasaEditrice == null) {
            return null;
        }
        return casaEditriceRepository.findById(idCasaEditrice)
                .orElseThrow(() -> new ResourceNotFoundException("CasaEditrice", idCasaEditrice));
    }
}
