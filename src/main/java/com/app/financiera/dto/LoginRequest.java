package com.app.financiera.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO que representa la solicitud de inicio de sesión.
 * Contiene las credenciales del usuario necesarias para autenticarlo.
 */
@Getter
@Setter
public class LoginRequest {
    private String dni;
    private String claveSol;
}
