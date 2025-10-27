package com.app.financiera.service;

import com.app.financiera.entity.CompaniaSeguro;

import java.util.HashMap;
import java.util.List;

public interface AdminCompaniasSeguroService {
    
    List<CompaniaSeguro> listarCompanias(String estado);
    
    CompaniaSeguro obtenerCompaniaPorId(int id);
    
    CompaniaSeguro crearCompania(CompaniaSeguro compania);
    
    CompaniaSeguro actualizarCompania(CompaniaSeguro compania);
    
    void eliminarCompania(int id);
    
    void cambiarEstado(int id, String estado);
    
    HashMap<String, Object> obtenerEstadisticas();
}
