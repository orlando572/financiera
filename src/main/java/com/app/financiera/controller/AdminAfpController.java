package com.app.financiera.controller;

import com.app.financiera.entity.Afp;
import com.app.financiera.service.AfpService;
import com.app.financiera.util.AppSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * Controlador REST para la gestión administrativa de las AFPs.
 * Proporciona endpoints para listar, crear, actualizar, eliminar y obtener estadísticas
 * relacionadas con las AFPs registradas en el sistema.
 */
@RestController
@RequestMapping("/api/admin/afps")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class AdminAfpController {

    private static final Logger logger = LoggerFactory.getLogger(AdminAfpController.class);

    @Autowired
    private AfpService afpService;

    /**
     * Obtiene la lista de AFPs según los criterios de búsqueda opcionales.
     * Si no se especifica ningún parámetro, devuelve todas las AFPs registradas.
     *
     * @param busqueda texto opcional para filtrar AFPs por nombre
     * @param estado estado opcional para filtrar AFPs (por ejemplo, Activo o Inactivo)
     * @return una respuesta HTTP con la lista de AFPs o un mensaje de error en caso de fallo
     */
    @GetMapping
    public ResponseEntity<?> listarAfps(
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) String estado) {
        try {
            logger.info("Listando AFPs - Búsqueda: {}, Estado: {}", busqueda, estado);
            List<Afp> afps;

            if (busqueda != null && !busqueda.isEmpty()) {
                afps = afpService.buscarPorNombre(busqueda);
            } else if (estado != null && !estado.isEmpty()) {
                afps = afpService.buscarPorEstado(estado);
            } else {
                afps = afpService.listarAfps();
            }

            return ResponseEntity.ok(afps);
        } catch (Exception e) {
            logger.error("Error al listar AFPs: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al listar AFPs");
        }
    }


    /**
     * Recupera una AFP específica a partir de su identificador único.
     *
     * @param id identificador numérico de la AFP a consultar
     * @return una respuesta HTTP con los datos de la AFP o un mensaje de error si no se encuentra
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerAfpPorId(@PathVariable int id) {
        try {
            logger.info("Obteniendo AFP por ID: {}", id);
            Afp afp = afpService.buscarPorId(id);

            if (afp == null) {
                return ResponseEntity.status(404).body("AFP no encontrada");
            }

            return ResponseEntity.ok(afp);
        } catch (Exception e) {
            logger.error("Error al obtener AFP: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener AFP");
        }
    }

    /**
     * Obtiene estadísticas generales relacionadas con las AFPs registradas.
     *
     * @return una respuesta HTTP con un mapa de estadísticas o un mensaje de error si ocurre una excepción
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<?> obtenerEstadisticas() {
        try {
            HashMap<String, Object> stats = afpService.obtenerEstadisticas();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error al obtener estadísticas: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener estadísticas");
        }
    }

    /**
     * Registra una nueva AFP en el sistema, validando que no exista otra con el mismo código SBS.
     *
     * @param afp objeto AFP con los datos a registrar
     * @return una respuesta HTTP con el resultado de la operación y la AFP creada en caso de éxito
     */
    @PostMapping
    public ResponseEntity<?> crearAfp(@RequestBody Afp afp) {
        HashMap<String, Object> respuesta = new HashMap<>();
        try {
            logger.info("Creando nueva AFP: {}", afp.getNombre());

            // Verificar si ya existe AFP con el mismo código SBS
            Afp existente = afpService.buscarPorCodigoSbs(afp.getCodigoSbs());
            if (existente != null) {
                respuesta.put("mensaje", "Ya existe una AFP con ese código SBS");
                return ResponseEntity.status(400).body(respuesta);
            }

            Afp nuevaAfp = afpService.guardarAfp(afp);
            respuesta.put("mensaje", "AFP creada exitosamente");
            respuesta.put("data", nuevaAfp);

            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al crear AFP: {}", e.getMessage());
            respuesta.put("mensaje", "Error al crear AFP");
            respuesta.put("error", e.getMessage());
            return ResponseEntity.status(500).body(respuesta);
        }
    }

    /**
     * Actualiza los datos de una AFP existente en base a su identificador.
     *
     * @param id identificador de la AFP que se desea actualizar
     * @param afp objeto AFP con los nuevos valores
     * @return una respuesta HTTP con el resultado de la actualización o un mensaje de error si no se encuentra
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarAfp(
            @PathVariable int id,
            @RequestBody Afp afp) {
        HashMap<String, Object> respuesta = new HashMap<>();
        try {
            logger.info("Actualizando AFP ID: {}", id);

            Afp existente = afpService.buscarPorId(id);
            if (existente == null) {
                respuesta.put("mensaje", "AFP no encontrada");
                return ResponseEntity.status(404).body(respuesta);
            }

            afp.setIdAfp(id);
            Afp actualizada = afpService.actualizarAfp(afp);

            respuesta.put("mensaje", "AFP actualizada exitosamente");
            respuesta.put("data", actualizada);

            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al actualizar AFP: {}", e.getMessage());
            respuesta.put("mensaje", "Error al actualizar AFP");
            respuesta.put("error", e.getMessage());
            return ResponseEntity.status(500).body(respuesta);
        }
    }

    /**
     * Marca una AFP como inactiva, simulando su eliminación lógica del sistema.
     *
     * @param id identificador de la AFP a eliminar
     * @return una respuesta HTTP con el resultado de la operación o un mensaje de error si no se encuentra la AFP
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarAfp(@PathVariable int id) {
        HashMap<String, Object> respuesta = new HashMap<>();
        try {
            logger.info("Eliminando AFP ID: {}", id);

            Afp existente = afpService.buscarPorId(id);
            if (existente == null) {
                respuesta.put("mensaje", "AFP no encontrada");
                return ResponseEntity.status(404).body(respuesta);
            }

            existente.setEstado("Inactivo");
            afpService.actualizarAfp(existente);

            respuesta.put("mensaje", "AFP eliminada exitosamente");
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al eliminar AFP: {}", e.getMessage());
            respuesta.put("mensaje", "Error al eliminar AFP");
            respuesta.put("error", e.getMessage());
            return ResponseEntity.status(500).body(respuesta);
        }
    }

    /**
     * Cambia el estado actual de una AFP.
     *
     * @param id identificador de la AFP a modificar
     * @param body mapa con la clave "estado" que contiene el nuevo valor a establecer
     * @return una respuesta HTTP con el resultado de la operación y la AFP actualizada
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable int id,
            @RequestBody HashMap<String, String> body) {
        HashMap<String, Object> respuesta = new HashMap<>();
        try {
            String nuevoEstado = body.get("estado");
            logger.info("Cambiando estado de AFP ID {} a {}", id, nuevoEstado);

            Afp existente = afpService.buscarPorId(id);
            if (existente == null) {
                respuesta.put("mensaje", "AFP no encontrada");
                return ResponseEntity.status(404).body(respuesta);
            }

            existente.setEstado(nuevoEstado);
            afpService.actualizarAfp(existente);

            respuesta.put("mensaje", "Estado actualizado exitosamente");
            respuesta.put("data", existente);

            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al cambiar estado: {}", e.getMessage());
            respuesta.put("mensaje", "Error al cambiar estado");
            return ResponseEntity.status(500).body(respuesta);
        }
    }

}