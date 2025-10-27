package com.app.financiera.service;

import com.app.financiera.entity.PagoSeguro;
import com.app.financiera.entity.Seguro;
import com.app.financiera.repository.PagoSeguroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PagoSeguroServiceImpl implements PagoSeguroService {

    private static final Logger logger = LoggerFactory.getLogger(PagoSeguroServiceImpl.class);

    @Autowired
    private PagoSeguroRepository pagoRepository;

    @Override
    @Transactional
    public List<PagoSeguro> generarPagosAutomaticos(Seguro seguro) {
        logger.info("Generando pagos automáticos para seguro ID: {}", seguro.getIdSeguro());

        List<PagoSeguro> pagos = new ArrayList<>();

        if (seguro.getFechaInicio() == null || seguro.getFechaVencimiento() == null) {
            logger.warn("Seguro sin fechas definidas, no se pueden generar pagos");
            return pagos;
        }

        LocalDate fechaInicio = seguro.getFechaInicio().toLocalDate();
        LocalDate fechaVencimiento = seguro.getFechaVencimiento().toLocalDate();
        String formaPago = seguro.getFormaPago() != null ? seguro.getFormaPago() : "Mensual";

        int numeroCuota = 1;
        LocalDate fechaPago = fechaInicio;

        // Determinar incremento según forma de pago
        int incrementoMeses = switch (formaPago) {
            case "Anual" -> 12;
            case "Semestral" -> 6;
            case "Trimestral" -> 3;
            case "Bimestral" -> 2;
            default -> 1; // Mensual
        };

        // Calcular monto de cuota
        Double montoCuota = calcularMontoCuota(seguro, formaPago);

        // Generar pagos hasta la fecha de vencimiento
        while (!fechaPago.isAfter(fechaVencimiento)) {
            PagoSeguro pago = new PagoSeguro();
            pago.setSeguro(seguro);
            pago.setNumeroCuota(numeroCuota);
            pago.setMontoCuota(montoCuota);
            pago.setFechaVencimiento(fechaPago.atTime(23, 59, 59));
            pago.setEstado("Pendiente");
            pago.setObservaciones("Cuota generada automáticamente");

            pagos.add(pagoRepository.save(pago));

            // Avanzar a la siguiente fecha de pago
            fechaPago = fechaPago.plusMonths(incrementoMeses);
            numeroCuota++;
        }

        logger.info("Se generaron {} pagos para el seguro ID: {}", pagos.size(), seguro.getIdSeguro());
        return pagos;
    }

    @Override
    @Transactional
    public int actualizarPagosVencidos() {
        logger.info("Actualizando estados de pagos vencidos");

        List<PagoSeguro> pagosVencidos = pagoRepository.findAllPendientesYVencidos();
        int contador = 0;

        LocalDateTime ahora = LocalDateTime.now();

        for (PagoSeguro pago : pagosVencidos) {
            if ("Pendiente".equals(pago.getEstado()) && 
                pago.getFechaVencimiento() != null && 
                pago.getFechaVencimiento().isBefore(ahora)) {
                
                pago.setEstado("Vencido");
                pagoRepository.save(pago);
                contador++;
            }
        }

        logger.info("Se actualizaron {} pagos a estado Vencido", contador);
        return contador;
    }

    @Override
    public List<PagoSeguro> obtenerPagosProximosVencer(int idUsuario, int dias) {
        LocalDateTime fechaLimite = LocalDateTime.now().plusDays(dias);
        return pagoRepository.findProximosVencerByUsuario(idUsuario, fechaLimite);
    }

    @Override
    public List<PagoSeguro> obtenerPagosVencidos(int idUsuario) {
        return pagoRepository.findVencidosByUsuario(idUsuario);
    }

    @Override
    @Transactional
    public PagoSeguro registrarPagoRealizado(int idPago, Double montoPagado, String metodoPago) {
        logger.info("Registrando pago realizado ID: {}", idPago);

        PagoSeguro pago = pagoRepository.findById(idPago)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        pago.setMontoPagado(montoPagado);
        pago.setMetodoPago(metodoPago);
        pago.setFechaPago(LocalDateTime.now());

        // Determinar estado según monto pagado
        if (montoPagado >= pago.getMontoCuota()) {
            pago.setEstado("Pagado");
        } else if (montoPagado > 0) {
            pago.setEstado("Parcial");
        }

        return pagoRepository.save(pago);
    }

    @Override
    @Transactional
    public void eliminarPagosDeSeguro(int idSeguro) {
        logger.info("Eliminando pagos del seguro ID: {}", idSeguro);
        List<PagoSeguro> pagos = pagoRepository.findBySeguroId(idSeguro);
        pagoRepository.deleteAll(pagos);
        logger.info("Se eliminaron {} pagos", pagos.size());
    }

    private Double calcularMontoCuota(Seguro seguro, String formaPago) {
        Double primaMensual = seguro.getPrimaMensual() != null ? seguro.getPrimaMensual() : 0.0;
        Double primaAnual = seguro.getPrimaAnual() != null ? seguro.getPrimaAnual() : 0.0;

        return switch (formaPago) {
            case "Anual" -> primaAnual > 0 ? primaAnual : primaMensual * 12;
            case "Semestral" -> primaMensual * 6;
            case "Trimestral" -> primaMensual * 3;
            case "Bimestral" -> primaMensual * 2;
            default -> primaMensual; // Mensual
        };
    }
}
