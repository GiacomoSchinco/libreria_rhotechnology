package com.rhotechnologylibri.libreria_rhotechnology.dto.request;

public class CasaEditriceRequestDTO {

    private String nome;

    public CasaEditriceRequestDTO() {
    }

    public CasaEditriceRequestDTO(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
