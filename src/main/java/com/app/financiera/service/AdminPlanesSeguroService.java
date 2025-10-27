package com.app.financiera.service;

import com.app.financiera.entity.TipoSeguro;

import java.util.HashMap;
import java.util.List;

public interface AdminPlanesSeguroService {
    
    List<TipoSeguro> listarPlanes(String categoria, String estado);
    
    TipoSeguro obtenerPlanPorId(int id);
    
    TipoSeguro crearPlan(TipoSeguro plan);
    
    TipoSeguro actualizarPlan(TipoSeguro plan);
    
    void eliminarPlan(int id);
    
    void cambiarEstado(int id, String estado);
    
    HashMap<String, Object> obtenerEstadisticas();
}
