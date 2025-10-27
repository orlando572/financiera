package com.app.financiera.controller;

import com.app.financiera.entity.TipoSeguro;
import com.app.financiera.service.AdminPlanesSeguroService;
import com.app.financiera.util.AppSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/admin/planes-seguro")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class AdminPlanesSeguroController {

    private static final Logger logger = LoggerFactory.getLogger(AdminPlanesSeguroController.class);

    @Autowired
    private AdminPlanesSeguroService service;

    // Listar todos los planes de seguro con filtros
    @GetMapping
    public ResponseEntity<?> listarPlanes(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String estado) {
        logger.info("Listando planes de seguro - Categoría: {}, Estado: {}", categoria, estado);
        try {
            List<TipoSeguro> planes = service.listarPlanes(categoria, estado);
            return ResponseEntity.ok(planes);
        } catch (Exception e) {
            logger.error("Error al listar planes: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error al listar planes de seguro");
        }
    }

    // Obtener un plan por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPlanPorId(@PathVariable int id) {
        logger.info("Obteniendo plan de seguro con ID: {}", id);
        try {
            TipoSeguro plan = service.obtenerPlanPorId(id);
            if (plan != null) {
                return ResponseEntity.ok(plan);
            } else {
                return ResponseEntity.status(404).body("Plan de seguro no encontrado");
            }
        } catch (Exception e) {
            logger.error("Error al obtener plan: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error al obtener plan de seguro");
        }
    }

    // Crear un nuevo plan de seguro
    @PostMapping
    public ResponseEntity<?> crearPlan(@RequestBody TipoSeguro plan) {
        logger.info("Creando nuevo plan de seguro: {}", plan.getNombre());
        HashMap<String, Object> response = new HashMap<>();
        try {
            TipoSeguro nuevoPlan = service.crearPlan(plan);
            response.put("mensaje", "Plan de seguro creado exitosamente");
            response.put("plan", nuevoPlan);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.warn("Error de validación al crear plan: {}", e.getMessage());
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(400).body(response);
        } catch (Exception e) {
            logger.error("Error al crear plan: {}", e.getMessage(), e);
            response.put("mensaje", "Error al crear plan de seguro");
            return ResponseEntity.status(500).body(response);
        }
    }

    // Actualizar un plan de seguro
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPlan(@PathVariable int id, @RequestBody TipoSeguro plan) {
        logger.info("Actualizando plan de seguro con ID: {}", id);
        HashMap<String, Object> response = new HashMap<>();
        try {
            plan.setIdTipoSeguro(id);
            TipoSeguro planActualizado = service.actualizarPlan(plan);
            response.put("mensaje", "Plan de seguro actualizado exitosamente");
            response.put("plan", planActualizado);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.warn("Error de validación al actualizar plan: {}", e.getMessage());
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(400).body(response);
        } catch (Exception e) {
            logger.error("Error al actualizar plan: {}", e.getMessage(), e);
            response.put("mensaje", "Error al actualizar plan de seguro");
            return ResponseEntity.status(500).body(response);
        }
    }

    // Eliminar un plan de seguro
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarPlan(@PathVariable int id) {
        logger.info("Eliminando plan de seguro con ID: {}", id);
        HashMap<String, Object> response = new HashMap<>();
        try {
            service.eliminarPlan(id);
            response.put("mensaje", "Plan de seguro eliminado exitosamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.warn("Error al eliminar plan: {}", e.getMessage());
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(400).body(response);
        } catch (Exception e) {
            logger.error("Error al eliminar plan: {}", e.getMessage(), e);
            response.put("mensaje", "Error al eliminar plan de seguro");
            return ResponseEntity.status(500).body(response);
        }
    }

    // Cambiar estado de un plan
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable int id, @RequestParam String estado) {
        logger.info("Cambiando estado del plan {} a: {}", id, estado);
        HashMap<String, Object> response = new HashMap<>();
        try {
            service.cambiarEstado(id, estado);
            response.put("mensaje", "Estado actualizado exitosamente");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error al cambiar estado: {}", e.getMessage(), e);
            response.put("mensaje", "Error al cambiar estado");
            return ResponseEntity.status(500).body(response);
        }
    }

    // Obtener estadísticas de planes
    @GetMapping("/estadisticas")
    public ResponseEntity<?> obtenerEstadisticas() {
        logger.info("Obteniendo estadísticas de planes de seguro");
        try {
            HashMap<String, Object> estadisticas = service.obtenerEstadisticas();
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            logger.error("Error al obtener estadísticas: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error al obtener estadísticas");
        }
    }
}
