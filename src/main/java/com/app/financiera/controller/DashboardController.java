package com.app.financiera.controller;

import com.app.financiera.service.DashboardService;
import com.app.financiera.util.AppSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;

/**
 * Controlador REST que gestiona la información del panel de control (dashboard)
 * del usuario. Permite obtener datos de resumen, perfil, alertas, actividad reciente
 * y estadísticas financieras.
 */
@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private DashboardService dashboardService;

    /**
     * Obtiene un resumen general del dashboard para un usuario específico.
     *
     * @param idUsuario identificador único del usuario
     * @return una respuesta HTTP con el resumen del dashboard o un mensaje de error en caso de fallo
     */
    @GetMapping("/resumen/{idUsuario}")
    public ResponseEntity<?> obtenerResumenDashboard(@PathVariable int idUsuario) {
        logger.info("Solicitud de resumen de dashboard para usuario: {}", idUsuario);
        try {
            HashMap<String, Object> resumen = dashboardService.obtenerResumenCompleto(idUsuario);
            return ResponseEntity.ok(resumen);
        } catch (Exception e) {
            logger.error("Error al obtener resumen de dashboard: {}", e.getMessage(), e);
            HashMap<String, Object> error = new HashMap<>();
            error.put("mensaje", "Error al obtener el resumen del dashboard");
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * Obtiene la información del perfil del usuario para mostrar en el dashboard.
     *
     * @param idUsuario identificador único del usuario
     * @return una respuesta HTTP con los datos del perfil o un mensaje de error en caso de fallo
     */
    @GetMapping("/perfil/{idUsuario}")
    public ResponseEntity<?> obtenerPerfilUsuario(@PathVariable int idUsuario) {
        logger.info("Solicitud de perfil para dashboard, usuario: {}", idUsuario);
        try {
            HashMap<String, Object> perfil = dashboardService.obtenerPerfilUsuario(idUsuario);
            return ResponseEntity.ok(perfil);
        } catch (Exception e) {
            logger.error("Error al obtener perfil de usuario: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error al obtener el perfil del usuario");
        }
    }

    /**
     * Obtiene las alertas actuales del usuario en el dashboard.
     *
     * @param idUsuario identificador único del usuario
     * @return una respuesta HTTP con la lista de alertas o un mensaje de error en caso de fallo
     */
    @GetMapping("/alertas/{idUsuario}")
    public ResponseEntity<?> obtenerAlertas(@PathVariable int idUsuario) {
        logger.info("Solicitud de alertas para usuario: {}", idUsuario);
        try {
            HashMap<String, Object> alertas = dashboardService.obtenerAlertas(idUsuario);
            return ResponseEntity.ok(alertas);
        } catch (Exception e) {
            logger.error("Error al obtener alertas: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error al obtener alertas");
        }
    }

    /**
     * Obtiene la actividad reciente del usuario en el sistema.
     *
     * @param idUsuario identificador único del usuario
     * @param limite cantidad máxima de registros de actividad a retornar (por defecto 5)
     * @return una respuesta HTTP con la actividad reciente o un mensaje de error en caso de fallo
     */
    @GetMapping("/actividad/{idUsuario}")
    public ResponseEntity<?> obtenerActividadReciente(
            @PathVariable int idUsuario,
            @RequestParam(defaultValue = "5") int limite) {
        logger.info("Solicitud de actividad reciente para usuario: {}, límite: {}", idUsuario, limite);
        try {
            HashMap<String, Object> actividad = dashboardService.obtenerActividadReciente(idUsuario, limite);
            return ResponseEntity.ok(actividad);
        } catch (Exception e) {
            logger.error("Error al obtener actividad reciente: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error al obtener actividad reciente");
        }
    }

    /**
     * Obtiene las estadísticas financieras asociadas al usuario.
     *
     * @param idUsuario identificador único del usuario
     * @return una respuesta HTTP con las estadísticas financieras o un mensaje de error en caso de fallo
     */
    @GetMapping("/estadisticas/{idUsuario}")
    public ResponseEntity<?> obtenerEstadisticasFinancieras(@PathVariable int idUsuario) {
        logger.info("Solicitud de estadísticas financieras para usuario: {}", idUsuario);
        try {
            HashMap<String, Object> estadisticas = dashboardService.obtenerEstadisticasFinancieras(idUsuario);
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            logger.error("Error al obtener estadísticas: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Error al obtener estadísticas financieras");
        }
    }

    /**
     * Verifica el estado de salud del servicio de dashboard.
     *
     * @return una respuesta HTTP con la información del estado y versión del servicio
     */
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        HashMap<String, Object> health = new HashMap<>();
        health.put("status", "OK");
        health.put("servicio", "Dashboard");
        health.put("version", "1.0.0");
        return ResponseEntity.ok(health);
    }
}