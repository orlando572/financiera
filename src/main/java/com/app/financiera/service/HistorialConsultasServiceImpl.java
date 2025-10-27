package com.app.financiera.service;

import com.app.financiera.entity.HistorialConsultas;
import com.app.financiera.entity.Usuario;
import com.app.financiera.repository.HistorialConsultasRepository;
import com.app.financiera.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HistorialConsultasServiceImpl implements HistorialConsultasService {

    private static final Logger logger = LoggerFactory.getLogger(HistorialConsultasServiceImpl.class);

    @Autowired
    private HistorialConsultasRepository historialConsultasRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public HistorialConsultas registrarConsulta(int idUsuario, String tipoConsulta, String detalleConsulta,
                                                String resultado, String detallesAdicionales) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
            if (usuarioOpt.isEmpty()) {
                logger.error("Usuario no encontrado: {}", idUsuario);
                return null;
            }

            HistorialConsultas historial = new HistorialConsultas();
            historial.setUsuario(usuarioOpt.get());
            historial.setFecha(LocalDateTime.now());
            historial.setTipoConsulta(tipoConsulta);
            historial.setDetalleConsulta(detalleConsulta);
            historial.setResultado(resultado);
            historial.setDetallesAdicionales(detallesAdicionales);

            HistorialConsultas guardado = historialConsultasRepository.save(historial);
            logger.info("Consulta registrada en historial: {} - {} - {}", idUsuario, tipoConsulta, resultado);
            
            return guardado;
        } catch (Exception e) {
            logger.error("Error al registrar consulta en historial: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public List<HistorialConsultas> obtenerHistorialUsuario(int idUsuario) {
        return historialConsultasRepository.findByUsuarioId(idUsuario);
    }

    @Override
    public List<HistorialConsultas> obtenerHistorialPorTipo(int idUsuario, String tipoConsulta) {
        return historialConsultasRepository.findByUsuarioAndTipo(idUsuario, tipoConsulta);
    }

    @Override
    public List<HistorialConsultas> obtenerConsultasExitosas(int idUsuario) {
        return historialConsultasRepository.findExitosos(idUsuario);
    }

    @Override
    public List<HistorialConsultas> obtenerConsultasConErrores(int idUsuario) {
        return historialConsultasRepository.findConErrores(idUsuario);
    }

    @Override
    public List<HistorialConsultas> obtenerUltimasConsultas(int idUsuario, int limite) {
        return historialConsultasRepository.findUltimas(idUsuario, limite);
    }

    @Override
    public List<HistorialConsultas> obtenerPorRangoFechas(int idUsuario, LocalDateTime inicio, LocalDateTime fin) {
        return historialConsultasRepository.findByFechaRango(idUsuario, inicio, fin);
    }

    @Override
    public void limpiarHistorialAntiguo(int dias) {
        try {
            LocalDateTime fechaLimite = LocalDateTime.now().minusDays(dias);
            List<HistorialConsultas> historialAntiguo = historialConsultasRepository.findAll()
                    .stream()
                    .filter(h -> h.getFecha().isBefore(fechaLimite))
                    .toList();
            
            if (!historialAntiguo.isEmpty()) {
                historialConsultasRepository.deleteAll(historialAntiguo);
                logger.info("Historial antiguo limpiado: {} registros eliminados", historialAntiguo.size());
            }
        } catch (Exception e) {
            logger.error("Error al limpiar historial antiguo: {}", e.getMessage());
        }
    }
}
