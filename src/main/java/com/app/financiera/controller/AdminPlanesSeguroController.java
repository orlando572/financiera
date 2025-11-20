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

/**
 * Controlador REST para la gestión de planes de seguro.
 * Proporciona endpoints para listar, registrar, actualizar, eliminar,
 * cambiar estado y obtener estadísticas de los planes registrados.
 */
@RestController
@RequestMapping("/api/admin/planes-seguro")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class AdminPlanesSeguroController {

    private static final Logger logger = LoggerFactory.getLogger(AdminPlanesSeguroController.class);

    @Autowired
    private AdminPlanesSeguroService service;

    /**
     * Obtiene la lista de planes de seguro según la categoría y el estado especificados.
     * Si no se indica ningún filtro, devuelve todos los planes registrados.
     *
     * @param categoria categoría opcional para filtrar los planes (por ejemplo, Vida, Salud, etc.)
     * @param estado estado opcional para filtrar los planes (por ejemplo, Activo o Inactivo)
     * @return una respuesta HTTP con la lista de planes o un mensaje de error en caso de fallo
     */
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

    /**
     * Recupera un plan de seguro específico a partir de su identificador único.
     *
     * @param id identificador numérico del plan de seguro a consultar
     * @return una respuesta HTTP con los datos del plan o un mensaje de error si no se encuentra
     */
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

    /**
     * Registra un nuevo plan de seguro en el sistema.
     *
     * @param plan objeto con los datos del plan de seguro a crear
     * @return una respuesta HTTP con el plan creado o un mensaje de error si ocurre un problema
     */
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

    /**
     * Actualiza los datos de un plan de seguro existente.
     *
     * @param id identificador numérico del plan de seguro a actualizar
     * @param plan objeto con los nuevos datos del plan
     * @return una respuesta HTTP con el plan actualizado o un mensaje de error si ocurre un fallo
     */
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

    /**
     * Elimina un plan de seguro del sistema.
     *
     * @param id identificador numérico del plan de seguro a eliminar
     * @return una respuesta HTTP con un mensaje indicando el resultado de la operación
     */
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

    /**
     * Cambia el estado actual de un plan de seguro (por ejemplo, de Activo a Inactivo o viceversa).
     *
     * @param id identificador numérico del plan de seguro a modificar
     * @param estado nuevo estado que se desea asignar al plan
     * @return una respuesta HTTP con el resultado de la operación
     */
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

    /**
     * Obtiene estadísticas generales sobre los planes de seguro registrados.
     *
     * @return una respuesta HTTP con un mapa de estadísticas o un mensaje de error si ocurre una excepción
     */
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
