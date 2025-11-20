package com.app.financiera.controller;

import com.app.financiera.entity.BeneficiarioSeguro;
import com.app.financiera.entity.PagoSeguro;
import com.app.financiera.entity.Seguro;
import com.app.financiera.entity.TramiteSeguro;
import com.app.financiera.service.SegurosService;
import com.app.financiera.util.AppSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * Controlador REST para la gestión integral de seguros.
 * Permite administrar pólizas, beneficiarios, pagos y trámites,
 * así como obtener resúmenes y estadísticas relacionadas con los seguros del usuario.
 */
@RestController
@RequestMapping("/api/seguros")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class SegurosController {

    private static final Logger logger = LoggerFactory.getLogger(SegurosController.class);

    @Autowired
    private SegurosService segurosService;

    /**
     * Obtiene un resumen administrativo de los seguros del usuario especificado.
     *
     * @param idUsuario identificador único del usuario
     * @return resumen de información general de los seguros del usuario
     */
    @GetMapping("/resumen/{idUsuario}")
    public ResponseEntity<?> obtenerResumen(@PathVariable int idUsuario) {
        logger.info("Solicitud de resumen de seguros para usuario: {}", idUsuario);
        try {
            HashMap<String, Object> resumen = segurosService.obtenerResumenAdministrativo(idUsuario);
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
     * Obtiene todas las pólizas registradas para un usuario.
     *
     * @param idUsuario identificador único del usuario
     * @return lista de pólizas asociadas al usuario
     */
    @GetMapping("/polizas/{idUsuario}")
    public ResponseEntity<?> obtenerPolizas(@PathVariable int idUsuario) {
        logger.info("Solicitud de pólizas para usuario: {}", idUsuario);
        try {
            List<Seguro> seguros = segurosService.obtenerSegurosUsuario(idUsuario);
            return ResponseEntity.ok(seguros);
        } catch (Exception e) {
            logger.error("Error al obtener pólizas: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener pólizas");
        }
    }

    /**
     * Obtiene las pólizas activas de un usuario.
     *
     * @param idUsuario identificador único del usuario
     * @return lista de pólizas con estado activo
     */
    @GetMapping("/polizas/{idUsuario}/activos")
    public ResponseEntity<?> obtenerPolizasActivas(@PathVariable int idUsuario) {
        try {
            List<Seguro> seguros = segurosService.obtenerSegurosActivos(idUsuario);
            return ResponseEntity.ok(seguros);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener pólizas activas");
        }
    }

    /**
     * Obtiene una póliza específica a partir de su identificador.
     *
     * @param idSeguro identificador único de la póliza
     * @return información detallada de la póliza encontrada o mensaje de error si no existe
     */
    @GetMapping("/poliza/{idSeguro}")
    public ResponseEntity<?> obtenerPolizaPorId(@PathVariable int idSeguro) {
        try {
            Seguro seguro = segurosService.obtenerSeguroPorId(idSeguro);
            if (seguro == null) {
                return ResponseEntity.status(404).body("Póliza no encontrada");
            }
            return ResponseEntity.ok(seguro);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener póliza");
        }
    }

    /**
     * Registra una nueva póliza para el usuario correspondiente.
     *
     * @param seguro objeto {@link Seguro} con la información de la nueva póliza
     * @return confirmación de creación junto con los datos de la póliza registrada
     */
    @PostMapping("/polizas")
    public ResponseEntity<?> crearPoliza(@RequestBody Seguro seguro) {
        logger.info("Creando nueva póliza para usuario: {}", seguro.getUsuario().getIdUsuario());
        try {
            Seguro nuevoSeguro = segurosService.crearSeguro(seguro);
            HashMap<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Póliza creada exitosamente");
            respuesta.put("data", nuevoSeguro);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al crear póliza: {}", e.getMessage());
            HashMap<String, Object> error = new HashMap<>();
            error.put("mensaje", "Error al crear póliza");
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * Actualiza la información de una póliza existente.
     *
     * @param idSeguro identificador único de la póliza
     * @param seguro objeto {@link Seguro} con los datos actualizados
     * @return confirmación de actualización junto con los datos modificados
     */
    @PutMapping("/polizas/{idSeguro}")
    public ResponseEntity<?> actualizarPoliza(
            @PathVariable int idSeguro,
            @RequestBody Seguro seguro) {
        logger.info("Actualizando póliza ID: {}", idSeguro);
        try {
            seguro.setIdSeguro(idSeguro);
            Seguro actualizado = segurosService.actualizarSeguro(seguro);
            HashMap<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Póliza actualizada exitosamente");
            respuesta.put("data", actualizado);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al actualizar póliza: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al actualizar póliza");
        }
    }

    /**
     * Cancela una póliza existente a partir de su identificador.
     *
     * @param idSeguro identificador único de la póliza
     * @return mensaje de confirmación de cancelación
     */
    @DeleteMapping("/polizas/{idSeguro}")
    public ResponseEntity<?> cancelarPoliza(@PathVariable int idSeguro) {
        logger.info("Cancelando póliza ID: {}", idSeguro);
        try {
            segurosService.eliminarSeguro(idSeguro);
            HashMap<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Póliza cancelada exitosamente");
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al cancelar póliza: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al cancelar póliza");
        }
    }

    /**
     * Obtiene las pólizas del usuario que están próximas a vencer.
     *
     * @param idUsuario identificador único del usuario
     * @param dias número de días de margen para considerar una póliza próxima a vencer
     * @return lista de pólizas próximas a vencer
     */
    @GetMapping("/polizas/{idUsuario}/proximos-vencer")
    public ResponseEntity<?> obtenerProximosVencer(
            @PathVariable int idUsuario,
            @RequestParam(defaultValue = "30") int dias) {
        try {
            List<Seguro> seguros = segurosService.obtenerSegurosProximosVencer(idUsuario, dias);
            return ResponseEntity.ok(seguros);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener seguros próximos a vencer");
        }
    }

    /**
     * Obtiene los beneficiarios asociados a una póliza específica.
     *
     * @param idSeguro identificador único de la póliza
     * @return lista de beneficiarios asociados
     */
    @GetMapping("/beneficiarios/{idSeguro}")
    public ResponseEntity<?> obtenerBeneficiarios(@PathVariable int idSeguro) {
        logger.info("Solicitud de beneficiarios para seguro: {}", idSeguro);
        try {
            List<BeneficiarioSeguro> beneficiarios = segurosService.obtenerBeneficiariosPorSeguro(idSeguro);
            return ResponseEntity.ok(beneficiarios);
        } catch (Exception e) {
            logger.error("Error al obtener beneficiarios: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener beneficiarios");
        }
    }

    /**
     * Agrega un nuevo beneficiario a una póliza existente.
     *
     * @param beneficiario objeto {@link BeneficiarioSeguro} con la información del beneficiario
     * @return confirmación de creación junto con los datos del beneficiario registrado
     */
    @PostMapping("/beneficiarios")
    public ResponseEntity<?> agregarBeneficiario(@RequestBody BeneficiarioSeguro beneficiario) {
        logger.info("Agregando beneficiario para seguro: {}", beneficiario.getSeguro().getIdSeguro());
        try {
            BeneficiarioSeguro nuevo = segurosService.agregarBeneficiario(beneficiario);
            HashMap<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Beneficiario agregado exitosamente");
            respuesta.put("data", nuevo);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al agregar beneficiario: {}", e.getMessage());
            HashMap<String, Object> error = new HashMap<>();
            error.put("mensaje", "Error al agregar beneficiario");
            error.put("error", e.getMessage());
            return ResponseEntity.status(400).body(error);
        }
    }

    /**
     * Actualiza los datos de un beneficiario existente.
     *
     * @param idBeneficiario identificador único del beneficiario
     * @param beneficiario objeto {@link BeneficiarioSeguro} con los datos actualizados
     * @return confirmación de actualización junto con los datos modificados
     */
    @PutMapping("/beneficiarios/{idBeneficiario}")
    public ResponseEntity<?> actualizarBeneficiario(
            @PathVariable int idBeneficiario,
            @RequestBody BeneficiarioSeguro beneficiario) {
        try {
            beneficiario.setIdBeneficiario(idBeneficiario);
            BeneficiarioSeguro actualizado = segurosService.actualizarBeneficiario(beneficiario);
            HashMap<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Beneficiario actualizado exitosamente");
            respuesta.put("data", actualizado);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al actualizar beneficiario");
        }
    }

    /**
     * Elimina un beneficiario asociado a una póliza.
     *
     * @param idBeneficiario identificador único del beneficiario
     * @return mensaje de confirmación de eliminación
     */
    @DeleteMapping("/beneficiarios/{idBeneficiario}")
    public ResponseEntity<?> eliminarBeneficiario(@PathVariable int idBeneficiario) {
        try {
            segurosService.eliminarBeneficiario(idBeneficiario);
            HashMap<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Beneficiario eliminado exitosamente");
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al eliminar beneficiario");
        }
    }

    /**
     * Obtiene todos los pagos realizados para una póliza.
     *
     * @param idSeguro identificador único de la póliza
     * @return lista de pagos registrados para el seguro
     */
    @GetMapping("/pagos/{idSeguro}")
    public ResponseEntity<?> obtenerPagos(@PathVariable int idSeguro) {
        try {
            List<PagoSeguro> pagos = segurosService.obtenerPagosPorSeguro(idSeguro);
            return ResponseEntity.ok(pagos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener pagos");
        }
    }

    /**
     * Obtiene los pagos pendientes de un usuario junto con el monto total adeudado.
     *
     * @param idUsuario identificador único del usuario
     * @return lista de pagos pendientes y el total pendiente
     */
    @GetMapping("/pagos/{idUsuario}/pendientes")
    public ResponseEntity<?> obtenerPagosPendientes(@PathVariable int idUsuario) {
        logger.info("Solicitud de pagos pendientes para usuario: {}", idUsuario);
        try {
            List<PagoSeguro> pagos = segurosService.obtenerPagosPendientes(idUsuario);
            Double total = segurosService.calcularTotalPendiente(idUsuario);

            HashMap<String, Object> respuesta = new HashMap<>();
            respuesta.put("pagos", pagos);
            respuesta.put("totalPendiente", total);

            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al obtener pagos pendientes: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener pagos pendientes");
        }
    }

    /**
     * Registra un nuevo pago asociado a una póliza.
     *
     * @param pago objeto {@link PagoSeguro} con la información del pago
     * @return confirmación de registro junto con los datos del pago
     */
    @PostMapping("/pagos")
    public ResponseEntity<?> registrarPago(@RequestBody PagoSeguro pago) {
        logger.info("Registrando pago para seguro: {}", pago.getSeguro().getIdSeguro());
        try {
            PagoSeguro nuevo = segurosService.registrarPago(pago);
            HashMap<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Pago registrado exitosamente");
            respuesta.put("data", nuevo);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al registrar pago: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al registrar pago");
        }
    }

    /**
     * Obtiene los trámites registrados por un usuario.
     *
     * @param idUsuario identificador único del usuario
     * @return lista de trámites asociados al usuario
     */
    @GetMapping("/tramites/{idUsuario}")
    public ResponseEntity<?> obtenerTramites(@PathVariable int idUsuario) {
        logger.info("Solicitud de trámites para usuario: {}", idUsuario);
        try {
            List<TramiteSeguro> tramites = segurosService.obtenerTramitesUsuario(idUsuario);
            return ResponseEntity.ok(tramites);
        } catch (Exception e) {
            logger.error("Error al obtener trámites: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener trámites");
        }
    }

    /**
     * Obtiene los trámites pendientes de un usuario.
     *
     * @param idUsuario identificador único del usuario
     * @return lista de trámites en estado pendiente
     */
    @GetMapping("/tramites/{idUsuario}/pendientes")
    public ResponseEntity<?> obtenerTramitesPendientes(@PathVariable int idUsuario) {
        try {
            List<TramiteSeguro> tramites = segurosService.obtenerTramitesPendientes(idUsuario);
            return ResponseEntity.ok(tramites);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener trámites pendientes");
        }
    }

    /**
     * Registra un nuevo trámite para un usuario.
     *
     * @param tramite objeto {@link TramiteSeguro} con la información del trámite
     * @return confirmación de creación junto con los datos registrados
     */
    @PostMapping("/tramites")
    public ResponseEntity<?> crearTramite(@RequestBody TramiteSeguro tramite) {
        logger.info("Creando trámite para usuario: {}", tramite.getUsuario().getIdUsuario());
        try {
            TramiteSeguro nuevo = segurosService.crearTramite(tramite);
            HashMap<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Trámite creado exitosamente");
            respuesta.put("data", nuevo);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al crear trámite: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al crear trámite");
        }
    }

    /**
     * Actualiza la información de un trámite existente.
     *
     * @param idTramite identificador único del trámite
     * @param tramite objeto {@link TramiteSeguro} con los datos actualizados
     * @return confirmación de actualización junto con los datos modificados
     */
    @PutMapping("/tramites/{idTramite}")
    public ResponseEntity<?> actualizarTramite(
            @PathVariable int idTramite,
            @RequestBody TramiteSeguro tramite) {
        logger.info("Actualizando trámite ID: {}", idTramite);
        try {
            tramite.setIdTramite(idTramite);
            TramiteSeguro actualizado = segurosService.actualizarTramite(tramite);
            HashMap<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Trámite actualizado exitosamente");
            respuesta.put("data", actualizado);
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al actualizar trámite: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al actualizar trámite");
        }
    }

    /**
     * Elimina un trámite existente a partir de su identificador.
     *
     * @param idTramite identificador único del trámite
     * @return mensaje de confirmación de eliminación
     */
    @DeleteMapping("/tramites/{idTramite}")
    public ResponseEntity<?> eliminarTramite(@PathVariable int idTramite) {
        logger.info("Eliminando trámite ID: {}", idTramite);
        try {
            segurosService.eliminarTramite(idTramite);
            HashMap<String, Object> respuesta = new HashMap<>();
            respuesta.put("mensaje", "Trámite eliminado exitosamente");
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al eliminar trámite: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al eliminar trámite");
        }
    }

    /**
     * Obtiene estadísticas generales sobre los seguros de un usuario.
     *
     * @param idUsuario identificador único del usuario
     * @return mapa con métricas y estadísticas relacionadas con los seguros
     */
    @GetMapping("/estadisticas/{idUsuario}")
    public ResponseEntity<?> obtenerEstadisticas(@PathVariable int idUsuario) {
        logger.info("Solicitud de estadísticas de seguros para usuario: {}", idUsuario);
        try {
            HashMap<String, Object> stats = segurosService.obtenerEstadisticas(idUsuario);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error al obtener estadísticas: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener estadísticas");
        }
    }

    /**
     * Endpoint de verificación de estado del servicio.
     *
     * @return información del estado actual del servicio y su versión
     */
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        HashMap<String, Object> health = new HashMap<>();
        health.put("status", "OK");
        health.put("servicio", "Gestión de Seguros");
        health.put("version", "1.0.0");
        return ResponseEntity.ok(health);
    }
}