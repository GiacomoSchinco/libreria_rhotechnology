package com.rhotechnologylibri.libreria_rhotechnology.dto.request;

public class NazionalitaRequestDTO {

    private String nome;

    public NazionalitaRequestDTO() {
    }

    public NazionalitaRequestDTO(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
