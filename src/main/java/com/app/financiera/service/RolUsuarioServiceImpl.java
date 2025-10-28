package com.app.financiera.service;

import com.app.financiera.entity.RolUsuario;
import com.app.financiera.repository.RolUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolUsuarioServiceImpl implements RolUsuarioService {

    @Autowired
    private RolUsuarioRepository rolUsuarioRepository;

    @Override
    public List<RolUsuario> listarRoles() {
        return rolUsuarioRepository.findAll();
    }
}