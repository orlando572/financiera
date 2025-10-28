package com.app.financiera.controller;

import com.app.financiera.entity.CompaniaSeguro;
import com.app.financiera.service.AdminCompaniasSeguroService;
import com.app.financiera.util.AppSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/admin/companias-seguro")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class AdminCompaniasSeguroController {

    private static final Logger logger = LoggerFactory.getLogger(AdminCompaniasSeguroController.class);

    @Autowired
    private AdminCompaniasSeguroService service;

    // Listar todas las compañías con filtros
    @GetMapping
    public ResponseEntity<?> listarCompanias(@RequestParam(required = false) String estado) {
        logger.info("Listando compañías de seguro - Estado: {}", estado);
        try {
            List<CompaniaSeguro> companias = service.listarCompanias(estado);
            return ResponseEntity.ok(companias);
        } catch (Exception e) {
            logger.error("Error al listar compañías: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error al listar compañías de seguro");
        }
    }

    // Obtener una compañía por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCompaniaPorId(@PathVariable int id) {
        logger.info("Obteniendo compañía de seguro con ID: {}", id);
        try {
            CompaniaSeguro compania = service.obtenerCompaniaPorId(id);
            if (compania != null) {
                return ResponseEntity.ok(compania);
            } else {
                return ResponseEntity.status(404).body("Compañía de seguro no encontrada");
            }
        } catch (Exception e) {
            logger.error("Error al obtener compañía: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error al obtener compañía de seguro");
        }
    }

    // Crear una nueva compañía
    @PostMapping
    public ResponseEntity<?> crearCompania(@RequestBody CompaniaSeguro compania) {
        logger.info("Creando nueva compañía de seguro: {}", compania.getNombre());
        HashMap<String, Object> response = new HashMap<>();
        try {
            CompaniaSeguro nuevaCompania = service.crearCompania(compania);
            response.put("mensaje", "Compañía de seguro creada exitosamente");
            response.put("compania", nuevaCompania);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.warn("Error de validación al crear compañía: {}", e.getMessage());
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(400).body(response);
        } catch (Exception e) {
            logger.error("Error al crear compañía: {}", e.getMessage(), e);
            response.put("mensaje", "Error al crear compañía de seguro");
            return ResponseEntity.status(500).body(response);
        }
    }

    // Actualizar una compañía
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCompania(@PathVariable int id, @RequestBody CompaniaSeguro compania) {
        logger.info("Actualizando compañía de seguro con ID: {}", id);
        HashMap<String, Object> response = new HashMap<>();
        try {
            compania.setIdCompania(id);
            CompaniaSeguro companiaActualizada = service.actualizarCompania(compania);
            response.put("mensaje", "Compañía de seguro actualizada exitosamente");
            response.put("compania", companiaActualizada);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.warn("Error de validación al actualizar compañía: {}", e.getMessage());
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(400).body(response);
        } catch (Exception e) {
            logger.error("Error al actualizar compañía: {}", e.getMessage(), e);
            response.put("mensaje", "Error al actualizar compañía de seguro");
            return ResponseEntity.status(500).body(response);
        }
    }

    // Eliminar una compañía
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCompania(@PathVariable int id) {
        logger.info("Eliminando compañía de seguro con ID: {}", id);
        HashMap<String, Object> response = new HashMap<>();
        try {
            service.eliminarCompania(id);
            response.put("mensaje", "Compañía de seguro eliminada exitosamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.warn("Error al eliminar compañía: {}", e.getMessage());
            response.put("mensaje", e.getMessage());
            return ResponseEntity.status(400).body(response);
        } catch (Exception e) {
            logger.error("Error al eliminar compañía: {}", e.getMessage(), e);
            response.put("mensaje", "Error al eliminar compañía de seguro");
            return ResponseEntity.status(500).body(response);
        }
    }

    // Cambiar estado de una compañía
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable int id, @RequestParam String estado) {
        logger.info("Cambiando estado de la compañía {} a: {}", id, estado);
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

    // Obtener estadísticas de compañías
    @GetMapping("/estadisticas")
    public ResponseEntity<?> obtenerEstadisticas() {
        logger.info("Obteniendo estadísticas de compañías de seguro");
        try {
            HashMap<String, Object> estadisticas = service.obtenerEstadisticas();
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            logger.error("Error al obtener estadísticas: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error al obtener estadísticas");
        }
    }
}
