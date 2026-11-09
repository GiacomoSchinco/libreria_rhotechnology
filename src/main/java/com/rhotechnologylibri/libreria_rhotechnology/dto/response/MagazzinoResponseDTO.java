package com.rhotechnologylibri.libreria_rhotechnology.dto.response;

public class MagazzinoResponseDTO {

    private Integer id;
    private LibroResponseDTO libro;
    private Integer quantita;
    private CasaEditriceResponseDTO casaEditrice;

    public MagazzinoResponseDTO() {
    }

    public MagazzinoResponseDTO(Integer id, Integer quantita) {
        this.id = id;
        this.quantita = quantita;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LibroResponseDTO getLibro() {
        return libro;
    }

    public void setLibro(LibroResponseDTO libro) {
        this.libro = libro;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    public CasaEditriceResponseDTO getCasaEditrice() {
        return casaEditrice;
    }

    public void setCasaEditrice(CasaEditriceResponseDTO casaEditrice) {
        this.casaEditrice = casaEditrice;
    }
}
