package com.app.financiera.controller;

import com.app.financiera.entity.CompaniaSeguro;
import com.app.financiera.entity.TipoSeguro;
import com.app.financiera.service.CatalogosService;
import com.app.financiera.util.AppSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión y consulta de catálogos del sistema.
 * Proporciona endpoints públicos para obtener tipos de seguros, categorías y compañías aseguradoras activas.
 */
@RestController
@RequestMapping("/api/catalogos")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class CatalogosController {

    private static final Logger logger = LoggerFactory.getLogger(CatalogosController.class);

    @Autowired
    private CatalogosService catalogosService;

    /**
     * Obtiene la lista de tipos de seguro activos registrados en el sistema.
     *
     * @return una respuesta HTTP con la lista de tipos de seguro activos o un mensaje de error en caso de fallo
     */
    @GetMapping("/tipos-seguro")
    public ResponseEntity<?> obtenerTiposSeguros() {
        logger.info("Solicitud de tipos de seguro");
        try {
            List<TipoSeguro> tipos = catalogosService.obtenerTiposSegurosActivos();
            return ResponseEntity.ok(tipos);
        } catch (Exception e) {
            logger.error("Error al obtener tipos de seguro: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener tipos de seguro");
        }
    }

    /**
     * Obtiene los tipos de seguro filtrados por una categoría específica.
     *
     * @param categoria nombre de la categoría por la cual se desea filtrar los tipos de seguro
     * @return una respuesta HTTP con la lista de tipos de seguro que pertenecen a la categoría indicada,
     *         o un mensaje de error si ocurre una excepción
     */
    @GetMapping("/tipos-seguro/categoria/{categoria}")
    public ResponseEntity<?> obtenerTiposPorCategoria(@PathVariable String categoria) {
        logger.info("Solicitud de tipos de seguro por categoría: {}", categoria);
        try {
            List<TipoSeguro> tipos = catalogosService.obtenerTiposPorCategoria(categoria);
            return ResponseEntity.ok(tipos);
        } catch (Exception e) {
            logger.error("Error al obtener tipos por categoría: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener tipos de seguro");
        }
    }

    /**
     * Obtiene la lista de todas las categorías de seguros disponibles en el sistema.
     *
     * @return una respuesta HTTP con la lista de categorías o un mensaje de error si ocurre un fallo
     */
    @GetMapping("/categorias")
    public ResponseEntity<?> obtenerCategorias() {
        logger.info("Solicitud de categorías de seguros");
        try {
            List<String> categorias = catalogosService.obtenerCategorias();
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            logger.error("Error al obtener categorías: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener categorías");
        }
    }

    /**
     * Obtiene la lista de compañías de seguros activas registradas en el sistema.
     *
     * @return ResponseEntity con la lista de compañías activas o un mensaje de error si ocurre una excepción
     */
    @GetMapping("/companias")
    public ResponseEntity<?> obtenerCompanias() {
        logger.info("Solicitud de compañías de seguros");
        try {
            List<CompaniaSeguro> companias = catalogosService.obtenerCompaniasActivas();
            return ResponseEntity.ok(companias);
        } catch (Exception e) {
            logger.error("Error al obtener compañías: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener compañías");
        }
    }

    /**
     * Busca compañías de seguros por nombre.
     * La búsqueda se realiza con el parámetro 'nombre' enviado como query string.
     *
     * @param nombre texto que se utilizará para buscar coincidencias en el nombre de las compañías
     * @return ResponseEntity con las compañías encontradas o un mensaje de error en caso de fallo
     */
    @GetMapping("/companias/buscar")
    public ResponseEntity<?> buscarCompanias(@RequestParam String nombre) {
        logger.info("Búsqueda de compañías: {}", nombre);
        try {
            List<CompaniaSeguro> companias = catalogosService.buscarCompaniasPorNombre(nombre);
            return ResponseEntity.ok(companias);
        } catch (Exception e) {
            logger.error("Error al buscar compañías: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al buscar compañías");
        }
    }

    /**
     * Verifica el estado del servicio de catálogos.
     * Se utiliza como endpoint de comprobación de salud del API.
     *
     * @return ResponseEntity con un texto indicando que el servicio está activo
     */
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok("Catálogos API OK");
    }
}
