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

/**
 * Controlador REST para la gestión de seguros.
 * Permite listar, crear, actualizar, eliminar seguros y gestionar sus beneficiarios.
 * También ofrece estadísticas generales sobre los seguros registrados.
 */
@RestController
@RequestMapping("/api/admin/seguros")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class AdminSeguroController {

    private static final Logger logger = LoggerFactory.getLogger(AdminSeguroController.class);

    @Autowired
    private SegurosService segurosService;

    /**
     * Obtiene la lista de seguros registrados, con filtros opcionales por búsqueda y estado.
     * Si no se indican filtros, devuelve todos los seguros existentes.
     *
     * @param busqueda texto opcional para filtrar por número de póliza, nombre, apellido o DNI del usuario
     * @param estado estado opcional para filtrar seguros (por ejemplo, Activo o Inactivo)
     * @return una respuesta HTTP con la lista de seguros filtrada o un mensaje de error en caso de fallo
     */
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

    /**
     * Recupera la información detallada de un seguro específico a partir de su identificador único.
     *
     * @param id identificador numérico del seguro a consultar
     * @return una respuesta HTTP con los datos del seguro o un mensaje de error si no se encuentra
     */
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

    /**
     * Obtiene estadísticas generales de los seguros registrados.
     * Las estadísticas incluyen métricas globales como cantidad total de seguros,
     * distribución por estado u otros indicadores definidos en el servicio.
     *
     * @return una respuesta HTTP con los datos estadísticos o un mensaje de error si ocurre una falla
     */
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

    /**
     * Registra un nuevo seguro en el sistema con los datos proporcionados.
     *
     * @param seguro objeto {@link Seguro} con la información del nuevo seguro
     * @return una respuesta HTTP con el mensaje de éxito y los datos del seguro creado,
     *         o un mensaje de error en caso de fallo
     */
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

    /**
     * Actualiza los datos de un seguro existente identificado por su ID.
     *
     * @param id identificador del seguro a actualizar
     * @param seguro objeto {@link Seguro} con los nuevos valores a registrar
     * @return una respuesta HTTP con el seguro actualizado o un mensaje de error si ocurre una falla
     */
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

    /**
     * Elimina un seguro del sistema de acuerdo con su identificador.
     *
     * @param id identificador del seguro a eliminar
     * @return una respuesta HTTP con un mensaje de confirmación o un error si la eliminación falla
     */
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

    /**
     * Obtiene la lista de beneficiarios asociados a un seguro específico.
     *
     * @param id identificador del seguro del cual se desean consultar los beneficiarios
     * @return una respuesta HTTP con la lista de beneficiarios o un mensaje de error en caso de fallo
     */
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

    /**
     * Cambia el estado actual de un seguro (por ejemplo, de Activo a Inactivo).
     *
     * @param id identificador del seguro cuyo estado se desea modificar
     * @param nuevoEstado nuevo valor del estado a asignar al seguro
     * @return una respuesta HTTP con el seguro actualizado o un mensaje de error si ocurre un problema
     */
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
