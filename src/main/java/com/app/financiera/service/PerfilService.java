package com.app.financiera.service;

import com.app.financiera.dto.PerfilUsuarioDTO;
import com.app.financiera.entity.Usuario;

public interface PerfilService {

    /**
     * Obtener perfil completo del usuario
     */
    PerfilUsuarioDTO obtenerPerfil(int idUsuario);

    /**
     * Actualizar información del perfil
     */
    Usuario actualizarPerfil(int idUsuario, PerfilUsuarioDTO perfilDTO);

    /**
     * Actualizar solo la foto de perfil
     */
    Usuario actualizarFotoPerfil(int idUsuario, String fotoPerfil);

    /**
     * Validar que el DNI no esté duplicado (excepto el usuario actual)
     */
    boolean validarDniUnico(String dni, int idUsuarioActual);

    /**
     * Validar que el correo no esté duplicado (excepto el usuario actual)
     */
    boolean validarCorreoUnico(String correo, int idUsuarioActual);
}