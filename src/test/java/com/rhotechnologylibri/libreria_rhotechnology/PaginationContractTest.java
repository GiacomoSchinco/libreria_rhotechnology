package com.rhotechnologylibri.libreria_rhotechnology;

import com.rhotechnologylibri.libreria_rhotechnology.config.SpringSecurityConfig;
import com.rhotechnologylibri.libreria_rhotechnology.controller.LibroController;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.LibroResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.dto.response.GenereResponseDTO;
import com.rhotechnologylibri.libreria_rhotechnology.service.LibroService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LibroController.class)
@Import(SpringSecurityConfig.class)
@ImportAutoConfiguration(classes = { SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class,
        ServletWebSecurityAutoConfiguration.class })
class PaginationContractTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    LibroService libroService;

    @Test
    void listIsSerializedAsPageWithDefaultSize10() throws Exception {
        LibroResponseDTO libro = new LibroResponseDTO(1, "Il nome della rosa", 1980);
        libro.setGenere(new GenereResponseDTO(1, "Romanzo"));
        Page<LibroResponseDTO> page = new PageImpl<>(List.of(libro), PageRequest.of(0, 10), 42);
        given(libroService.findAll(any())).willReturn(page);

        mockMvc.perform(get("/api/libri"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].titolo").value("Il nome della rosa"))
                .andExpect(jsonPath("$.page.totalElements").value(42))
                .andExpect(jsonPath("$.page.totalPages").value(5))
                .andExpect(jsonPath("$.page.size").value(10))
                .andExpect(jsonPath("$.page.number").value(0));

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        org.mockito.Mockito.verify(libroService).findAll(captor.capture());
        assertThat(captor.getValue().getPageSize()).isEqualTo(10);
        assertThat(captor.getValue().getSort().getOrderFor("titolo")).isNotNull();
    }

    @Test
    void pageAndSizeComeFromQueryParams() throws Exception {
        Page<LibroResponseDTO> page = new PageImpl<>(List.of(), PageRequest.of(1, 5), 0);
        given(libroService.findAll(any())).willReturn(page);

        mockMvc.perform(get("/api/libri").param("page", "1").param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.page.size").value(5))
                .andExpect(jsonPath("$.page.number").value(1));

        ArgumentCaptor<Pageable> captor = ArgumentCaptor.forClass(Pageable.class);
        org.mockito.Mockito.verify(libroService).findAll(captor.capture());
        assertThat(captor.getValue().getPageNumber()).isEqualTo(1);
        assertThat(captor.getValue().getPageSize()).isEqualTo(5);
        // l'ordinamento e' fisso lato server, non guidato dal client
        assertThat(captor.getValue().getSort().getOrderFor("titolo")).isNotNull();
    }
}
