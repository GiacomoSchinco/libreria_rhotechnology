package com.rhotechnologylibri.libreria_rhotechnology.service;

import com.rhotechnologylibri.libreria_rhotechnology.dto.request.NazionalitaRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.NazionalitaResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.entity.Nazionalita;
import com.rhotechnologylibri.libreria_rhotechnology.exception.ResourceNotFoundException;
import com.rhotechnologylibri.libreria_rhotechnology.mapper.NazionalitaMapper;
import com.rhotechnologylibri.libreria_rhotechnology.repository.NazionalitaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NazionalitaService {

    private final NazionalitaRepository nazionalitaRepository;
    private final NazionalitaMapper nazionalitaMapper;

    public NazionalitaService(NazionalitaRepository nazionalitaRepository,
                              NazionalitaMapper nazionalitaMapper) {
        this.nazionalitaRepository = nazionalitaRepository;
        this.nazionalitaMapper = nazionalitaMapper;
    }

    @Transactional(readOnly = true)
    public List<NazionalitaResponseDTO> findAll() {
        return nazionalitaMapper.toResponseList(nazionalitaRepository.findAll());
    }

    @Transactional(readOnly = true)
    public NazionalitaResponseDTO findById(Integer id) {
        Nazionalita entity = nazionalitaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nazionalita", id));
        return nazionalitaMapper.toResponse(entity);
    }

    @Transactional
    public NazionalitaResponseDTO create(NazionalitaRequestDTO request) {
        Nazionalita salvata = nazionalitaRepository.save(nazionalitaMapper.toEntity(request));
        return nazionalitaMapper.toResponse(salvata);
    }

    @Transactional
    public NazionalitaResponseDTO update(Integer id, NazionalitaRequestDTO request) {
        Nazionalita entity = nazionalitaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nazionalita", id));
        nazionalitaMapper.updateEntity(entity, request);
        return nazionalitaMapper.toResponse(nazionalitaRepository.save(entity));
    }

    @Transactional
    public void delete(Integer id) {
        if (!nazionalitaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Nazionalita", id);
        }
        nazionalitaRepository.deleteById(id);
    }
}
