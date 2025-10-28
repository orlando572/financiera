package com.app.financiera.service;

import com.app.financiera.entity.TipoSeguro;
import com.app.financiera.repository.TipoSeguroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminPlanesSeguroServiceImpl implements AdminPlanesSeguroService {

    private static final Logger logger = LoggerFactory.getLogger(AdminPlanesSeguroServiceImpl.class);

    @Autowired
    private TipoSeguroRepository repository;

    @Override
    public List<TipoSeguro> listarPlanes(String categoria, String estado) {
        logger.info("Listando planes - Categoría: {}, Estado: {}", categoria, estado);
        
        List<TipoSeguro> planes = repository.findAll();
        
        // Filtrar por categoría si se proporciona
        if (categoria != null && !categoria.isEmpty()) {
            planes = planes.stream()
                    .filter(p -> categoria.equals(p.getCategoria()))
                    .collect(Collectors.toList());
        }
        
        // Filtrar por estado si se proporciona
        if (estado != null && !estado.isEmpty()) {
            planes = planes.stream()
                    .filter(p -> estado.equals(p.getEstado()))
                    .collect(Collectors.toList());
        }
        
        return planes;
    }

    @Override
    public TipoSeguro obtenerPlanPorId(int id) {
        logger.info("Obteniendo plan con ID: {}", id);
        Optional<TipoSeguro> plan = repository.findById(id);
        return plan.orElse(null);
    }

    @Override
    @Transactional
    public TipoSeguro crearPlan(TipoSeguro plan) {
        logger.info("Creando nuevo plan: {}", plan.getNombre());
        
        // Validaciones
        if (plan.getNombre() == null || plan.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del plan es obligatorio");
        }
        
        if (plan.getCategoria() == null || plan.getCategoria().trim().isEmpty()) {
            throw new RuntimeException("La categoría es obligatoria");
        }
        
        // Establecer estado por defecto si no se proporciona
        if (plan.getEstado() == null || plan.getEstado().trim().isEmpty()) {
            plan.setEstado("Activo");
        }
        
        return repository.save(plan);
    }

    @Override
    @Transactional
    public TipoSeguro actualizarPlan(TipoSeguro plan) {
        logger.info("Actualizando plan con ID: {}", plan.getIdTipoSeguro());
        
        // Verificar que el plan existe
        Optional<TipoSeguro> planExistente = repository.findById(plan.getIdTipoSeguro());
        if (!planExistente.isPresent()) {
            throw new RuntimeException("Plan de seguro no encontrado");
        }
        
        // Validaciones
        if (plan.getNombre() == null || plan.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del plan es obligatorio");
        }
        
        if (plan.getCategoria() == null || plan.getCategoria().trim().isEmpty()) {
            throw new RuntimeException("La categoría es obligatoria");
        }
        
        return repository.save(plan);
    }

    @Override
    @Transactional
    public void eliminarPlan(int id) {
        logger.info("Eliminando plan con ID: {}", id);
        
        Optional<TipoSeguro> plan = repository.findById(id);
        if (!plan.isPresent()) {
            throw new RuntimeException("Plan de seguro no encontrado");
        }
        
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void cambiarEstado(int id, String estado) {
        logger.info("Cambiando estado del plan {} a: {}", id, estado);
        
        Optional<TipoSeguro> planOpt = repository.findById(id);
        if (!planOpt.isPresent()) {
            throw new RuntimeException("Plan de seguro no encontrado");
        }
        
        TipoSeguro plan = planOpt.get();
        plan.setEstado(estado);
        repository.save(plan);
    }

    @Override
    public HashMap<String, Object> obtenerEstadisticas() {
        logger.info("Obteniendo estadísticas de planes de seguro");
        
        HashMap<String, Object> estadisticas = new HashMap<>();
        
        List<TipoSeguro> todosPlanes = repository.findAll();
        
        // Total de planes
        estadisticas.put("total", todosPlanes.size());
        
        // Planes activos
        long activos = todosPlanes.stream()
                .filter(p -> "Activo".equals(p.getEstado()))
                .count();
        estadisticas.put("activos", activos);
        
        // Planes inactivos
        long inactivos = todosPlanes.stream()
                .filter(p -> "Inactivo".equals(p.getEstado()))
                .count();
        estadisticas.put("inactivos", inactivos);
        
        // Planes por categoría
        HashMap<String, Long> porCategoria = new HashMap<>();
        porCategoria.put("Vehicular", todosPlanes.stream()
                .filter(p -> "Vehicular".equals(p.getCategoria()))
                .count());
        porCategoria.put("Hogar", todosPlanes.stream()
                .filter(p -> "Hogar".equals(p.getCategoria()))
                .count());
        porCategoria.put("Salud", todosPlanes.stream()
                .filter(p -> "Salud".equals(p.getCategoria()))
                .count());
        porCategoria.put("Vida", todosPlanes.stream()
                .filter(p -> "Vida".equals(p.getCategoria()))
                .count());
        
        estadisticas.put("porCategoria", porCategoria);
        
        return estadisticas;
    }
}
