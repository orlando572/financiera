package com.app.financiera.controller;

import com.app.financiera.service.CotizacionService;
import com.app.financiera.util.AppSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de solicitudes de cotización de seguros.
 * Permite a los usuarios enviar solicitudes de cotización de uno o varios planes
 * y verificar el estado del servicio.
 */
@RestController
@RequestMapping("/api/cotizacion")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class CotizacionController {

    private static final Logger logger = LoggerFactory.getLogger(CotizacionController.class);

    @Autowired
    private CotizacionService cotizacionService;

    /**
     * Procesa una solicitud de cotización enviada por el usuario.
     * Valida los datos requeridos y delega el procesamiento a {@link CotizacionService}.
     * @param request mapa que contiene el ID del usuario, la lista de IDs de los planes y comentarios opcionales
     * @return una respuesta HTTP con el resultado de la solicitud o un mensaje de error en caso de fallo
     */
    @PostMapping("/solicitar")
    public ResponseEntity<?> solicitarCotizacion(@RequestBody Map<String, Object> request) {
        logger.info("Solicitud de cotización recibida");
        
        try {
            // Validar datos requeridos
            if (!request.containsKey("idUsuario") || !request.containsKey("idsPlanes")) {
                return ResponseEntity.badRequest().body(Map.of(
                    "exitoso", false,
                    "mensaje", "Faltan datos requeridos: idUsuario e idsPlanes"
                ));
            }

            Integer idUsuario = (Integer) request.get("idUsuario");
            @SuppressWarnings("unchecked")
            List<Integer> idsPlanes = (List<Integer>) request.get("idsPlanes");
            String comentarios = (String) request.getOrDefault("comentarios", "");

            if (idsPlanes == null || idsPlanes.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "exitoso", false,
                    "mensaje", "Debe seleccionar al menos un plan"
                ));
            }

            if (idsPlanes.size() > 5) {
                return ResponseEntity.badRequest().body(Map.of(
                    "exitoso", false,
                    "mensaje", "No puede solicitar cotización de más de 5 planes"
                ));
            }

            Map<String, Object> resultado = cotizacionService.solicitarCotizacion(
                idUsuario, idsPlanes, comentarios
            );

            if ((Boolean) resultado.get("exitoso")) {
                return ResponseEntity.ok(resultado);
            } else {
                return ResponseEntity.status(500).body(resultado);
            }

        } catch (Exception e) {
            logger.error("Error al procesar solicitud de cotización: {}", e.getMessage(), e);
            HashMap<String, Object> error = new HashMap<>();
            error.put("exitoso", false);
            error.put("mensaje", "Error al procesar la solicitud de cotización");
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * Verifica el estado de salud del servicio de cotización.
     *
     * @return una respuesta HTTP con la información del estado del servicio y su versión actual
     */
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        HashMap<String, Object> health = new HashMap<>();
        health.put("status", "OK");
        health.put("servicio", "Cotización de Seguros");
        health.put("version", "1.0.0");
        return ResponseEntity.ok(health);
    }
}
