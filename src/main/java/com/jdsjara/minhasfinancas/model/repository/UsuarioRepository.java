package com.jdsjara.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jdsjara.minhasfinancas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	boolean existsByEmail(String email);
	
	Usuario findByEmail(String email);
	
}