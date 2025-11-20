package com.app.financiera.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad que representa el historial de rentabilidad de un fondo de pensiones en un periodo específico.
 * Incluye información sobre la AFP, el tipo de fondo, rentabilidad nominal y real, patrimonio del fondo,
 * número de afiliados y fecha de registro de la información.
 */
@Getter
@Setter
@Entity
@Table(name = "historial_rentabilidad")
public class HistorialRentabilidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rentabilidad")
    private int idRentabilidad;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_afp")
    private Afp afp;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_fondo")
    private TipoFondo tipoFondo;

    private String periodo; // "YYYY-MM" formato año-mes

    @Column(name = "rentabilidad_nominal")
    private Double rentabilidadNominal; // Rentabilidad sin ajustar por inflación

    @Column(name = "rentabilidad_real")
    private Double rentabilidadReal; // Rentabilidad ajustada por inflación

    private Double patrimonio; // Patrimonio del fondo en el periodo

    @Column(name = "numero_afiliados")
    private Integer numeroAfiliados;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void onCreate() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
    }
}
