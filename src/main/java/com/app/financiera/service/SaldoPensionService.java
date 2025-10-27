package com.app.financiera.service;

import com.app.financiera.entity.SaldoPension;

import java.util.List;

public interface SaldoPensionService {

    List<SaldoPension> obtenerSaldosUsuario(int idUsuario);

    SaldoPension obtenerSaldoPorAfp(int idUsuario, int idAfp);

    SaldoPension obtenerSaldoONP(int idUsuario);

    Double obtenerSaldoTotalUsuario(int idUsuario);

    Double obtenerSaldosDisponibles(int idUsuario);

    SaldoPension guardarSaldo(SaldoPension saldo);

    SaldoPension actualizarSaldo(SaldoPension saldo);

    void eliminarSaldo(int idSaldo);

    List<SaldoPension> obtenerTodos();
}