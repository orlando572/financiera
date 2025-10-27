package com.app.financiera.service;

import java.util.HashMap;

public interface DashboardService {

    HashMap<String, Object> obtenerResumenCompleto(int idUsuario);

    HashMap<String, Object> obtenerPerfilUsuario(int idUsuario);

    HashMap<String, Object> obtenerAlertas(int idUsuario);

    HashMap<String, Object> obtenerActividadReciente(int idUsuario, int limite);

    HashMap<String, Object> obtenerEstadisticasFinancieras(int idUsuario);
}