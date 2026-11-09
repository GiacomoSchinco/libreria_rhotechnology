package com.rhotechnologylibri.libreria_rhotechnology.service;

import com.rhotechnologylibri.libreria_rhotechnology.dto.request.LibroRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.LibroResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.entity.Autore;
import com.rhotechnologylibri.libreria_rhotechnology.entity.Genere;
import com.rhotechnologylibri.libreria_rhotechnology.entity.Libro;
import com.rhotechnologylibri.libreria_rhotechnology.exception.ResourceNotFoundException;
import com.rhotechnologylibri.libreria_rhotechnology.mapper.LibroMapper;
import com.rhotechnologylibri.libreria_rhotechnology.repository.AutoreRepository;
import com.rhotechnologylibri.libreria_rhotechnology.repository.GenereRepository;
import com.rhotechnologylibri.libreria_rhotechnology.repository.LibroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class LibroService {

    private final LibroRepository libroRepository;
    private final LibroMapper libroMapper;
    private final GenereRepository genereRepository;
    private final AutoreRepository autoreRepository;

    public LibroService(LibroRepository libroRepository,
                        LibroMapper libroMapper,
                        GenereRepository genereRepository,
                        AutoreRepository autoreRepository) {
        this.libroRepository = libroRepository;
        this.libroMapper = libroMapper;
        this.genereRepository = genereRepository;
        this.autoreRepository = autoreRepository;
    }

    @Transactional(readOnly = true)
    public List<LibroResponseDTO> findAll() {
        return libroMapper.toResponseList(libroRepository.findAll());
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
        entity.setGenere(resolveGenere(request.getIdGenere()));
        replaceAutori(entity, request.getIdAutori());
        return libroMapper.toResponse(libroRepository.save(entity));
    }

    @Transactional
    public LibroResponseDTO update(Integer id, LibroRequestDTO request) {
        Libro entity = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro", id));
        libroMapper.updateEntity(entity, request);
        entity.setGenere(resolveGenere(request.getIdGenere()));
        replaceAutori(entity, request.getIdAutori());
        return libroMapper.toResponse(libroRepository.save(entity));
    }

    @Transactional
    public void delete(Integer id) {
        if (!libroRepository.existsById(id)) {
            throw new ResourceNotFoundException("Libro", id);
        }
        libroRepository.deleteById(id);
    }

    // null = libro senza genere (la relazione e' opzionale sull'entity)
    private Genere resolveGenere(Integer idGenere) {
        if (idGenere == null) {
            return null;
        }
        return genereRepository.findById(idGenere)
                .orElseThrow(() -> new ResourceNotFoundException("Genere", idGenere));
    }

    // Lista vuota o null = libro senza autori: sostituisce l'intera associazione
    private void replaceAutori(Libro libro, List<Integer> idAutori) {
        Set<Integer> ids = idAutori == null ? Set.of() : new LinkedHashSet<>(idAutori);
        List<Autore> autori = autoreRepository.findAllById(ids);

        if (autori.size() != ids.size()) {
            List<Integer> mancanti = ids.stream()
                    .filter(id -> autori.stream().noneMatch(autore -> autore.getId().equals(id)))
                    .toList();
            throw new ResourceNotFoundException("Autore non esistenti: " + mancanti);
        }

        // Si passa da removeAutore/addAutore: aggiornano anche il lato inverso (Autore.libri)
        new ArrayList<>(libro.getAutori()).forEach(libro::removeAutore);
        autori.forEach(libro::addAutore);
    }
}
