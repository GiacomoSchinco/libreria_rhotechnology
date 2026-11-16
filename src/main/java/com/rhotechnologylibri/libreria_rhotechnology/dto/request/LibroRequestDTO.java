package com.rhotechnologylibri.libreria_rhotechnology.dto.request;

import java.util.ArrayList;
import java.util.List;

public class LibroRequestDTO {

    private String titolo;
    private Integer anno;
    // Genere: idGenere per un genere esistente, nomeGenere per riusarlo o crearlo al volo.
    // Non si possono indicare entrambi; nessuno dei due = libro senza genere.
    private Integer idGenere;
    private String nomeGenere;
    // Autori: idAutori per autori esistenti, nuoviAutori per riusarli (per nome e cognome) o crearli.
    private List<Integer> idAutori = new ArrayList<>();
    private List<AutoreRequestDTO> nuoviAutori = new ArrayList<>();

    public LibroRequestDTO() {
    }

    public LibroRequestDTO(String titolo, Integer anno, Integer idGenere, List<Integer> idAutori) {
        this.titolo = titolo;
        this.anno = anno;
        this.idGenere = idGenere;
        this.idAutori = idAutori;
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

    public Integer getIdGenere() {
        return idGenere;
    }

    public void setIdGenere(Integer idGenere) {
        this.idGenere = idGenere;
    }

    public String getNomeGenere() {
        return nomeGenere;
    }

    public void setNomeGenere(String nomeGenere) {
        this.nomeGenere = nomeGenere;
    }

    public List<Integer> getIdAutori() {
        return idAutori;
    }

    public void setIdAutori(List<Integer> idAutori) {
        this.idAutori = idAutori;
    }

    public List<AutoreRequestDTO> getNuoviAutori() {
        return nuoviAutori;
    }

    public void setNuoviAutori(List<AutoreRequestDTO> nuoviAutori) {
        this.nuoviAutori = nuoviAutori;
    }
}
