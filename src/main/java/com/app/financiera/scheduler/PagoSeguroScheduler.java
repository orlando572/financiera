package com.app.financiera.scheduler;

import com.app.financiera.entity.PagoSeguro;
import com.app.financiera.service.NotificacionService;
import com.app.financiera.service.PagoSeguroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PagoSeguroScheduler {

    private static final Logger logger = LoggerFactory.getLogger(PagoSeguroScheduler.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Autowired
    private PagoSeguroService pagoSeguroService;

    @Autowired
    private NotificacionService notificacionService;

    @Scheduled(cron = "0 0 2 * * *")
    public void actualizarPagosVencidos() {
        logger.info("=== INICIANDO ACTUALIZACIÓN DE PAGOS VENCIDOS ===");
        
        try {
            int pagosActualizados = pagoSeguroService.actualizarPagosVencidos();
            logger.info("Se actualizaron {} pagos a estado Vencido", pagosActualizados);
        } catch (Exception e) {
            logger.error("Error al actualizar pagos vencidos: {}", e.getMessage(), e);
        }
        
        logger.info("=== FINALIZADA ACTUALIZACIÓN DE PAGOS VENCIDOS ===");
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void enviarRecordatorios7Dias() {
        logger.info("=== INICIANDO ENVÍO DE RECORDATORIOS (7 DÍAS) ===");
        
        try {
            enviarRecordatoriosPorDias(7, "en 7 días");
        } catch (Exception e) {
            logger.error("Error al enviar recordatorios de 7 días: {}", e.getMessage(), e);
        }
        
        logger.info("=== FINALIZADO ENVÍO DE RECORDATORIOS (7 DÍAS) ===");
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void enviarRecordatorios3Dias() {
        logger.info("=== INICIANDO ENVÍO DE RECORDATORIOS (3 DÍAS) ===");
        
        try {
            enviarRecordatoriosPorDias(3, "en 3 días");
        } catch (Exception e) {
            logger.error("Error al enviar recordatorios de 3 días: {}", e.getMessage(), e);
        }
        
        logger.info("=== FINALIZADO ENVÍO DE RECORDATORIOS (3 DÍAS) ===");
    }

    @Scheduled(cron = "0 0 10 * * *")
    public void enviarRecordatoriosHoy() {
        logger.info("=== INICIANDO ENVÍO DE RECORDATORIOS (HOY) ===");
        
        try {
            enviarRecordatoriosPorDias(0, "hoy");
        } catch (Exception e) {
            logger.error("Error al enviar recordatorios de hoy: {}", e.getMessage(), e);
        }
        
        logger.info("=== FINALIZADO ENVÍO DE RECORDATORIOS (HOY) ===");
    }

    private void enviarRecordatoriosPorDias(int dias, String textoTiempo) {
        
        List<PagoSeguro> pagosProximos = pagoSeguroService.obtenerPagosProximosVencer(0, dias);
        
        logger.info("Encontrados {} pagos que vencen {}", pagosProximos.size(), textoTiempo);
        
        for (PagoSeguro pago : pagosProximos) {
            try {
                if (pago.getSeguro() != null && pago.getSeguro().getUsuario() != null) {
                    int idUsuario = pago.getSeguro().getUsuario().getIdUsuario();
                    String numeroPoliza = pago.getSeguro().getNumeroPoliza();
                    String tipoSeguro = pago.getSeguro().getTipoSeguro() != null ? 
                                       pago.getSeguro().getTipoSeguro().getNombre() : "Seguro";
                    Double monto = pago.getMontoCuota();
                    String fechaVencimiento = pago.getFechaVencimiento() != null ? 
                                             pago.getFechaVencimiento().format(formatter) : "";
                    
                    String concepto = String.format("Cuota %d de %s (Póliza %s)", 
                                                   pago.getNumeroCuota(), tipoSeguro, numeroPoliza);
                    
                    notificacionService.notificarPagoPendiente(idUsuario, concepto, monto);
                    
                    logger.info("Recordatorio enviado a usuario {} para pago que vence {}", 
                               idUsuario, textoTiempo);
                }
            } catch (Exception e) {
                logger.error("Error al enviar recordatorio para pago ID {}: {}", 
                           pago.getIdPago(), e.getMessage());
            }
        }
    }

    @Scheduled(cron = "0 0 3 * * MON")
    public void enviarResumenSemanal() {
        logger.info("=== INICIANDO ENVÍO DE RESUMEN SEMANAL ===");
        
        try {
            logger.info("Resumen semanal pendiente de implementación");
        } catch (Exception e) {
            logger.error("Error al enviar resumen semanal: {}", e.getMessage(), e);
        }
        
        logger.info("=== FINALIZADO ENVÍO DE RESUMEN SEMANAL ===");
    }
}
