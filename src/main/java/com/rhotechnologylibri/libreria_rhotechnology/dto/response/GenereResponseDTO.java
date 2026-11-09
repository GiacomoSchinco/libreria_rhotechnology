package com.rhotechnologylibri.libreria_rhotechnology.dto.response;

public class GenereResponseDTO {

    private Integer id;
    private String nome;

    public GenereResponseDTO() {
    }

    public GenereResponseDTO(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
