package com.app.financiera.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/**
 * Entidad que representa el saldo de pensión de un usuario dentro del sistema financiero.
 * Contiene información sobre los saldos totales, disponibles y desglosados por tipo de fondo,
 * rentabilidad acumulada, fechas de corte y actualización, así como el estado del registro.
 */
@Getter
@Setter
@Entity
@Table(name = "saldo_pension")
public class SaldoPension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_saldo")
    private int idSaldo;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_afp", nullable = true)
    private Afp afp;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_fondo")
    private TipoFondo tipoFondo;

    @Column(name = "saldo_total")
    private Double saldoTotal;

    @Column(name = "saldo_disponible")
    private Double saldoDisponible;

    @Column(name = "saldo_cic")
    private Double saldoCIC;

    @Column(name = "saldo_cv")
    private Double saldoCV;

    @Column(name = "rentabilidad_acumulada")
    private Double rentabilidadAcumulada;

    @Column(name = "fecha_corte")
    private LocalDate fechaCorte;

    @Column(name = "fecha_actualizacion")
    private LocalDate fechaActualizacion;

    private String estado;
}