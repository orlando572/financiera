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

/**
 * Controlador REST para la administración de usuarios.
 * Permite listar, crear, actualizar, eliminar y cambiar el estado de los usuarios del sistema.
 * También proporciona estadísticas generales de los registros de usuarios.
 */
@RestController
@RequestMapping("/api/admin/usuarios")
@CrossOrigin(origins = AppSettings.URL_CROSS_ORIGIN)
public class AdminUsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(AdminUsuarioController.class);

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Obtiene la lista de usuarios registrados en el sistema.
     * Permite filtrar por nombre, DNI o estado según los parámetros opcionales.
     * Si no se proporcionan filtros, devuelve todos los usuarios.
     *
     * @param busqueda texto opcional para buscar usuarios por nombre o DNI
     * @param estado estado opcional para filtrar usuarios (por ejemplo, Activo o Inactivo)
     * @return una respuesta HTTP con la lista de usuarios o un mensaje de error en caso de fallo
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
     * Recupera los datos de un usuario específico a partir de su identificador único.
     *
     * @param id identificador numérico del usuario a consultar
     * @return una respuesta HTTP con los datos del usuario o un mensaje de error si no se encuentra
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
     * Obtiene estadísticas generales de los usuarios registrados.
     * Las estadísticas pueden incluir métricas como cantidad total de usuarios o distribución por estado.
     *
     * @return una respuesta HTTP con los datos estadísticos o un mensaje de error en caso de fallo
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

    /**
     * Registra un nuevo usuario en el sistema con la información proporcionada.
     *
     * @param usuario objeto {@link Usuario} con los datos del nuevo usuario
     * @return una respuesta HTTP con el mensaje de éxito y los datos del usuario creado,
     *         o un mensaje de error si ocurre una excepción
     */
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

    /**
     * Actualiza la información de un usuario existente identificado por su ID.
     *
     * @param id identificador del usuario a actualizar
     * @param usuario objeto {@link Usuario} con los nuevos valores a registrar
     * @return una respuesta HTTP con el usuario actualizado o un mensaje de error en caso de fallo
     */
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

    /**
     * Elimina lógicamente un usuario del sistema, cambiando su estado a "Inactivo".
     *
     * @param id identificador del usuario a eliminar
     * @return una respuesta HTTP con un mensaje de confirmación o un error si el usuario no se encuentra
     */
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

    /**
     * Cambia el estado de un usuario (por ejemplo, de Activo a Inactivo).
     *
     * @param id identificador del usuario cuyo estado se desea modificar
     * @param body cuerpo del request que contiene el nuevo estado
     * @return una respuesta HTTP con el usuario actualizado o un mensaje de error si ocurre un fallo
     */
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