package com.app.financiera.controller;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.financiera.entity.Usuario;
import com.app.financiera.service.UsuarioService;
import com.app.financiera.util.AppSettings;

/**
 * Controlador REST para administración de usuarios
 * Maneja operaciones CRUD completas y estadísticas para administradores
 *
 * @author Sistema Financiero
 * @version 1.0
 */
@RestController
@RequestMapping("/api/admin/usuarios")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class AdminUsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(AdminUsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;

    // ========================================
    // ENDPOINTS DE CONSULTA
    // ========================================

    /**
     * Lista todos los usuarios con filtros opcionales
     * Permite filtrar por nombre/DNI o estado
     *
     * @param busqueda Término de búsqueda para filtrar por nombre o DNI (opcional)
     * @param estado Estado del usuario: "Activo", "Inactivo" o "Bloqueado" (opcional)
     * @return ResponseEntity con lista de usuarios filtrados
     */
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

    /**
     * Obtiene un usuario específico por su ID
     *
     * @param id ID del usuario a consultar
     * @return ResponseEntity con datos del usuario o mensaje de error
     */
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

    /**
     * Obtiene estadísticas generales de usuarios
     * Incluye: total, activos, inactivos y bloqueados
     *
     * @return ResponseEntity con mapa de estadísticas
     */
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

    // ========================================
    // ENDPOINTS DE CREACIÓN Y ACTUALIZACIÓN
    // ========================================

    /**
     * Crea un nuevo usuario en el sistema
     * Valida que no exista otro usuario con el mismo DNI
     *
     * @param usuario Datos del usuario a crear
     * @return ResponseEntity con usuario creado o mensaje de error
     */
    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        HashMap<String, Object> respuesta = new HashMap<>();
        try {
            logger.info("Creando nuevo usuario: {}", usuario.getDni());

            // Verificar si ya existe
            Usuario existente = usuarioService.buscarPorDni(usuario.getDni());
            if (existente != null) {
                respuesta.put("mensaje", "Ya existe un usuario con ese DNI");
                return ResponseEntity.status(400).body(respuesta);
            }

            Usuario nuevoUsuario = usuarioService.registraUsuario(usuario);
            respuesta.put("mensaje", "Usuario creado exitosamente");
            respuesta.put("data", nuevoUsuario);

            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al crear usuario: {}", e.getMessage());
            respuesta.put("mensaje", "Error al crear usuario");
            respuesta.put("error", e.getMessage());
            return ResponseEntity.status(500).body(respuesta);
        }
    }

    /**
     * Actualiza los datos de un usuario existente
     *
     * @param id ID del usuario a actualizar
     * @param usuario Nuevos datos del usuario
     * @return ResponseEntity con usuario actualizado o mensaje de error
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(
            @PathVariable int id,
            @RequestBody Usuario usuario) {
        HashMap<String, Object> respuesta = new HashMap<>();
        try {
            logger.info("Actualizando usuario ID: {}", id);

            List<Usuario> existente = usuarioService.buscarUsuarioPorId(id);
            if (existente.isEmpty()) {
                respuesta.put("mensaje", "Usuario no encontrado");
                return ResponseEntity.status(404).body(respuesta);
            }

            usuario.setIdUsuario(id);
            Usuario actualizado = usuarioService.actualizaUsuario(usuario);

            respuesta.put("mensaje", "Usuario actualizado exitosamente");
            respuesta.put("data", actualizado);

            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al actualizar usuario: {}", e.getMessage());
            respuesta.put("mensaje", "Error al actualizar usuario");
            respuesta.put("error", e.getMessage());
            return ResponseEntity.status(500).body(respuesta);
        }
    }

    // ========================================
    // ENDPOINTS DE ELIMINACIÓN Y ESTADO
    // ========================================

    /**
     * Elimina un usuario (soft delete)
     * Cambia el estado del usuario a "Inactivo" sin eliminarlo físicamente
     *
     * @param id ID del usuario a eliminar
     * @return ResponseEntity con mensaje de confirmación o error
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable int id) {
        HashMap<String, Object> respuesta = new HashMap<>();
        try {
            logger.info("Eliminando usuario ID: {}", id);

            List<Usuario> existente = usuarioService.buscarUsuarioPorId(id);
            if (existente.isEmpty()) {
                respuesta.put("mensaje", "Usuario no encontrado");
                return ResponseEntity.status(404).body(respuesta);
            }

            Usuario usuario = existente.get(0);
            usuario.setEstado("Inactivo");
            usuarioService.actualizaUsuario(usuario);

            respuesta.put("mensaje", "Usuario eliminado exitosamente");
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al eliminar usuario: {}", e.getMessage());
            respuesta.put("mensaje", "Error al eliminar usuario");
            respuesta.put("error", e.getMessage());
            return ResponseEntity.status(500).body(respuesta);
        }
    }

    /**
     * Cambia el estado de un usuario
     * Permite activar, desactivar o bloquear un usuario
     *
     * @param id ID del usuario
     * @param body Mapa con el nuevo estado {"estado": "Activo"|"Inactivo"|"Bloqueado"}
     * @return ResponseEntity con usuario actualizado o mensaje de error
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable int id,
            @RequestBody HashMap<String, String> body) {
        HashMap<String, Object> respuesta = new HashMap<>();
        try {
            String nuevoEstado = body.get("estado");
            logger.info("Cambiando estado de usuario ID {} a {}", id, nuevoEstado);

            List<Usuario> existente = usuarioService.buscarUsuarioPorId(id);
            if (existente.isEmpty()) {
                respuesta.put("mensaje", "Usuario no encontrado");
                return ResponseEntity.status(404).body(respuesta);
            }

            Usuario usuario = existente.get(0);
            usuario.setEstado(nuevoEstado);
            usuarioService.actualizaUsuario(usuario);

            respuesta.put("mensaje", "Estado actualizado exitosamente");
            respuesta.put("data", usuario);

            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            logger.error("Error al cambiar estado: {}", e.getMessage());
            respuesta.put("mensaje", "Error al cambiar estado");
            return ResponseEntity.status(500).body(respuesta);
        }
    }

}