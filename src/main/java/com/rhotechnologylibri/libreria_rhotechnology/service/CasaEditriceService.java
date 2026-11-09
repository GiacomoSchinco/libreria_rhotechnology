package com.rhotechnologylibri.libreria_rhotechnology.service;

import com.rhotechnologylibri.libreria_rhotechnology.dto.request.CasaEditriceRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.CasaEditriceResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.entity.CasaEditrice;
import com.rhotechnologylibri.libreria_rhotechnology.exception.ResourceInUseException;
import com.rhotechnologylibri.libreria_rhotechnology.exception.ResourceNotFoundException;
import com.rhotechnologylibri.libreria_rhotechnology.mapper.CasaEditriceMapper;
import com.rhotechnologylibri.libreria_rhotechnology.repository.CasaEditriceRepository;
import com.rhotechnologylibri.libreria_rhotechnology.repository.MagazzinoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CasaEditriceService {

    private final CasaEditriceRepository casaEditriceRepository;
    private final CasaEditriceMapper casaEditriceMapper;
    private final MagazzinoRepository magazzinoRepository;

    public CasaEditriceService(CasaEditriceRepository casaEditriceRepository,
                               CasaEditriceMapper casaEditriceMapper,
                               MagazzinoRepository magazzinoRepository) {
        this.casaEditriceRepository = casaEditriceRepository;
        this.casaEditriceMapper = casaEditriceMapper;
        this.magazzinoRepository = magazzinoRepository;
    }

    @Transactional(readOnly = true)
    public List<CasaEditriceResponseDTO> findAll() {
        return casaEditriceMapper.toResponseList(casaEditriceRepository.findAll());
    }

    @Transactional(readOnly = true)
    public CasaEditriceResponseDTO findById(Integer id) {
        CasaEditrice entity = casaEditriceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CasaEditrice", id));
        return casaEditriceMapper.toResponse(entity);
    }

    @Transactional
    public CasaEditriceResponseDTO create(CasaEditriceRequestDTO request) {
        CasaEditrice salvata = casaEditriceRepository.save(casaEditriceMapper.toEntity(request));
        return casaEditriceMapper.toResponse(salvata);
    }

    @Transactional
    public CasaEditriceResponseDTO update(Integer id, CasaEditriceRequestDTO request) {
        CasaEditrice entity = casaEditriceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CasaEditrice", id));
        casaEditriceMapper.updateEntity(entity, request);
        return casaEditriceMapper.toResponse(casaEditriceRepository.save(entity));
    }

    @Transactional
    public void delete(Integer id) {
        if (!casaEditriceRepository.existsById(id)) {
            throw new ResourceNotFoundException("CasaEditrice", id);
        }
        if (magazzinoRepository.existsByCasaEditriceId(id)) {
            throw new ResourceInUseException("CasaEditrice", id, "almeno una riga di magazzino");
        }
        casaEditriceRepository.deleteById(id);
    }
}
