package com.app.financiera.controller;

import com.app.financiera.entity.AportePension;
import com.app.financiera.entity.SaldoPension;
import com.app.financiera.entity.TipoFondo;
import com.app.financiera.service.AportePensionService;
import com.app.financiera.service.FinancieroService;
import com.app.financiera.service.SaldoPensionService;
import com.app.financiera.service.TipoFondoService;
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
@RequestMapping("/api/financiero")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class FinancieroController {

    private static final Logger logger = LoggerFactory.getLogger(FinancieroController.class);

    @Autowired
    private FinancieroService financieroService;

    @Autowired
    private AportePensionService aportePensionService;

    @Autowired
    private SaldoPensionService saldoPensionService;

    @Autowired
    private TipoFondoService tipoFondoService;

    @GetMapping("/resumen/{idUsuario}")
    public ResponseEntity<?> obtenerResumenFinanciero(@PathVariable int idUsuario) {
        logger.info("Solicitud de resumen financiero para usuario ID: {}", idUsuario);
        try {
            HashMap<String, Object> resumen = financieroService.obtenerResumenFinanciero(idUsuario);
            logger.info("Resumen financiero generado correctamente para usuario ID: {}", idUsuario);
            return ResponseEntity.ok(resumen);
        } catch (Exception e) {
            logger.error("Error al obtener resumen financiero para usuario ID {}: {}", idUsuario, e.getMessage());
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al obtener resumen financiero", "error", e.getMessage()));
        }
    }

    // Aportes
    @GetMapping("/aportes/{idUsuario}")
    public ResponseEntity<?> obtenerAportesUsuario(@PathVariable int idUsuario) {
        logger.info("Solicitud de aportes para usuario ID: {}", idUsuario);
        try {
            List<AportePension> aportes = aportePensionService.obtenerAportesUsuario(idUsuario);
            logger.info("Se obtuvieron {} aportes para usuario ID {}", aportes.size(), idUsuario);
            return ResponseEntity.ok(aportes);
        } catch (Exception e) {
            logger.error("Error al obtener aportes para usuario ID {}: {}", idUsuario, e.getMessage(), e);
            return ResponseEntity.status(500).body("Error al obtener aportes");
        }
    }

    @GetMapping("/aportes/{idUsuario}/{year}")
    public ResponseEntity<?> obtenerAportesYear(@PathVariable int idUsuario, @PathVariable int year) {
        logger.info("Solicitud de aportes del año {} para usuario ID: {}", year, idUsuario);
        try {
            List<AportePension> aportes = aportePensionService.obtenerAportesUsuarioYear(idUsuario, year);
            return ResponseEntity.ok(aportes);
        } catch (Exception e) {
            logger.error("Error al obtener aportes del año {} para usuario ID {}: {}", year, idUsuario, e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener aportes del año");
        }
    }

    @GetMapping("/aportes/{idUsuario}/sistema/{sistema}")
    public ResponseEntity<?> obtenerAportesPorSistema(@PathVariable int idUsuario, @PathVariable String sistema) {
        logger.info("Solicitud de aportes del sistema {} para usuario ID: {}", sistema, idUsuario);
        try {
            List<AportePension> aportes = aportePensionService.obtenerAportesPorSistema(idUsuario, sistema);
            return ResponseEntity.ok(aportes);
        } catch (Exception e) {
            logger.error("Error al obtener aportes del sistema {} para usuario ID {}: {}", sistema, idUsuario, e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener aportes por sistema");
        }
    }

    @PostMapping("/aportes")
    public ResponseEntity<?> crearAporte(@RequestBody AportePension aporte) {
        logger.info("Solicitud para crear aporte para usuario ID: {}", aporte.getUsuario().getIdUsuario());
        try {
            AportePension nuevoAporte = aportePensionService.guardarAporte(aporte);
            return ResponseEntity.ok(Map.of("mensaje", "Aporte registrado exitosamente", "data", nuevoAporte));
        } catch (Exception e) {
            logger.error("Error al registrar aporte: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al registrar aporte", "error", e.getMessage()));
        }
    }

    // Saldos
    @GetMapping("/saldos/{idUsuario}")
    public ResponseEntity<?> obtenerSaldosUsuario(@PathVariable int idUsuario) {
        logger.info("Solicitud de saldos para usuario ID: {}", idUsuario);
        try {
            List<SaldoPension> saldos = saldoPensionService.obtenerSaldosUsuario(idUsuario);
            logger.info("Se obtuvieron {} saldos para usuario ID {}", saldos.size(), idUsuario);
            return ResponseEntity.ok(saldos);
        } catch (Exception e) {
            logger.error("Error al obtener saldos para usuario ID {}: {}", idUsuario, e.getMessage(), e);
            return ResponseEntity.status(500).body("Error al obtener saldos");
        }
    }

    @GetMapping("/saldos/{idUsuario}/total")
    public ResponseEntity<?> obtenerSaldoTotal(@PathVariable int idUsuario) {
        logger.info("Solicitud de saldo total para usuario ID: {}", idUsuario);
        try {
            HashMap<String, Object> resultado = financieroService.obtenerSaldoTotalYDisponible(idUsuario);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            logger.error("Error al obtener saldo total para usuario ID {}: {}", idUsuario, e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener saldo total");
        }
    }

    @GetMapping("/estadisticas/{idUsuario}")
    public ResponseEntity<?> obtenerEstadisticas(@PathVariable int idUsuario) {
        logger.info("Solicitud de estadísticas para usuario ID: {}", idUsuario);
        try {
            HashMap<String, Object> stats = financieroService.obtenerEstadisticasFinancieras(idUsuario);
            logger.info("Estadísticas generadas correctamente para usuario ID {}", idUsuario);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error al obtener estadísticas para usuario ID {}: {}", idUsuario, e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener estadísticas");
        }
    }

    @GetMapping("/fondos")
    public ResponseEntity<?> obtenerFondosActivos() {
        logger.info("Solicitud de fondos activos");
        try {
            List<TipoFondo> fondos = tipoFondoService.obtenerFondosActivos();
            return ResponseEntity.ok(fondos);
        } catch (Exception e) {
            logger.error("Error al obtener fondos: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener fondos");
        }
    }

    // Analisis comparativo
    @GetMapping("/comparativo/{idUsuario}")
    public ResponseEntity<?> obtenerComparativo(@PathVariable int idUsuario) {
        logger.info("Solicitud de comparativo financiero para usuario ID: {}", idUsuario);
        try {
            HashMap<String, Object> comparativo = financieroService.obtenerComparativoSistemas(idUsuario);
            logger.info("Comparativo generado correctamente para usuario ID {}", idUsuario);
            return ResponseEntity.ok(comparativo);
        } catch (Exception e) {
            logger.error("Error al obtener comparativo financiero para usuario ID {}: {}", idUsuario, e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener comparativo");
        }
    }
}