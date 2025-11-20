package com.app.financiera.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad que representa un trámite relacionado con un seguro dentro del sistema financiero.
 * Contiene información sobre el usuario solicitante, el seguro asociado, tipo de trámite,
 * fechas de solicitud y resolución, estado, prioridad, respuesta y documentos adjuntos.
 */
@Getter
@Setter
@Entity
@Table(name = "tramite_seguro")
public class TramiteSeguro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tramite")
    private int idTramite;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_seguro")
    private Seguro seguro;

    @Column(name = "tipo_tramite")
    private String tipoTramite; // "Reclamo", "Solicitud", "Consulta", "Renovación"

    private String descripcion;

    @Column(name = "fecha_solicitud")
    private LocalDateTime fechaSolicitud;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    private String estado; // "Pendiente", "En proceso", "Resuelto", "Rechazado"

    private String respuesta;

    @Column(name = "documentos_adjuntos", columnDefinition = "jsonb")
    private String documentosAdjuntos;

    private String prioridad; // "Alta", "Media", "Baja"

    @Column(name = "canal_atencion")
    private String canalAtencion; // "Web", "Teléfono", "Presencial", "Email"
}