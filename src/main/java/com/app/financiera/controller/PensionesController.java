package com.app.financiera.controller;

import com.app.financiera.entity.AportePension;
import com.app.financiera.entity.ConsultaAportes;
import com.app.financiera.entity.HistorialConsultas;
import com.app.financiera.entity.SaldoPension;
import com.app.financiera.service.PensionesService;
import com.app.financiera.util.AppSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pensiones")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class PensionesController {

    private static final Logger logger = LoggerFactory.getLogger(PensionesController.class);

    @Autowired
    private PensionesService pensionesService;

    // Resumen General
    @GetMapping("/resumen/{idUsuario}")
    public ResponseEntity<?> obtenerResumen(@PathVariable int idUsuario) {
        logger.info("Solicitando resumen de pensiones para usuario: {}", idUsuario);
        try {
            HashMap<String, Object> resumen = pensionesService.obtenerResumenPensiones(idUsuario);
            return ResponseEntity.ok(resumen);
        } catch (Exception e) {
            logger.error("Error al obtener resumen: {}", e.getMessage());
            HashMap<String, Object> error = new HashMap<>();
            error.put("mensaje", "Error al obtener resumen");
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    // APORTES - Obtener todos
    @GetMapping("/aportes/{idUsuario}")
    public ResponseEntity<?> obtenerAportes(@PathVariable int idUsuario) {
        logger.info("Solicitando aportes para usuario: {}", idUsuario);
        try {
            List<AportePension> aportes = pensionesService.obtenerAportes(idUsuario);
            return ResponseEntity.ok(aportes);
        } catch (Exception e) {
            logger.error("Error al obtener aportes: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener aportes");
        }
    }

    // APORTES - Obtener ONP
    @GetMapping("/aportes/{idUsuario}/onp")
    public ResponseEntity<?> obtenerAportesONP(@PathVariable int idUsuario) {
        try {
            List<AportePension> aportes = pensionesService.obtenerAportesONP(idUsuario);
            return ResponseEntity.ok(aportes);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener aportes ONP");
        }
    }

    // APORTES - Obtener AFP
    @GetMapping("/aportes/{idUsuario}/afp")
    public ResponseEntity<?> obtenerAportesAFP(@PathVariable int idUsuario) {
        try {
            List<AportePension> aportes = pensionesService.obtenerAportesAFP(idUsuario);
            return ResponseEntity.ok(aportes);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al obtener aportes AFP", "error", e.getMessage()));
        }
    }

    // APORTES - Crear
    @PostMapping("/aportes")
    public ResponseEntity<?> crearAporte(@RequestBody AportePension aporte) {
        logger.info("Creando nuevo aporte para usuario: {}", aporte.getUsuario().getIdUsuario());
        try {
            AportePension nuevoAporte = pensionesService.crearAporte(aporte);
            return ResponseEntity.ok(Map.of("mensaje", "Aporte registrado exitosamente", "data", nuevoAporte));
        } catch (Exception e) {
            logger.error("Error al crear aporte: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al registrar aporte", "error", e.getMessage()));
        }
    }

    // APORTES - Actualizar
    @PutMapping("/aportes/{idAporte}")
    public ResponseEntity<?> actualizarAporte(@PathVariable int idAporte, @RequestBody AportePension aporte) {
        logger.info("Actualizando aporte: {}", idAporte);
        try {
            aporte.setIdAporte(idAporte);
            AportePension actualizado = pensionesService.actualizarAporte(aporte);
            return ResponseEntity.ok(Map.of("mensaje", "Aporte actualizado exitosamente", "data", actualizado));
        } catch (Exception e) {
            logger.error("Error al actualizar aporte: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al actualizar aporte", "error", e.getMessage()));
        }
    }

    // APORTES - Eliminar
    @DeleteMapping("/aportes/{idAporte}")
    public ResponseEntity<?> eliminarAporte(@PathVariable int idAporte) {
        logger.info("Eliminando aporte: {}", idAporte);
        try {
            pensionesService.eliminarAporte(idAporte);
            return ResponseEntity.ok(Map.of("mensaje", "Aporte eliminado exitosamente"));
        } catch (Exception e) {
            logger.error("Error al eliminar aporte: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al eliminar aporte", "error", e.getMessage()));
        }
    }

    // SALDOS - Obtener todos
    @GetMapping("/saldos/{idUsuario}")
    public ResponseEntity<?> obtenerSaldos(@PathVariable int idUsuario) {
        logger.info("Solicitando saldos para usuario: {}", idUsuario);
        try {
            List<SaldoPension> saldos = pensionesService.obtenerSaldos(idUsuario);
            return ResponseEntity.ok(saldos);
        } catch (Exception e) {
            logger.error("Error al obtener saldos: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al obtener saldos", "error", e.getMessage()));
        }
    }

    // SALDOS - Total
    @GetMapping("/saldos/{idUsuario}/total")
    public ResponseEntity<?> obtenerSaldoTotal(@PathVariable int idUsuario) {
        try {
            Double total = pensionesService.obtenerSaldoTotal(idUsuario);
            return ResponseEntity.ok(Map.of("saldoTotal", total));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al obtener saldo total", "error", e.getMessage()));
        }
    }

    // SALDOS - Disponible
    @GetMapping("/saldos/{idUsuario}/disponible")
    public ResponseEntity<?> obtenerSaldoDisponible(@PathVariable int idUsuario) {
        try {
            Double disponible = pensionesService.obtenerSaldoDisponible(idUsuario);
            return ResponseEntity.ok(Map.of("saldoDisponible", disponible));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al obtener saldo disponible", "error", e.getMessage()));
        }
    }

    // PROYECCIONES
    @GetMapping("/proyecciones/{idUsuario}")
    public ResponseEntity<?> obtenerProyecciones(@PathVariable int idUsuario) {
        logger.info("Solicitando proyecciones para usuario: {}", idUsuario);
        try {
            Map<String, Object> proyecciones = pensionesService.obtenerProyecciones(idUsuario);
            return ResponseEntity.ok(proyecciones);
        } catch (Exception e) {
            logger.error("Error al obtener proyecciones: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al obtener proyecciones", "error", e.getMessage()));
        }
    }

    // ESTADO
    @GetMapping("/estado/{idUsuario}")
    public ResponseEntity<?> obtenerEstado(@PathVariable int idUsuario) {
        logger.info("Solicitando estado de pensiones para usuario: {}", idUsuario);
        try {
            Map<String, Object> estado = Map.of(
                "estadoAFP", pensionesService.obtenerEstadoAFP(idUsuario),
                "estadoONP", pensionesService.obtenerEstadoONP(idUsuario),
                "años", pensionesService.obtenerAñosAportados(idUsuario)
            );
            return ResponseEntity.ok(estado);
        } catch (Exception e) {
            logger.error("Error al obtener estado: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al obtener estado", "error", e.getMessage()));
        }
    }

    // CONSULTAS - Registrar
    @PostMapping("/consultas")
    public ResponseEntity<?> registrarConsulta(@RequestBody ConsultaAportes consulta) {
        logger.info("Registrando consulta para usuario: {}", consulta.getUsuario().getIdUsuario());
        try {
            ConsultaAportes nueva = pensionesService.registrarConsulta(consulta);
            return ResponseEntity.ok(nueva);
        } catch (Exception e) {
            logger.error("Error al registrar consulta: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al registrar consulta", "error", e.getMessage()));
        }
    }

    // CONSULTAS - Obtener
    @GetMapping("/consultas/{idUsuario}")
    public ResponseEntity<?> obtenerConsultas(@PathVariable int idUsuario) {
        try {
            List<ConsultaAportes> consultas = pensionesService.obtenerConsultasUsuario(idUsuario);
            return ResponseEntity.ok(consultas);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al obtener consultas", "error", e.getMessage()));
        }
    }

    // HISTORIAL - Registrar
    @PostMapping("/historial")
    public ResponseEntity<?> registrarHistorial(@RequestBody HistorialConsultas historial) {
        logger.info("Registrando historial para usuario: {}", historial.getUsuario().getIdUsuario());
        try {
            HistorialConsultas nuevo = pensionesService.registrarHistorial(historial);
            return ResponseEntity.ok(nuevo);
        } catch (Exception e) {
            logger.error("Error al registrar historial: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al registrar historial");
        }
    }

    // HISTORIAL - Obtener
    @GetMapping("/historial/{idUsuario}")
    public ResponseEntity<?> obtenerHistorial(@PathVariable int idUsuario) {
        try {
            List<HistorialConsultas> historial = pensionesService.obtenerHistorialUsuario(idUsuario);
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            logger.error("Error al obtener historial: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener historial");
        }
    }
}