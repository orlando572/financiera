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
    /**
     * Genera PDF de Gestión de Pensiones
     */
    public byte[] generarPdfGestionPensiones(int idUsuario) {
        logger.info("Generando PDF de Gestión de Pensiones para usuario: {}", idUsuario);

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
            agregarEncabezadoPensiones(document, fontBold, usuario);

            // INFORMACIÓN DEL SISTEMA DE PENSIONES
            agregarInfoSistemaPensiones(document, fontBold, fontRegular, usuario);

            // RESUMEN DE PENSIONES
            agregarResumenPensiones(document, fontBold, fontRegular, idUsuario);

            // APORTES ONP
            agregarAportesONP(document, fontBold, fontRegular, idUsuario);

            // APORTES AFP
            agregarAportesAFP(document, fontBold, fontRegular, idUsuario);

            // PROYECCIONES
            agregarProyeccionesPensiones(document, fontBold, fontRegular, idUsuario);

            // ESTADO DE APORTES
            agregarEstadoAportes(document, fontBold, fontRegular, idUsuario);

            // PIE DE PÁGINA
            agregarPiePagina(document, fontRegular);

            document.close();
            logger.info("PDF de Gestión de Pensiones generado exitosamente para usuario: {}", idUsuario);

            return baos.toByteArray();

        } catch (Exception e) {
            logger.error("Error generando PDF de Pensiones: {}", e.getMessage(), e);
            throw new RuntimeException("Error al generar PDF: " + e.getMessage());
        }
    }

    /**
     * Genera PDF de Gestión de Seguros
     */
    public byte[] generarPdfGestionSeguros(int idUsuario) {
        logger.info("Generando PDF de Gestión de Seguros para usuario: {}", idUsuario);

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
            agregarEncabezadoSeguros(document, fontBold, usuario);

            // RESUMEN DE SEGUROS
            agregarResumenSeguros(document, fontBold, fontRegular, idUsuario);

            // PÓLIZAS ACTIVAS
            agregarPolizasActivas(document, fontBold, fontRegular, idUsuario);

            // BENEFICIARIOS
            agregarBeneficiarios(document, fontBold, fontRegular, idUsuario);

            // PAGOS PENDIENTES
            agregarPagosPendientes(document, fontBold, fontRegular, idUsuario);

            // TRÁMITES
            agregarTramites(document, fontBold, fontRegular, idUsuario);

            // ESTADÍSTICAS DE SEGUROS
            agregarEstadisticasSeguros(document, fontBold, fontRegular, idUsuario);

            // PIE DE PÁGINA
            agregarPiePagina(document, fontRegular);

            document.close();
            logger.info("PDF de Gestión de Seguros generado exitosamente para usuario: {}", idUsuario);

            return baos.toByteArray();

        } catch (Exception e) {
            logger.error("Error generando PDF de Seguros: {}", e.getMessage(), e);
            throw new RuntimeException("Error al generar PDF: " + e.getMessage());
        }
    }

// ============== MÉTODOS AUXILIARES PARA GESTIÓN DE PENSIONES ==============

    private void agregarEncabezadoPensiones(Document document, PdfFont fontBold, Usuario usuario) {
        Paragraph titulo = new Paragraph("GESTIÓN DE PENSIONES")
                .setFont(fontBold)
                .setFontSize(20)
                .setFontColor(AZUL_PRINCIPAL)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
        document.add(titulo);

        Paragraph subtitulo = new Paragraph("Reporte Detallado de Aportes y Proyecciones")
                .setFont(fontBold)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        document.add(subtitulo);

        Paragraph infoUsuario = new Paragraph()
                .add(new Paragraph("Afiliado: ").setFont(fontBold).setFontSize(10))
                .add(new Paragraph(usuario.getNombre() + " " + usuario.getApellido()).setFontSize(10))
                .add("\n")
                .add(new Paragraph("DNI: ").setFont(fontBold).setFontSize(10))
                .add(new Paragraph(usuario.getDni()).setFontSize(10))
                .add("\n")
                .add(new Paragraph("CUSPP: ").setFont(fontBold).setFontSize(10))
                .add(new Paragraph(usuario.getCuspp() != null ? usuario.getCuspp() : "No disponible").setFontSize(10))
                .add("\n")
                .add(new Paragraph("Fecha de generación: ").setFont(fontBold).setFontSize(10))
                .add(new Paragraph(LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).setFontSize(10))
                .setMarginBottom(20);
        document.add(infoUsuario);

        document.add(new Paragraph("\n"));
    }

    private void agregarInfoSistemaPensiones(Document document, PdfFont fontBold,
                                             PdfFont fontRegular, Usuario usuario) {
        Paragraph tituloSeccion = new Paragraph("Información del Sistema de Pensiones")
                .setFont(fontBold)
                .setFontSize(14)
                .setFontColor(AZUL_PRINCIPAL)
                .setMarginBottom(10);
        document.add(tituloSeccion);

        Table tablaInfo = new Table(UnitValue.createPercentArray(new float[]{3, 2}))
                .useAllAvailableWidth()
                .setMarginBottom(20);

        tablaInfo.addCell(crearCeldaHeader("Tipo de Régimen", fontBold));
        tablaInfo.addCell(crearCeldaDato(
                usuario.getTipoRegimen() != null ? usuario.getTipoRegimen() : "No especificado",
                fontRegular));

        tablaInfo.addCell(crearCeldaHeader("AFP Afiliada", fontBold));
        tablaInfo.addCell(crearCeldaDato(
                usuario.getAfp() != null ? usuario.getAfp().getNombre() : "No afiliado",
                fontRegular));

        if (usuario.getFechaAfiliacion() != null) {
            tablaInfo.addCell(crearCeldaHeader("Fecha de Afiliación", fontBold));
            tablaInfo.addCell(crearCeldaDato(
                    new java.text.SimpleDateFormat("dd/MM/yyyy").format(usuario.getFechaAfiliacion()),
                    fontRegular));
        }

        document.add(tablaInfo);
    }

    private void agregarResumenPensiones(Document document, PdfFont fontBold,
                                         PdfFont fontRegular, int idUsuario) {
        Paragraph tituloSeccion = new Paragraph("Resumen General")
                .setFont(fontBold)
                .setFontSize(14)
                .setFontColor(AZUL_PRINCIPAL)
                .setMarginBottom(10);
        document.add(tituloSeccion);

        Double saldoTotal = saldoPensionService.obtenerSaldoTotalUsuario(idUsuario);
        Double saldoDisponible = saldoPensionService.obtenerSaldosDisponibles(idUsuario);

        List<AportePension> aportesONP = aportePensionService.obtenerAportesPorSistema(idUsuario, "Pensiones");
        Double montoONP = aportesONP.stream()
                .mapToDouble(a -> a.getMontoAporte() != null ? a.getMontoAporte() : 0).sum();

        List<AportePension> aportesAFP = aportePensionService.obtenerAportesPorSistema(idUsuario, "Financiera");
        Double montoAFP = aportesAFP.stream()
                .mapToDouble(a -> a.getMontoAporte() != null ? a.getMontoAporte() : 0).sum();

        Table tablaResumen = new Table(UnitValue.createPercentArray(new float[]{3, 2}))
                .useAllAvailableWidth()
                .setMarginBottom(20);

        tablaResumen.addCell(crearCeldaHeader("Saldo Total Acumulado", fontBold));
        tablaResumen.addCell(crearCeldaDato("S/ " + String.format("%.2f",
                saldoTotal != null ? saldoTotal : 0), fontRegular));

        tablaResumen.addCell(crearCeldaHeader("Saldo Disponible", fontBold));
        tablaResumen.addCell(crearCeldaDato("S/ " + String.format("%.2f",
                saldoDisponible != null ? saldoDisponible : 0), fontRegular));

        tablaResumen.addCell(crearCeldaHeader("Total Aportes ONP", fontBold));
        tablaResumen.addCell(crearCeldaDato("S/ " + String.format("%.2f", montoONP), fontRegular));

        tablaResumen.addCell(crearCeldaHeader("Total Aportes AFP", fontBold));
        tablaResumen.addCell(crearCeldaDato("S/ " + String.format("%.2f", montoAFP), fontRegular));

        tablaResumen.addCell(crearCeldaHeader("Total General", fontBold));
        tablaResumen.addCell(crearCeldaDato("S/ " + String.format("%.2f", montoONP + montoAFP), fontRegular));

        document.add(tablaResumen);
    }

    private void agregarAportesONP(Document document, PdfFont fontBold,
                                   PdfFont fontRegular, int idUsuario) {
        Paragraph tituloSeccion = new Paragraph("Aportes ONP")
                .setFont(fontBold)
                .setFontSize(14)
                .setFontColor(AZUL_PRINCIPAL)
                .setMarginBottom(10);
        document.add(tituloSeccion);

        List<AportePension> aportesONP = aportePensionService.obtenerAportesPorSistema(idUsuario, "Pensiones");

        if (aportesONP.isEmpty()) {
            document.add(new Paragraph("No hay aportes ONP registrados.")
                    .setFont(fontRegular)
                    .setFontSize(10)
                    .setMarginBottom(20));
            return;
        }

        Table tablaONP = new Table(UnitValue.createPercentArray(new float[]{2, 2, 3, 2}))
                .useAllAvailableWidth()
                .setMarginBottom(20);

        tablaONP.addHeaderCell(crearCeldaHeaderTabla("Período", fontBold));
        tablaONP.addHeaderCell(crearCeldaHeaderTabla("Monto", fontBold));
        tablaONP.addHeaderCell(crearCeldaHeaderTabla("Empleador", fontBold));
        tablaONP.addHeaderCell(crearCeldaHeaderTabla("Estado", fontBold));

        int limite = Math.min(aportesONP.size(), 15);
        for (int i = 0; i < limite; i++) {
            AportePension aporte = aportesONP.get(i);
            tablaONP.addCell(crearCeldaTabla(aporte.getPeriodo() != null ? aporte.getPeriodo() : "-", fontRegular));
            tablaONP.addCell(crearCeldaTabla("S/ " + String.format("%.2f", aporte.getMontoAporte()), fontRegular));
            tablaONP.addCell(crearCeldaTabla(aporte.getEmpleador() != null ? aporte.getEmpleador() : "-", fontRegular));
            tablaONP.addCell(crearCeldaTabla(aporte.getEstado() != null ? aporte.getEstado() : "-", fontRegular));
        }

        document.add(tablaONP);

        if (aportesONP.size() > 15) {
            document.add(new Paragraph("Mostrando los 15 aportes más recientes de " + aportesONP.size() + " totales.")
                    .setFont(fontRegular)
                    .setFontSize(8)
                    .setItalic()
                    .setMarginBottom(20));
        }
    }

    private void agregarAportesAFP(Document document, PdfFont fontBold,
                                   PdfFont fontRegular, int idUsuario) {
        Paragraph tituloSeccion = new Paragraph("Aportes AFP")
                .setFont(fontBold)
                .setFontSize(14)
                .setFontColor(AZUL_PRINCIPAL)
                .setMarginBottom(10);
        document.add(tituloSeccion);

        List<AportePension> aportesAFP = aportePensionService.obtenerAportesPorSistema(idUsuario, "Financiera");

        if (aportesAFP.isEmpty()) {
            document.add(new Paragraph("No hay aportes AFP registrados.")
                    .setFont(fontRegular)
                    .setFontSize(10)
                    .setMarginBottom(20));
            return;
        }

        Table tablaAFP = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 2, 2}))
                .useAllAvailableWidth()
                .setMarginBottom(20);

        tablaAFP.addHeaderCell(crearCeldaHeaderTabla("Período", fontBold));
        tablaAFP.addHeaderCell(crearCeldaHeaderTabla("Monto", fontBold));
        tablaAFP.addHeaderCell(crearCeldaHeaderTabla("Comisión", fontBold));
        tablaAFP.addHeaderCell(crearCeldaHeaderTabla("Empleador", fontBold));
        tablaAFP.addHeaderCell(crearCeldaHeaderTabla("Estado", fontBold));

        int limite = Math.min(aportesAFP.size(), 15);
        for (int i = 0; i < limite; i++) {
            AportePension aporte = aportesAFP.get(i);
            tablaAFP.addCell(crearCeldaTabla(aporte.getPeriodo() != null ? aporte.getPeriodo() : "-", fontRegular));
            tablaAFP.addCell(crearCeldaTabla("S/ " + String.format("%.2f", aporte.getMontoAporte()), fontRegular));
            tablaAFP.addCell(crearCeldaTabla("S/ " + String.format("%.2f",
                    aporte.getComisionCobrada() != null ? aporte.getComisionCobrada() : 0), fontRegular));
            tablaAFP.addCell(crearCeldaTabla(aporte.getEmpleador() != null ? aporte.getEmpleador() : "-", fontRegular));
            tablaAFP.addCell(crearCeldaTabla(aporte.getEstado() != null ? aporte.getEstado() : "-", fontRegular));
        }

        document.add(tablaAFP);

        if (aportesAFP.size() > 15) {
            document.add(new Paragraph("Mostrando los 15 aportes más recientes de " + aportesAFP.size() + " totales.")
                    .setFont(fontRegular)
                    .setFontSize(8)
                    .setItalic()
                    .setMarginBottom(20));
        }
    }

    private void agregarProyeccionesPensiones(Document document, PdfFont fontBold,
                                              PdfFont fontRegular, int idUsuario) {
        Paragraph tituloSeccion = new Paragraph("Proyecciones de Pensión")
                .setFont(fontBold)
                .setFontSize(14)
                .setFontColor(AZUL_PRINCIPAL)
                .setMarginBottom(10);
        document.add(tituloSeccion);

        Double saldoTotal = saldoPensionService.obtenerSaldoTotalUsuario(idUsuario);
        Double proyeccionMensual = (saldoTotal != null && saldoTotal > 0) ? saldoTotal / 240 : 0;

        Table tablaProyeccion = new Table(UnitValue.createPercentArray(new float[]{3, 2}))
                .useAllAvailableWidth()
                .setMarginBottom(20);

        tablaProyeccion.addCell(crearCeldaHeader("Proyección Pensión Mensual", fontBold));
        tablaProyeccion.addCell(crearCeldaDato("S/ " + String.format("%.2f", proyeccionMensual), fontRegular));

        tablaProyeccion.addCell(crearCeldaHeader("Escenario Conservador (90%)", fontBold));
        tablaProyeccion.addCell(crearCeldaDato("S/ " + String.format("%.2f", proyeccionMensual * 0.9), fontRegular));

        tablaProyeccion.addCell(crearCeldaHeader("Escenario Optimista (110%)", fontBold));
        tablaProyeccion.addCell(crearCeldaDato("S/ " + String.format("%.2f", proyeccionMensual * 1.1), fontRegular));

        tablaProyeccion.addCell(crearCeldaHeader("Tasa Anual Estimada", fontBold));
        tablaProyeccion.addCell(crearCeldaDato("5.4%", fontRegular));

        document.add(tablaProyeccion);

        document.add(new Paragraph("Nota: Las proyecciones son estimadas y pueden variar según las condiciones del mercado.")
                .setFont(fontRegular)
                .setFontSize(8)
                .setItalic()
                .setMarginBottom(20));
    }

    private void agregarEstadoAportes(Document document, PdfFont fontBold,
                                      PdfFont fontRegular, int idUsuario) {
        Paragraph tituloSeccion = new Paragraph("Estado de Aportes por Año")
                .setFont(fontBold)
                .setFontSize(14)
                .setFontColor(AZUL_PRINCIPAL)
                .setMarginBottom(10);
        document.add(tituloSeccion);

        int yearActual = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);

        Table tablaEstado = new Table(UnitValue.createPercentArray(new float[]{1, 2, 2}))
                .useAllAvailableWidth()
                .setMarginBottom(20);

        tablaEstado.addHeaderCell(crearCeldaHeaderTabla("Año", fontBold));
        tablaEstado.addHeaderCell(crearCeldaHeaderTabla("Total Aportes", fontBold));
        tablaEstado.addHeaderCell(crearCeldaHeaderTabla("N° Aportes", fontBold));

        for (int i = 3; i >= 0; i--) {
            int year = yearActual - i;
            Double total = aportePensionService.obtenerTotalAportesUsuarioYear(idUsuario, year);
            List<AportePension> aportes = aportePensionService.obtenerAportesUsuarioYear(idUsuario, year);

            tablaEstado.addCell(crearCeldaTabla(String.valueOf(year), fontRegular));
            tablaEstado.addCell(crearCeldaTabla("S/ " + String.format("%.2f",
                    total != null ? total : 0), fontRegular));
            tablaEstado.addCell(crearCeldaTabla(String.valueOf(aportes.size()), fontRegular));
        }

        document.add(tablaEstado);
    }

// ============== MÉTODOS AUXILIARES PARA GESTIÓN DE SEGUROS ==============

    @Autowired
    private SegurosService segurosService;

    private void agregarEncabezadoSeguros(Document document, PdfFont fontBold, Usuario usuario) {
        Paragraph titulo = new Paragraph("GESTIÓN DE SEGUROS")
                .setFont(fontBold)
                .setFontSize(20)
                .setFontColor(AZUL_PRINCIPAL)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
        document.add(titulo);

        Paragraph subtitulo = new Paragraph("Reporte de Pólizas y Coberturas")
                .setFont(fontBold)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        document.add(subtitulo);

        Paragraph infoUsuario = new Paragraph()
                .add(new Paragraph("Asegurado: ").setFont(fontBold).setFontSize(10))
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

        document.add(new Paragraph("\n"));
    }

    private void agregarResumenSeguros(Document document, PdfFont fontBold,
                                       PdfFont fontRegular, int idUsuario) {
        Paragraph tituloSeccion = new Paragraph("Resumen General de Seguros")
                .setFont(fontBold)
                .setFontSize(14)
                .setFontColor(AZUL_PRINCIPAL)
                .setMarginBottom(10);
        document.add(tituloSeccion);

        HashMap<String, Object> resumen = segurosService.obtenerResumenAdministrativo(idUsuario);

        Table tablaResumen = new Table(UnitValue.createPercentArray(new float[]{3, 2}))
                .useAllAvailableWidth()
                .setMarginBottom(20);

        tablaResumen.addCell(crearCeldaHeader("Pólizas Activas", fontBold));
        tablaResumen.addCell(crearCeldaDato(String.valueOf(resumen.get("polizasActivas")), fontRegular));

        tablaResumen.addCell(crearCeldaHeader("Beneficiarios Registrados", fontBold));
        tablaResumen.addCell(crearCeldaDato(String.valueOf(resumen.get("beneficiariosRegistrados")), fontRegular));

        Double pagosPendientes = (Double) resumen.get("pagosPendientes");
        tablaResumen.addCell(crearCeldaHeader("Pagos Pendientes", fontBold));
        tablaResumen.addCell(crearCeldaDato("S/ " + String.format("%.2f", pagosPendientes), fontRegular));

        tablaResumen.addCell(crearCeldaHeader("Trámites Pendientes", fontBold));
        tablaResumen.addCell(crearCeldaDato(String.valueOf(resumen.get("tramitesPendientes")), fontRegular));

        tablaResumen.addCell(crearCeldaHeader("Alertas Activas", fontBold));
        tablaResumen.addCell(crearCeldaDato(String.valueOf(resumen.get("alertas")), fontRegular));

        document.add(tablaResumen);
    }

    private void agregarPolizasActivas(Document document, PdfFont fontBold,
                                       PdfFont fontRegular, int idUsuario) {
        Paragraph tituloSeccion = new Paragraph("Pólizas Activas")
                .setFont(fontBold)
                .setFontSize(14)
                .setFontColor(AZUL_PRINCIPAL)
                .setMarginBottom(10);
        document.add(tituloSeccion);

        var seguros = segurosService.obtenerSegurosActivos(idUsuario);

        if (seguros.isEmpty()) {
            document.add(new Paragraph("No hay pólizas activas.")
                    .setFont(fontRegular)
                    .setFontSize(10)
                    .setMarginBottom(20));
            return;
        }

        Table tablaSeguros = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 2, 2}))
                .useAllAvailableWidth()
                .setMarginBottom(20);

        tablaSeguros.addHeaderCell(crearCeldaHeaderTabla("N° Póliza", fontBold));
        tablaSeguros.addHeaderCell(crearCeldaHeaderTabla("Tipo", fontBold));
        tablaSeguros.addHeaderCell(crearCeldaHeaderTabla("Compañía", fontBold));
        tablaSeguros.addHeaderCell(crearCeldaHeaderTabla("Prima Mensual", fontBold));
        tablaSeguros.addHeaderCell(crearCeldaHeaderTabla("Vencimiento", fontBold));

        for (var seguro : seguros) {
            tablaSeguros.addCell(crearCeldaTabla(seguro.getNumeroPoliza(), fontRegular));
            tablaSeguros.addCell(crearCeldaTabla(
                    seguro.getTipoSeguro() != null ? seguro.getTipoSeguro().getNombre() : "-", fontRegular));
            tablaSeguros.addCell(crearCeldaTabla(
                    seguro.getCompania() != null ? seguro.getCompania().getNombre() : "-", fontRegular));
            tablaSeguros.addCell(crearCeldaTabla("S/ " + String.format("%.2f",
                    seguro.getPrimaMensual()), fontRegular));
            tablaSeguros.addCell(crearCeldaTabla(
                    seguro.getFechaVencimiento() != null ?
                            new java.text.SimpleDateFormat("dd/MM/yyyy").format(seguro.getFechaVencimiento()) : "-",
                    fontRegular));
        }

        document.add(tablaSeguros);
    }

    private void agregarBeneficiarios(Document document, PdfFont fontBold,
                                      PdfFont fontRegular, int idUsuario) {
        Paragraph tituloSeccion = new Paragraph("Beneficiarios Registrados")
                .setFont(fontBold)
                .setFontSize(14)
                .setFontColor(AZUL_PRINCIPAL)
                .setMarginBottom(10);
        document.add(tituloSeccion);

        var seguros = segurosService.obtenerSegurosActivos(idUsuario);
        boolean hayBeneficiarios = false;

        for (var seguro : seguros) {
            var beneficiarios = segurosService.obtenerBeneficiariosPorSeguro(seguro.getIdSeguro());

            if (!beneficiarios.isEmpty()) {
                hayBeneficiarios = true;

                Paragraph tituloPoliza = new Paragraph("Póliza: " + seguro.getNumeroPoliza())
                        .setFont(fontBold)
                        .setFontSize(11)
                        .setMarginTop(10)
                        .setMarginBottom(5);
                document.add(tituloPoliza);

                Table tablaBenef = new Table(UnitValue.createPercentArray(new float[]{3, 2, 2, 1}))
                        .useAllAvailableWidth()
                        .setMarginBottom(15);

                tablaBenef.addHeaderCell(crearCeldaHeaderTabla("Nombre", fontBold));
                tablaBenef.addHeaderCell(crearCeldaHeaderTabla("Parentesco", fontBold));
                tablaBenef.addHeaderCell(crearCeldaHeaderTabla("DNI", fontBold));
                tablaBenef.addHeaderCell(crearCeldaHeaderTabla("%", fontBold));

                for (var benef : beneficiarios) {
                    tablaBenef.addCell(crearCeldaTabla(benef.getNombreCompleto(), fontRegular));
                    tablaBenef.addCell(crearCeldaTabla(benef.getParentesco(), fontRegular));
                    tablaBenef.addCell(crearCeldaTabla(benef.getDni(), fontRegular));
                    tablaBenef.addCell(crearCeldaTabla(String.format("%.0f%%", benef.getPorcentaje()), fontRegular));
                }

                document.add(tablaBenef);
            }
        }

        if (!hayBeneficiarios) {
            document.add(new Paragraph("No hay beneficiarios registrados.")
                    .setFont(fontRegular)
                    .setFontSize(10)
                    .setMarginBottom(20));
        }
    }

    private void agregarPagosPendientes(Document document, PdfFont fontBold,
                                        PdfFont fontRegular, int idUsuario) {
        Paragraph tituloSeccion = new Paragraph("Pagos Pendientes")
                .setFont(fontBold)
                .setFontSize(14)
                .setFontColor(AZUL_PRINCIPAL)
                .setMarginBottom(10);
        document.add(tituloSeccion);

        var pagos = segurosService.obtenerPagosPendientes(idUsuario);

        if (pagos.isEmpty()) {
            document.add(new Paragraph("No hay pagos pendientes.")
                    .setFont(fontRegular)
                    .setFontSize(10)
                    .setMarginBottom(20));
            return;
        }

        Table tablaPagos = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 2}))
                .useAllAvailableWidth()
                .setMarginBottom(20);

        tablaPagos.addHeaderCell(crearCeldaHeaderTabla("N° Póliza", fontBold));
        tablaPagos.addHeaderCell(crearCeldaHeaderTabla("N° Cuota", fontBold));
        tablaPagos.addHeaderCell(crearCeldaHeaderTabla("Monto", fontBold));
        tablaPagos.addHeaderCell(crearCeldaHeaderTabla("Estado", fontBold));

        Double totalPendiente = 0.0;

        for (var pago : pagos) {
            tablaPagos.addCell(crearCeldaTabla(
                    pago.getSeguro() != null ? pago.getSeguro().getNumeroPoliza() : "-", fontRegular));
            tablaPagos.addCell(crearCeldaTabla(
                    pago.getNumeroCuota() != null ? String.valueOf(pago.getNumeroCuota()) : "-", fontRegular));
            tablaPagos.addCell(crearCeldaTabla("S/ " + String.format("%.2f",
                    pago.getMontoPagado()), fontRegular));
            tablaPagos.addCell(crearCeldaTabla(pago.getEstado(), fontRegular));

            totalPendiente += pago.getMontoPagado();
        }

        // Fila de total
        Cell celdaTotal = new Cell(1, 3)
                .add(new Paragraph("TOTAL PENDIENTE").setFont(fontBold).setFontSize(9))
                .setBackgroundColor(GRIS_CLARO)
                .setTextAlignment(TextAlignment.RIGHT)
                .setPadding(5);
        tablaPagos.addCell(celdaTotal);

        Cell celdaMontoTotal = new Cell()
                .add(new Paragraph("S/ " + String.format("%.2f", totalPendiente)).setFont(fontBold).setFontSize(9))
                .setBackgroundColor(GRIS_CLARO)
                .setPadding(5);
        tablaPagos.addCell(celdaMontoTotal);

        document.add(tablaPagos);
    }

    private void agregarTramites(Document document, PdfFont fontBold,
                                 PdfFont fontRegular, int idUsuario) {
        Paragraph tituloSeccion = new Paragraph("Trámites y Solicitudes")
                .setFont(fontBold)
                .setFontSize(14)
                .setFontColor(AZUL_PRINCIPAL)
                .setMarginBottom(10);
        document.add(tituloSeccion);

        var tramites = segurosService.obtenerTramitesUsuario(idUsuario);

        if (tramites.isEmpty()) {
            document.add(new Paragraph("No hay trámites registrados.")
                    .setFont(fontRegular)
                    .setFontSize(10)
                    .setMarginBottom(20));
            return;
        }

        Table tablaTramites = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 2, 2}))
                .useAllAvailableWidth()
                .setMarginBottom(20);

        tablaTramites.addHeaderCell(crearCeldaHeaderTabla("Tipo", fontBold));
        tablaTramites.addHeaderCell(crearCeldaHeaderTabla("Descripción", fontBold));
        tablaTramites.addHeaderCell(crearCeldaHeaderTabla("Estado", fontBold));
        tablaTramites.addHeaderCell(crearCeldaHeaderTabla("Prioridad", fontBold));
        tablaTramites.addHeaderCell(crearCeldaHeaderTabla("Fecha", fontBold));

        int limite = Math.min(tramites.size(), 10);
        for (int i = 0; i < limite; i++) {
            var tramite = tramites.get(i);

            tablaTramites.addCell(crearCeldaTabla(tramite.getTipoTramite(), fontRegular));
            tablaTramites.addCell(crearCeldaTabla(
                    tramite.getDescripcion() != null && tramite.getDescripcion().length() > 30 ?
                            tramite.getDescripcion().substring(0, 30) + "..." :
                            tramite.getDescripcion() != null ? tramite.getDescripcion() : "-",
                    fontRegular));
            tablaTramites.addCell(crearCeldaTabla(tramite.getEstado(), fontRegular));
            tablaTramites.addCell(crearCeldaTabla(tramite.getPrioridad(), fontRegular));
            tablaTramites.addCell(crearCeldaTabla(
                    tramite.getFechaSolicitud() != null ?
                            tramite.getFechaSolicitud().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-",
                    fontRegular));
        }

        document.add(tablaTramites);

        if (tramites.size() > 10) {
            document.add(new Paragraph("Mostrando los 10 trámites más recientes de " + tramites.size() + " totales.")
                    .setFont(fontRegular)
                    .setFontSize(8)
                    .setItalic()
                    .setMarginBottom(20));
        }
    }

    private void agregarEstadisticasSeguros(Document document, PdfFont fontBold,
                                            PdfFont fontRegular, int idUsuario) {
        Paragraph tituloSeccion = new Paragraph("Estadísticas de Seguros")
                .setFont(fontBold)
                .setFontSize(14)
                .setFontColor(AZUL_PRINCIPAL)
                .setMarginBottom(10);
        document.add(tituloSeccion);

        var seguros = segurosService.obtenerSegurosActivos(idUsuario);

        if (seguros.isEmpty()) {
            document.add(new Paragraph("No hay datos de seguros para mostrar estadísticas.")
                    .setFont(fontRegular)
                    .setFontSize(10)
                    .setMarginBottom(20));
            return;
        }

        // Calcular estadísticas
        Double primaTotalMensual = seguros.stream()
                .mapToDouble(s -> s.getPrimaMensual() != null ? s.getPrimaMensual() : 0)
                .sum();

        Double primaTotalAnual = seguros.stream()
                .mapToDouble(s -> s.getPrimaAnual() != null ? s.getPrimaAnual() : 0)
                .sum();

        Double coberturaTotalAsegurada = seguros.stream()
                .mapToDouble(s -> s.getMontoAsegurado() != null ? s.getMontoAsegurado() : 0)
                .sum();

        // Contar por categoría
        java.util.Map<String, Long> segurosPorCategoria = seguros.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        s -> s.getTipoSeguro() != null ? s.getTipoSeguro().getCategoria() : "Sin categoría",
                        java.util.stream.Collectors.counting()
                ));

        Table tablaEstadisticas = new Table(UnitValue.createPercentArray(new float[]{3, 2}))
                .useAllAvailableWidth()
                .setMarginBottom(15);

        tablaEstadisticas.addCell(crearCeldaHeader("Prima Total Mensual", fontBold));
        tablaEstadisticas.addCell(crearCeldaDato("S/ " + String.format("%.2f", primaTotalMensual), fontRegular));

        tablaEstadisticas.addCell(crearCeldaHeader("Prima Total Anual", fontBold));
        tablaEstadisticas.addCell(crearCeldaDato("S/ " + String.format("%.2f", primaTotalAnual), fontRegular));

        tablaEstadisticas.addCell(crearCeldaHeader("Cobertura Total Asegurada", fontBold));
        tablaEstadisticas.addCell(crearCeldaDato("S/ " + String.format("%.2f", coberturaTotalAsegurada), fontRegular));

        tablaEstadisticas.addCell(crearCeldaHeader("Total de Pólizas Activas", fontBold));
        tablaEstadisticas.addCell(crearCeldaDato(String.valueOf(seguros.size()), fontRegular));

        document.add(tablaEstadisticas);

        // Distribución por categoría
        if (!segurosPorCategoria.isEmpty()) {
            Paragraph tituloDistribucion = new Paragraph("Distribución por Categoría")
                    .setFont(fontBold)
                    .setFontSize(12)
                    .setMarginTop(10)
                    .setMarginBottom(5);
            document.add(tituloDistribucion);

            Table tablaDistribucion = new Table(UnitValue.createPercentArray(new float[]{3, 1}))
                    .useAllAvailableWidth()
                    .setMarginBottom(20);

            tablaDistribucion.addHeaderCell(crearCeldaHeaderTabla("Categoría", fontBold));
            tablaDistribucion.addHeaderCell(crearCeldaHeaderTabla("Cantidad", fontBold));

            for (var entry : segurosPorCategoria.entrySet()) {
                tablaDistribucion.addCell(crearCeldaTabla(entry.getKey(), fontRegular));
                tablaDistribucion.addCell(crearCeldaTabla(String.valueOf(entry.getValue()), fontRegular));
            }

            document.add(tablaDistribucion);
        }
    }
}
