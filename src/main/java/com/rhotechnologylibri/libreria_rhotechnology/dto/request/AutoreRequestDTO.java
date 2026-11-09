package com.rhotechnologylibri.libreria_rhotechnology.dto.request;

import java.time.LocalDate;

public class AutoreRequestDTO {

    private String nome;
    private String cognome;
    private LocalDate dataNascita;
    private Integer idNazionalita;

    public AutoreRequestDTO() {
    }

    public AutoreRequestDTO(String nome, String cognome, LocalDate dataNascita, Integer idNazionalita) {
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
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

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public Integer getIdNazionalita() {
        return idNazionalita;
    }

    public void setIdNazionalita(Integer idNazionalita) {
        this.idNazionalita = idNazionalita;
    }
}
