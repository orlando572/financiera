package com.app.financiera.service;

import java.util.List;
import java.util.Map;

public interface CotizacionService {
    Map<String, Object> solicitarCotizacion(Integer idUsuario, List<Integer> idsPlanes, String comentarios);
}
