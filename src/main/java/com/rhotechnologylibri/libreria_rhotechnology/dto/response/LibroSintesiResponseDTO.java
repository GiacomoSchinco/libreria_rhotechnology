package com.rhotechnologylibri.libreria_rhotechnology.dto.response;

/**
 * Risposta ridotta per la ricerca libri: solo id e titolo.
 */
public class LibroSintesiResponseDTO {

    private Integer id;
    private String titolo;

    public LibroSintesiResponseDTO() {
    }

    public LibroSintesiResponseDTO(Integer id, String titolo) {
        this.id = id;
        this.titolo = titolo;
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
}
