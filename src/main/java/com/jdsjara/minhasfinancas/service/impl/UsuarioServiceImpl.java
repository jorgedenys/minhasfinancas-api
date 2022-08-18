package com.jdsjara.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jdsjara.minhasfinancas.exception.ErroAutenticacao;
import com.jdsjara.minhasfinancas.exception.RegraNegocioException;

import com.jdsjara.minhasfinancas.model.entity.Usuario;
import com.jdsjara.minhasfinancas.model.repository.UsuarioRepository;

import com.jdsjara.minhasfinancas.service.UsuarioService;

// Essa anotação é para que o Container do Spring gerencie uma instância dessa classe. 
// Ou seja, ele irá criar uma instância e irá adicionar um Container para quando injetar em outras classes.
@Service
public class UsuarioServiceImpl implements UsuarioService {

	private UsuarioRepository repository;
	
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}
	
	@Override
	public Usuario autenticar(String email, String senha) {
	
		Optional<Usuario> usuario = Optional.ofNullable(repository.findByEmail(email));
		
		if (!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado para o email informado.");
		}
		
		if (!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha Inválida.");
		}
				
		return usuario.get();
		
	}
	
	@Override
	@Transactional // Esta annotation vai criar/abrir uma transação na base de dados, executa o método salvar e commit 
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);		
	}
	
	@Override
	public void validarEmail(String email) {
		
		boolean existe = repository.existsByEmail(email);
		if (existe) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");
		}
	}
	
}