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

@RestController
@RequestMapping("/api/catalogos")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class CatalogosController {

    private static final Logger logger = LoggerFactory.getLogger(CatalogosController.class);

    @Autowired
    private CatalogosService catalogosService;

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

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok("Catálogos API OK");
    }
}
