package com.rhotechnologylibri.libreria_rhotechnology.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "nazionalita")
public class Nazionalita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100, unique = true)
    private String nome;

    public Nazionalita() {
    }

    public Nazionalita(String nome) {
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
