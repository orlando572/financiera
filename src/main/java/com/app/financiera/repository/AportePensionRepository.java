package com.app.financiera.repository;

import com.app.financiera.entity.AportePension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AportePensionRepository extends JpaRepository<AportePension, Integer> {

    // Obtener aportes por usuario
    @Query("SELECT a FROM AportePension a WHERE a.usuario.idUsuario = ?1 ORDER BY a.fechaAporte DESC")
    List<AportePension> findByUsuarioId(int idUsuario);

    // Obtener aportes por usuario y periodo
    @Query("SELECT a FROM AportePension a WHERE a.usuario.idUsuario = ?1 AND YEAR(a.fechaAporte) = ?2 ORDER BY a.fechaAporte DESC")
    List<AportePension> findByUsuarioAndYear(int idUsuario, int year);

    // Obtener aportes por usuario y sistema (ONP o AFP)
    @Query("SELECT a FROM AportePension a WHERE a.usuario.idUsuario = ?1 AND a.institucion.tipo = ?2 ORDER BY a.fechaAporte DESC")
    List<AportePension> findByUsuarioAndSistema(int idUsuario, String sistema);

    // Obtener aportes del último año
    @Query("SELECT a FROM AportePension a WHERE a.usuario.idUsuario = ?1 AND YEAR(a.fechaAporte) = YEAR(CURRENT_DATE) ORDER BY a.fechaAporte DESC")
    List<AportePension> findAportesUltimoYear(int idUsuario);

    // Suma de aportes por usuario
    @Query("SELECT COALESCE(SUM(a.montoAporte), 0) FROM AportePension a WHERE a.usuario.idUsuario = ?1")
    Double sumAportesUsuario(int idUsuario);

    // Suma de aportes por usuario y año
    @Query("SELECT COALESCE(SUM(a.montoAporte), 0) FROM AportePension a WHERE a.usuario.idUsuario = ?1 AND YEAR(a.fechaAporte) = ?2")
    Double sumAportesUsuarioYear(int idUsuario, int year);

    // Buscar aportes por estado
    @Query("SELECT a FROM AportePension a WHERE a.estado = ?1 ORDER BY a.fechaAporte DESC")
    List<AportePension> findByEstado(String estado);

    // Buscar aportes por usuario y estado
    @Query("SELECT a FROM AportePension a WHERE a.usuario.idUsuario = ?1 AND a.estado = ?2 ORDER BY a.fechaAporte DESC")
    List<AportePension> findByUsuarioAndEstado(int idUsuario, String estado);

    // Contar aportes por estado
    @Query("SELECT COUNT(a) FROM AportePension a WHERE a.estado = ?1")
    Long countByEstado(String estado);
}