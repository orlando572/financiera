package com.app.financiera.service;

import com.app.financiera.entity.TipoFondo;

import java.util.List;

public interface TipoFondoService {
    
    List<TipoFondo> obtenerFondosActivos();
    
    List<TipoFondo> obtenerFondosPorCategoria(String categoria);
    
    TipoFondo obtenerPorId(int id);
    
    List<TipoFondo> obtenerTodos();
}
