package com.rhotechnologylibri.libreria_rhotechnology.service;

import com.rhotechnologylibri.libreria_rhotechnology.dto.request.GenereRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.GenereResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.entity.Genere;
import com.rhotechnologylibri.libreria_rhotechnology.exception.ResourceNotFoundException;
import com.rhotechnologylibri.libreria_rhotechnology.mapper.GenereMapper;
import com.rhotechnologylibri.libreria_rhotechnology.repository.GenereRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GenereService {

    private final GenereRepository genereRepository;
    private final GenereMapper genereMapper;

    public GenereService(GenereRepository genereRepository,
                         GenereMapper genereMapper) {
        this.genereRepository = genereRepository;
        this.genereMapper = genereMapper;
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
        genereRepository.deleteById(id);
    }
}
