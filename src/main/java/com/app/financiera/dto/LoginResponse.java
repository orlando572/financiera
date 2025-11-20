package com.app.financiera.dto;

import com.app.financiera.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO que representa la respuesta de una solicitud de inicio de sesión.
 * Contiene el token JWT generado, la información del usuario autenticado
 * y un mensaje de estado de la operación.
 */
@Getter
@Setter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Usuario usuario;
    private String mensaje;
}
