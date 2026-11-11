package com.rhotechnologylibri.libreria_rhotechnology.service;

import com.rhotechnologylibri.libreria_rhotechnology.dto.request.GenereRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.GenereResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.LibroResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.entity.Genere;
import com.rhotechnologylibri.libreria_rhotechnology.exception.ResourceInUseException;
import com.rhotechnologylibri.libreria_rhotechnology.exception.ResourceNotFoundException;
import com.rhotechnologylibri.libreria_rhotechnology.mapper.GenereMapper;
import com.rhotechnologylibri.libreria_rhotechnology.mapper.LibroMapper;
import com.rhotechnologylibri.libreria_rhotechnology.repository.GenereRepository;
import com.rhotechnologylibri.libreria_rhotechnology.repository.LibroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GenereService {

    private final GenereRepository genereRepository;
    private final GenereMapper genereMapper;
    private final LibroRepository libroRepository;
    private final LibroMapper libroMapper;

    public GenereService(GenereRepository genereRepository,
            GenereMapper genereMapper,
            LibroRepository libroRepository,
            LibroMapper libroMapper) {
        this.genereRepository = genereRepository;
        this.genereMapper = genereMapper;
        this.libroRepository = libroRepository;
        this.libroMapper = libroMapper;
    }

    @Transactional(readOnly = true)
    public List<GenereResponseDTO> findAll() {
        return genereMapper.toResponseList(genereRepository.findAll());
    }

    @Transactional(readOnly = true)
    public GenereResponseDTO findById(Integer id) {
        Genere entity = genereRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genere", id));
        return genereMapper.toResponse(entity);
    }

    @Transactional
    public GenereResponseDTO create(GenereRequestDTO request) {
        Genere salvato = genereRepository.save(genereMapper.toEntity(request));
        return genereMapper.toResponse(salvato);
    }

    @Transactional
    public GenereResponseDTO update(Integer id, GenereRequestDTO request) {
        Genere entity = genereRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genere", id));
        genereMapper.updateEntity(entity, request);
        return genereMapper.toResponse(genereRepository.save(entity));
    }

    @Transactional
    public void delete(Integer id) {
        if (!genereRepository.existsById(id)) {
            throw new ResourceNotFoundException("Genere", id);
        }
        if (libroRepository.existsByGenereId(id)) {
            throw new ResourceInUseException("Genere", id, "almeno un libro");
        }
        genereRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<LibroResponseDTO> getLibriByGenere(Integer id) {
        if (!genereRepository.existsById(id)) {
            throw new ResourceNotFoundException("Genere", id);
        }
        return libroMapper.toResponseList(libroRepository.findByGenereId(id));
    }
}
