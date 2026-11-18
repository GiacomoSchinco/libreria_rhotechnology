package com.rhotechnologylibri.libreria_rhotechnology.pagination;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

class PagingTest {

    private static final Sort SORT = Sort.by("titolo");

    @Test
    void usesRequestedPageAndSizeWithFixedSort() {
        Pageable pageable = Paging.of(2, 5, SORT);

        assertThat(pageable.getPageNumber()).isEqualTo(2);
        assertThat(pageable.getPageSize()).isEqualTo(5);
        assertThat(pageable.getSort().getOrderFor("titolo").isAscending()).isTrue();
    }

    @Test
    void startsFromFirstPageAndDefaultSizeWhenParamsAreMissingOrInvalid() {
        assertThat(Paging.of(-3, 500, SORT).getPageNumber()).isZero();
        assertThat(Paging.of(0, 500, SORT).getPageSize()).isEqualTo(100);
        assertThat(Paging.of(0, 0, SORT).getPageSize()).isEqualTo(10);
    }
}
