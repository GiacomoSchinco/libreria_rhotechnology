package com.rhotechnologylibri.libreria_rhotechnology.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Credenziali in ingresso per /auth/register e /auth/login.
 * L'id non compare: lo genera il DB (AUTO_INCREMENT).
 */
public class AuthRequestDTO {

    @NotBlank(message = "username obbligatorio")
    @Size(max = 100, message = "username troppo lungo (max 100 caratteri)")
    private String username;

    // BCrypt ignora i byte oltre il 72esimo: meglio rifiutare che troncare in silenzio
    @NotBlank(message = "password obbligatoria")
    @Size(max = 72, message = "password troppo lunga (max 72 caratteri)")
    private String password;

    public AuthRequestDTO() {
    }

    public AuthRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
