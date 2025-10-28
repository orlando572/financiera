package com.app.financiera.service;

import com.app.financiera.entity.*;
import com.app.financiera.repository.*;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatBotServiceImpl implements ChatBotService {

    private static final Logger logger = LoggerFactory.getLogger(ChatBotServiceImpl.class);

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SaldoPensionRepository saldoPensionRepository;

    @Autowired
    private AportePensionRepository aportePensionRepository;

    @Autowired
    private SeguroRepository seguroRepository;

    @Autowired
    private TipoSeguroRepository tipoSeguroRepository;

    @Autowired
    private CompaniaSeguroRepository companiaSeguroRepository;

    @Autowired
    private BeneficiarioSeguroRepository beneficiarioSeguroRepository;

    @Autowired
    private EmailService emailService;

    private static final String PROMPT_SISTEMA = """
        Eres un asistente virtual experto en pensiones y seguros para SumaqSeguros.
        
        INSTRUCCIONES CRÍTICAS:
        - Responde SOLO sobre: pensiones (ONP, AFP), seguros, aportes, saldos, proyecciones, pólizas, beneficiarios e información personal
        - USA SIEMPRE los datos exactos del CONTEXTO DEL USUARIO proporcionado
        - NUNCA inventes datos ni sugieras contactar a un asesor si la información está en el contexto
        - Cuando el usuario pregunte por saldo, proyección o datos personales, USA los valores exactos del contexto
        - Respuestas CLARAS, DIRECTAS y con DATOS ESPECÍFICOS (máximo 5-6 líneas)
        - Sé amable, profesional y proactivo
        - Usa emojis ocasionalmente (📊 💰 🏦 🛡️ 📈 👤 📧 📱)
        
        REGLAS PARA PROYECCIONES:
        - Si el contexto muestra "Proyección Pensión Mensual: S/ X.XX", DEBES mencionar ese valor exacto
        - Si el contexto muestra "Saldo Total: S/ X.XX", DEBES mencionar ese valor exacto
        - NO digas "contacte a un asesor" si los datos están disponibles en el contexto
        - La proyección ya está calculada y es válida, solo repórtala al usuario
        
        REGLAS PARA INFORMACIÓN PERSONAL:
        - Si el usuario pregunta por sus datos personales, usa la información de la sección "INFORMACIÓN DEL USUARIO"
        - Puedes compartir: nombre, DNI, correo, teléfono, régimen, AFP, CUSPP
        - Responde de forma clara y organizada
        
        TEMAS QUE PUEDES RESPONDER:
        ✓ Información personal del usuario (nombre, DNI, correo, teléfono, etc.)
        ✓ ¿Qué es ONP/AFP? Diferencias y ventajas
        ✓ Consultar saldos actuales (usa el valor del contexto)
        ✓ Proyecciones de pensión mensual (usa el valor del contexto)
        ✓ Aportes registrados y cómo agregar nuevos
        ✓ Tipos de seguros disponibles en la plataforma
        ✓ Pólizas activas del usuario (usa los datos del contexto)
        ✓ Beneficiarios registrados (usa los datos del contexto)
        ✓ Compañías de seguros disponibles
        ✓ Rentabilidad y estadísticas
        ✓ Cómo usar el comparador de seguros
        
        Si el usuario pregunta algo fuera de estos temas, responde:
        "Lo siento, solo puedo ayudarte con consultas sobre pensiones, seguros e información personal. ¿Necesitas hablar con un asesor? 📞"
        """;

    @Override
    public String procesarMensaje(String mensaje, Integer idUsuario) {
        try {
            String contextoUsuario = obtenerContextoUsuario(idUsuario);

            String promptCompleto = PROMPT_SISTEMA + "\n\n" +
                    "CONTEXTO DEL USUARIO:\n" + contextoUsuario + "\n\n" +
                    "PREGUNTA DEL USUARIO: " + mensaje;

            System.out.println("GOOGLE_API_KEY: " + geminiApiKey);

            Client client = new Client();

            GenerateContentResponse response =
                    client.models.generateContent("gemini-2.5-flash", promptCompleto, null);

            String respuesta = response.text();
            if (respuesta == null || respuesta.isEmpty()) {
                respuesta = generarRespuestaBasica(mensaje, contextoUsuario);
            }

            return respuesta;

        } catch (Exception e) {
            logger.error("Error generando respuesta con Gemini: {}", e.getMessage());
            return "Lo siento, ocurrió un error al generar la respuesta.";
        }
    }


    @Override
    public String obtenerContextoUsuario(Integer idUsuario) {
        StringBuilder contexto = new StringBuilder();

        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();

                contexto.append("=== INFORMACIÓN DEL USUARIO ===\n");
                contexto.append("Nombre Completo: ").append(usuario.getNombre()).append(" ").append(usuario.getApellido()).append("\n");
                contexto.append("DNI: ").append(usuario.getDni()).append("\n");
                contexto.append("Correo: ").append(usuario.getCorreo()).append("\n");
                if (usuario.getTelefono() != null && !usuario.getTelefono().isEmpty()) {
                    contexto.append("Teléfono: ").append(usuario.getTelefono()).append("\n");
                }
                if (usuario.getDireccion() != null && !usuario.getDireccion().isEmpty()) {
                    contexto.append("Dirección: ").append(usuario.getDireccion()).append("\n");
                }
                contexto.append("Régimen: ").append(usuario.getTipoRegimen() != null ? usuario.getTipoRegimen() : "No especificado").append("\n");
                contexto.append("AFP: ").append(usuario.getAfp() != null ? usuario.getAfp().getNombre() : "No afiliado").append("\n");
                if (usuario.getCuspp() != null && !usuario.getCuspp().isEmpty()) {
                    contexto.append("CUSPP: ").append(usuario.getCuspp()).append("\n");
                }
                if (usuario.getFechaNacimiento() != null) {
                    contexto.append("Fecha de Nacimiento: ").append(usuario.getFechaNacimiento()).append("\n");
                }

                // SALDOS Y PROYECCIONES
                contexto.append("\n=== SALDOS Y PROYECCIONES ===\n");
                Double saldoTotal = saldoPensionRepository.sumSaldosUsuario(idUsuario);
                if (saldoTotal != null && saldoTotal > 0) {
                    contexto.append("Saldo Total: S/ ").append(String.format("%.2f", saldoTotal)).append("\n");
                    
                    // Calcular proyección simple (saldo * 0.06 / 12 para pensión mensual estimada)
                    double proyeccionMensual = (saldoTotal * 0.06) / 12;
                    contexto.append("Proyección Pensión Mensual: S/ ").append(String.format("%.2f", proyeccionMensual)).append("\n");
                } else {
                    contexto.append("Saldo Total: S/ 0.00\n");
                    contexto.append("Proyección Pensión Mensual: S/ 0.00\n");
                }

                // APORTES
                contexto.append("\n=== APORTES ===\n");
                List<AportePension> aportes = aportePensionRepository.findByUsuarioId(idUsuario);
                if (!aportes.isEmpty()) {
                    contexto.append("Total aportes registrados: ").append(aportes.size()).append("\n");
                    
                    // Calcular suma de aportes
                    double sumaAportes = aportes.stream()
                            .mapToDouble(AportePension::getMontoAporte)
                            .sum();
                    contexto.append("Suma total aportada: S/ ").append(String.format("%.2f", sumaAportes)).append("\n");
                    
                    // Último aporte
                    AportePension ultimoAporte = aportes.get(aportes.size() - 1);
                    contexto.append("Último aporte: ").append(ultimoAporte.getPeriodo())
                            .append(" - S/ ").append(String.format("%.2f", ultimoAporte.getMontoAporte())).append("\n");
                } else {
                    contexto.append("No hay aportes registrados\n");
                }

                // SEGUROS Y PÓLIZAS
                contexto.append("\n=== SEGUROS Y PÓLIZAS ===\n");
                List<Seguro> seguros = seguroRepository.findByUsuario(idUsuario);
                if (!seguros.isEmpty()) {
                    long segurosActivos = seguros.stream()
                            .filter(s -> "Activo".equals(s.getEstado()) || "Vigente".equals(s.getEstado()))
                            .count();
                    contexto.append("Pólizas activas: ").append(segurosActivos).append(" de ").append(seguros.size()).append("\n");
                    
                    // Detalles de pólizas activas
                    seguros.stream()
                            .filter(s -> "Activo".equals(s.getEstado()) || "Vigente".equals(s.getEstado()))
                            .forEach(s -> {
                                contexto.append("  • ").append(s.getTipoSeguro() != null ? s.getTipoSeguro().getNombre() : "Seguro")
                                        .append(" - ").append(s.getCompania() != null ? s.getCompania().getNombre() : "")
                                        .append(" - Póliza: ").append(s.getNumeroPoliza())
                                        .append(" - Prima: S/ ").append(String.format("%.2f", s.getPrimaMensual()))
                                        .append("\n");
                            });
                    
                    // Suma de primas
                    double sumaPrimas = seguros.stream()
                            .filter(s -> "Activo".equals(s.getEstado()) || "Vigente".equals(s.getEstado()))
                            .mapToDouble(Seguro::getPrimaMensual)
                            .sum();
                    contexto.append("Prima mensual total: S/ ").append(String.format("%.2f", sumaPrimas)).append("\n");
                } else {
                    contexto.append("No hay seguros registrados\n");
                }

                // BENEFICIARIOS
                contexto.append("\n=== BENEFICIARIOS ===\n");
                List<BeneficiarioSeguro> beneficiarios = beneficiarioSeguroRepository.findByUsuarioId(idUsuario);
                if (!beneficiarios.isEmpty()) {
                    contexto.append("Beneficiarios registrados: ").append(beneficiarios.size()).append("\n");
                    beneficiarios.forEach(b -> {
                        contexto.append("  • ").append(b.getNombreCompleto())
                                .append(" (").append(b.getParentesco()).append(") - ")
                                .append(b.getPorcentaje()).append("%\n");
                    });
                } else {
                    contexto.append("No hay beneficiarios registrados\n");
                }

                // INFORMACIÓN GENERAL DE LA PLATAFORMA
                contexto.append("\n=== SEGUROS DISPONIBLES EN LA PLATAFORMA ===\n");
                List<TipoSeguro> tiposSeguros = tipoSeguroRepository.findAll();
                if (!tiposSeguros.isEmpty()) {
                    tiposSeguros.forEach(ts -> {
                        contexto.append("  • ").append(ts.getNombre())
                                .append(" - ").append(ts.getDescripcion() != null ? ts.getDescripcion() : "")
                                .append("\n");
                    });
                }

                contexto.append("\n=== COMPAÑÍAS ASOCIADAS ===\n");
                List<CompaniaSeguro> companias = companiaSeguroRepository.findAll();
                if (!companias.isEmpty()) {
                    contexto.append("Trabajamos con ").append(companias.size()).append(" compañías: ");
                    companias.forEach(c -> contexto.append(c.getNombre()).append(", "));
                    contexto.append("\n");
                }
            }
        } catch (Exception e) {
            logger.error("Error obteniendo contexto: {}", e.getMessage());
        }

        return contexto.length() > 0 ? contexto.toString() : "Usuario sin datos registrados";
    }

    @Override
    public boolean solicitarContactoAsesor(Integer idUsuario, String motivo) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();

                // Enviar email al equipo de soporte
                String asunto = "Solicitud de Asesoría - ChatBot";
                String mensaje = String.format("""
                    Un usuario ha solicitado contacto con un asesor desde el ChatBot.
                    
                    DATOS DEL USUARIO:
                    - Nombre: %s %s
                    - DNI: %s
                    - Email: %s
                    - Teléfono: %s
                    
                    MOTIVO:
                    %s
                    
                    Por favor, contactar al usuario a la brevedad.
                    """,
                        usuario.getNombre(),
                        usuario.getApellido(),
                        usuario.getDni(),
                        usuario.getCorreo(),
                        usuario.getTelefono(),
                        motivo != null ? motivo : "No especificado"
                );

                emailService.enviarCorreoSimple("soporte@sumaqseguros.com", asunto, mensaje);

                // Notificar al usuario
                if (usuario.isNotificacionesEmail()) {
                    emailService.enviarCorreoSimple(
                            usuario.getCorreo(),
                            "Solicitud de Asesoría Recibida",
                            String.format("""
                            Hola %s,
                            
                            Hemos recibido tu solicitud de asesoría.
                            Un asesor se comunicará contigo pronto por correo o teléfono.
                            
                            Gracias por confiar en SumaqSeguros.
                            """, usuario.getNombre())
                    );
                }

                logger.info("Solicitud de asesor enviada para usuario: {}", idUsuario);
                return true;
            }

            return false;

        } catch (Exception e) {
            logger.error("Error solicitando asesor: {}", e.getMessage());
            return false;
        }
    }

    private String generarRespuestaBasica(String mensaje, String contextoUsuario) {
        String mensajeLower = mensaje.toLowerCase();

        if (mensajeLower.contains("onp") && mensajeLower.contains("afp")) {
            return "📊 ONP es un sistema público de pensiones con aporte solidario del 13%. AFP es privado con cuentas individuales y diferentes fondos de inversión (conservador, moderado, agresivo). ¿Necesitas más detalles sobre cuál te conviene?";
        }

        if (mensajeLower.contains("saldo") || mensajeLower.contains("cuanto tengo")) {
            return "💰 Puedes consultar tu saldo actual y disponible en la sección 'Panel Financiero' o 'Gestión de Pensiones'. También verás tu proyección de pensión mensual estimada. ¿Necesitas ayuda navegando?";
        }

        if (mensajeLower.contains("proyecc") || mensajeLower.contains("pension mensual")) {
            // Extraer valores del contexto
            String saldo = extraerValor(contextoUsuario, "Saldo Total: S/ ");
            String proyeccion = extraerValor(contextoUsuario, "Proyección Pensión Mensual: S/ ");
            
            if (saldo != null && proyeccion != null) {
                return String.format("📈 Según tu saldo actual de S/ %s, tu proyección de pensión mensual es de S/ %s. " +
                        "Esta estimación se basa en una rentabilidad del 6%% anual. " +
                        "¿Quieres saber cómo mejorar tu proyección?", saldo, proyeccion);
            }
            return "📈 Tu proyección de pensión se calcula en base a tu saldo actual. Puedes verla en el Dashboard o Panel Financiero. ¿Quieres saber cómo mejorar tu proyección?";
        }

        if (mensajeLower.contains("aporte") || mensajeLower.contains("registrar")) {
            return "📝 Puedes registrar aportes en 'Gestión de Pensiones'. El monto total se calcula automáticamente sumando tu aporte y el del empleador. Necesitas: período, empleador, fecha y montos. ¿Quieres que te guíe?";
        }

        if (mensajeLower.contains("seguro") || mensajeLower.contains("poliza")) {
            return "🛡️ Ofrecemos seguros vehiculares, de hogar, salud y vida con múltiples compañías. Puedes compararlos en el 'Comparador' y solicitar cotizaciones. ¿Te interesa algún tipo en particular?";
        }

        if (mensajeLower.contains("beneficiario")) {
            return "👥 Puedes gestionar tus beneficiarios en 'Gestión de Seguros'. Recuerda que la suma de porcentajes debe ser 100%. ¿Necesitas ayuda para agregar o modificar beneficiarios?";
        }

        if (mensajeLower.contains("compara") || mensajeLower.contains("cotiza")) {
            return "🔍 El Comparador te permite ver hasta 3 planes simultáneamente, comparar coberturas, primas y solicitar cotizaciones. ¿Qué tipo de seguro te interesa comparar?";
        }

        if (mensajeLower.contains("mis datos") || mensajeLower.contains("mi informacion") || 
            mensajeLower.contains("datos personales") || mensajeLower.contains("mi dni") || 
            mensajeLower.contains("mi correo") || mensajeLower.contains("mi telefono")) {
            // Extraer información personal del contexto
            String nombre = extraerValor(contextoUsuario, "Nombre Completo: ");
            String dni = extraerValor(contextoUsuario, "DNI: ");
            String correo = extraerValor(contextoUsuario, "Correo: ");
            String telefono = extraerValor(contextoUsuario, "Teléfono: ");
            
            StringBuilder respuesta = new StringBuilder("👤 Aquí está tu información personal:\n\n");
            if (nombre != null) respuesta.append("📝 Nombre: ").append(nombre).append("\n");
            if (dni != null) respuesta.append("🆔 DNI: ").append(dni).append("\n");
            if (correo != null) respuesta.append("📧 Correo: ").append(correo).append("\n");
            if (telefono != null) respuesta.append("📱 Teléfono: ").append(telefono).append("\n");
            
            respuesta.append("\n¿Necesitas actualizar algún dato?");
            return respuesta.toString();
        }

        return "Puedo ayudarte con: 👤 información personal, 💰 saldos y proyecciones, 📊 aportes, 🛡️ seguros y pólizas, 👥 beneficiarios, 🔍 comparador. ¿Sobre qué necesitas información?";
    }

    private String extraerValor(String contexto, String clave) {
        try {
            int inicio = contexto.indexOf(clave);
            if (inicio == -1) return null;
            
            inicio += clave.length();
            int fin = contexto.indexOf("\n", inicio);
            if (fin == -1) fin = contexto.length();
            
            return contexto.substring(inicio, fin).trim();
        } catch (Exception e) {
            return null;
        }
    }
}