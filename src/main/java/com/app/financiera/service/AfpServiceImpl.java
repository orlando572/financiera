package com.app.financiera.service;

import com.app.financiera.entity.Afp;
import com.app.financiera.repository.AfpRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class AfpServiceImpl implements AfpService {

    private static final Logger logger = LoggerFactory.getLogger(AfpServiceImpl.class);

    @Autowired
    private AfpRepository afpRepository;

    @Autowired
    private HistorialRentabilidadService historialRentabilidadService;

    @Override
    public List<Afp> listarAfps() {
        return afpRepository.findAll();
    }

    @Override
    public Afp buscarPorId(int id) {
        Optional<Afp> afp = afpRepository.findById(id);
        return afp.orElse(null);
    }

    @Override
    public Afp buscarPorNombreExacto(String nombre) {
        return afpRepository.findByNombre(nombre);
    }

    @Override
    public List<Afp> buscarPorNombre(String busqueda) {
        return afpRepository.findByNombreContaining(busqueda);
    }

    @Override
    public Afp buscarPorCodigoSbs(String codigoSbs) {
        return afpRepository.findByCodigoSbs(codigoSbs);
    }

    @Override
    public List<Afp> buscarPorEstado(String estado) {
        return afpRepository.findByEstado(estado);
    }

    @Override
    public Afp guardarAfp(Afp afp) {
        return afpRepository.save(afp);
    }

    @Override
    public Afp actualizarAfp(Afp afp) {
        try {
            logger.info("=== INICIO AfpServiceImpl.actualizarAfp ===");
            logger.info("AFP ID: {}, Nombre: {}", afp.getIdAfp(), afp.getNombre());
            logger.info("Nueva rentabilidad: {}", afp.getRentabilidadPromedio());
            
            // Obtener AFP anterior y guardar la rentabilidad ANTES de actualizar
            Afp afpAnterior = afpRepository.findById(afp.getIdAfp()).orElse(null);
            Double rentabilidadAnterior = null;
            
            if (afpAnterior != null) {
                rentabilidadAnterior = afpAnterior.getRentabilidadPromedio();
                logger.info("Rentabilidad anterior: {}", rentabilidadAnterior);
            } else {
                logger.warn("No se encontró AFP anterior con ID: {}", afp.getIdAfp());
            }
            
            // Guardar AFP actualizada
            Afp afpActualizada = afpRepository.save(afp);
            logger.info("AFP guardada exitosamente");
            
            // Verificar si cambió la rentabilidad comparando con el valor guardado
            boolean rentabilidadCambio = rentabilidadAnterior != null && 
                afpActualizada.getRentabilidadPromedio() != null &&
                !rentabilidadAnterior.equals(afpActualizada.getRentabilidadPromedio());
            
            logger.info("¿Rentabilidad cambió?: {} (Anterior: {}, Nueva: {})", 
                rentabilidadCambio, rentabilidadAnterior, afpActualizada.getRentabilidadPromedio());
            
            // Si cambió la rentabilidad, registrar en historial
            if (rentabilidadCambio) {
                logger.info(">>> REGISTRANDO EN HISTORIAL <<<");
                logger.info("Rentabilidad actualizada para AFP: {}, registrando en historial", afp.getNombre());
                
                String periodo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
                logger.info("Período: {}", periodo);
                
                historialRentabilidadService.registrarRentabilidad(
                    afpActualizada.getIdAfp(),
                    1, // ID tipo de fondo
                    periodo,
                    afpActualizada.getRentabilidadPromedio(), // rentabilidad nominal
                    afpActualizada.getRentabilidadPromedio(), // rentabilidad real
                    null, // patrimonio
                    null  // número de afiliados
                );
                
                logger.info(">>> HISTORIAL REGISTRADO EXITOSAMENTE <<<");
            } else {
                logger.info("No se registra en historial porque la rentabilidad no cambió");
            }
            
            logger.info("=== FIN AfpServiceImpl.actualizarAfp ===");
            return afpActualizada;
        } catch (Exception e) {
            logger.error("ERROR en actualizarAfp: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void eliminarAfp(int id) {
        afpRepository.deleteById(id);
    }

    @Override
    public java.util.HashMap<String, Object> obtenerEstadisticas() {
        java.util.HashMap<String, Object> stats = new java.util.HashMap<>();

        List<Afp> todas = listarAfps();
        long activas = todas.stream()
                .filter(a -> "Activo".equals(a.getEstado()))
                .count();
        long inactivas = todas.stream()
                .filter(a -> "Inactivo".equals(a.getEstado()))
                .count();

        stats.put("total", todas.size());
        stats.put("activas", activas);
        stats.put("inactivas", inactivas);

        return stats;
    }
}