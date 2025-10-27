package com.app.financiera.service;

import com.app.financiera.entity.CompaniaSeguro;
import com.app.financiera.entity.TipoSeguro;

import java.util.List;

public interface CatalogosService {
    
    List<TipoSeguro> obtenerTiposSegurosActivos();
    
    List<TipoSeguro> obtenerTiposPorCategoria(String categoria);
    
    List<String> obtenerCategorias();
    
    List<CompaniaSeguro> obtenerCompaniasActivas();
    
    List<CompaniaSeguro> buscarCompaniasPorNombre(String nombre);
}
