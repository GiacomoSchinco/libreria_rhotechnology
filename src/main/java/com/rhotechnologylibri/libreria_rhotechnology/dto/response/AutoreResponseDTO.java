package com.rhotechnologylibri.libreria_rhotechnology.dto.response;

import java.time.LocalDate;

public class AutoreResponseDTO {

    private Integer id;
    private String nome;
    private String cognome;
    private LocalDate data;
    private NazionalitaResponseDTO nazionalita;

    public AutoreResponseDTO() {
    }

    public AutoreResponseDTO(Integer id, String nome, String cognome, LocalDate data) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public NazionalitaResponseDTO getNazionalita() {
        return nazionalita;
    }

    public void setNazionalita(NazionalitaResponseDTO nazionalita) {
        this.nazionalita = nazionalita;
    }
}
