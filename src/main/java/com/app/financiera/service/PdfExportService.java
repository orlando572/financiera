package com.app.financiera.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.financiera.entity.AportePension;
import com.app.financiera.entity.Usuario;
import com.app.financiera.repository.UsuarioRepository;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;



@Service
public class PdfExportService {
    private static final Logger logger = LoggerFactory.getLogger(PdfExportService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AportePensionService aportePensionService;

    @Autowired
    private SaldoPensionService saldoPensionService;

    // Colores corporativos
    private static final DeviceRgb AZUL_PRINCIPAL = new DeviceRgb(41, 98, 255);
    private static final DeviceRgb GRIS_CLARO = new DeviceRgb(248, 249, 250);

    /**
     * Genera PDF del Panel Financiero
     */
    public byte[] generarPdfPanelFinanciero(int idUsuario) {
        logger.info("Generando PDF del Panel Financiero para usuario: {}", idUsuario);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Fuentes
            PdfFont fontBold = PdfFontFactory.createFont(
                    com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD);
            PdfFont fontRegular = PdfFontFactory.createFont(
                    com.itextpdf.io.font.constants.StandardFonts.HELVETICA);

            // Obtener datos del usuario
            Usuario usuario = usuarioRepository.findById(idUsuario)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // ENCABEZADO
            agregarEncabezado(document, fontBold, usuario);

            // RESUMEN FINANCIERO
            agregarResumenFinanciero(document, fontBold, fontRegular, idUsuario);

            // APORTES
            agregarTablaAportes(document, fontBold, fontRegular, idUsuario);

            // SALDOS
            agregarTablaSaldos(document, fontBold, fontRegular, idUsuario);

            // ESTADÍSTICAS
            agregarEstadisticas(document, fontBold, fontRegular, idUsuario);

            // PIE DE PÁGINA
            agregarPiePagina(document, fontRegular);

            document.close();
            logger.info("PDF generado exitosamente para usuario: {}", idUsuario);

            return baos.toByteArray();

        } catch (Exception e) {
            logger.error("Error generando PDF: {}", e.getMessage(), e);
            throw new RuntimeException("Error al generar PDF: " + e.getMessage());
        }
    }

    private void agregarEncabezado(Document document, PdfFont fontBold, Usuario usuario) {
        // Título principal
        Paragraph titulo = new Paragraph("PANEL FINANCIERO")
                .setFont(fontBold)
                .setFontSize(20)
                .setFontColor(AZUL_PRINCIPAL)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
        document.add(titulo);

        // Subtítulo
        Paragraph subtitulo = new Paragraph("Reporte de Pensiones y Aportes")
                .setFont(fontBold)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        document.add(subtitulo);

        // Información del usuario
        Paragraph infoUsuario = new Paragraph()
                .add(new Paragraph("Usuario: ").setFont(fontBold).setFontSize(10))
                .add(new Paragraph(usuario.getNombre() + " " + usuario.getApellido()).setFontSize(10))
                .add("\n")
                .add(new Paragraph("DNI: ").setFont(fontBold).setFontSize(10))
                .add(new Paragraph(usuario.getDni()).setFontSize(10))
                .add("\n")
                .add(new Paragraph("Fecha de generación: ").setFont(fontBold).setFontSize(10))
                .add(new Paragraph(LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).setFontSize(10))
                .setMarginBottom(20);
        document.add(infoUsuario);

        // Línea separadora
        document.add(new Paragraph("\n"));
    }

    private void agregarResumenFinanciero(Document document, PdfFont fontBold,
                                          PdfFont fontRegular, int idUsuario) {
        // Título de sección
        Paragraph tituloSeccion = new Paragraph("Resumen Financiero")
                .setFont(fontBold)
                .setFontSize(14)
                .setFontColor(AZUL_PRINCIPAL)
                .setMarginBottom(10);
        document.add(tituloSeccion);

        // Obtener datos
        Double saldoTotal = saldoPensionService.obtenerSaldoTotalUsuario(idUsuario);
        Double saldoDisponible = saldoPensionService.obtenerSaldosDisponibles(idUsuario);
        int yearActual = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        Double aportesYear = aportePensionService.obtenerTotalAportesUsuarioYear(idUsuario, yearActual);
        Double proyeccion = (saldoTotal != null && saldoTotal > 0) ? saldoTotal / 240 : 0;

        // Tabla de resumen
        Table tablaResumen = new Table(UnitValue.createPercentArray(new float[]{3, 2}))
                .useAllAvailableWidth()
                .setMarginBottom(20);

        // Fila 1
        tablaResumen.addCell(crearCeldaHeader("Saldo Total", fontBold));
        tablaResumen.addCell(crearCeldaDato("S/ " + String.format("%.2f",
                saldoTotal != null ? saldoTotal : 0), fontRegular));

        // Fila 2
        tablaResumen.addCell(crearCeldaHeader("Saldo Disponible", fontBold));
        tablaResumen.addCell(crearCeldaDato("S/ " + String.format("%.2f",
                saldoDisponible != null ? saldoDisponible : 0), fontRegular));

        // Fila 3
        tablaResumen.addCell(crearCeldaHeader("Aportes " + yearActual, fontBold));
        tablaResumen.addCell(crearCeldaDato("S/ " + String.format("%.2f",
                aportesYear != null ? aportesYear : 0), fontRegular));

        // Fila 4
        tablaResumen.addCell(crearCeldaHeader("Proyección Mensual", fontBold));
        tablaResumen.addCell(crearCeldaDato("S/ " + String.format("%.2f", proyeccion), fontRegular));

        document.add(tablaResumen);
    }

    private void agregarTablaAportes(Document document, PdfFont fontBold,
                                     PdfFont fontRegular, int idUsuario) {
        Paragraph tituloSeccion = new Paragraph("Detalle de Aportes")
                .setFont(fontBold)
                .setFontSize(14)
                .setFontColor(AZUL_PRINCIPAL)
                .setMarginBottom(10);
        document.add(tituloSeccion);

        List<AportePension> aportes = aportePensionService.obtenerAportesUsuario(idUsuario);

        if (aportes.isEmpty()) {
            document.add(new Paragraph("No hay aportes registrados.")
                    .setFont(fontRegular)
                    .setFontSize(10)
                    .setMarginBottom(20));
            return;
        }

        // Tabla de aportes (últimos 10)
        Table tablaAportes = new Table(UnitValue.createPercentArray(
                new float[]{2, 2, 2, 2, 2}))
                .useAllAvailableWidth()
                .setMarginBottom(20);

        // Headers
        tablaAportes.addHeaderCell(crearCeldaHeaderTabla("Período", fontBold));
        tablaAportes.addHeaderCell(crearCeldaHeaderTabla("Monto", fontBold));
        tablaAportes.addHeaderCell(crearCeldaHeaderTabla("Empleador", fontBold));
        tablaAportes.addHeaderCell(crearCeldaHeaderTabla("Sistema", fontBold));
        tablaAportes.addHeaderCell(crearCeldaHeaderTabla("Estado", fontBold));

        // Datos (máximo 10 aportes más recientes)
        int limite = Math.min(aportes.size(), 10);
        for (int i = 0; i < limite; i++) {
            AportePension aporte = aportes.get(i);

            tablaAportes.addCell(crearCeldaTabla(
                    aporte.getPeriodo() != null ? aporte.getPeriodo() : "-", fontRegular));
            tablaAportes.addCell(crearCeldaTabla(
                    "S/ " + String.format("%.2f", aporte.getMontoAporte()), fontRegular));
            tablaAportes.addCell(crearCeldaTabla(
                    aporte.getEmpleador() != null ? aporte.getEmpleador() : "-", fontRegular));
            tablaAportes.addCell(crearCeldaTabla(
                    aporte.getInstitucion() != null ? aporte.getInstitucion().getTipo() : "-", fontRegular));
            tablaAportes.addCell(crearCeldaTabla(
                    aporte.getEstado() != null ? aporte.getEstado() : "-", fontRegular));
        }

        document.add(tablaAportes);

        if (aportes.size() > 10) {
            document.add(new Paragraph("Mostrando los 10 aportes más recientes de " + aportes.size() + " totales.")
                    .setFont(fontRegular)
                    .setFontSize(8)
                    .setItalic()
                    .setMarginBottom(20));
        }
    }

    private void agregarTablaSaldos(Document document, PdfFont fontBold,
                                    PdfFont fontRegular, int idUsuario) {
        Paragraph tituloSeccion = new Paragraph("Detalle de Saldos")
                .setFont(fontBold)
                .setFontSize(14)
                .setFontColor(AZUL_PRINCIPAL)
                .setMarginBottom(10);
        document.add(tituloSeccion);

        var saldos = saldoPensionService.obtenerSaldosUsuario(idUsuario);

        if (saldos.isEmpty()) {
            document.add(new Paragraph("No hay saldos registrados.")
                    .setFont(fontRegular)
                    .setFontSize(10)
                    .setMarginBottom(20));
            return;
        }

        Table tablaSaldos = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 2}))
                .useAllAvailableWidth()
                .setMarginBottom(20);

        // Headers
        tablaSaldos.addHeaderCell(crearCeldaHeaderTabla("Tipo Fondo", fontBold));
        tablaSaldos.addHeaderCell(crearCeldaHeaderTabla("Saldo Total", fontBold));
        tablaSaldos.addHeaderCell(crearCeldaHeaderTabla("Disponible", fontBold));
        tablaSaldos.addHeaderCell(crearCeldaHeaderTabla("Rentabilidad", fontBold));

        // Datos
        for (var saldo : saldos) {
            tablaSaldos.addCell(crearCeldaTabla(
                    saldo.getTipoFondo() != null ? saldo.getTipoFondo().getNombreTipo() : "N/A",
                    fontRegular));
            tablaSaldos.addCell(crearCeldaTabla(
                    "S/ " + String.format("%.2f", saldo.getSaldoTotal()), fontRegular));
            tablaSaldos.addCell(crearCeldaTabla(
                    "S/ " + String.format("%.2f", saldo.getSaldoDisponible()), fontRegular));
            tablaSaldos.addCell(crearCeldaTabla(
                    String.format("%.2f%%", saldo.getRentabilidadAcumulada() != null ?
                            saldo.getRentabilidadAcumulada() : 0), fontRegular));
        }

        document.add(tablaSaldos);
    }

    private void agregarEstadisticas(Document document, PdfFont fontBold,
                                     PdfFont fontRegular, int idUsuario) {
        Paragraph tituloSeccion = new Paragraph("Estadísticas por Año")
                .setFont(fontBold)
                .setFontSize(14)
                .setFontColor(AZUL_PRINCIPAL)
                .setMarginBottom(10);
        document.add(tituloSeccion);

        int yearActual = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);

        Table tablaEstadisticas = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                .useAllAvailableWidth()
                .setMarginBottom(20);

        // Headers
        tablaEstadisticas.addHeaderCell(crearCeldaHeaderTabla("Año", fontBold));
        tablaEstadisticas.addHeaderCell(crearCeldaHeaderTabla("Total Aportes", fontBold));

        // Datos últimos 3 años
        for (int i = 2; i >= 0; i--) {
            int year = yearActual - i;
            Double total = aportePensionService.obtenerTotalAportesUsuarioYear(idUsuario, year);

            tablaEstadisticas.addCell(crearCeldaTabla(String.valueOf(year), fontRegular));
            tablaEstadisticas.addCell(crearCeldaTabla(
                    "S/ " + String.format("%.2f", total != null ? total : 0), fontRegular));
        }

        document.add(tablaEstadisticas);
    }

    private void agregarPiePagina(Document document, PdfFont fontRegular) {
        document.add(new Paragraph("\n\n"));

        Paragraph piePagina = new Paragraph()
                .add("Este documento es un reporte generado automáticamente por SumaqSeguros.\n")
                .add("Fecha de generación: " + LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                .setFont(fontRegular)
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.GRAY);

        document.add(piePagina);
    }

    // Métodos auxiliares para crear celdas
    private Cell crearCeldaHeader(String texto, PdfFont font) {
        return new Cell()
                .add(new Paragraph(texto).setFont(font).setFontSize(10))
                .setBackgroundColor(GRIS_CLARO)
                .setPadding(5);
    }

    private Cell crearCeldaDato(String texto, PdfFont font) {
        return new Cell()
                .add(new Paragraph(texto).setFont(font).setFontSize(10))
                .setPadding(5);
    }

    private Cell crearCeldaHeaderTabla(String texto, PdfFont font) {
        return new Cell()
                .add(new Paragraph(texto).setFont(font).setFontSize(9))
                .setBackgroundColor(AZUL_PRINCIPAL)
                .setFontColor(ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(5);
    }

    private Cell crearCeldaTabla(String texto, PdfFont font) {
        return new Cell()
                .add(new Paragraph(texto).setFont(font).setFontSize(8))
                .setPadding(4)
                .setTextAlignment(TextAlignment.LEFT);
    }
}
