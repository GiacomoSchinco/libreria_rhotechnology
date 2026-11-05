package com.rhotechnologylibri.libreria_rhotechnology.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "Generi")
public class Genere {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_genere", columnDefinition = "binary(16)", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 100, unique = true)
    private String nome;

    public Genere() {
    }

    public Genere(String nome) {
        this.nome = nome;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
