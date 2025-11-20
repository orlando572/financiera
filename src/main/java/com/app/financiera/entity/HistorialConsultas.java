package com.app.financiera.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad que representa el historial de consultas realizadas por un usuario dentro del sistema financiero.
 * Registra la fecha de la consulta, el tipo de consulta, el detalle de la misma,
 * el resultado obtenido y cualquier información adicional relevante.
 */
@Getter
@Setter
@Entity
@Table(name = "historial_consultas")
public class HistorialConsultas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private int idHistorial;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "detalle_consulta")
    private String detalleConsulta;

    @Column(name = "tipo_consulta")
    private String tipoConsulta; // "Aporte", "Seguro", "Rentabilidad", "Proyección"

    private String resultado; // "Exitoso", "Error"

    @Column(name = "detalles_adicionales", columnDefinition = "TEXT")
    private String detallesAdicionales; // JSON con información adicional
}