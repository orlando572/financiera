package com.app.financiera.controller;

import com.app.financiera.entity.HistorialConsultas;
import com.app.financiera.service.HistorialConsultasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial-consultas")
@CrossOrigin(origins = "*")
public class HistorialConsultasController {

    @Autowired
    private HistorialConsultasService historialConsultasService;

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<HistorialConsultas>> obtenerHistorialUsuario(@PathVariable int idUsuario) {
        return ResponseEntity.ok(historialConsultasService.obtenerHistorialUsuario(idUsuario));
    }

    @GetMapping("/usuario/{idUsuario}/tipo/{tipoConsulta}")
    public ResponseEntity<List<HistorialConsultas>> obtenerHistorialPorTipo(
            @PathVariable int idUsuario,
            @PathVariable String tipoConsulta) {
        return ResponseEntity.ok(historialConsultasService.obtenerHistorialPorTipo(idUsuario, tipoConsulta));
    }

    @GetMapping("/usuario/{idUsuario}/ultimas/{limite}")
    public ResponseEntity<List<HistorialConsultas>> obtenerUltimasConsultas(
            @PathVariable int idUsuario,
            @PathVariable int limite) {
        return ResponseEntity.ok(historialConsultasService.obtenerUltimasConsultas(idUsuario, limite));
    }
}
