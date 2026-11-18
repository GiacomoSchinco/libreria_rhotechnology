package com.rhotechnologylibri.libreria_rhotechnology;

import com.rhotechnologylibri.libreria_rhotechnology.config.SpringSecurityConfig;
import com.rhotechnologylibri.libreria_rhotechnology.controller.LibroController;
import com.rhotechnologylibri.libreria_rhotechnology.service.LibroService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.ServletWebSecurityAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifica il CORS con la security attiva: e' il caso in cui le preflight OPTIONS
 * vengono bloccate se il CORS non e' dichiarato nel filter chain.
 */
@WebMvcTest(controllers = LibroController.class, properties = "app.security.enabled=true")
@Import(SpringSecurityConfig.class)
@ImportAutoConfiguration(classes = { SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class,
        ServletWebSecurityAutoConfiguration.class })
class CorsContractTest {

    private static final String ANGULAR_DEV_SERVER = "http://localhost:4200";

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    LibroService libroService;

    @Test
    void preflightFromAngularDevServerIsAllowed() throws Exception {
        mockMvc.perform(options("/api/libri")
                        .header(HttpHeaders.ORIGIN, ANGULAR_DEV_SERVER)
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, ANGULAR_DEV_SERVER))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true"));
    }

    @Test
    void preflightFromUnknownOriginIsRejected() throws Exception {
        mockMvc.perform(options("/api/libri")
                        .header(HttpHeaders.ORIGIN, "http://evil.example")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET"))
                .andExpect(status().isForbidden());
    }

    @Test
    void responseCarriesCorsHeaderForAllowedOrigin() throws Exception {
        mockMvc.perform(get("/api/libri").header(HttpHeaders.ORIGIN, ANGULAR_DEV_SERVER))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, ANGULAR_DEV_SERVER));
    }
}
