package com.app.financiera.controller;

import com.app.financiera.dto.LoginRequest;
import com.app.financiera.dto.LoginResponse;
import com.app.financiera.entity.Afp;
import com.app.financiera.entity.RolUsuario;
import com.app.financiera.entity.Usuario;
import com.app.financiera.security.JwtUtil;
import com.app.financiera.service.AfpService;
import com.app.financiera.service.RolUsuarioService;
import com.app.financiera.service.UsuarioService;
import com.app.financiera.util.AppSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;

/**
 * Controlador REST que gestiona las operaciones relacionadas con los usuarios,
 * incluyendo registro, autenticación, búsqueda y obtención de roles o AFPs.
 */
@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RolUsuarioService rolUsuarioService;

    @Autowired
    private AfpService afpService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Obtiene la lista de roles de usuario disponibles en el sistema.
     *
     * @return lista de objetos {@link RolUsuario}
     */
    @GetMapping("/roles")
    @ResponseBody
    public List<RolUsuario> listarRoles() {
        return rolUsuarioService.listarRoles();
    }

    /**
     * Obtiene la lista de AFPs registradas en el sistema.
     *
     * @return lista de objetos {@link Afp}
     */
    @GetMapping("/afps")
    @ResponseBody
    public List<Afp> listarAfps() {
        return afpService.listarAfps();
    }

    /**
     * Obtiene la lista de AFPs registradas en el sistema.
     *
     * @return lista de objetos {@link Afp}
     */
    @PostMapping("/registrarUsuario")
    @ResponseBody
    public ResponseEntity<?> registra(@RequestBody Usuario obj) {
        HashMap<String, Object> salida = new HashMap<>();
        try {
            logger.info("Intentando registrar usuario con DNI: {}", obj.getDni());
            Usuario objSalida = usuarioService.registraUsuario(obj);

            salida.put("mensaje", "Se registró exitosamente el usuario " +
                    "'" + objSalida.getNombre() + " " + objSalida.getApellido() + "'" +
                    " ID asignado: " + objSalida.getIdUsuario());
            logger.info("Usuario registrado exitosamente: {} {} (ID: {})",
                    objSalida.getNombre(), objSalida.getApellido(), objSalida.getIdUsuario());
            return ResponseEntity.ok(salida);
        } catch (RuntimeException e) {
            logger.warn("Error de validación al registrar usuario: {}", e.getMessage());
            salida.put("mensaje", e.getMessage());
            return ResponseEntity.status(400).body(salida);
        } catch (Exception e) {
            logger.error("Excepción al registrar usuario con DNI: {} - {}", obj.getDni(), e.getMessage(), e);
            salida.put("mensaje", AppSettings.MENSAJE_REG_ERROR);
            return ResponseEntity.status(500).body(salida);
        }
    }

    /**
     * Inicia sesión para un usuario existente validando su DNI y clave SOL.
     * Genera y retorna un token JWT en caso de autenticación exitosa.
     *
     * @param loginRequest objeto {@link LoginRequest} con las credenciales del usuario
     * @return respuesta HTTP con token JWT y datos del usuario, o mensaje de error
     */
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        HashMap<String, Object> salida = new HashMap<>();
        try {
            String dni = loginRequest.getDni();
            String claveSol = loginRequest.getClaveSol();

            logger.info("Intento de login con DNI: {}", dni);

            Usuario usuario = usuarioService.buscarPorDni(dni);

            if (usuario == null) {
                logger.warn("Login fallido: usuario no encontrado (DNI: {})", dni);
                salida.put("mensaje", "Usuario no encontrado");
                return ResponseEntity.status(404).body(salida);
            }

            if (!usuario.getClaveSol().equals(claveSol)) {
                logger.warn("Login fallido: clave incorrecta para usuario con DNI: {}", dni);
                salida.put("mensaje", "Clave incorrecta");
                return ResponseEntity.status(401).body(salida);
            }

            // Generar token JWT
            String token = jwtUtil.generateToken(
                    usuario.getDni(),
                    usuario.getIdUsuario(),
                    usuario.getRol().getIdRol()
            );

            logger.info("Login exitoso para usuario: {} {} - Token generado",
                    usuario.getNombre(), usuario.getApellido());

            // Crear respuesta con token y datos del usuario
            LoginResponse response = new LoginResponse(token, usuario, "Login exitoso");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error en login (DNI: {}): {}", loginRequest.getDni(), e.getMessage(), e);
            salida.put("mensaje", "Error en el servidor");
            return ResponseEntity.status(500).body(salida);
        }
    }

    /**
     * Busca un usuario por su identificador único.
     *
     * @param id identificador del usuario
     * @return respuesta HTTP con el usuario encontrado o mensaje de error
     */
    @GetMapping("/buscarPorId/{id}")
    @ResponseBody
    public ResponseEntity<?> buscarUsuarioPorId(@PathVariable Integer id) {
        try {
            List<Usuario> usuarios = usuarioService.buscarUsuarioPorId(id);
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            logger.error("Error al buscar usuario por ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(500).body("Error al buscar usuario");
        }
    }

    /**
     * Obtiene la lista completa de usuarios registrados.
     *
     * @return respuesta HTTP con la lista de usuarios o error del servidor
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.listaTodos();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            logger.error("Error al listar usuarios: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }

}