package com.app.financiera.service;

import com.app.financiera.entity.HistorialRentabilidad;

import java.time.LocalDateTime;
import java.util.List;

public interface HistorialRentabilidadService {

    HistorialRentabilidad registrarRentabilidad(
        int idAfp,
        int idTipoFondo,
        String periodo,
        Double rentabilidadNominal,
        Double rentabilidadReal,
        Double patrimonio,
        Integer numeroAfiliados
    );

    List<HistorialRentabilidad> obtenerHistorialPorAfpYFondo(int idAfp, int idTipoFondo);

    HistorialRentabilidad obtenerUltimoRegistro(int idAfp, int idTipoFondo);

    List<HistorialRentabilidad> obtenerHistorialPorPeriodo(String periodo);

    List<HistorialRentabilidad> obtenerHistorialPorRangoFechas(
        LocalDateTime fechaInicio,
        LocalDateTime fechaFin
    );

    Double calcularRentabilidadPromedio(int idAfp, int idTipoFondo);

    List<HistorialRentabilidad> compararAfpsPorPeriodo(String periodo, int idTipoFondo);
}
