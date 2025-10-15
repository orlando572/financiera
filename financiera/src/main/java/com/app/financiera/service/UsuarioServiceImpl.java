package com.app.financiera.service;

import java.util.List;

import com.app.financiera.entity.RolUsuario;
import com.app.financiera.repository.RolUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.app.financiera.entity.Usuario;
import com.app.financiera.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private RolUsuarioRepository rolUsuarioRepository;

    @Override
    public Usuario buscarPorDni(String dni) {
        return repository.findByDni(dni);
    }

    @Override
    public Usuario registraUsuario(Usuario obj) {
        RolUsuario rolUsuario = rolUsuarioRepository.findById(2)
                .orElseThrow(() -> new RuntimeException("Rol 'Usuario' no encontrado"));
        obj.setRol(rolUsuario);

        return repository.save(obj);
    }

    @Override
    public Usuario actualizaUsuario(Usuario obj) {
        return repository.save(obj);
    }

    @Override
    public void eliminaUsuario(int id) {
        repository.deleteById(id);
    }

    @Override
    public List<Usuario> listaTodos() {
        return repository.findAll();
    }

    @Override
    public List<Usuario> buscarUsuarioPorId(int id) {
        return repository.findByIdUsuario(id);
    }
}