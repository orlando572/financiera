package com.app.financiera.repository;

import com.app.financiera.entity.PagoSeguro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PagoSeguroRepository extends JpaRepository<PagoSeguro, Integer> {

    // Obtener pagos por seguro
    @Query("SELECT p FROM PagoSeguro p WHERE p.seguro.idSeguro = ?1 ORDER BY p.numeroCuota")
    List<PagoSeguro> findBySeguroId(int idSeguro);

    // Obtener pagos pendientes por usuario
    @Query("SELECT p FROM PagoSeguro p WHERE p.seguro.usuario.idUsuario = ?1 AND p.estado = 'Pendiente' ORDER BY p.numeroCuota")
    List<PagoSeguro> findPendientesByUsuario(int idUsuario);

    // Obtener último pago de un seguro
    @Query("SELECT p FROM PagoSeguro p WHERE p.seguro.idSeguro = ?1 ORDER BY p.numeroCuota DESC LIMIT 1")
    PagoSeguro findUltimoPagoBySeguro(int idSeguro);

    // Contar pagos pendientes por usuario
    @Query("SELECT COUNT(p) FROM PagoSeguro p WHERE p.seguro.usuario.idUsuario = ?1 AND p.estado = 'Pendiente'")
    long countPendientesByUsuario(int idUsuario);

    // Suma de montos pendientes por usuario
    @Query("SELECT COALESCE(SUM(p.montoCuota), 0) FROM PagoSeguro p WHERE p.seguro.usuario.idUsuario = ?1 AND p.estado = 'Pendiente'")
    Double sumMontosPendientesByUsuario(int idUsuario);

    // Obtener pagos vencidos (fecha vencimiento pasada y estado pendiente)
    @Query("SELECT p FROM PagoSeguro p WHERE p.seguro.usuario.idUsuario = ?1 AND p.estado = 'Pendiente' AND p.fechaVencimiento < CURRENT_TIMESTAMP ORDER BY p.fechaVencimiento")
    List<PagoSeguro> findVencidosByUsuario(int idUsuario);

    // Obtener pagos próximos a vencer (en los próximos N días)
    @Query("SELECT p FROM PagoSeguro p WHERE p.seguro.usuario.idUsuario = :idUsuario AND p.estado = 'Pendiente' AND p.fechaVencimiento BETWEEN CURRENT_TIMESTAMP AND :fechaLimite ORDER BY p.fechaVencimiento")
    List<PagoSeguro> findProximosVencerByUsuario(@Param("idUsuario") int idUsuario, @Param("fechaLimite") LocalDateTime fechaLimite);

    // Obtener todos los pagos pendientes y vencidos
    @Query("SELECT p FROM PagoSeguro p WHERE p.estado IN ('Pendiente', 'Vencido') ORDER BY p.fechaVencimiento")
    List<PagoSeguro> findAllPendientesYVencidos();

    // Contar pagos por seguro
    @Query("SELECT COUNT(p) FROM PagoSeguro p WHERE p.seguro.idSeguro = ?1")
    long countBySeguroId(int idSeguro);
}