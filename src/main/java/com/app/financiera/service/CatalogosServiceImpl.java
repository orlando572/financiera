package com.app.financiera.service;

import com.app.financiera.entity.CompaniaSeguro;
import com.app.financiera.entity.TipoSeguro;
import com.app.financiera.repository.CompaniaSeguroRepository;
import com.app.financiera.repository.TipoSeguroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogosServiceImpl implements CatalogosService {

    @Autowired
    private TipoSeguroRepository tipoSeguroRepository;

    @Autowired
    private CompaniaSeguroRepository companiaSeguroRepository;

    @Override
    public List<TipoSeguro> obtenerTiposSegurosActivos() {
        return tipoSeguroRepository.findActivos();
    }

    @Override
    public List<TipoSeguro> obtenerTiposPorCategoria(String categoria) {
        return tipoSeguroRepository.findByCategoria(categoria);
    }

    @Override
    public List<String> obtenerCategorias() {
        return tipoSeguroRepository.findAllCategorias();
    }

    @Override
    public List<CompaniaSeguro> obtenerCompaniasActivas() {
        return companiaSeguroRepository.findActivas();
    }

    @Override
    public List<CompaniaSeguro> buscarCompaniasPorNombre(String nombre) {
        return companiaSeguroRepository.findByNombre(nombre);
    }
}
