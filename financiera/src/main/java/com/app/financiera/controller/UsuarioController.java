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

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class UsuarioController {

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
            // Validar que no exista el DNI
            Usuario existente = usuarioService.buscarPorDni(obj.getDni());
            if (existente != null) {
                salida.put("mensaje", "Ya existe un usuario con ese DNI");
                return ResponseEntity.status(400).body(salida);
            }

            Usuario objSalida = usuarioService.registraUsuario(obj);
            if (objSalida == null) {
                salida.put("mensaje", "Ocurrió un error al registrar");
            } else {
                salida.put("mensaje", "Se registró exitosamente el usuario " +
                        "'" + objSalida.getNombre() + " " + objSalida.getApellido() + "'" +
                        " ID asignado: " + objSalida.getIdUsuario());
            }
        } catch (Exception e) {
            e.printStackTrace();
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

            Usuario usuario = usuarioService.buscarPorDni(dni);

            if (usuario == null) {
                salida.put("mensaje", "Usuario no encontrado");
                return ResponseEntity.status(404).body(salida);
            }

            if (!usuario.getClaveSol().equals(claveSol)) {
                salida.put("mensaje", "Clave incorrecta");
                return ResponseEntity.status(401).body(salida);
            }

            // Login exitoso
            return ResponseEntity.ok(usuario);

        } catch (Exception e) {
            e.printStackTrace();
            salida.put("mensaje", "Error en el servidor");
            return ResponseEntity.status(500).body(salida);
        }
    }
}