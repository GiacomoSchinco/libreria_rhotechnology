package com.rhotechnologylibri.libreria_rhotechnology.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "Magazzino")
public class Magazzino {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_copia", columnDefinition = "binary(16)", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_libro", nullable = false)
    private Libro libro;

    @Column(nullable = false)
    private Integer quantita = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_casa_editrice")
    private CasaEditrice casaEditrice;

    public Magazzino() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    public CasaEditrice getCasaEditrice() {
        return casaEditrice;
    }

    public void setCasaEditrice(CasaEditrice casaEditrice) {
        this.casaEditrice = casaEditrice;
    }

}