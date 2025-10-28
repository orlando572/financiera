package com.app.financiera.service;

import com.app.financiera.entity.Afp;
import com.app.financiera.entity.HistorialRentabilidad;
import com.app.financiera.entity.TipoFondo;
import com.app.financiera.repository.AfpRepository;
import com.app.financiera.repository.HistorialRentabilidadRepository;
import com.app.financiera.repository.TipoFondoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistorialRentabilidadServiceImpl implements HistorialRentabilidadService {

    private static final Logger logger = LoggerFactory.getLogger(HistorialRentabilidadServiceImpl.class);

    @Autowired
    private HistorialRentabilidadRepository historialRentabilidadRepository;

    @Autowired
    private AfpRepository afpRepository;

    @Autowired
    private TipoFondoRepository tipoFondoRepository;

    @Override
    public HistorialRentabilidad registrarRentabilidad(
            int idAfp,
            int idTipoFondo,
            String periodo,
            Double rentabilidadNominal,
            Double rentabilidadReal,
            Double patrimonio,
            Integer numeroAfiliados) {
        
        try {
            logger.info("Registrando rentabilidad para AFP: {}, Fondo: {}, Período: {}", 
                idAfp, idTipoFondo, periodo);

            HistorialRentabilidad historial = new HistorialRentabilidad();
            
            // Buscar AFP y TipoFondo
            Afp afp = afpRepository.findById(idAfp).orElse(null);
            TipoFondo tipoFondo = tipoFondoRepository.findById(idTipoFondo).orElse(null);
            
            if (afp == null || tipoFondo == null) {
                logger.error("AFP o TipoFondo no encontrado");
                return null;
            }
            
            historial.setAfp(afp);
            historial.setTipoFondo(tipoFondo);
            historial.setPeriodo(periodo);
            historial.setRentabilidadNominal(rentabilidadNominal);
            historial.setRentabilidadReal(rentabilidadReal);
            historial.setPatrimonio(patrimonio);
            historial.setNumeroAfiliados(numeroAfiliados);
            historial.setFechaRegistro(LocalDateTime.now());
            
            HistorialRentabilidad guardado = historialRentabilidadRepository.save(historial);
            logger.info("Rentabilidad registrada exitosamente con ID: {}", guardado.getIdRentabilidad());
            
            return guardado;
        } catch (Exception e) {
            logger.error("Error al registrar rentabilidad: {}", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<HistorialRentabilidad> obtenerHistorialPorAfpYFondo(int idAfp, int idTipoFondo) {
        try {
            return historialRentabilidadRepository.findByAfpAndTipoFondo(idAfp, idTipoFondo);
        } catch (Exception e) {
            logger.error("Error al obtener historial: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public HistorialRentabilidad obtenerUltimoRegistro(int idAfp, int idTipoFondo) {
        try {
            return historialRentabilidadRepository.findUltimoRegistro(idAfp, idTipoFondo).orElse(null);
        } catch (Exception e) {
            logger.error("Error al obtener último registro: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<HistorialRentabilidad> obtenerHistorialPorPeriodo(String periodo) {
        try {
            return historialRentabilidadRepository.findByPeriodo(periodo);
        } catch (Exception e) {
            logger.error("Error al obtener historial por período: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public List<HistorialRentabilidad> obtenerHistorialPorRangoFechas(
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin) {
        try {
            return historialRentabilidadRepository.findByFechaRegistroBetween(fechaInicio, fechaFin);
        } catch (Exception e) {
            logger.error("Error al obtener historial por rango de fechas: {}", e.getMessage());
            return List.of();
        }
    }

    @Override
    public Double calcularRentabilidadPromedio(int idAfp, int idTipoFondo) {
        try {
            Double promedio = historialRentabilidadRepository.calcularRentabilidadPromedio(idAfp, idTipoFondo);
            return promedio != null ? promedio : 0.0;
        } catch (Exception e) {
            logger.error("Error al calcular rentabilidad promedio: {}", e.getMessage());
            return 0.0;
        }
    }

    @Override
    public List<HistorialRentabilidad> compararAfpsPorPeriodo(String periodo, int idTipoFondo) {
        try {
            return historialRentabilidadRepository.compararAfpsPorPeriodo(periodo, idTipoFondo);
        } catch (Exception e) {
            logger.error("Error al comparar AFPs: {}", e.getMessage());
            return List.of();
        }
    }
}
