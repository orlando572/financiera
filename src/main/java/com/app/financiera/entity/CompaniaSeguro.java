package com.app.financiera.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Entidad que representa a una compañía de seguros dentro del sistema financiero.
 * Contiene información sobre la institución asociada, los tipos de seguros que ofrece,
 * su calificación de riesgo y datos de contacto.
 */
@Getter
@Setter
@Entity
@Table(name = "compania_seguro")
public class CompaniaSeguro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compania")
    private int idCompania;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_institucion", nullable = true)
    private Institucion institucion;

    private String nombre;

    @Column(name = "codigo_sbs")
    private String codigoSbs;

    @Column(name = "tipos_seguro_ofrecidos", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String tiposSeguroOfrecidos;

    @Column(name = "calificacion_riesgo")
    private String calificacionRiesgo;

    private String telefono;

    private String email;

    @Column(name = "sitio_web")
    private String sitioWeb;

    private String estado;
}