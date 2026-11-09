package com.rhotechnologylibri.libreria_rhotechnology.dto.request;

import java.util.ArrayList;
import java.util.List;

public class LibroRequestDTO {

    private String titolo;
    private Integer anno;
    private Integer idGenere;
    private List<Integer> idAutori = new ArrayList<>();

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

    public List<Integer> getIdAutori() {
        return idAutori;
    }

    public void setIdAutori(List<Integer> idAutori) {
        this.idAutori = idAutori;
    }
}
