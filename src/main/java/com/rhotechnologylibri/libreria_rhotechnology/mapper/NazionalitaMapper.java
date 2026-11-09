package com.rhotechnologylibri.libreria_rhotechnology.mapper;

import com.rhotechnologylibri.libreria_rhotechnology.dto.request.NazionalitaRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.NazionalitaResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.entity.Nazionalita;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Conversione Entity <-> DTO per Nazionalita.
 * Modello da copiare per gli altri mapper.
 */
@Component
public class NazionalitaMapper {

    // Entity -> Response (per GET / POST / PUT)
    public NazionalitaResponseDTO toResponse(Nazionalita entity) {
        if (entity == null) {
            return null;
        }
        return new NazionalitaResponseDTO(entity.getId(), entity.getNome());
    }

    // Entity -> Response per le liste
    public List<NazionalitaResponseDTO> toResponseList(List<Nazionalita> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }

    // Request -> Entity (solo campi scalari: le relazioni le risolve il service)
    public Nazionalita toEntity(NazionalitaRequestDTO request) {
        if (request == null) {
            return null;
        }
        Nazionalita entity = new Nazionalita();
        entity.setNome(request.getNome());
        return entity;
    }

    // Request -> Entity esistente (per la PUT: l'id arriva dal path e non va toccato)
    public void updateEntity(Nazionalita entity, NazionalitaRequestDTO request) {
        entity.setNome(request.getNome());
    }
}
