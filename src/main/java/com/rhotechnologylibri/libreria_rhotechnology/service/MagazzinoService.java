package com.rhotechnologylibri.libreria_rhotechnology.service;

import com.rhotechnologylibri.libreria_rhotechnology.dto.request.MagazzinoRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.MagazzinoResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.entity.CasaEditrice;
import com.rhotechnologylibri.libreria_rhotechnology.entity.Libro;
import com.rhotechnologylibri.libreria_rhotechnology.entity.Magazzino;
import com.rhotechnologylibri.libreria_rhotechnology.exception.BadRequestException;
import com.rhotechnologylibri.libreria_rhotechnology.exception.ResourceNotFoundException;
import com.rhotechnologylibri.libreria_rhotechnology.mapper.MagazzinoMapper;
import com.rhotechnologylibri.libreria_rhotechnology.repository.CasaEditriceRepository;
import com.rhotechnologylibri.libreria_rhotechnology.repository.LibroRepository;
import com.rhotechnologylibri.libreria_rhotechnology.repository.MagazzinoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MagazzinoService {

    private static final int QUANTITA_DEFAULT = 1;

    private final MagazzinoRepository magazzinoRepository;
    private final MagazzinoMapper magazzinoMapper;
    private final LibroRepository libroRepository;
    private final CasaEditriceRepository casaEditriceRepository;

    public MagazzinoService(MagazzinoRepository magazzinoRepository,
                            MagazzinoMapper magazzinoMapper,
                            LibroRepository libroRepository,
                            CasaEditriceRepository casaEditriceRepository) {
        this.magazzinoRepository = magazzinoRepository;
        this.magazzinoMapper = magazzinoMapper;
        this.libroRepository = libroRepository;
        this.casaEditriceRepository = casaEditriceRepository;
    }

    @Transactional(readOnly = true)
    public Page<MagazzinoResponseDTO> findAll(Pageable pageable) {
        return magazzinoRepository.findAll(pageable).map(magazzinoMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public MagazzinoResponseDTO findById(Integer id) {
        Magazzino entity = magazzinoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Magazzino", id));
        return magazzinoMapper.toResponse(entity);
    }

    // Se libro + casa editrice esistono gia', non crea una seconda riga: somma la quantita' a quella presente
    @Transactional
    public EsitoSalvataggio create(MagazzinoRequestDTO request) {
        Libro libro = resolveLibro(request.getIdLibro());
        CasaEditrice casaEditrice = resolveCasaEditrice(request.getIdCasaEditrice());

        Optional<Magazzino> esistente = findEsistente(libro.getId(), casaEditrice);
        if (esistente.isPresent()) {
            Magazzino riga = esistente.get();
            riga.setQuantita(riga.getQuantita() + quantitaRichiesta(request));
            return new EsitoSalvataggio(magazzinoMapper.toResponse(magazzinoRepository.save(riga)), false);
        }

        Magazzino entity = magazzinoMapper.toEntity(request);
        entity.setLibro(libro);
        entity.setCasaEditrice(casaEditrice);
        return new EsitoSalvataggio(magazzinoMapper.toResponse(magazzinoRepository.save(entity)), true);
    }

    @Transactional
    public MagazzinoResponseDTO update(Integer id, MagazzinoRequestDTO request) {
        Magazzino entity = magazzinoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Magazzino", id));
        Libro libro = resolveLibro(request.getIdLibro());
        CasaEditrice casaEditrice = resolveCasaEditrice(request.getIdCasaEditrice());

        // Se la modifica punta a libro + casa editrice gia' presenti su un'altra riga,
        // la riga duplicata viene assorbita invece di creare un doppione
        Optional<Magazzino> duplicata = findEsistente(libro.getId(), casaEditrice)
                .filter(altra -> !altra.getId().equals(id));

        int quantitaAssorbita = 0;
        if (duplicata.isPresent()) {
            quantitaAssorbita = duplicata.get().getQuantita();
            // Il DELETE va eseguito prima di spostare libro/casa editrice su entity,
            // altrimenti l'UPDATE violerebbe il vincolo UNIQUE
            magazzinoRepository.delete(duplicata.get());
            magazzinoRepository.flush();
        }

        magazzinoMapper.updateEntity(entity, request);
        entity.setLibro(libro);
        entity.setCasaEditrice(casaEditrice);
        entity.setQuantita(entity.getQuantita() + quantitaAssorbita);
        return magazzinoMapper.toResponse(magazzinoRepository.save(entity));
    }

    @Transactional
    public void delete(Integer id) {
        if (!magazzinoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Magazzino", id);
        }
        magazzinoRepository.deleteById(id);
    }

    // Cerca la riga con lo stesso libro e la stessa casa editrice (null incluso)
    private Optional<Magazzino> findEsistente(Integer idLibro, CasaEditrice casaEditrice) {
        if (casaEditrice == null) {
            return magazzinoRepository.findFirstByLibroIdAndCasaEditriceIsNull(idLibro);
        }
        return magazzinoRepository.findFirstByLibroIdAndCasaEditriceId(idLibro, casaEditrice.getId());
    }

    // quantita' assente nel DTO = un pezzo, coerente con il default dell'entity
    private int quantitaRichiesta(MagazzinoRequestDTO request) {
        return request.getQuantita() != null ? request.getQuantita() : QUANTITA_DEFAULT;
    }

    // Libro obbligatorio: la riga di magazzino non esiste senza libro
    private Libro resolveLibro(Integer idLibro) {
        if (idLibro == null) {
            throw new BadRequestException("idLibro e' obbligatorio");
        }
        return libroRepository.findById(idLibro)
                .orElseThrow(() -> new ResourceNotFoundException("Libro", idLibro));
    }

    // Casa editrice opzionale: null azzera la relazione
    private CasaEditrice resolveCasaEditrice(Integer idCasaEditrice) {
        if (idCasaEditrice == null) {
            return null;
        }
        return casaEditriceRepository.findById(idCasaEditrice)
                .orElseThrow(() -> new ResourceNotFoundException("CasaEditrice", idCasaEditrice));
    }

    // creato = true quando nasce una nuova riga (201), false quando si unisce a una esistente (200)
    public record EsitoSalvataggio(MagazzinoResponseDTO magazzino, boolean creato) {
    }
}
