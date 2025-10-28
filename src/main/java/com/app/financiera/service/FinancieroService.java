package com.app.financiera.service;

import java.util.HashMap;
import java.util.List;

public interface FinancieroService {
    
    HashMap<String, Object> obtenerResumenFinanciero(int idUsuario);
    
    HashMap<String, Object> obtenerEstadisticasFinancieras(int idUsuario);
    
    HashMap<String, Object> obtenerComparativoSistemas(int idUsuario);
    
    HashMap<String, Object> obtenerSaldoTotalYDisponible(int idUsuario);
    
    List<HashMap<String, Object>> obtenerAportesPorAnio(int idUsuario, int cantidadAnios);
}
