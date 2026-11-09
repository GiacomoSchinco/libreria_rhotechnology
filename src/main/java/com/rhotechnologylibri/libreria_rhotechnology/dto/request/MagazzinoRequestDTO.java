package com.rhotechnologylibri.libreria_rhotechnology.dto.request;

public class MagazzinoRequestDTO {

    private Integer idLibro;
    private Integer quantita;
    private Integer idCasaEditrice;

    public MagazzinoRequestDTO() {
    }

    public MagazzinoRequestDTO(Integer idLibro, Integer quantita, Integer idCasaEditrice) {
        this.idLibro = idLibro;
        this.quantita = quantita;
        this.idCasaEditrice = idCasaEditrice;
    }

    public Integer getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Integer idLibro) {
        this.idLibro = idLibro;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    public Integer getIdCasaEditrice() {
        return idCasaEditrice;
    }

    public void setIdCasaEditrice(Integer idCasaEditrice) {
        this.idCasaEditrice = idCasaEditrice;
    }
}
