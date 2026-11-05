package com.rhotechnologylibri.libreria_rhotechnology.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "Libri")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_libro", columnDefinition = "binary(16)", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String titolo;

    private Integer anno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_genere")
    private Genere genere;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "Libri_Autori",
            joinColumns = @JoinColumn(name = "id_libro"),
            inverseJoinColumns = @JoinColumn(name = "id_autore")
    )
    private Set<Autore> autori = new HashSet<>();

    public Libro() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        this.anno = anno;
    }

    public Genere getGenere() {
        return genere;
    }

    public void setGenere(Genere genere) {
        this.genere = genere;
    }

    public Set<Autore> getAutori() {
        return autori;
    }

    public void setAutori(Set<Autore> autori) {
        this.autori = autori;
    }

    public void addAutore(Autore autore) {
        autori.add(autore);
        autore.getLibri().add(this);
    }

    public void removeAutore(Autore autore) {
        autori.remove(autore);
        autore.getLibri().remove(this);
    }

}
