package com.app.financiera.service;

import com.app.financiera.entity.Seguro;
import com.app.financiera.entity.Usuario;
import com.app.financiera.repository.SeguroRepository;
import com.app.financiera.repository.UsuarioRepository;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CotizacionServiceImpl implements CotizacionService {

    private static final Logger logger = LoggerFactory.getLogger(CotizacionServiceImpl.class);

    @Autowired
    private SeguroRepository seguroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public Map<String, Object> solicitarCotizacion(Integer idUsuario, List<Integer> idsPlanes, String comentarios) {
        Map<String, Object> resultado = new HashMap<>();
        
        try {
            // Obtener usuario
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
            if (!usuarioOpt.isPresent()) {
                resultado.put("exitoso", false);
                resultado.put("mensaje", "Usuario no encontrado");
                return resultado;
            }

            Usuario usuario = usuarioOpt.get();

            // Obtener planes seleccionados
            List<Seguro> planesSeleccionados = new ArrayList<>();
            for (Integer idPlan : idsPlanes) {
                Optional<Seguro> seguroOpt = seguroRepository.findById(idPlan);
                if (seguroOpt.isPresent()) {
                    planesSeleccionados.add(seguroOpt.get());
                }
            }

            if (planesSeleccionados.isEmpty()) {
                resultado.put("exitoso", false);
                resultado.put("mensaje", "No se encontraron los planes seleccionados");
                return resultado;
            }

            // Enviar correo al usuario
            boolean emailEnviado = enviarCorreoCotizacion(usuario, planesSeleccionados, comentarios);

            if (emailEnviado) {
                resultado.put("exitoso", true);
                resultado.put("mensaje", "Cotización enviada exitosamente a su correo electrónico");
                resultado.put("correo", usuario.getCorreo());
                resultado.put("planesIncluidos", planesSeleccionados.size());
                resultado.put("fecha", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            } else {
                resultado.put("exitoso", false);
                resultado.put("mensaje", "Error al enviar el correo electrónico");
            }

        } catch (Exception e) {
            logger.error("Error al procesar cotización: {}", e.getMessage(), e);
            resultado.put("exitoso", false);
            resultado.put("mensaje", "Error al procesar la cotización");
            resultado.put("error", e.getMessage());
        }

        return resultado;
    }

    private boolean enviarCorreoCotizacion(Usuario usuario, List<Seguro> planes, String comentarios) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(usuario.getCorreo());
            helper.setSubject("Cotización de Seguros - Sumaq Seguros");
            helper.setText(construirHtmlCotizacion(usuario, planes, comentarios), true);

            mailSender.send(message);
            logger.info("Correo de cotización enviado a: {}", usuario.getCorreo());
            return true;

        } catch (Exception e) {
            logger.error("Error al enviar correo de cotización: {}", e.getMessage(), e);
            return false;
        }
    }

    private String construirHtmlCotizacion(Usuario usuario, List<Seguro> planes, String comentarios) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "PE"));
        currencyFormat.setCurrency(Currency.getInstance("PEN"));

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang='es'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; background-color: #f4f4f4; margin: 0; padding: 0; }");
        html.append(".container { max-width: 700px; margin: 20px auto; background: #fff; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
        html.append(".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; margin: -30px -30px 30px -30px; }");
        html.append(".header h1 { margin: 0; font-size: 28px; }");
        html.append(".header p { margin: 10px 0 0 0; font-size: 14px; opacity: 0.9; }");
        html.append(".plan-card { background: #f8f9fa; border-left: 4px solid #667eea; padding: 20px; margin: 20px 0; border-radius: 5px; }");
        html.append(".plan-card h3 { margin: 0 0 10px 0; color: #667eea; font-size: 20px; }");
        html.append(".plan-info { display: grid; grid-template-columns: 1fr 1fr; gap: 15px; margin-top: 15px; }");
        html.append(".info-item { padding: 10px; background: white; border-radius: 5px; }");
        html.append(".info-label { font-size: 12px; color: #666; text-transform: uppercase; font-weight: bold; }");
        html.append(".info-value { font-size: 16px; color: #333; font-weight: bold; margin-top: 5px; }");
        html.append(".price-highlight { color: #667eea; font-size: 24px; }");
        html.append(".footer { text-align: center; margin-top: 30px; padding-top: 20px; border-top: 2px solid #eee; color: #666; font-size: 14px; }");
        html.append(".contact-info { background: #e8eaf6; padding: 15px; border-radius: 5px; margin: 20px 0; }");
        html.append(".btn { display: inline-block; padding: 12px 30px; background: #667eea; color: white; text-decoration: none; border-radius: 5px; margin: 10px 5px; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");

        // Header
        html.append("<div class='header'>");
        html.append("<h1>🛡️ Cotización de Seguros</h1>");
        html.append("<p>Sumaq Seguros - Tu protección, nuestra prioridad</p>");
        html.append("</div>");

        // Saludo
        html.append("<p>Estimado(a) <strong>").append(usuario.getNombre()).append(" ").append(usuario.getApellido()).append("</strong>,</p>");
        html.append("<p>Gracias por su interés en nuestros servicios. A continuación, encontrará la información detallada de los planes de seguro que ha seleccionado:</p>");

        // Planes
        for (int i = 0; i < planes.size(); i++) {
            Seguro plan = planes.get(i);
            html.append("<div class='plan-card'>");
            html.append("<h3>Plan ").append(i + 1).append(": ").append(plan.getTipoSeguro().getNombre()).append("</h3>");
            html.append("<p><strong>Compañía:</strong> ").append(plan.getCompania().getNombre()).append("</p>");
            
            html.append("<div class='plan-info'>");
            
            html.append("<div class='info-item'>");
            html.append("<div class='info-label'>Prima Mensual</div>");
            html.append("<div class='info-value price-highlight'>").append(currencyFormat.format(plan.getPrimaMensual())).append("</div>");
            html.append("</div>");
            
            html.append("<div class='info-item'>");
            html.append("<div class='info-label'>Prima Anual</div>");
            html.append("<div class='info-value'>").append(currencyFormat.format(plan.getPrimaAnual())).append("</div>");
            html.append("</div>");
            
            html.append("<div class='info-item'>");
            html.append("<div class='info-label'>Monto Asegurado</div>");
            html.append("<div class='info-value'>").append(currencyFormat.format(plan.getMontoAsegurado())).append("</div>");
            html.append("</div>");
            
            html.append("<div class='info-item'>");
            html.append("<div class='info-label'>Deducible</div>");
            html.append("<div class='info-value'>").append(currencyFormat.format(plan.getDeducible())).append("</div>");
            html.append("</div>");
            
            html.append("</div>");
            
            if (plan.getCoberturas() != null && !plan.getCoberturas().isEmpty()) {
                html.append("<p style='margin-top: 15px;'><strong>Coberturas:</strong> ").append(plan.getCoberturas()).append("</p>");
            }
            
            html.append("</div>");
        }

        // Comentarios
        if (comentarios != null && !comentarios.trim().isEmpty()) {
            html.append("<div class='contact-info'>");
            html.append("<p><strong>Sus comentarios:</strong></p>");
            html.append("<p>").append(comentarios).append("</p>");
            html.append("</div>");
        }

        // Información de contacto
        html.append("<div class='contact-info'>");
        html.append("<h3 style='margin-top: 0;'> ¿Necesita más información?</h3>");
        html.append("<p>Nuestros asesores están listos para atenderle:</p>");
        html.append("<p><strong>Teléfono:</strong> (01) 234-5678<br>");
        html.append("<strong>WhatsApp:</strong> +51 987 654 321<br>");
        html.append("<strong>Email:</strong> contacto@sumaqseguros.com</p>");
        html.append("</div>");

        // Call to action
        html.append("<div style='text-align: center; margin: 30px 0;'>");
        html.append("<p><strong>¿Listo para contratar?</strong></p>");
        html.append("<a href='http://localhost:5173/seguros' class='btn'>Ver Más Planes</a>");
        html.append("<a href='http://localhost:5173/contacto' class='btn' style='background: #764ba2;'>Contactar Asesor</a>");
        html.append("</div>");

        // Footer
        html.append("<div class='footer'>");
        html.append("<p><strong>Sumaq Seguros</strong></p>");
        html.append("<p>Av. Principal 123, Arequipa - Perú</p>");
        html.append("<p>Este correo fue generado automáticamente. Por favor, no responda a este mensaje.</p>");
        html.append("<p style='font-size: 12px; color: #999; margin-top: 15px;'>© 2025 Sumaq Seguros. Todos los derechos reservados.</p>");
        html.append("</div>");

        html.append("</div>");
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }
}
