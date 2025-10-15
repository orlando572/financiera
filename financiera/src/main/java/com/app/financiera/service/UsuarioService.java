package com.app.financiera.service;

import java.util.List;
import com.app.financiera.entity.Usuario;

public interface UsuarioService {

    Usuario buscarPorDni(String dni);

    // CRUD
    Usuario registraUsuario(Usuario obj);
    Usuario actualizaUsuario(Usuario obj);
    void eliminaUsuario(int id);
    List<Usuario> listaTodos();
    List<Usuario> buscarUsuarioPorId(int id);
}