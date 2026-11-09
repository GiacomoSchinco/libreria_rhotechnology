package com.rhotechnologylibri.libreria_rhotechnology.dto.request;

public class GenereRequestDTO {

    private String nome;

    public GenereRequestDTO() {
    }

    public GenereRequestDTO(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
