package com.app.financiera.controller;

import com.app.financiera.entity.HistorialConsultas;
import com.app.financiera.service.HistorialConsultasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que gestiona las consultas realizadas por los usuarios,
 * permitiendo obtener el historial completo, filtrado por tipo o limitado a las últimas consultas.
 */
@RestController
@RequestMapping("/api/historial-consultas")
@CrossOrigin(origins = "*")
public class HistorialConsultasController {

    @Autowired
    private HistorialConsultasService historialConsultasService;

    /**
     * Obtiene todo el historial de consultas registradas por un usuario.
     * @param idUsuario identificador único del usuario
     * @return lista completa del historial de consultas del usuario
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<HistorialConsultas>> obtenerHistorialUsuario(@PathVariable int idUsuario) {
        return ResponseEntity.ok(historialConsultasService.obtenerHistorialUsuario(idUsuario));
    }

    /**
     * Obtiene el historial de consultas del usuario filtrado por tipo de consulta.
     *
     * @param idUsuario identificador único del usuario
     * @param tipoConsulta tipo de consulta a filtrar (por ejemplo: AFP, ONP, comparativo)
     * @return lista de consultas que coinciden con el tipo especificado
     */
    @GetMapping("/usuario/{idUsuario}/tipo/{tipoConsulta}")
    public ResponseEntity<List<HistorialConsultas>> obtenerHistorialPorTipo(
            @PathVariable int idUsuario,
            @PathVariable String tipoConsulta) {
        return ResponseEntity.ok(historialConsultasService.obtenerHistorialPorTipo(idUsuario, tipoConsulta));
    }

    /**
     * Obtiene las últimas consultas realizadas por el usuario, limitadas a una cantidad específica.
     *
     * @param idUsuario identificador único del usuario
     * @param limite número máximo de registros a devolver
     * @return lista de las últimas consultas del usuario
     */
    @GetMapping("/usuario/{idUsuario}/ultimas/{limite}")
    public ResponseEntity<List<HistorialConsultas>> obtenerUltimasConsultas(
            @PathVariable int idUsuario,
            @PathVariable int limite) {
        return ResponseEntity.ok(historialConsultasService.obtenerUltimasConsultas(idUsuario, limite));
    }
}
