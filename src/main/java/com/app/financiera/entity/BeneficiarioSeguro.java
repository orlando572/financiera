package com.app.financiera.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entidad que representa a un beneficiario asociado a un seguro dentro del sistema financiero.
 * Contiene los datos personales, el vínculo con el asegurado, el porcentaje de participación,
 * y su estado actual en relación al seguro.
 */
@Getter
@Setter
@Entity
@Table(name = "beneficiario_seguro")
public class BeneficiarioSeguro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_beneficiario")
    private int idBeneficiario;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_seguro")
    private Seguro seguro;

    @Column(name = "nombre_completo")
    private String nombreCompleto;

    private String parentesco;

    private Double porcentaje;

    private String dni;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    private String telefono;

    private String estado;
}
