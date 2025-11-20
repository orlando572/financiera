package com.app.financiera.controller;

import com.app.financiera.entity.CompaniaSeguro;
import com.app.financiera.entity.Seguro;
import com.app.financiera.entity.TipoSeguro;
import com.app.financiera.service.ComparadorService;
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
 * Controlador REST para la gestión y comparación de seguros.
 * Permite obtener categorías, tipos, compañías, planes y estadísticas,
 * además de ofrecer filtrado y comparación de diferentes seguros.
 */
@RestController
@RequestMapping("/api/comparador")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class ComparadorController {

    private static final Logger logger = LoggerFactory.getLogger(ComparadorController.class);

    @Autowired
    private ComparadorService comparadorService;

    /**
     * Obtiene todas las categorías de seguros disponibles.
     *
     * @return una respuesta HTTP con la lista de categorías o un mensaje de error si ocurre una excepción
     */
    @GetMapping("/categorias")
    public ResponseEntity<?> obtenerCategorias() {
        logger.info("Solicitud de categorías de seguros");
        try {
            List<String> categorias = comparadorService.obtenerCategorias();
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            logger.error("Error al obtener categorías: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener categorías");
        }
    }

    /**
     * Obtiene los tipos de seguros pertenecientes a una categoría específica.
     *
     * @param categoria nombre de la categoría de seguros
     * @return una respuesta HTTP con la lista de tipos de seguro o un mensaje de error en caso de fallo
     */
    @GetMapping("/tipos/{categoria}")
    public ResponseEntity<?> obtenerTiposPorCategoria(@PathVariable String categoria) {
        logger.info("Solicitud de tipos de seguro para categoría: {}", categoria);
        try {
            List<TipoSeguro> tipos = comparadorService.obtenerTiposPorCategoria(categoria);
            return ResponseEntity.ok(tipos);
        } catch (Exception e) {
            logger.error("Error al obtener tipos: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener tipos de seguro");
        }
    }

    /**
     * Obtiene la lista completa de compañías de seguros disponibles.
     *
     * @return una respuesta HTTP con la lista de compañías o un mensaje de error en caso de fallo
     */
    @GetMapping("/companias")
    public ResponseEntity<?> obtenerCompanias() {
        logger.info("Solicitud de compañías de seguros");
        try {
            List<CompaniaSeguro> companias = comparadorService.obtenerCompanias();
            return ResponseEntity.ok(companias);
        } catch (Exception e) {
            logger.error("Error al obtener compañías: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener compañías");
        }
    }

    /**
     * Obtiene los seguros asociados a una categoría específica.
     *
     * @param categoria nombre de la categoría
     * @return una respuesta HTTP con la lista de seguros o un mensaje de error en caso de fallo
     */
    @GetMapping("/seguros/categoria/{categoria}")
    public ResponseEntity<?> obtenerSegurosPorCategoria(@PathVariable String categoria) {
        logger.info("Solicitud de seguros por categoría: {}", categoria);
        try {
            List<Seguro> seguros = comparadorService.obtenerSegurosPorCategoria(categoria);
            return ResponseEntity.ok(seguros);
        } catch (Exception e) {
            logger.error("Error al obtener seguros: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener seguros");
        }
    }

    /**
     * Obtiene los seguros asociados a un tipo de seguro específico.
     *
     * @param idTipo identificador del tipo de seguro
     * @return una respuesta HTTP con la lista de seguros o un mensaje de error en caso de fallo
     */
    @GetMapping("/seguros/tipo/{idTipo}")
    public ResponseEntity<?> obtenerSegurosPorTipo(@PathVariable int idTipo) {
        logger.info("Solicitud de seguros por tipo: {}", idTipo);
        try {
            List<Seguro> seguros = comparadorService.obtenerSegurosPorTipo(idTipo);
            return ResponseEntity.ok(seguros);
        } catch (Exception e) {
            logger.error("Error al obtener seguros: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener seguros");
        }
    }

    /**
     * Obtiene los seguros ofrecidos por una compañía específica.
     *
     * @param idCompania identificador de la compañía de seguros
     * @return una respuesta HTTP con la lista de seguros o un mensaje de error en caso de fallo
     */
    @GetMapping("/seguros/compania/{idCompania}")
    public ResponseEntity<?> obtenerSegurosPorCompania(@PathVariable int idCompania) {
        logger.info("Solicitud de seguros por compañía: {}", idCompania);
        try {
            List<Seguro> seguros = comparadorService.obtenerSegurosPorCompania(idCompania);
            return ResponseEntity.ok(seguros);
        } catch (Exception e) {
            logger.error("Error al obtener seguros: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener seguros");
        }
    }

    /**
     * Compara los planes de seguros seleccionados por el usuario.
     * Valida el número de planes y realiza una comparación detallada de sus atributos.
     * @param request mapa con los identificadores de los planes a comparar
     * @return una respuesta HTTP con el resultado de la comparación o un mensaje de error si ocurre una excepción
     */
    @PostMapping("/comparar")
    public ResponseEntity<?> compararPlanes(@RequestBody Map<String, List<Integer>> request) {
        List<Integer> idsPlanes = request.get("idsPlanes");
        logger.info("Solicitud de comparación de {} planes", idsPlanes.size());

        try {
            if (idsPlanes == null || idsPlanes.isEmpty()) {
                return ResponseEntity.badRequest().body("Debe seleccionar al menos un plan");
            }

            if (idsPlanes.size() > 5) {
                return ResponseEntity.badRequest().body("No puede comparar más de 5 planes");
            }

            Map<String, Object> comparacion = comparadorService.compararSeguros(idsPlanes);
            return ResponseEntity.ok(comparacion);
        } catch (Exception e) {
            logger.error("Error al comparar planes: {}", e.getMessage());
            HashMap<String, Object> error = new HashMap<>();
            error.put("mensaje", "Error al comparar planes");
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    /**
     * Filtra los seguros según los criterios especificados como categoría, primas o coberturas.
     *
     * @param categoria categoría de seguro
     * @param primaMin prima mínima (opcional)
     * @param primaMax prima máxima (opcional)
     * @param coberturaMin cobertura mínima (opcional)
     * @param coberturaMax cobertura máxima (opcional)
     * @return una respuesta HTTP con la lista de seguros filtrados o un mensaje de error si ocurre una excepción
     */
    @GetMapping("/filtrar")
    public ResponseEntity<?> filtrarSeguros(
            @RequestParam String categoria,
            @RequestParam(required = false) Double primaMin,
            @RequestParam(required = false) Double primaMax,
            @RequestParam(required = false) Double coberturaMin,
            @RequestParam(required = false) Double coberturaMax) {

        logger.info("Filtrando seguros - Categoría: {}", categoria);
        try {
            List<Seguro> seguros = comparadorService.filtrarSeguros(
                    categoria, primaMin, primaMax, coberturaMin, coberturaMax
            );
            return ResponseEntity.ok(seguros);
        } catch (Exception e) {
            logger.error("Error al filtrar seguros: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al filtrar seguros");
        }
    }

    /**
     * Obtiene un resumen detallado del plan de seguro seleccionado.
     *
     * @param idSeguro identificador del seguro
     * @return una respuesta HTTP con el resumen del plan o un mensaje de error en caso de fallo
     */
    @GetMapping("/plan/{idSeguro}")
    public ResponseEntity<?> obtenerResumenPlan(@PathVariable int idSeguro) {
        logger.info("Solicitud de resumen de plan: {}", idSeguro);
        try {
            Map<String, Object> resumen = comparadorService.obtenerResumenPlan(idSeguro);

            if (resumen.get("exitoso") != null && !(Boolean) resumen.get("exitoso")) {
                return ResponseEntity.status(404).body(resumen);
            }

            return ResponseEntity.ok(resumen);
        } catch (Exception e) {
            logger.error("Error al obtener resumen: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener resumen del plan");
        }
    }

    /**
     * Obtiene estadísticas generales relacionadas con una categoría de seguros.
     *
     * @param categoria nombre de la categoría de seguros
     * @return una respuesta HTTP con los datos estadísticos o un mensaje de error si ocurre una excepción
     */
    @GetMapping("/estadisticas/{categoria}")
    public ResponseEntity<?> obtenerEstadisticas(@PathVariable String categoria) {
        logger.info("Solicitud de estadísticas para categoría: {}", categoria);
        try {
            Map<String, Object> estadisticas = comparadorService.obtenerEstadisticasCategoria(categoria);
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            logger.error("Error al obtener estadísticas: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener estadísticas");
        }
    }

    /**
     * Verifica el estado de salud del servicio del comparador.
     *
     * @return una respuesta HTTP con información del estado del servicio y versión actual
     */
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        HashMap<String, Object> health = new HashMap<>();
        health.put("status", "OK");
        health.put("servicio", "Comparador de Seguros");
        health.put("version", "1.0.0");
        return ResponseEntity.ok(health);
    }
}