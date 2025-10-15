package com.app.financiera.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.app.financiera.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @Query("select u from Usuario u where u.idUsuario = ?1")
    List<Usuario> listaPorIdIgual(int id);

    Usuario findByCorreo(String correo);

    // Buscar por DNI (para login)
    Usuario findByDni(String dni);

    // Buscar por ID
    List<Usuario> findByIdUsuario(int idUsuario);
}