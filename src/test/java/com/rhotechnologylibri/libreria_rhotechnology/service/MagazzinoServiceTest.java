package com.rhotechnologylibri.libreria_rhotechnology.service;

import com.rhotechnologylibri.libreria_rhotechnology.dto.request.MagazzinoRequestDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.MagazzinoResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.entity.CasaEditrice;
import com.rhotechnologylibri.libreria_rhotechnology.entity.Libro;
import com.rhotechnologylibri.libreria_rhotechnology.entity.Magazzino;
import com.rhotechnologylibri.libreria_rhotechnology.mapper.MagazzinoMapper;
import com.rhotechnologylibri.libreria_rhotechnology.repository.CasaEditriceRepository;
import com.rhotechnologylibri.libreria_rhotechnology.repository.LibroRepository;
import com.rhotechnologylibri.libreria_rhotechnology.repository.MagazzinoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MagazzinoServiceTest {

    private static final Integer ID_LIBRO = 3;
    private static final Integer ID_CASA_EDITRICE = 7;

    private MagazzinoRepository magazzinoRepository;
    private MagazzinoMapper magazzinoMapper;
    private MagazzinoService service;

    private Libro libro;
    private CasaEditrice casaEditrice;

    @BeforeEach
    void setUp() throws Exception {
        magazzinoRepository = mock(MagazzinoRepository.class);
        magazzinoMapper = mock(MagazzinoMapper.class);
        LibroRepository libroRepository = mock(LibroRepository.class);
        CasaEditriceRepository casaEditriceRepository = mock(CasaEditriceRepository.class);
        service = new MagazzinoService(magazzinoRepository, magazzinoMapper, libroRepository, casaEditriceRepository);

        libro = new Libro();
        setId(libro, ID_LIBRO);
        casaEditrice = new CasaEditrice("Einaudi");
        setId(casaEditrice, ID_CASA_EDITRICE);

        when(libroRepository.findById(ID_LIBRO)).thenReturn(Optional.of(libro));
        when(casaEditriceRepository.findById(ID_CASA_EDITRICE)).thenReturn(Optional.of(casaEditrice));
        when(magazzinoMapper.toResponse(any(Magazzino.class))).thenReturn(new MagazzinoResponseDTO());
        when(magazzinoRepository.save(any(Magazzino.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void createSommaLaQuantitaSeLaRigaEsisteGia() throws Exception {
        Magazzino esistente = new Magazzino();
        esistente.setLibro(libro);
        esistente.setCasaEditrice(casaEditrice);
        esistente.setQuantita(3);
        setId(esistente, 11);
        when(magazzinoRepository.findFirstByLibroIdAndCasaEditriceId(ID_LIBRO, ID_CASA_EDITRICE))
                .thenReturn(Optional.of(esistente));

        MagazzinoService.EsitoSalvataggio esito =
                service.create(new MagazzinoRequestDTO(ID_LIBRO, 2, ID_CASA_EDITRICE));

        assertFalse(esito.creato());
        ArgumentCaptor<Magazzino> captor = ArgumentCaptor.forClass(Magazzino.class);
        verify(magazzinoRepository).save(captor.capture());
        assertSame(esistente, captor.getValue());
        assertEquals(5, captor.getValue().getQuantita());
    }

    @Test
    void createCreaUnaNuovaRigaSeNonEsiste() {
        when(magazzinoRepository.findFirstByLibroIdAndCasaEditriceId(ID_LIBRO, ID_CASA_EDITRICE))
                .thenReturn(Optional.empty());
        when(magazzinoMapper.toEntity(any(MagazzinoRequestDTO.class))).thenReturn(new Magazzino());

        MagazzinoService.EsitoSalvataggio esito =
                service.create(new MagazzinoRequestDTO(ID_LIBRO, null, ID_CASA_EDITRICE));

        assertTrue(esito.creato());
        ArgumentCaptor<Magazzino> captor = ArgumentCaptor.forClass(Magazzino.class);
        verify(magazzinoRepository).save(captor.capture());
        assertEquals(1, captor.getValue().getQuantita());
        assertSame(libro, captor.getValue().getLibro());
        assertSame(casaEditrice, captor.getValue().getCasaEditrice());
    }

    @Test
    void updateAssorbeLaRigaDuplicataSommandoLeQuantita() throws Exception {
        Magazzino daModificare = new Magazzino();
        daModificare.setLibro(new Libro());
        daModificare.setQuantita(4);
        setId(daModificare, 1);
        when(magazzinoRepository.findById(1)).thenReturn(Optional.of(daModificare));

        Magazzino duplicata = new Magazzino();
        duplicata.setQuantita(6);
        setId(duplicata, 2);
        when(magazzinoRepository.findFirstByLibroIdAndCasaEditriceId(ID_LIBRO, ID_CASA_EDITRICE))
                .thenReturn(Optional.of(duplicata));
        stubUpdateEntityConQuantita();

        service.update(1, new MagazzinoRequestDTO(ID_LIBRO, 10, ID_CASA_EDITRICE));

        verify(magazzinoRepository).delete(duplicata);
        verify(magazzinoRepository).flush();
        ArgumentCaptor<Magazzino> captor = ArgumentCaptor.forClass(Magazzino.class);
        verify(magazzinoRepository).save(captor.capture());
        assertSame(daModificare, captor.getValue());
        assertEquals(16, captor.getValue().getQuantita());
        assertSame(libro, captor.getValue().getLibro());
    }

    @Test
    void updateNonAssorbeNullaSeLaRigaTrovataEEssaStessa() throws Exception {
        Magazzino daModificare = new Magazzino();
        daModificare.setQuantita(4);
        setId(daModificare, 1);
        when(magazzinoRepository.findById(1)).thenReturn(Optional.of(daModificare));
        when(magazzinoRepository.findFirstByLibroIdAndCasaEditriceId(ID_LIBRO, ID_CASA_EDITRICE))
                .thenReturn(Optional.of(daModificare));
        stubUpdateEntityConQuantita();

        service.update(1, new MagazzinoRequestDTO(ID_LIBRO, 10, ID_CASA_EDITRICE));

        verify(magazzinoRepository, never()).delete(any(Magazzino.class));
        verify(magazzinoRepository, never()).flush();
        assertEquals(10, daModificare.getQuantita());
    }

    // Riproduce il comportamento reale del mapper: aggiorna la quantita' solo se presente nel DTO
    private void stubUpdateEntityConQuantita() {
        doAnswer(invocation -> {
            Magazzino entity = invocation.getArgument(0);
            MagazzinoRequestDTO dto = invocation.getArgument(1);
            if (dto.getQuantita() != null) {
                entity.setQuantita(dto.getQuantita());
            }
            return null;
        }).when(magazzinoMapper).updateEntity(any(Magazzino.class), any(MagazzinoRequestDTO.class));
    }

    private static void setId(Object entity, Integer id) throws Exception {
        Field field = entity.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(entity, id);
    }
}
