package com.rhotechnologylibri.libreria_rhotechnology.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Costruisce il {@link Pageable} dai parametri di query dichiarati esplicitamente nei
 * controller: cosi' in Swagger compaiono solo i campi compilabili, senza le lunghe
 * descrizioni che Spring genera per {@code Pageable}.
 */
public final class Paging {

    private static final int DEFAULT_SIZE = 10;
    private static final int MAX_SIZE = 100;

    private Paging() {
    }

    public static Pageable of(int page, int size, Sort sort) {
        return PageRequest.of(
                Math.max(page, 0),
                size <= 0 ? DEFAULT_SIZE : Math.min(size, MAX_SIZE),
                sort);
    }
}
