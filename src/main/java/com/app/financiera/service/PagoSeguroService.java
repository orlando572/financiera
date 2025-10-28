package com.app.financiera.service;

import com.app.financiera.entity.PagoSeguro;
import com.app.financiera.entity.Seguro;

import java.util.List;

public interface PagoSeguroService {

    List<PagoSeguro> generarPagosAutomaticos(Seguro seguro);

    int actualizarPagosVencidos();

    List<PagoSeguro> obtenerPagosProximosVencer(int idUsuario, int dias);

    List<PagoSeguro> obtenerPagosVencidos(int idUsuario);

    PagoSeguro registrarPagoRealizado(int idPago, Double montoPagado, String metodoPago);

    void eliminarPagosDeSeguro(int idSeguro);
}
