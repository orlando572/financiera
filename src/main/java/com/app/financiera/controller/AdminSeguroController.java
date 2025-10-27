package com.app.financiera.controller;

import com.app.financiera.entity.BeneficiarioSeguro;
import com.app.financiera.entity.Seguro;
import com.app.financiera.service.SegurosService;
import com.app.financiera.util.AppSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/admin/seguros")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class AdminSeguroController {

    private static final Logger logger = LoggerFactory.getLogger(AdminSeguroController.class);

    @Autowired
    private SegurosService segurosService;

    @GetMapping
    public ResponseEntity<?> listarTodosSeguros(
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) String estado) {
        try {
            logger.info("Admin - Listando seguros - Búsqueda: {}, Estado: {}", busqueda, estado);
            List<Seguro> seguros = segurosService.listarTodosSeguros();

            // Filtrar por búsqueda si se proporciona
            if (busqueda != null && !busqueda.isEmpty()) {
                String busquedaLower = busqueda.toLowerCase();
                seguros = seguros.stream()
                    .filter(s -> 
                        (s.getNumeroPoliza() != null && s.getNumeroPoliza().toLowerCase().contains(busquedaLower)) ||
                        (s.getUsuario() != null && s.getUsuario().getNombre() != null && 
                         s.getUsuario().getNombre().toLowerCase().contains(busquedaLower)) ||
                        (s.getUsuario() != null && s.getUsuario().getApellido() != null && 
                         s.getUsuario().getApellido().toLowerCase().contains(busquedaLower)) ||
                        (s.getUsuario() != null && s.getUsuario().getDni() != null && 
                         s.getUsuario().getDni().contains(busqueda))
                    )
                    .toList();
            }

            // Filtrar por estado si se proporciona
            if (estado != null && !estado.isEmpty()) {
                seguros = seguros.stream()
                    .filter(s -> estado.equals(s.getEstado()))
                    .toList();
            }

            return ResponseEntity.ok(seguros);
        } catch (Exception e) {
            logger.error("Error al listar seguros: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al listar seguros");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerSeguroPorId(@PathVariable int id) {
        try {
            logger.info("Admin - Obteniendo seguro por ID: {}", id);
            Seguro seguro = segurosService.obtenerSeguroPorId(id);

            if (seguro == null) {
                return ResponseEntity.status(404).body("Seguro no encontrado");
            }

            return ResponseEntity.ok(seguro);
        } catch (Exception e) {
            logger.error("Error al obtener seguro: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener seguro");
        }
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<?> obtenerEstadisticas() {
        try {
            logger.info("Admin - Obteniendo estadísticas de seguros");
            HashMap<String, Object> stats = segurosService.obtenerEstadisticasGenerales();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error al obtener estadísticas: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener estadísticas");
        }
    }

    @PostMapping
    public ResponseEntity<?> crearSeguro(@RequestBody Seguro seguro) {
        try {
            logger.info("Admin - Creando nuevo seguro para usuario: {}", 
                seguro.getUsuario() != null ? seguro.getUsuario().getIdUsuario() : "null");
            
            Seguro nuevoSeguro = segurosService.crearSeguro(seguro);
            
            HashMap<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Seguro creado exitosamente");
            respuesta.put("data", nuevoSeguro);
            
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al crear seguro: {}", e.getMessage());
            HashMap<String, Object> error = new HashMap<>();
            error.put("mensaje", "Error al crear seguro");
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarSeguro(
            @PathVariable int id,
            @RequestBody Seguro seguro) {
        try {
            logger.info("Admin - Actualizando seguro ID: {}", id);
            
            seguro.setIdSeguro(id);
            Seguro actualizado = segurosService.actualizarSeguro(seguro);
            
            HashMap<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Seguro actualizado exitosamente");
            respuesta.put("data", actualizado);
            
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al actualizar seguro: {}", e.getMessage());
            HashMap<String, Object> error = new HashMap<>();
            error.put("mensaje", "Error al actualizar seguro");
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarSeguro(@PathVariable int id) {
        try {
            logger.info("Admin - Eliminando seguro ID: {}", id);
            
            segurosService.eliminarSeguro(id);
            
            HashMap<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Seguro eliminado exitosamente");
            
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al eliminar seguro: {}", e.getMessage());
            HashMap<String, Object> error = new HashMap<>();
            error.put("mensaje", "Error al eliminar seguro");
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @GetMapping("/{id}/beneficiarios")
    public ResponseEntity<?> obtenerBeneficiarios(@PathVariable int id) {
        try {
            logger.info("Admin - Obteniendo beneficiarios del seguro ID: {}", id);
            List<BeneficiarioSeguro> beneficiarios = segurosService.obtenerBeneficiariosPorSeguro(id);
            return ResponseEntity.ok(beneficiarios);
        } catch (Exception e) {
            logger.error("Error al obtener beneficiarios: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener beneficiarios");
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable int id,
            @RequestParam String nuevoEstado) {
        try {
            logger.info("Admin - Cambiando estado del seguro ID: {} a {}", id, nuevoEstado);
            
            Seguro seguro = segurosService.obtenerSeguroPorId(id);
            if (seguro == null) {
                return ResponseEntity.status(404).body("Seguro no encontrado");
            }
            
            seguro.setEstado(nuevoEstado);
            Seguro actualizado = segurosService.actualizarSeguro(seguro);
            
            HashMap<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Estado actualizado exitosamente");
            respuesta.put("data", actualizado);
            
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al cambiar estado: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al cambiar estado");
        }
    }
}
