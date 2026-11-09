package com.rhotechnologylibri.libreria_rhotechnology.mapper;

import com.rhotechnologylibri.libreria_rhotechnology.dto.request.GenereRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.GenereResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.entity.Genere;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Conversione Entity <-> DTO per Genere.
 * Stessa struttura di NazionalitaMapper.
 */
@Component
public class GenereMapper {

    // Entity -> Response (per GET / POST / PUT)
    public GenereResponseDTO toResponse(Genere entity) {
        if (entity == null) {
            return null;
        }
        return new GenereResponseDTO(entity.getId(), entity.getNome());
    }

    // Entity -> Response per le liste
    public List<GenereResponseDTO> toResponseList(List<Genere> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }

    // Request -> Entity (solo campi scalari: le relazioni le risolve il service)
    public Genere toEntity(GenereRequestDTO request) {
        if (request == null) {
            return null;
        }
        Genere entity = new Genere();
        entity.setNome(request.getNome());
        return entity;
    }

    // Request -> Entity esistente (per la PUT: l'id arriva dal path e non va toccato)
    public void updateEntity(Genere entity, GenereRequestDTO request) {
        entity.setNome(request.getNome());
    }
}
