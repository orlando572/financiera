package com.app.financiera.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.financiera.service.PdfExportService;
import com.app.financiera.util.AppSettings;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/pdf-export")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)

public class PdfExportController {

    private static final Logger logger = LoggerFactory.getLogger(PdfExportController.class);

    @Autowired
    private PdfExportService pdfExportService;

    /**
     * Endpoint para exportar el Panel Financiero a PDF
     * GET /api/pdf-export/panel-financiero/{idUsuario}
     */
    @GetMapping("/panel-financiero/{idUsuario}")
    public ResponseEntity<byte[]> exportarPanelFinanciero(@PathVariable int idUsuario) {
        logger.info("Solicitud de exportación PDF del Panel Financiero para usuario: {}", idUsuario);

        try {
            // Generar PDF
            byte[] pdfBytes = pdfExportService.generarPdfPanelFinanciero(idUsuario);

            // Nombre del archivo con fecha y hora
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = "Panel_Financiero_" + timestamp + ".pdf";

            // Configurar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(pdfBytes.length);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            logger.info("PDF generado exitosamente: {} ({} bytes)", filename, pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error al exportar PDF para usuario {}: {}", idUsuario, e.getMessage(), e);

            // Devolver error con mensaje
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String errorJson = String.format(
                    "{\"error\": true, \"mensaje\": \"Error al generar PDF: %s\"}",
                    e.getMessage()
            );

            return new ResponseEntity<>(errorJson.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*Endpoint de prueba*/

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok()
                .body("{\"status\": \"OK\", \"servicio\": \"Exportación PDF\", \"version\": \"1.0.0\"}");
    }

    /**
     * Endpoint para exportar Gestión de Pensiones a PDF
     * GET /api/pdf-export/gestion-pensiones/{idUsuario}
     */
    @GetMapping("/gestion-pensiones/{idUsuario}")
    public ResponseEntity<byte[]> exportarGestionPensiones(@PathVariable int idUsuario) {
        logger.info("Solicitud de exportación PDF de Gestión de Pensiones para usuario: {}", idUsuario);

        try {
            // Generar PDF
            byte[] pdfBytes = pdfExportService.generarPdfGestionPensiones(idUsuario);

            // Nombre del archivo con fecha y hora
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = "Gestion_Pensiones_" + timestamp + ".pdf";

            // Configurar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(pdfBytes.length);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            logger.info("PDF de Gestión de Pensiones generado exitosamente: {} ({} bytes)",
                    filename, pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error al exportar PDF de Gestión de Pensiones para usuario {}: {}",
                    idUsuario, e.getMessage(), e);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String errorJson = String.format(
                    "{\"error\": true, \"mensaje\": \"Error al generar PDF de Pensiones: %s\"}",
                    e.getMessage()
            );

            return new ResponseEntity<>(errorJson.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para exportar Gestión de Seguros a PDF
     * GET /api/pdf-export/gestion-seguros/{idUsuario}
     */
    @GetMapping("/gestion-seguros/{idUsuario}")
    public ResponseEntity<byte[]> exportarGestionSeguros(@PathVariable int idUsuario) {
        logger.info("Solicitud de exportación PDF de Gestión de Seguros para usuario: {}", idUsuario);

        try {
            // Generar PDF
            byte[] pdfBytes = pdfExportService.generarPdfGestionSeguros(idUsuario);

            // Nombre del archivo con fecha y hora
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = "Gestion_Seguros_" + timestamp + ".pdf";

            // Configurar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(pdfBytes.length);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            logger.info("PDF de Gestión de Seguros generado exitosamente: {} ({} bytes)",
                    filename, pdfBytes.length);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error al exportar PDF de Gestión de Seguros para usuario {}: {}",
                    idUsuario, e.getMessage(), e);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String errorJson = String.format(
                    "{\"error\": true, \"mensaje\": \"Error al generar PDF de Seguros: %s\"}",
                    e.getMessage()
            );

            return new ResponseEntity<>(errorJson.getBytes(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
