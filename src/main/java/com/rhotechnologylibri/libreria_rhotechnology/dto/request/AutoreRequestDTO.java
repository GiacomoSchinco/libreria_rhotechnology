package com.rhotechnologylibri.libreria_rhotechnology.dto.request;

import java.time.LocalDate;

public class AutoreRequestDTO {

    private String nome;
    private String cognome;
    private LocalDate data;
    private Integer idNazionalita;

    public AutoreRequestDTO() {
    }

    public AutoreRequestDTO(String nome, String cognome, LocalDate data, Integer idNazionalita) {
        this.nome = nome;
        this.cognome = cognome;
        this.data = data;
        this.idNazionalita = idNazionalita;
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

    public Integer getIdNazionalita() {
        return idNazionalita;
    }

    public void setIdNazionalita(Integer idNazionalita) {
        this.idNazionalita = idNazionalita;
    }
}
