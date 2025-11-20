package com.app.financiera.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidad que representa un registro de asesoramiento financiero dentro del sistema.
 * Cada asesoramiento está asociado a un usuario, un asesor y opcionalmente a un aporte de pensión.
 * Incluye información sobre el tipo de asesoramiento, canal de atención, fechas, duración,
 * nivel de satisfacción del cliente, y observaciones relevantes.
 */
@Getter
@Setter
@Entity
@Table(name = "asesoramiento_financiero")
public class AsesoramientoFinanciero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asesoramiento")
    private int idAsesoramiento;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_asesor")
    private Asesor asesor;

    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aporte")
    private AportePension aporte;

    @Column(name = "tipo_asesoramiento")
    private String tipoAsesoramiento; // "Pensiones", "Seguros", "Inversiones", "Trámites", "Consulta General"

    @Column(name = "canal_atencion")
    private String canalAtencion; // "Presencial", "Telefónico", "Virtual", "Chat"

    @Column(name = "fecha_asesoramiento")
    private LocalDateTime fechaAsesoramiento;

    @Column(name = "seguimiento_requerido")
    private Boolean seguimientoRequerido;

    @Column(name = "fecha_seguimiento")
    private LocalDateTime fechaSeguimiento;

    @Column(name = "satisfaccion_cliente")
    private Integer satisfaccionCliente; // 1-5

    @Column(columnDefinition = "TEXT")
    private String comentarios;

    private String estado; // "Programado", "En Proceso", "Completado", "Cancelado"

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;

    @Column(name = "temas_tratados", columnDefinition = "TEXT")
    private String temasTratados; // JSON con lista de temas

    @Column(name = "recomendaciones", columnDefinition = "TEXT")
    private String recomendaciones;

    @PrePersist
    protected void onCreate() {
        if (fechaAsesoramiento == null) {
            fechaAsesoramiento = LocalDateTime.now();
        }
        if (estado == null) {
            estado = "Programado";
        }
        if (seguimientoRequerido == null) {
            seguimientoRequerido = false;
        }
    }
}
