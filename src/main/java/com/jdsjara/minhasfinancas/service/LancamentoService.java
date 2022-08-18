package com.jdsjara.minhasfinancas.service;

import com.jdsjara.minhasfinancas.model.entity.Lancamento;
import com.jdsjara.minhasfinancas.model.enums.StatusLancamento;

public interface LancamentoService {

	Lancamento salvar(Lancamento lancamento);
	
	Lancamento atualizar(Lancamento lancamento);
	
	void deletar(Lancamento lancamento);
	
	java.util.List<Lancamento> buscar(Lancamento lancamentoFiltro);
		
	void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	void validar(Lancamento lancamento);
	
}