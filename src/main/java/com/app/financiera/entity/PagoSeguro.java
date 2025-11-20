package com.app.financiera.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad que representa un pago asociado a un seguro dentro del sistema financiero.
 * Contiene información sobre el seguro correspondiente, número de cuota, monto pagado,
 * fechas de pago y vencimiento, monto de la cuota, método de pago, estado y observaciones.
 */
@Getter
@Setter
@Entity
@Table(name = "pago_seguro")
public class PagoSeguro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private int idPago;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_seguro")
    private Seguro seguro;

    @Column(name = "numero_cuota")
    private Integer numeroCuota;

    @Column(name = "monto_pagado")
    private Double montoPagado;

    @Column(name = "fecha_pago")
    private LocalDateTime fechaPago;

    @Column(name = "fecha_vencimiento")
    private LocalDateTime fechaVencimiento;

    @Column(name = "monto_cuota")
    private Double montoCuota;

    @Column(name = "metodo_pago")
    private String metodoPago;

    private String estado; // "Pagado", "Pendiente", "Vencido", "Parcial", "Anulado"

    private String observaciones;
}