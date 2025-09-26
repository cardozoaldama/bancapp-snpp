package com.intepy.bancapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intepy.bancapp.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
