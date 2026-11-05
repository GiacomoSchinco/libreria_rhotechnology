package com.rhotechnologylibri.libreria_rhotechnology.service;

import com.rhotechnologylibri.libreria_rhotechnology.entity.Nazionalita;
import com.rhotechnologylibri.libreria_rhotechnology.repository.NazionalitaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NazionalitaService {

    private final NazionalitaRepository nazionalitaRepository;

    public NazionalitaService(NazionalitaRepository nazionalitaRepository) {
        this.nazionalitaRepository = nazionalitaRepository;
    }

    public List<Nazionalita> getAll() {
        return nazionalitaRepository.findAll();
    }

    public Nazionalita getById(Integer id) {
        return nazionalitaRepository.findById(id).orElse(null);
    }

    public Nazionalita create(String nomeNazionalita) {
        Nazionalita nazionalita = new Nazionalita();
        nazionalita.setNome(nomeNazionalita);
        return nazionalitaRepository.save(nazionalita);
    }

    public Nazionalita update(Integer id, String nomeNazionalita) {
        Nazionalita nazionalita = nazionalitaRepository.findById(id).orElse(null);
        if (nazionalita != null) {
            nazionalita.setNome(nomeNazionalita);
            return nazionalitaRepository.save(nazionalita);
        }
        return null;
    }

    public boolean delete(Integer id) {
        if (nazionalitaRepository.existsById(id)) {
            nazionalitaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
