package com.app.financiera.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que representa un rol de usuario dentro del sistema.
 * Contiene información sobre el nombre del rol, descripción, permisos asociados (en formato JSON)
 * y el estado del rol (activo, inactivo, etc.).
 */
@Getter
@Setter
@Entity
@Table(name = "rol_usuario")
public class RolUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private int idRol;

    @Column(name = "nombre_rol")
    private String nombreRol;

    private String descripcion;

    // Campo tipo JSON
    @Column(columnDefinition = "JSON")
    private String permisos;

    private String estado;
}
