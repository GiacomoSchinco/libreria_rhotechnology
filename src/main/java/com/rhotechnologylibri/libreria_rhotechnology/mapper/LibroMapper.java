package com.rhotechnologylibri.libreria_rhotechnology.mapper;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.rhotechnologylibri.libreria_rhotechnology.dto.request.LibroRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.LibroResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.entity.Autore;
import com.rhotechnologylibri.libreria_rhotechnology.entity.Libro;

/**
 * Conversione Entity <-> DTO per Libro.
 */
@Component
public class LibroMapper {
    private final AutoreMapper autoreMapper;
    private final GenereMapper genereMapper;

    public LibroMapper(AutoreMapper autoreMapper, GenereMapper genereMapper) {
        this.autoreMapper = autoreMapper;
        this.genereMapper = genereMapper;
    }

    public LibroResponseDTO toResponse(Libro entity) {
        if (entity == null) {
            return null;
        }
        LibroResponseDTO dto = new LibroResponseDTO(
                entity.getId(),
                entity.getTitolo(),
                entity.getAnno());
        dto.setGenere(genereMapper.toResponse(entity.getGenere()));
        dto.setAutori(entity.getAutori().stream()
                .sorted(Comparator.comparing(Autore::getCognome)
                        .thenComparing(Autore::getNome))
                .map(autoreMapper::toResponse)
                .toList());
        return dto;
    }

    public List<LibroResponseDTO> toResponseList(List<Libro> entities) {
        if (entities == null) {
            return List.of();
        }
        return entities.stream()
                .map(this::toResponse)
                .toList();
    }

    public Libro toEntity(LibroRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Libro entity = new Libro();
        entity.setTitolo(dto.getTitolo());
        entity.setAnno(dto.getAnno());
        return entity;
    }

    public void updateEntity(Libro entity, LibroRequestDTO dto) {
        entity.setTitolo(dto.getTitolo());
        entity.setAnno(dto.getAnno());
    }

}
