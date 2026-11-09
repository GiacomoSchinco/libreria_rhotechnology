package com.rhotechnologylibri.libreria_rhotechnology.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "generi")
public class Genere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100, unique = true)
    private String nome;

    public Genere() {
    }

    public Genere(String nome) {
        this.nome = nome;
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
}
