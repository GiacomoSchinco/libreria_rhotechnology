package com.rhotechnologylibri.libreria_rhotechnology.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.rhotechnologylibri.libreria_rhotechnology.dto.request.MagazzinoRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.MagazzinoResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.entity.Magazzino;

/**
 * Conversione Entity <-> DTO per Magazzino.
 */
@Component
public class MagazzinoMapper {
    private final LibroMapper libroMapper;
    private final CasaEditriceMapper casaEditriceMapper;

    public MagazzinoMapper(LibroMapper libroMapper, CasaEditriceMapper casaEditriceMapper) {
        this.libroMapper = libroMapper;
        this.casaEditriceMapper = casaEditriceMapper;
    }

    // Entity -> Response (per GET / POST / PUT)
    public MagazzinoResponseDTO toResponse(Magazzino entity) {
        if (entity == null) {
            return null;
        }
        MagazzinoResponseDTO dto = new MagazzinoResponseDTO(
                entity.getId(),
                entity.getQuantita());
        dto.setLibro(libroMapper.toResponse(entity.getLibro()));
        dto.setCasaEditrice(casaEditriceMapper.toResponse(entity.getCasaEditrice()));
        return dto;
    }

    // Entity -> Response per le liste
    public List<MagazzinoResponseDTO> toResponseList(List<Magazzino> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }

    // Request -> Entity (solo campi scalari: libro e casaEditrice li risolve il service)
    public Magazzino toEntity(MagazzinoRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Magazzino entity = new Magazzino();
        setQuantitaIfPresent(entity, dto);
        return entity;
    }

    // Request -> Entity esistente (per la PUT: l'id arriva dal path e non va toccato)
    public void updateEntity(Magazzino entity, MagazzinoRequestDTO dto) {
        setQuantitaIfPresent(entity, dto);
    }

    // quantita e' NOT NULL sull'entity (default 1): null nel DTO significa "lascia il valore attuale"
    private void setQuantitaIfPresent(Magazzino entity, MagazzinoRequestDTO dto) {
        if (dto.getQuantita() != null) {
            entity.setQuantita(dto.getQuantita());
        }
    }
}
