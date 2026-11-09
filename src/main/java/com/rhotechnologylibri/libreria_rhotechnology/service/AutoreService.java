package com.rhotechnologylibri.libreria_rhotechnology.service;

import com.rhotechnologylibri.libreria_rhotechnology.mapper.AutoreMapper;
import com.rhotechnologylibri.libreria_rhotechnology.repository.AutoreRepository;
import com.rhotechnologylibri.libreria_rhotechnology.repository.NazionalitaRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.rhotechnologylibri.libreria_rhotechnology.dto.request.AutoreRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.AutoreResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.entity.Autore;
import com.rhotechnologylibri.libreria_rhotechnology.entity.Nazionalita;
import com.rhotechnologylibri.libreria_rhotechnology.exception.ResourceNotFoundException;

@Service
public class AutoreService {

    private final AutoreRepository autoreRepository;
    private final AutoreMapper autoreMapper;
    private final NazionalitaRepository nazionalitaRepository;

    public AutoreService(AutoreRepository autoreRepository,
            AutoreMapper autoreMapper, NazionalitaRepository nazionalitaRepository) {
        this.autoreRepository = autoreRepository;
        this.autoreMapper = autoreMapper;
        this.nazionalitaRepository = nazionalitaRepository;
    }

    private Nazionalita resolveNazionalita(Integer idNazionalita) {
        if (idNazionalita == null) {
            return null; // relazione opzionale: lecito
        }
        return nazionalitaRepository.findById(idNazionalita)
                .orElseThrow(() -> new ResourceNotFoundException("Nazionalita", idNazionalita));
    }

    @Transactional(readOnly = true)
    public List<AutoreResponseDTO> findAll() {
        return autoreMapper.toResponseList(autoreRepository.findAll());
    }

    @Transactional(readOnly = true)
    public AutoreResponseDTO findById(Integer id) {
        Autore entity = autoreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autore", id));
        return autoreMapper.toResponse(entity);
    }

    @Transactional
    public AutoreResponseDTO create(AutoreRequestDTO request) {
        Autore entity = autoreMapper.toEntity(request);
        entity.setNazionalita(resolveNazionalita(request.getIdNazionalita()));
        Autore salvato = autoreRepository.save(entity);
        return autoreMapper.toResponse(salvato);
    }

    @Transactional
    public AutoreResponseDTO update(Integer id, AutoreRequestDTO request) {
        Autore entity = autoreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autore", id));
        autoreMapper.updateEntity(entity, request);
        entity.setNazionalita(resolveNazionalita(request.getIdNazionalita()));
        return autoreMapper.toResponse(autoreRepository.save(entity));
    }

    @Transactional
    public void delete(Integer id) {
        if (!autoreRepository.existsById(id)) {
            throw new ResourceNotFoundException("Autore", id);
        }
        autoreRepository.deleteById(id);
    }
}
