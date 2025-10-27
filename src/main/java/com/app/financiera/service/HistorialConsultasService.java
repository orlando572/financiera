package com.app.financiera.service;

import com.app.financiera.entity.HistorialConsultas;

import java.time.LocalDateTime;
import java.util.List;

public interface HistorialConsultasService {

    HistorialConsultas registrarConsulta(int idUsuario, String tipoConsulta, String detalleConsulta, 
                                         String resultado, String detallesAdicionales);

    List<HistorialConsultas> obtenerHistorialUsuario(int idUsuario);

    List<HistorialConsultas> obtenerHistorialPorTipo(int idUsuario, String tipoConsulta);

    List<HistorialConsultas> obtenerConsultasExitosas(int idUsuario);

    List<HistorialConsultas> obtenerConsultasConErrores(int idUsuario);

    List<HistorialConsultas> obtenerUltimasConsultas(int idUsuario, int limite);

    List<HistorialConsultas> obtenerPorRangoFechas(int idUsuario, LocalDateTime inicio, LocalDateTime fin);

    void limpiarHistorialAntiguo(int dias);
}
