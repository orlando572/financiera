package com.app.financiera.service;

public interface ChatBotService {

    /**
     * Procesa el mensaje del usuario y genera respuesta con Gemini AI
     */
    String procesarMensaje(String mensaje, Integer idUsuario);

    /**
     * Solicita contacto con asesor humano
     */
    boolean solicitarContactoAsesor(Integer idUsuario, String motivo);

    /**
     * Obtiene contexto del usuario para personalizar respuestas
     */
    String obtenerContextoUsuario(Integer idUsuario);
}