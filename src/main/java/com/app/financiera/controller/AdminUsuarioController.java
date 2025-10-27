package com.app.financiera.controller;

import com.app.financiera.entity.Usuario;
import com.app.financiera.service.UsuarioService;
import com.app.financiera.util.AppSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/usuarios")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class AdminUsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(AdminUsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<?> listarUsuarios(
            @RequestParam(required = false) String busqueda,
            @RequestParam(required = false) String estado) {
        try {
            logger.info("Listando usuarios - Búsqueda: {}, Estado: {}", busqueda, estado);
            List<Usuario> usuarios;

            if (busqueda != null && !busqueda.isEmpty()) {
                usuarios = usuarioService.buscarPorNombreODni(busqueda);
            } else if (estado != null && !estado.isEmpty()) {
                usuarios = usuarioService.buscarPorEstado(estado);
            } else {
                usuarios = usuarioService.listaTodos();
            }

            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            logger.error("Error al listar usuarios: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al listar usuarios");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUsuarioPorId(@PathVariable int id) {
        try {
            logger.info("Obteniendo usuario por ID: {}", id);
            List<Usuario> usuarios = usuarioService.buscarUsuarioPorId(id);

            if (usuarios.isEmpty()) {
                return ResponseEntity.status(404).body("Usuario no encontrado");
            }

            return ResponseEntity.ok(usuarios.get(0));
        } catch (Exception e) {
            logger.error("Error al obtener usuario: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener usuario");
        }
    }

    @GetMapping("/estadisticas")
    public ResponseEntity<?> obtenerEstadisticas() {
        try {
            HashMap<String, Object> stats = usuarioService.obtenerEstadisticas();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            logger.error("Error al obtener estadísticas: {}", e.getMessage());
            return ResponseEntity.status(500).body("Error al obtener estadísticas");
        }
    }

    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        try {
            logger.info("Creando nuevo usuario: {}", usuario.getDni());
            Usuario nuevoUsuario = usuarioService.registraUsuario(usuario);
            return ResponseEntity.ok(Map.of("mensaje", "Usuario creado exitosamente", "data", nuevoUsuario));
        } catch (RuntimeException e) {
            logger.warn("Error de validación al crear usuario: {}", e.getMessage());
            return ResponseEntity.status(400).body(Map.of("mensaje", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error al crear usuario: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al crear usuario", "error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable int id, @RequestBody Usuario usuario) {
        try {
            logger.info("Actualizando usuario ID: {}", id);
            usuario.setIdUsuario(id);
            Usuario actualizado = usuarioService.actualizaUsuario(usuario);
            return ResponseEntity.ok(Map.of("mensaje", "Usuario actualizado exitosamente", "data", actualizado));
        } catch (Exception e) {
            logger.error("Error al actualizar usuario: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al actualizar usuario", "error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable int id) {
        try {
            logger.info("Eliminando usuario ID: {}", id);
            List<Usuario> existente = usuarioService.buscarUsuarioPorId(id);
            if (existente.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("mensaje", "Usuario no encontrado"));
            }
            Usuario usuario = existente.get(0);
            usuario.setEstado("Inactivo");
            usuarioService.actualizaUsuario(usuario);
            return ResponseEntity.ok(Map.of("mensaje", "Usuario eliminado exitosamente"));
        } catch (Exception e) {
            logger.error("Error al eliminar usuario: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al eliminar usuario", "error", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable int id, @RequestBody HashMap<String, String> body) {
        try {
            String nuevoEstado = body.get("estado");
            logger.info("Cambiando estado de usuario ID {} a {}", id, nuevoEstado);
            List<Usuario> existente = usuarioService.buscarUsuarioPorId(id);
            if (existente.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("mensaje", "Usuario no encontrado"));
            }
            Usuario usuario = existente.get(0);
            usuario.setEstado(nuevoEstado);
            usuarioService.actualizaUsuario(usuario);
            return ResponseEntity.ok(Map.of("mensaje", "Estado actualizado exitosamente", "data", usuario));
        } catch (Exception e) {
            logger.error("Error al cambiar estado: {}", e.getMessage());
            return ResponseEntity.status(500).body(Map.of("mensaje", "Error al cambiar estado"));
        }
    }
}