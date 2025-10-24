package com.app.financiera.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.app.financiera.entity.AportePension;
import com.app.financiera.repository.AportePensionRepository;

@Service
public class AportePensionServiceImpl implements AportePensionService {

    @Autowired
    private AportePensionRepository aportePensionRepository;

    @Override
    public List<AportePension> obtenerAportesUsuario(int idUsuario) {
        return aportePensionRepository.findByUsuarioId(idUsuario);
    }

    @Override
    public List<AportePension> obtenerAportesUsuarioYear(int idUsuario, int year) {
        return aportePensionRepository.findByUsuarioAndYear(idUsuario, year);
    }

    @Override
    public List<AportePension> obtenerAportesUltimoYear(int idUsuario) {
        return aportePensionRepository.findAportesUltimoYear(idUsuario);
    }

    @Override
    public List<AportePension> obtenerAportesPorSistema(int idUsuario, String sistema) {
        return aportePensionRepository.findByUsuarioAndSistema(idUsuario, sistema);
    }

    @Override
    public Double obtenerTotalAportesUsuario(int idUsuario) {
        return aportePensionRepository.sumAportesUsuario(idUsuario);
    }

    @Override
    public Double obtenerTotalAportesUsuarioYear(int idUsuario, int year) {
        return aportePensionRepository.sumAportesUsuarioYear(idUsuario, year);
    }

    @Override
    public AportePension guardarAporte(AportePension aporte) {
        return aportePensionRepository.save(aporte);
    }

    @Override
    public AportePension actualizarAporte(AportePension aporte) {
        return aportePensionRepository.save(aporte);
    }

    @Override
    public void eliminarAporte(int idAporte) {
        aportePensionRepository.deleteById(idAporte);
    }

    @Override
    public List<AportePension> obtenerTodos() {
        return aportePensionRepository.findAll();
    }
}