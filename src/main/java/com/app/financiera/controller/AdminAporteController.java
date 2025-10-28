package com.app.financiera.controller;

import com.app.financiera.entity.AportePension;
import com.app.financiera.service.AportePensionService;
import com.app.financiera.util.AppSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/admin/aportes")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class AdminAporteController {

    private static final Logger logger = LoggerFactory.getLogger(AdminAporteController.class);

    @Autowired
    private AportePensionService aportePensionService;

    @GetMapping
    public ResponseEntity<?> listarAportes(
            @RequestParam(required = false) Integer idUsuario,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Integer year) {
        try {
            logger.info("Listando aportes - Usuario: {}, Estado: {}, Año: {}", idUsuario, estado, year);
            List<AportePension> aportes;

            if (idUsuario != null && year != null) {
                aportes = aportePensionService.obtenerAportesUsuarioYear(idUsuario, year);
            } else if (idUsuario != null) {
                aportes = aportePensionService.obtenerAportesUsuario(idUsuario);
            } else {
                aportes = aportePensionService.obtenerTodos();
            }

            // Filtrar por estado si se proporciona
            if (estado != null && !estado.isEmpty()) {
                aportes = aportes.stream()
                        .filter(a -> estado.equals(a.getEstado()))
                        .toList();
            }

            return ResponseEntity.ok(aportes);
        } catch (Exception e) {
            logger.error("Error al listar aportes: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al listar aportes");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerAportePorId(@PathVariable int id) {
        try {
            logger.info("Obteniendo aporte por ID: {}", id);
            AportePension aporte = aportePensionService.obtenerAportesUsuario(0).stream()
                    .filter(a -> a.getIdAporte() == id)
                    .findFirst()
                    .orElse(null);

            if (aporte == null) {
                return ResponseEntity.status(404).body("Aporte no encontrado");
            }

            return ResponseEntity.ok(aporte);
        } catch (Exception e) {
            logger.error("Error al obtener aporte: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener aporte");
        }
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<?> obtenerEstadisticas() {
        try {
            List<AportePension> todosAportes = aportePensionService.obtenerTodos();
            
            HashMap<String, Object> stats = new HashMap<>();
            stats.put("total", todosAportes.size());
            stats.put("registrados", todosAportes.stream().filter(a -> "Registrado".equals(a.getEstado())).count());
            stats.put("procesados", todosAportes.stream().filter(a -> "Procesado".equals(a.getEstado())).count());
            stats.put("observados", todosAportes.stream().filter(a -> "Observado".equals(a.getEstado())).count());
            stats.put("eliminados", todosAportes.stream().filter(a -> "Eliminado".equals(a.getEstado())).count());
            
            Double montoTotal = todosAportes.stream()
                    .filter(a -> !"Eliminado".equals(a.getEstado()))
                    .mapToDouble(a -> a.getMontoAporte() != null ? a.getMontoAporte() : 0.0)
                    .sum();
            stats.put("montoTotal", montoTotal);

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error al obtener estadísticas: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener estadísticas");
        }
    }

    @PostMapping
    public ResponseEntity<?> crearAporte(@RequestBody AportePension aporte) {
        HashMap<String, Object> respuesta = new HashMap<>();
        try {
            logger.info("Creando nuevo aporte para usuario: {}", aporte.getUsuario().getIdUsuario());

            if (aporte.getEstado() == null || aporte.getEstado().isEmpty()) {
                aporte.setEstado("Registrado");
            }

            AportePension nuevoAporte = aportePensionService.guardarAporte(aporte);
            respuesta.put("mensaje", "Aporte creado exitosamente");
            respuesta.put("data", nuevoAporte);

            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al crear aporte: {}", e.getMessage());
            respuesta.put("mensaje", "Error al crear aporte");
            respuesta.put("error", e.getMessage());
            return ResponseEntity.status(500).body(respuesta);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarAporte(
            @PathVariable int id,
            @RequestBody AportePension aporte) {
        HashMap<String, Object> respuesta = new HashMap<>();
        try {
            logger.info("Actualizando aporte ID: {}", id);

            aporte.setIdAporte(id);
            AportePension actualizado = aportePensionService.actualizarAporte(aporte);

            respuesta.put("mensaje", "Aporte actualizado exitosamente");
            respuesta.put("data", actualizado);

            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al actualizar aporte: {}", e.getMessage());
            respuesta.put("mensaje", "Error al actualizar aporte");
            respuesta.put("error", e.getMessage());
            return ResponseEntity.status(500).body(respuesta);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarAporte(@PathVariable int id) {
        HashMap<String, Object> respuesta = new HashMap<>();
        try {
            logger.info("Eliminando aporte ID: {}", id);
            aportePensionService.eliminarAporte(id);
            respuesta.put("mensaje", "Aporte eliminado exitosamente");
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al eliminar aporte: {}", e.getMessage());
            respuesta.put("mensaje", "Error al eliminar aporte");
            respuesta.put("error", e.getMessage());
            return ResponseEntity.status(500).body(respuesta);
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable int id,
            @RequestBody HashMap<String, String> body) {
        HashMap<String, Object> respuesta = new HashMap<>();
        try {
            String nuevoEstado = body.get("estado");
            logger.info("Cambiando estado de aporte ID {} a {}", id, nuevoEstado);

            // Validar que el estado sea válido
            if (!nuevoEstado.equals("Registrado") && !nuevoEstado.equals("Procesado") && 
                !nuevoEstado.equals("Observado") && !nuevoEstado.equals("Eliminado")) {
                respuesta.put("mensaje", "Estado no válido");
                return ResponseEntity.status(400).body(respuesta);
            }

            List<AportePension> aportes = aportePensionService.obtenerTodos();
            AportePension aporte = aportes.stream()
                    .filter(a -> a.getIdAporte() == id)
                    .findFirst()
                    .orElse(null);

            if (aporte == null) {
                respuesta.put("mensaje", "Aporte no encontrado");
                return ResponseEntity.status(404).body(respuesta);
            }

            aporte.setEstado(nuevoEstado);
            aportePensionService.actualizarAporte(aporte);

            respuesta.put("mensaje", "Estado actualizado exitosamente");
            respuesta.put("data", aporte);

            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al cambiar estado: {}", e.getMessage());
            respuesta.put("mensaje", "Error al cambiar estado");
            return ResponseEntity.status(500).body(respuesta);
        }
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> obtenerAportesPorUsuario(@PathVariable int idUsuario) {
        try {
            logger.info("Obteniendo aportes del usuario: {}", idUsuario);
            List<AportePension> aportes = aportePensionService.obtenerAportesUsuario(idUsuario);
            return ResponseEntity.ok(aportes);
        } catch (Exception e) {
            logger.error("Error al obtener aportes del usuario: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener aportes del usuario");
        }
    }
}
