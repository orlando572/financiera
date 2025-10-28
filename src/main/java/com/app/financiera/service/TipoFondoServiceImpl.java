package com.app.financiera.service;

import com.app.financiera.entity.TipoFondo;
import com.app.financiera.repository.TipoFondoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoFondoServiceImpl implements TipoFondoService {

    @Autowired
    private TipoFondoRepository repository;

    @Override
    public List<TipoFondo> obtenerFondosActivos() {
        return repository.findActivos();
    }

    @Override
    public List<TipoFondo> obtenerFondosPorCategoria(String categoria) {
        return repository.findByCategoria(categoria);
    }

    @Override
    public TipoFondo obtenerPorId(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<TipoFondo> obtenerTodos() {
        return repository.findAll();
    }
}
