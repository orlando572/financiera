package com.app.financiera.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que representa a un administrador dentro del sistema financiero.
 * Un administrador está vinculado a un usuario base y posee información adicional
 * relacionada con sus permisos y funciones dentro del sistema.
 */
@Getter
@Setter
@Entity
@Table(name = "administrador")
public class Administrador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_admin")
    private int idAdmin;

    // Relación con Usuario
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "rol_detalle")
    private String rolDetalle;

    @Column(name = "permisos_sistema", columnDefinition = "TEXT")
    private String permisosSistema;

    private String estado;
}
