package com.app.financiera.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.app.financiera.entity.Afp;
import com.app.financiera.repository.AfpRepository;

@Service
public class AfpServiceImpl implements AfpService {

    @Autowired
    private AfpRepository afpRepository;

    @Override
    public List<Afp> listarAfps() {
        return afpRepository.findAll();
    }

    @Override
    public Afp buscarPorId(int id) {
        Optional<Afp> afp = afpRepository.findById(id);
        return afp.orElse(null);
    }

    @Override
    public Afp buscarPorNombreExacto(String nombre) {
        return afpRepository.findByNombre(nombre);
    }

    @Override
    public List<Afp> buscarPorNombre(String busqueda) {
        return afpRepository.findByNombreContaining(busqueda);
    }

    @Override
    public Afp buscarPorCodigoSbs(String codigoSbs) {
        return afpRepository.findByCodigoSbs(codigoSbs);
    }

    @Override
    public List<Afp> buscarPorEstado(String estado) {
        return afpRepository.findByEstado(estado);
    }

    @Override
    public Afp guardarAfp(Afp afp) {
        return afpRepository.save(afp);
    }

    @Override
    public Afp actualizarAfp(Afp afp) {
        return afpRepository.save(afp);
    }

    @Override
    public void eliminarAfp(int id) {
        afpRepository.deleteById(id);
    }
}