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

/**
 * Controlador REST que gestiona las operaciones relacionadas con las pensiones,
 * incluyendo aportes, saldos, proyecciones, estado y registro de consultas o historial.
 */
@RestController
@RequestMapping("/api/pensiones")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class PensionesController {

    private static final Logger logger = LoggerFactory.getLogger(PensionesController.class);

    @Autowired
    private PensionesService pensionesService;

    /**
     * Obtiene el resumen general de pensiones del usuario.
     *
     * @param idUsuario identificador único del usuario
     * @return resumen con los datos consolidados de aportes, saldos y estado de pensiones
     */
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

    /**
     * Obtiene la lista completa de aportes del usuario.
     *
     * @param idUsuario identificador único del usuario
     * @return lista de aportes realizados por el usuario
     */
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

    /**
     * Obtiene los aportes del sistema ONP para un usuario específico.
     *
     * @param idUsuario identificador único del usuario
     * @return lista de aportes asociados al sistema ONP
     */
    @GetMapping("/aportes/{idUsuario}/onp")
    public ResponseEntity<?> obtenerAportesONP(@PathVariable int idUsuario) {
        try {
            List<AportePension> aportes = pensionesService.obtenerAportesONP(idUsuario);
            return ResponseEntity.ok(aportes);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener aportes ONP");
        }
    }

    /**
     * Obtiene los aportes del sistema AFP para un usuario específico.
     *
     * @param idUsuario identificador único del usuario
     * @return lista de aportes asociados al sistema AFP
     */
    @GetMapping("/aportes/{idUsuario}/afp")
    public ResponseEntity<?> obtenerAportesAFP(@PathVariable int idUsuario) {
        try {
            List<AportePension> aportes = pensionesService.obtenerAportesAFP(idUsuario);
            return ResponseEntity.ok(aportes);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al obtener aportes AFP", "error", e.getMessage()));
        }
    }

    /**
     * Registra un nuevo aporte de pensión para el usuario.
     *
     * @param aporte objeto con los datos del aporte a registrar
     * @return mensaje de confirmación y datos del aporte registrado
     */
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

    /**
     * Actualiza un aporte existente del usuario.
     *
     * @param idAporte identificador del aporte a actualizar
     * @param aporte objeto con los nuevos datos del aporte
     * @return mensaje de confirmación y datos del aporte actualizado
     */
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

    /**
     * Elimina un aporte de pensión según su identificador.
     *
     * @param idAporte identificador del aporte a eliminar
     * @return mensaje de confirmación de eliminación del aporte
     */
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

    /**
     * Obtiene los saldos registrados del usuario.
     *
     * @param idUsuario identificador único del usuario
     * @return lista de saldos del sistema de pensiones
     */
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

    /**
     * Obtiene el saldo total acumulado en las cuentas de pensión del usuario.
     *
     * @param idUsuario identificador único del usuario
     * @return monto total de los saldos del usuario
     */
    @GetMapping("/saldos/{idUsuario}/total")
    public ResponseEntity<?> obtenerSaldoTotal(@PathVariable int idUsuario) {
        try {
            Double total = pensionesService.obtenerSaldoTotal(idUsuario);
            return ResponseEntity.ok(Map.of("saldoTotal", total));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al obtener saldo total", "error", e.getMessage()));
        }
    }

    /**
     * Obtiene el saldo disponible actual para el usuario.
     *
     * @param idUsuario identificador único del usuario
     * @return monto del saldo disponible
     */
    @GetMapping("/saldos/{idUsuario}/disponible")
    public ResponseEntity<?> obtenerSaldoDisponible(@PathVariable int idUsuario) {
        try {
            Double disponible = pensionesService.obtenerSaldoDisponible(idUsuario);
            return ResponseEntity.ok(Map.of("saldoDisponible", disponible));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al obtener saldo disponible", "error", e.getMessage()));
        }
    }

    /**
     * Obtiene las proyecciones de pensión para el usuario según sus aportes y estado actual.
     *
     * @param idUsuario identificador único del usuario
     * @return mapa con los datos proyectados de pensión futura
     */
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

    /**
     * Obtiene el estado actual del sistema de pensiones del usuario,
     * incluyendo información de AFP, ONP y años aportados.
     *
     * @param idUsuario identificador único del usuario
     * @return mapa con el estado del usuario en los distintos sistemas
     */
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

    /**
     * Registra una nueva consulta de aportes realizada por el usuario.
     *
     * @param consulta objeto que contiene los datos de la consulta
     * @return consulta registrada con los datos almacenados
     */
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

    /**
     * Obtiene todas las consultas registradas para un usuario.
     *
     * @param idUsuario identificador único del usuario
     * @return lista de consultas realizadas por el usuario
     */
    @GetMapping("/consultas/{idUsuario}")
    public ResponseEntity<?> obtenerConsultas(@PathVariable int idUsuario) {
        try {
            List<ConsultaAportes> consultas = pensionesService.obtenerConsultasUsuario(idUsuario);
            return ResponseEntity.ok(consultas);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al obtener consultas", "error", e.getMessage()));
        }
    }

    /**
     * Registra una nueva entrada en el historial de consultas del usuario.
     *
     * @param historial objeto con la información del historial a registrar
     * @return historial registrado con los datos almacenados
     */
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

    /**
     * Obtiene el historial completo de consultas realizadas por un usuario.
     *
     * @param idUsuario identificador único del usuario
     * @return lista de registros del historial de consultas
     */
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