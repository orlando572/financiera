package com.app.financiera.service;

import com.app.financiera.entity.AportePension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@Service
public class FinancieroServiceImpl implements FinancieroService {

    private static final Logger logger = LoggerFactory.getLogger(FinancieroServiceImpl.class);

    @Autowired
    private AportePensionService aportePensionService;

    @Autowired
    private SaldoPensionService saldoPensionService;

    @Autowired
    private HistorialConsultasService historialConsultasService;

    private static final int MESES_PROYECCION_PENSION = 240;

    @Override
    public HashMap<String, Object> obtenerResumenFinanciero(int idUsuario) {
        try {
            HashMap<String, Object> resumen = new HashMap<>();
            
            Double saldoTotal = saldoPensionService.obtenerSaldoTotalUsuario(idUsuario);
            Double saldoDisponible = saldoPensionService.obtenerSaldosDisponibles(idUsuario);
            
            int anioActual = Calendar.getInstance().get(Calendar.YEAR);
            Double aportesUltimoAnio = aportePensionService.obtenerTotalAportesUsuarioYear(idUsuario, anioActual);
            
            Double proyeccionMensual = calcularProyeccionPension(saldoTotal);
            
            HashMap<String, Double> aportesONPyAFP = calcularAportesPorSistema(idUsuario);
            
            resumen.put("saldoTotal", saldoTotal != null ? saldoTotal : 0.0);
            resumen.put("saldoDisponible", saldoDisponible != null ? saldoDisponible : 0.0);
            resumen.put("aportesUltimoAnio", aportesUltimoAnio != null ? aportesUltimoAnio : 0.0);
            resumen.put("proyeccionPensionMensual", proyeccionMensual);
            resumen.put("aportesONP12m", aportesONPyAFP.get("onp"));
            resumen.put("aportesAFP12m", aportesONPyAFP.get("afp"));
            
            // Registrar en historial
            logger.info("Registrando consulta de resumen financiero para usuario: {}", idUsuario);
            historialConsultasService.registrarConsulta(
                idUsuario,
                "Proyección",
                String.format("Consulta resumen financiero - Saldo: S/ %.2f, Proyección: S/ %.2f",
                    saldoTotal != null ? saldoTotal : 0.0, proyeccionMensual),
                "Exitoso",
                crearDetallesResumen(resumen)
            );
            logger.info("Consulta registrada exitosamente");
            
            return resumen;
        } catch (Exception e) {
            logger.error("Error al obtener resumen financiero: {}", e.getMessage(), e);
            try {
                historialConsultasService.registrarConsulta(
                    idUsuario,
                    "Proyección",
                    "Error al consultar resumen financiero",
                    "Error",
                    e.getMessage()
                );
            } catch (Exception ex) {
                logger.error("Error al registrar en historial: {}", ex.getMessage());
            }
            throw e;
        }
    }

    @Override
    public HashMap<String, Object> obtenerEstadisticasFinancieras(int idUsuario) {
        HashMap<String, Object> stats = new HashMap<>();
        
        List<HashMap<String, Object>> aportesPorAnio = obtenerAportesPorAnio(idUsuario, 3);
        Double saldoTotal = saldoPensionService.obtenerSaldoTotalUsuario(idUsuario);
        
        stats.put("aportesPorAnio", aportesPorAnio);
        stats.put("saldoTotal", saldoTotal != null ? saldoTotal : 0.0);
        stats.put("rentabilidadPromedio", 5.2);
        
        return stats;
    }

    @Override
    public HashMap<String, Object> obtenerComparativoSistemas(int idUsuario) {
        try {
            HashMap<String, Object> comparativo = new HashMap<>();
            HashMap<String, Double> montos = calcularAportesPorSistema(idUsuario);
            
            Double montoONP = montos.get("onp");
            Double montoAFP = montos.get("afp");
            Double total = montoONP + montoAFP;
            
            comparativo.put("onp", montoONP);
            comparativo.put("afp", montoAFP);
            comparativo.put("total", total);
            comparativo.put("porcentajeONP", total > 0 ? (montoONP / total) * 100 : 0.0);
            comparativo.put("porcentajeAFP", total > 0 ? (montoAFP / total) * 100 : 0.0);
            
            // Registrar en historial
            logger.info("Registrando consulta comparativa para usuario: {}", idUsuario);
            historialConsultasService.registrarConsulta(
                idUsuario,
                "Rentabilidad",
                String.format("Comparativo ONP vs AFP - ONP: S/ %.2f, AFP: S/ %.2f", montoONP, montoAFP),
                "Exitoso",
                crearDetallesComparativo(comparativo)
            );
            logger.info("Consulta comparativa registrada exitosamente");
            
            return comparativo;
        } catch (Exception e) {
            logger.error("Error al obtener comparativo: {}", e.getMessage(), e);
            try {
                historialConsultasService.registrarConsulta(
                    idUsuario,
                    "Rentabilidad",
                    "Error al consultar comparativo ONP vs AFP",
                    "Error",
                    e.getMessage()
                );
            } catch (Exception ex) {
                logger.error("Error al registrar en historial: {}", ex.getMessage());
            }
            throw e;
        }
    }

    @Override
    public HashMap<String, Object> obtenerSaldoTotalYDisponible(int idUsuario) {
        HashMap<String, Object> resultado = new HashMap<>();
        resultado.put("saldoTotal", saldoPensionService.obtenerSaldoTotalUsuario(idUsuario));
        resultado.put("saldoDisponible", saldoPensionService.obtenerSaldosDisponibles(idUsuario));
        return resultado;
    }

    @Override
    public List<HashMap<String, Object>> obtenerAportesPorAnio(int idUsuario, int cantidadAnios) {
        List<HashMap<String, Object>> aportesPorAnio = new ArrayList<>();
        int anioActual = Calendar.getInstance().get(Calendar.YEAR);
        
        for (int i = cantidadAnios - 1; i >= 0; i--) {
            int anio = anioActual - i;
            Double total = aportePensionService.obtenerTotalAportesUsuarioYear(idUsuario, anio);
            
            HashMap<String, Object> anioData = new HashMap<>();
            anioData.put("anio", anio);
            anioData.put("total", total != null ? total : 0.0);
            aportesPorAnio.add(anioData);
        }
        
        return aportesPorAnio;
    }

    private Double calcularProyeccionPension(Double saldoTotal) {
        return (saldoTotal != null && saldoTotal > 0) ? saldoTotal / MESES_PROYECCION_PENSION : 0.0;
    }

    private HashMap<String, Double> calcularAportesPorSistema(int idUsuario) {
        HashMap<String, Double> montos = new HashMap<>();
        
        List<AportePension> aportesONP = aportePensionService.obtenerAportesPorSistema(idUsuario, "Pensiones");
        Double montoONP = aportesONP.stream()
                .mapToDouble(a -> a.getMontoAporte() != null ? a.getMontoAporte() : 0.0)
                .sum();
        
        List<AportePension> aportesAFP = aportePensionService.obtenerAportesPorSistema(idUsuario, "Financiera");
        Double montoAFP = aportesAFP.stream()
                .mapToDouble(a -> a.getMontoAporte() != null ? a.getMontoAporte() : 0.0)
                .sum();
        
        montos.put("onp", montoONP);
        montos.put("afp", montoAFP);
        
        return montos;
    }

    private String crearDetallesResumen(HashMap<String, Object> resumen) {
        try {
            return String.format("{\"saldoTotal\":%.2f,\"saldoDisponible\":%.2f,\"proyeccion\":%.2f,\"aportesONP\":%.2f,\"aportesAFP\":%.2f}",
                    (Double) resumen.get("saldoTotal"),
                    (Double) resumen.get("saldoDisponible"),
                    (Double) resumen.get("proyeccionPensionMensual"),
                    (Double) resumen.get("aportesONP12m"),
                    (Double) resumen.get("aportesAFP12m"));
        } catch (Exception e) {
            logger.error("Error al crear detalles JSON: {}", e.getMessage());
            return "{}";
        }
    }

    private String crearDetallesComparativo(HashMap<String, Object> comparativo) {
        try {
            return String.format("{\"onp\":%.2f,\"afp\":%.2f,\"total\":%.2f,\"porcentajeONP\":%.2f,\"porcentajeAFP\":%.2f}",
                    (Double) comparativo.get("onp"),
                    (Double) comparativo.get("afp"),
                    (Double) comparativo.get("total"),
                    (Double) comparativo.get("porcentajeONP"),
                    (Double) comparativo.get("porcentajeAFP"));
        } catch (Exception e) {
            logger.error("Error al crear detalles JSON: {}", e.getMessage());
            return "{}";
        }
    }
}
