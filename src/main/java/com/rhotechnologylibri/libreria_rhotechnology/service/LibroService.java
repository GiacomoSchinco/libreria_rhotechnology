package com.rhotechnologylibri.libreria_rhotechnology.service;

import com.rhotechnologylibri.libreria_rhotechnology.dto.request.LibroRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.LibroResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.entity.Autore;
import com.rhotechnologylibri.libreria_rhotechnology.entity.Libro;
import com.rhotechnologylibri.libreria_rhotechnology.exception.ResourceNotFoundException;
import com.rhotechnologylibri.libreria_rhotechnology.mapper.LibroMapper;
import com.rhotechnologylibri.libreria_rhotechnology.repository.LibroRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LibroService {

    private final LibroRepository libroRepository;
    private final LibroMapper libroMapper;
    private final GenereService genereService;
    private final AutoreService autoreService;

    public LibroService(LibroRepository libroRepository,
                        LibroMapper libroMapper,
                        GenereService genereService,
                        AutoreService autoreService) {
        this.libroRepository = libroRepository;
        this.libroMapper = libroMapper;
        this.genereService = genereService;
        this.autoreService = autoreService;
    }

    @Transactional(readOnly = true)
    public Page<LibroResponseDTO> findAll(Pageable pageable) {
        return libroRepository.findAll(pageable).map(libroMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public LibroResponseDTO findById(Integer id) {
        Libro entity = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro", id));
        return libroMapper.toResponse(entity);
    }

    @Transactional
    public LibroResponseDTO create(LibroRequestDTO request) {
        Libro entity = libroMapper.toEntity(request);
        entity.setGenere(genereService.resolve(request.getIdGenere(), request.getNomeGenere()));
        replaceAutori(entity, autoreService.resolve(request.getIdAutori(), request.getNuoviAutori()));
        return libroMapper.toResponse(libroRepository.save(entity));
    }

    @Transactional
    public LibroResponseDTO update(Integer id, LibroRequestDTO request) {
        Libro entity = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro", id));
        libroMapper.updateEntity(entity, request);
        entity.setGenere(genereService.resolve(request.getIdGenere(), request.getNomeGenere()));
        replaceAutori(entity, autoreService.resolve(request.getIdAutori(), request.getNuoviAutori()));
        return libroMapper.toResponse(libroRepository.save(entity));
    }

    @Transactional
    public void delete(Integer id) {
        if (!libroRepository.existsById(id)) {
            throw new ResourceNotFoundException("Libro", id);
        }
        libroRepository.deleteById(id);
    }

    // Lista vuota = libro senza autori: sostituisce l'intera associazione
    private void replaceAutori(Libro libro, List<Autore> autori) {
        // Si passa da removeAutore/addAutore: aggiornano anche il lato inverso (Autore.libri)
        new ArrayList<>(libro.getAutori()).forEach(libro::removeAutore);
        autori.forEach(libro::addAutore);
    }
}
