package com.app.financiera.service;

/**
 * Servicio para envío de correos electrónicos
 *
 * @author Sistema Financiero
 * @version 1.0
 */
public interface EmailService {

    /**
     * Enviar correo simple
     */
    void enviarCorreoSimple(String destinatario, String asunto, String mensaje);

    /**
     * Enviar correo HTML
     */
    void enviarCorreoHTML(String destinatario, String asunto, String contenidoHTML);

    /**
     * Enviar notificación de vencimiento de seguro
     */
    void enviarNotificacionVencimientoSeguro(String destinatario, String nombreUsuario,
                                             String tipoSeguro, String fechaVencimiento);

    /**
     * Enviar notificación de pago pendiente
     */
    void enviarNotificacionPagoPendiente(String destinatario, String nombreUsuario,
                                         String concepto, Double monto);

    /**
     * Enviar notificación de aporte registrado
     */
    void enviarNotificacionAporteRegistrado(String destinatario, String nombreUsuario,
                                            Double monto, String periodo);

    /**
     * Enviar notificación de asesoramiento programado
     */
    void enviarNotificacionAsesoramiento(String destinatario, String nombreUsuario,
                                         String tipoAsesoramiento, String fechaHora);

    /**
     * Enviar notificación de trámite actualizado
     */
    void enviarNotificacionTramite(String destinatario, String nombreUsuario,
                                   String tipoTramite, String nuevoEstado);
}
