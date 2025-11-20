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

/**
 * Controlador REST para la gestión de compañías de seguro.
 * Proporciona endpoints para listar, registrar, actualizar, eliminar,
 * cambiar estado y obtener estadísticas de las compañías registradas.
 */
@RestController
@RequestMapping("/api/admin/companias-seguro")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class AdminCompaniasSeguroController {

    private static final Logger logger = LoggerFactory.getLogger(AdminCompaniasSeguroController.class);

    @Autowired
    private AdminCompaniasSeguroService service;

    /**
     * Obtiene la lista de compañías de seguro según el estado especificado.
     * Si no se indica un estado, devuelve todas las compañías registradas.
     *
     * @param estado estado opcional para filtrar las compañías (por ejemplo, Activo o Inactivo)
     * @return una respuesta HTTP con la lista de compañías o un mensaje de error en caso de fallo
     */
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

    /**
     * Recupera una compañía de seguro específica a partir de su identificador único.
     *
     * @param id identificador numérico de la compañía a consultar
     * @return una respuesta HTTP con los datos de la compañía o un mensaje de error si no se encuentra
     */
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

    /**
     * Registra una nueva compañía de seguro en el sistema.
     *
     * @param compania objeto con los datos de la compañía a crear
     * @return una respuesta HTTP con la compañía registrada o un mensaje de error si ocurre un problema
     */
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

    /**
     * Actualiza los datos de una compañía de seguro existente.
     *
     * @param id identificador numérico de la compañía a actualizar
     * @param compania objeto con los nuevos datos de la compañía
     * @return una respuesta HTTP con la compañía actualizada o un mensaje de error si ocurre un fallo
     */
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

    /**
     * Elimina una compañía de seguro del sistema.
     *
     * @param id identificador numérico de la compañía a eliminar
     * @return una respuesta HTTP con un mensaje indicando el resultado de la operación
     */
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

    /**
     * Cambia el estado actual de una compañía de seguro (por ejemplo, de Activo a Inactivo o viceversa).
     *
     * @param id identificador numérico de la compañía a modificar
     * @param estado nuevo estado que se desea asignar a la compañía
     * @return una respuesta HTTP con el resultado de la operación
     */
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

    /**
     * Obtiene estadísticas generales sobre las compañías de seguro registradas.
     *
     * @return una respuesta HTTP con un mapa de estadísticas o un mensaje de error si ocurre una excepción
     */
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
