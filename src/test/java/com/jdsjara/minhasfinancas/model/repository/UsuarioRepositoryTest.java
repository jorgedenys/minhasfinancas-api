package com.jdsjara.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.jdsjara.minhasfinancas.model.entity.Usuario;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase (replace = Replace.NONE)
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository repository;
	
	@Autowired
	TestEntityManager entityManager;
	
	public static Usuario criarUsuario() {
		
		return Usuario.builder().nome("usuario").email("usuario@email.com").senha("senha").build();
		
	}
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		
		// Cenário
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		// Ação / Execução 
		boolean result = repository.existsByEmail("usuario@email.com");
			
		// Verificação
		Assertions.assertThat(result).isTrue();
		
	}
	
	@Test(expected = Test.None.class)
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		
		// Cenário
						
		// Ação / Execução 
		boolean result = repository.existsByEmail("usuario@email.com");
					
		// Verificação
		Assertions.assertThat(result).isFalse();
		
	}
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		
		// Cenário
		Usuario usuario = criarUsuario();
		
		// Ação / Execução
		Usuario usuarioSalvo = repository.save(usuario);
		
		// Verificação
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
				
	}
	
	public void deveBuscarUmUsuarioPorEmail() {
		
		// Cenário
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		// Verificação
		Optional<Usuario> result = Optional.ofNullable(repository.findByEmail("usuario@email.com"));
		
		Assertions.assertThat(result.isPresent()).isTrue();
				
	}
	
	public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase() {
		
		// Cenário
				
		// Verificação
		Optional<Usuario> result = Optional.ofNullable(repository.findByEmail("usuario@email.com"));
		
		Assertions.assertThat(result.isPresent()).isFalse();
				
	}
	
}