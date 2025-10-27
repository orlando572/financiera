package com.app.financiera.service;

import com.app.financiera.entity.Notificacion;
import com.app.financiera.entity.Usuario;
import com.app.financiera.repository.NotificacionRepository;
import com.app.financiera.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificacionServiceImpl implements NotificacionService {

    private static final Logger logger = LoggerFactory.getLogger(NotificacionServiceImpl.class);

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Override
    @Transactional
    public Notificacion crearYEnviarNotificacion(Notificacion notificacion) {
        logger.info("Creando y enviando notificación para usuario ID: {}", 
                    notificacion.getUsuario().getIdUsuario());

        // Guardar en base de datos
        Notificacion notificacionGuardada = notificacionRepository.save(notificacion);

        // Enviar por email si está configurado
        if (mailSender != null && "Email".equals(notificacion.getCanal())) {
            try {
                enviarEmail(notificacion);
                notificacionGuardada.setEstado("Enviada");
            } catch (Exception e) {
                logger.error("Error al enviar email: {}", e.getMessage());
                notificacionGuardada.setEstado("Error");
            }
            notificacionRepository.save(notificacionGuardada);
        } else {
            notificacionGuardada.setEstado("Enviada");
            notificacionRepository.save(notificacionGuardada);
        }

        return notificacionGuardada;
    }

    @Override
    public List<Notificacion> obtenerNotificacionesUsuario(int idUsuario) {
        return notificacionRepository.findByUsuarioId(idUsuario);
    }

    @Override
    public List<Notificacion> obtenerNotificacionesNoLeidas(int idUsuario) {
        return notificacionRepository.findNoLeidasByUsuario(idUsuario);
    }

    @Override
    @Transactional
    public Notificacion marcarComoLeida(int idNotificacion) {
        Optional<Notificacion> notificacionOpt = notificacionRepository.findById(idNotificacion);
        if (notificacionOpt.isPresent()) {
            Notificacion notificacion = notificacionOpt.get();
            notificacion.setFechaLectura(LocalDateTime.now());
            notificacion.setEstado("Leída");
            return notificacionRepository.save(notificacion);
        }
        return null;
    }

    @Override
    public long contarNotificacionesNoLeidas(int idUsuario) {
        return notificacionRepository.countNoLeidasByUsuario(idUsuario);
    }

    @Override
    public List<Notificacion> obtenerNotificacionesPorTipo(int idUsuario, String tipo) {
        return notificacionRepository.findByUsuarioAndTipo(idUsuario, tipo);
    }

    @Override
    @Transactional
    public void eliminarNotificacion(int idNotificacion) {
        notificacionRepository.deleteById(idNotificacion);
    }

    @Override
    @Transactional
    public void notificarVencimientoSeguro(int idUsuario, String tipoSeguro, String fechaVencimiento) {
        logger.info("Notificando vencimiento de seguro a usuario ID: {}", idUsuario);

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (!usuarioOpt.isPresent()) {
            logger.warn("Usuario no encontrado ID: {}", idUsuario);
            return;
        }

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuarioOpt.get());
        notificacion.setTipoNotificacion("Alerta");
        notificacion.setAsunto("Vencimiento de Póliza de Seguro");
        notificacion.setMensaje(String.format(
            "Tu póliza de %s vence el %s. Por favor, renueva tu seguro para mantener tu cobertura activa.",
            tipoSeguro, fechaVencimiento
        ));
        notificacion.setCanal("Email");
        notificacion.setPrioridad("Alta");
        notificacion.setUrlAccion("/seguros");

        crearYEnviarNotificacion(notificacion);
    }

    @Override
    @Transactional
    public void notificarPagoPendiente(int idUsuario, String concepto, Double monto) {
        logger.info("Notificando pago pendiente a usuario ID: {}", idUsuario);

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (!usuarioOpt.isPresent()) {
            logger.warn("Usuario no encontrado ID: {}", idUsuario);
            return;
        }

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuarioOpt.get());
        notificacion.setTipoNotificacion("Recordatorio");
        notificacion.setAsunto("Recordatorio de Pago Pendiente");
        notificacion.setMensaje(String.format(
            "Tienes un pago pendiente de %s por S/ %.2f. Por favor, realiza tu pago antes del vencimiento.",
            concepto, monto
        ));
        notificacion.setCanal("Email");
        notificacion.setPrioridad("Alta");
        notificacion.setUrlAccion("/seguros");

        crearYEnviarNotificacion(notificacion);
    }

    @Override
    @Transactional
    public void notificarAporteRegistrado(int idUsuario, Double monto, String periodo) {
        logger.info("Notificando aporte registrado a usuario ID: {}", idUsuario);

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (!usuarioOpt.isPresent()) {
            logger.warn("Usuario no encontrado ID: {}", idUsuario);
            return;
        }

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuarioOpt.get());
        notificacion.setTipoNotificacion("Informativa");
        notificacion.setAsunto("Aporte Registrado Exitosamente");
        notificacion.setMensaje(String.format(
            "Se ha registrado tu aporte de S/ %.2f para el periodo %s.",
            monto, periodo
        ));
        notificacion.setCanal("Email");
        notificacion.setPrioridad("Media");
        notificacion.setUrlAccion("/pensiones");

        crearYEnviarNotificacion(notificacion);
    }

    @Override
    @Transactional
    public void notificarAsesoramiento(int idUsuario, String tipoAsesoramiento, String fechaHora) {
        logger.info("Notificando asesoramiento a usuario ID: {}", idUsuario);

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (!usuarioOpt.isPresent()) {
            logger.warn("Usuario no encontrado ID: {}", idUsuario);
            return;
        }

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuarioOpt.get());
        notificacion.setTipoNotificacion("Recordatorio");
        notificacion.setAsunto("Recordatorio de Asesoramiento");
        notificacion.setMensaje(String.format(
            "Tienes una sesión de asesoramiento de %s programada para %s.",
            tipoAsesoramiento, fechaHora
        ));
        notificacion.setCanal("Email");
        notificacion.setPrioridad("Media");
        notificacion.setUrlAccion("/asesoramiento");

        crearYEnviarNotificacion(notificacion);
    }

    @Override
    @Transactional
    public void notificarTramite(int idUsuario, String tipoTramite, String nuevoEstado) {
        logger.info("Notificando cambio de estado de trámite a usuario ID: {}", idUsuario);

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (!usuarioOpt.isPresent()) {
            logger.warn("Usuario no encontrado ID: {}", idUsuario);
            return;
        }

        Notificacion notificacion = new Notificacion();
        notificacion.setUsuario(usuarioOpt.get());
        notificacion.setTipoNotificacion("Informativa");
        notificacion.setAsunto("Actualización de Trámite");
        notificacion.setMensaje(String.format(
            "Tu trámite de %s ha cambiado a estado: %s.",
            tipoTramite, nuevoEstado
        ));
        notificacion.setCanal("Email");
        notificacion.setPrioridad("Media");
        notificacion.setUrlAccion("/seguros");

        crearYEnviarNotificacion(notificacion);
    }

    /**
     * Método privado para enviar email
     */
    private void enviarEmail(Notificacion notificacion) {
        if (mailSender == null) {
            logger.warn("JavaMailSender no configurado, no se puede enviar email");
            return;
        }

        Usuario usuario = notificacion.getUsuario();
        if (usuario.getCorreo() == null || usuario.getCorreo().isEmpty()) {
            logger.warn("Usuario sin correo electrónico configurado");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(usuario.getCorreo());
            message.setSubject(notificacion.getAsunto());
            message.setText(construirCuerpoEmail(notificacion, usuario));
            message.setFrom("noreply@sumaqseguros.com");

            mailSender.send(message);
            logger.info("Email enviado exitosamente a: {}", usuario.getCorreo());
        } catch (Exception e) {
            logger.error("Error al enviar email: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Construye el cuerpo del email
     */
    private String construirCuerpoEmail(Notificacion notificacion, Usuario usuario) {
        StringBuilder sb = new StringBuilder();
        sb.append("Hola ").append(usuario.getNombre()).append(",\n\n");
        sb.append(notificacion.getMensaje()).append("\n\n");
        
        if (notificacion.getUrlAccion() != null) {
            sb.append("Puedes ver más detalles en: https://sumaqseguros.com")
              .append(notificacion.getUrlAccion()).append("\n\n");
        }
        
        sb.append("Saludos,\n");
        sb.append("Equipo SumaqSeguros\n\n");
        sb.append("---\n");
        sb.append("Este es un mensaje automático, por favor no responder.");
        
        return sb.toString();
    }
}
