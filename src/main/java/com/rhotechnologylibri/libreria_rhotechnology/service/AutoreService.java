package com.rhotechnologylibri.libreria_rhotechnology.service;

import com.rhotechnologylibri.libreria_rhotechnology.mapper.AutoreMapper;
import com.rhotechnologylibri.libreria_rhotechnology.repository.AutoreRepository;
import com.rhotechnologylibri.libreria_rhotechnology.repository.NazionalitaRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.rhotechnologylibri.libreria_rhotechnology.dto.request.AutoreRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.AutoreResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.entity.Autore;
import com.rhotechnologylibri.libreria_rhotechnology.entity.Nazionalita;
import com.rhotechnologylibri.libreria_rhotechnology.exception.BadRequestException;
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

    /**
     * Risolve gli autori di un libro: quelli indicati per id (devono esistere) piu quelli
     * indicati per nome e cognome, riusati se gia presenti o creati altrimenti.
     */
    @Transactional
    public List<Autore> resolve(List<Integer> idAutori, List<AutoreRequestDTO> nuoviAutori) {
        Set<Integer> ids = idAutori == null ? new LinkedHashSet<>() : new LinkedHashSet<>(idAutori);

        Map<Integer, Autore> trovati = new LinkedHashMap<>();
        List<Autore> esistenti = autoreRepository.findAllById(ids);
        if (esistenti.size() != ids.size()) {
            List<Integer> mancanti = ids.stream()
                    .filter(id -> esistenti.stream().noneMatch(autore -> autore.getId().equals(id)))
                    .toList();
            throw new ResourceNotFoundException("Autore non esistenti: " + mancanti);
        }
        esistenti.forEach(autore -> trovati.put(autore.getId(), autore));

        if (nuoviAutori != null) {
            nuoviAutori.forEach(nuovo -> {
                Autore autore = findOrCreate(nuovo);
                trovati.put(autore.getId(), autore);
            });
        }
        return new ArrayList<>(trovati.values());
    }

    // Riusa l'autore omonimo (stesso nome e cognome) invece di duplicarlo: su autori non c'e' UNIQUE
    private Autore findOrCreate(AutoreRequestDTO request) {
        String nome = request.getNome() == null ? "" : request.getNome().trim();
        String cognome = request.getCognome() == null ? "" : request.getCognome().trim();
        if (nome.isEmpty() || cognome.isEmpty()) {
            throw new BadRequestException("nome e cognome dell'autore sono obbligatori");
        }
        return autoreRepository.findFirstByNomeIgnoreCaseAndCognomeIgnoreCase(nome, cognome)
                .orElseGet(() -> {
                    Autore entity = autoreMapper.toEntity(request);
                    entity.setNome(nome);
                    entity.setCognome(cognome);
                    entity.setNazionalita(resolveNazionalita(request.getIdNazionalita()));
                    return autoreRepository.save(entity);
                });
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
