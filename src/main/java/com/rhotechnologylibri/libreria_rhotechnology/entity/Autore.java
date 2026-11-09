package com.rhotechnologylibri.libreria_rhotechnology.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "autori")
public class Autore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100)
    private String cognome;

    @Column(name = "data_nascita")
    private LocalDate dataNascita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_nazionalita")
    private Nazionalita nazionalita;

    @ManyToMany(mappedBy = "autori", fetch = FetchType.LAZY)
    private Set<Libro> libri = new HashSet<>();

    public Autore() {
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public Nazionalita getNazionalita() {
        return nazionalita;
    }

    public void setNazionalita(Nazionalita nazionalita) {
        this.nazionalita = nazionalita;
    }

    public Set<Libro> getLibri() {
        return libri;
    }

    public void setLibri(Set<Libro> libri) {
        this.libri = libri;
    }

}
