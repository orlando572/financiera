package com.app.financiera.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.financiera.entity.Usuario;
import com.app.financiera.entity.RolUsuario;
import com.app.financiera.entity.Afp;
import com.app.financiera.service.UsuarioService;
import com.app.financiera.service.RolUsuarioService;
import com.app.financiera.service.AfpService;
import com.app.financiera.util.AppSettings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    // Obtener lista de roles
    @GetMapping("/roles")
    @ResponseBody
    public List<RolUsuario> listarRoles() {
        return rolUsuarioService.listarRoles();
    }

    // Obtener lista de AFPs
    @GetMapping("/afps")
    @ResponseBody
    public List<Afp> listarAfps() {
        return afpService.listarAfps();
    }

    // Registrar Usuario
    @PostMapping("/registrarUsuario")
    @ResponseBody
    public ResponseEntity<?> registra(@RequestBody Usuario obj) {
        HashMap<String, Object> salida = new HashMap<>();
        try {
            logger.info("Intentando registrar usuario con DNI: {}", obj.getDni());

            Usuario existente = usuarioService.buscarPorDni(obj.getDni());
            if (existente != null) {
                logger.warn("Intento de registro con DNI duplicado: {}", obj.getDni());
                salida.put("mensaje", "Ya existe un usuario con ese DNI");
                return ResponseEntity.status(400).body(salida);
            }

            Usuario objSalida = usuarioService.registraUsuario(obj);
            if (objSalida == null) {
                logger.error("Error al registrar usuario con DNI: {}", obj.getDni());
                salida.put("mensaje", "Ocurrió un error al registrar");
            } else {
                logger.info("Usuario registrado exitosamente: {} {} (ID: {})",
                        objSalida.getNombre(), objSalida.getApellido(), objSalida.getIdUsuario());
                salida.put("mensaje", "Se registró exitosamente el usuario " +
                        "'" + objSalida.getNombre() + " " + objSalida.getApellido() + "'" +
                        " ID asignado: " + objSalida.getIdUsuario());
            }
        } catch (Exception e) {
            logger.error("Excepción al registrar usuario con DNI: {} - {}", obj.getDni(), e.getMessage(), e);
            salida.put("mensaje", AppSettings.MENSAJE_REG_ERROR);
        }
        return ResponseEntity.ok(salida);
    }

    // Buscar usuario por id
    @GetMapping("/buscarPorId/{id}")
    @ResponseBody
    public ResponseEntity<?> buscarUsuarioPorId(@PathVariable Integer id) {
        try {
            List<Usuario> usuarios = usuarioService.buscarUsuarioPorId(id);
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al buscar usuario");
        }
    }

    // Listar todos los usuarios
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<Usuario>> getAllUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.listaTodos();
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    // Login
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        HashMap<String, Object> salida = new HashMap<>();
        try {
            String dni = credentials.get("dni");
            String claveSol = credentials.get("claveSol");

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

            logger.info("Login exitoso para usuario: {} {}", usuario.getNombre(), usuario.getApellido());
            return ResponseEntity.ok(usuario);

        } catch (Exception e) {
            logger.error("Error en login (DNI: {}): {}", credentials.get("dni"), e.getMessage(), e);
            salida.put("mensaje", "Error en el servidor");
            return ResponseEntity.status(500).body(salida);
        }
    }
}