package com.app.financiera.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad que representa una consulta de aportes de un usuario dentro del sistema financiero.
 * Contiene información sobre el tipo de consulta realizada, los parámetros enviados,
 * el resultado obtenido, y metadatos como la fecha, el estado de éxito y la IP de acceso.
 */
@Getter
@Setter
@Entity
@Table(name = "consulta_aportes")
public class ConsultaAportes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consulta")
    private int idConsulta;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "tipo_consulta")
    private String tipoConsulta; // "SaldoActual", "HistorialAportes", "Rentabilidad", "Proyección"

    @Column(name = "parametros", columnDefinition = "JSON")
    private String parametros; // JSON con parámetros de la consulta

    @Column(name = "fecha_consulta")
    private LocalDateTime fechaConsulta;

    @Column(name = "resultado", columnDefinition = "JSON")
    private String resultado; // JSON con resultado de la consulta

    private boolean exitoso;

    @Column(name = "mensaje_error")
    private String mensajeError;

    @Column(name = "ip_acceso")
    private String ipAcceso;
}