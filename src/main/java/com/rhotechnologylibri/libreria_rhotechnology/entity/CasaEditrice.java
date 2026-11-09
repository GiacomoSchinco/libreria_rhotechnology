package com.rhotechnologylibri.libreria_rhotechnology.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "case_editrici")
public class CasaEditrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150, unique = true)
    private String nome;

    public CasaEditrice() {
    }

    public CasaEditrice(String nome) {
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
