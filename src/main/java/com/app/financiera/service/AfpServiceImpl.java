package com.app.financiera.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.financiera.entity.Afp;
import com.app.financiera.repository.AfpRepository;

/**
 * Implementación del servicio de gestión de AFPs
 * Contiene la lógica de negocio para operaciones CRUD y estadísticas
 *
 * @author Sistema Financiero
 * @version 1.0
 */
@Service
public class AfpServiceImpl implements AfpService {

    @Autowired
    private AfpRepository afpRepository;

    // ========================================
    // MÉTODOS DE CONSULTA
    // ========================================

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

    // ========================================
    // MÉTODOS DE CREACIÓN Y ACTUALIZACIÓN
    // ========================================

    @Override
    public Afp guardarAfp(Afp afp) {
        return afpRepository.save(afp);
    }

    @Override
    public Afp actualizarAfp(Afp afp) {
        return afpRepository.save(afp);
    }

    // ========================================
    // MÉTODOS DE ELIMINACIÓN
    // ========================================

    @Override
    public void eliminarAfp(int id) {
        afpRepository.deleteById(id);
    }

    // ========================================
    // MÉTODOS DE ESTADÍSTICAS
    // ========================================

    /**
     * Calcula estadísticas generales de AFPs
     * Cuenta total, activas e inactivas
     *
     * @return Mapa con las estadísticas calculadas
     */
    @Override
    public java.util.HashMap<String, Object> obtenerEstadisticas() {
        java.util.HashMap<String, Object> stats = new java.util.HashMap<>();

        List<Afp> todas = listarAfps();
        long activas = todas.stream()
                .filter(a -> "Activo".equals(a.getEstado()))
                .count();
        long inactivas = todas.stream()
                .filter(a -> "Inactivo".equals(a.getEstado()))
                .count();

        stats.put("total", todas.size());
        stats.put("activas", activas);
        stats.put("inactivas", inactivas);

        return stats;
    }
}