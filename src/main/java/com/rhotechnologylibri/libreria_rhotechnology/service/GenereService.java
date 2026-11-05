package com.rhotechnologylibri.libreria_rhotechnology.service;

import com.rhotechnologylibri.libreria_rhotechnology.entity.Genere;
import com.rhotechnologylibri.libreria_rhotechnology.repository.GenereRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenereService {

    private final GenereRepository genereRepository;

    public GenereService(GenereRepository genereRepository) {
        this.genereRepository = genereRepository;
    }

    public List<Genere> getAll() {
        return genereRepository.findAll();
    }

    public Genere getById(Integer id) {
        return genereRepository.findById(id).orElse(null);
    }

    public Genere create(String nomeGenere) {
        Genere genere = new Genere();
        genere.setNome(nomeGenere);
        return genereRepository.save(genere);
    }

    public Genere update(Integer id, String nomeGenere) {
        Genere genere = genereRepository.findById(id).orElse(null);
        if (genere != null) {
            genere.setNome(nomeGenere);
            return genereRepository.save(genere);
        }
        return null;
    }

    public boolean delete(Integer id) {
        if (genereRepository.existsById(id)) {
            genereRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
