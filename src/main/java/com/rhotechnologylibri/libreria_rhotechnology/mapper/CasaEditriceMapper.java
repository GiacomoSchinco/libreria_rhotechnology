package com.rhotechnologylibri.libreria_rhotechnology.mapper;

import com.rhotechnologylibri.libreria_rhotechnology.dto.request.CasaEditriceRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.CasaEditriceResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.entity.CasaEditrice;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Conversione Entity <-> DTO per CasaEditrice.
 * Stessa struttura di GenereMapper.
 */
@Component
public class CasaEditriceMapper {

    // Entity -> Response (per GET / POST / PUT)
    public CasaEditriceResponseDTO toResponse(CasaEditrice entity) {
        if (entity == null) {
            return null;
        }
        return new CasaEditriceResponseDTO(entity.getId(), entity.getNome());
    }

    // Entity -> Response per le liste
    public List<CasaEditriceResponseDTO> toResponseList(List<CasaEditrice> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }

    // Request -> Entity (solo campi scalari: le relazioni le risolve il service)
    public CasaEditrice toEntity(CasaEditriceRequestDTO request) {
        if (request == null) {
            return null;
        }
        CasaEditrice entity = new CasaEditrice();
        entity.setNome(request.getNome());
        return entity;
    }

    // Request -> Entity esistente (per la PUT: l'id arriva dal path e non va toccato)
    public void updateEntity(CasaEditrice entity, CasaEditriceRequestDTO request) {
        entity.setNome(request.getNome());
    }
}
