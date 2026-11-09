package com.rhotechnologylibri.libreria_rhotechnology.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.rhotechnologylibri.libreria_rhotechnology.dto.request.AutoreRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.AutoreResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.entity.Autore;

/**
 * Conversione Entity <-> DTO per Autore.
 */
@Component
public class AutoreMapper {
    private final NazionalitaMapper nazionalitaMapper;

    public AutoreMapper(NazionalitaMapper nazionalitaMapper) {
        this.nazionalitaMapper = nazionalitaMapper;
    }

    public AutoreResponseDTO toResponse(Autore entity) {
        if (entity == null) {
            return null;
        }
        AutoreResponseDTO dto = new AutoreResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getCognome(),
                entity.getDataNascita());
        dto.setNazionalita(nazionalitaMapper.toResponse(entity.getNazionalita()));
        return dto;
    }

    public List<AutoreResponseDTO> toResponseList(List<Autore> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }

    public Autore toEntity(AutoreRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Autore entity = new Autore();
        entity.setNome(dto.getNome());
        entity.setCognome(dto.getCognome());
        entity.setDataNascita(dto.getDataNascita());
        // Le relazioni vengono risolte nel service
        return entity;
    }

    public void updateEntity(Autore entity, AutoreRequestDTO dto) {
        entity.setNome(dto.getNome());
        entity.setCognome(dto.getCognome());
        entity.setDataNascita(dto.getDataNascita());
        // nazionalita: risolta dal service, come in create
    }
}
