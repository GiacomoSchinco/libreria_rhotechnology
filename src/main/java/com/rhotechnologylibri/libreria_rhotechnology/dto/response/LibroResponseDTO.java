package com.rhotechnologylibri.libreria_rhotechnology.dto.response;

import java.util.ArrayList;
import java.util.List;

public class LibroResponseDTO {

    private Integer id;
    private String titolo;
    private Integer anno;
    private GenereResponseDTO genere;
    private List<AutoreResponseDTO> autori = new ArrayList<>();

    public LibroResponseDTO() {
    }

    public LibroResponseDTO(Integer id, String titolo, Integer anno) {
        this.id = id;
        this.titolo = titolo;
        this.anno = anno;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public GenereResponseDTO getGenere() {
        return genere;
    }

    public void setGenere(GenereResponseDTO genere) {
        this.genere = genere;
    }

    public List<AutoreResponseDTO> getAutori() {
        return autori;
    }

    public void setAutori(List<AutoreResponseDTO> autori) {
        this.autori = autori;
    }
}
