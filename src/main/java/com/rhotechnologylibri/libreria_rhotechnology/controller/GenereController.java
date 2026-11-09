package com.rhotechnologylibri.libreria_rhotechnology.controller;

import com.rhotechnologylibri.libreria_rhotechnology.service.GenereService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/generi")
public class GenereController {

    private final GenereService genereService;

    public GenereController(GenereService genereService) {
        this.genereService = genereService;
    }
}
