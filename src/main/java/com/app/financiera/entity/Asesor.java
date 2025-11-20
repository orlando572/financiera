package com.app.financiera.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidad que representa a un asesor financiero dentro del sistema.
 * Contiene información sobre su usuario asociado, especialidad,
 * nivel de calificación, certificaciones, disponibilidad y canales de atención.
 */
@Getter
@Setter
@Entity
@Table(name = "asesor")
public class Asesor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asesor")
    private int idAsesor;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    private String especialidad; // "Pensiones", "Seguros", "Inversiones", "General"

    @Column(name = "codigo_asesor", unique = true)
    private String codigoAsesor;

    private String estado; // "Activo", "Inactivo", "Ocupado", "Disponible"

    @Column(name = "calificacion_promedio")
    private Double calificacionPromedio;

    @Column(name = "total_asesoramientos")
    private Integer totalAsesoramientos;

    @Column(name = "horario_atencion")
    private String horarioAtencion; // JSON con horarios disponibles

    @Column(name = "canales_atencion")
    private String canalesAtencion; // "Presencial,Telefónico,Virtual,Chat"

    @Column(name = "certificaciones")
    private String certificaciones; // JSON con certificaciones profesionales
}
