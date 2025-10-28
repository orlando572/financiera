package com.app.financiera.service;

import com.app.financiera.entity.CompaniaSeguro;
import com.app.financiera.repository.CompaniaSeguroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminCompaniasSeguroServiceImpl implements AdminCompaniasSeguroService {

    private static final Logger logger = LoggerFactory.getLogger(AdminCompaniasSeguroServiceImpl.class);

    @Autowired
    private CompaniaSeguroRepository repository;

    @Override
    public List<CompaniaSeguro> listarCompanias(String estado) {
        logger.info("Listando compañías - Estado: {}", estado);
        
        List<CompaniaSeguro> companias = repository.findAll();
        
        // Filtrar por estado si se proporciona
        if (estado != null && !estado.isEmpty()) {
            companias = companias.stream()
                    .filter(c -> estado.equals(c.getEstado()))
                    .collect(Collectors.toList());
        }
        
        return companias;
    }

    @Override
    public CompaniaSeguro obtenerCompaniaPorId(int id) {
        logger.info("Obteniendo compañía con ID: {}", id);
        Optional<CompaniaSeguro> compania = repository.findById(id);
        return compania.orElse(null);
    }

    @Override
    @Transactional
    public CompaniaSeguro crearCompania(CompaniaSeguro compania) {
        logger.info("Creando nueva compañía: {}", compania.getNombre());
        
        // Validaciones
        if (compania.getNombre() == null || compania.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre de la compañía es obligatorio");
        }
        
        // Establecer estado por defecto si no se proporciona
        if (compania.getEstado() == null || compania.getEstado().trim().isEmpty()) {
            compania.setEstado("Activo");
        }
        
        return repository.save(compania);
    }

    @Override
    @Transactional
    public CompaniaSeguro actualizarCompania(CompaniaSeguro compania) {
        logger.info("Actualizando compañía con ID: {}", compania.getIdCompania());
        
        // Verificar que la compañía existe
        Optional<CompaniaSeguro> companiaExistente = repository.findById(compania.getIdCompania());
        if (!companiaExistente.isPresent()) {
            throw new RuntimeException("Compañía de seguro no encontrada");
        }
        
        // Validaciones
        if (compania.getNombre() == null || compania.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre de la compañía es obligatorio");
        }
        
        return repository.save(compania);
    }

    @Override
    @Transactional
    public void eliminarCompania(int id) {
        logger.info("Eliminando compañía con ID: {}", id);
        
        Optional<CompaniaSeguro> compania = repository.findById(id);
        if (!compania.isPresent()) {
            throw new RuntimeException("Compañía de seguro no encontrada");
        }
        
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void cambiarEstado(int id, String estado) {
        logger.info("Cambiando estado de la compañía {} a: {}", id, estado);
        
        Optional<CompaniaSeguro> companiaOpt = repository.findById(id);
        if (!companiaOpt.isPresent()) {
            throw new RuntimeException("Compañía de seguro no encontrada");
        }
        
        CompaniaSeguro compania = companiaOpt.get();
        compania.setEstado(estado);
        repository.save(compania);
    }

    @Override
    public HashMap<String, Object> obtenerEstadisticas() {
        logger.info("Obteniendo estadísticas de compañías de seguro");
        
        HashMap<String, Object> estadisticas = new HashMap<>();
        
        List<CompaniaSeguro> todasCompanias = repository.findAll();
        
        // Total de compañías
        estadisticas.put("total", todasCompanias.size());
        
        // Compañías activas
        long activas = todasCompanias.stream()
                .filter(c -> "Activo".equals(c.getEstado()))
                .count();
        estadisticas.put("activas", activas);
        
        // Compañías inactivas
        long inactivas = todasCompanias.stream()
                .filter(c -> "Inactivo".equals(c.getEstado()))
                .count();
        estadisticas.put("inactivas", inactivas);
        
        return estadisticas;
    }
}
