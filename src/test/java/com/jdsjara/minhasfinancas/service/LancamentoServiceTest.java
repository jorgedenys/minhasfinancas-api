package com.jdsjara.minhasfinancas.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.jdsjara.minhasfinancas.exception.RegraNegocioException;
import com.jdsjara.minhasfinancas.model.entity.Lancamento;
import com.jdsjara.minhasfinancas.model.entity.Usuario;
import com.jdsjara.minhasfinancas.model.enums.StatusLancamento;
import com.jdsjara.minhasfinancas.model.repository.LancamentoRepository;
import com.jdsjara.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.jdsjara.minhasfinancas.service.impl.LancamentoServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class LancamentoServiceTest {

	@SpyBean
	LancamentoServiceImpl service;
	
	@MockBean
	LancamentoRepository repository;

	@Test
	public void deveSalvarUmLancamento() {
		
		// Cenário
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doNothing().when(service).validar(lancamentoASalvar);
		
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);
		
		// Execução
		Lancamento lancamento = service.salvar(lancamentoASalvar);
		
		// Verificação
		Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
		
	}
	
	@Test
	public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
		
		// Cenário
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doThrow( RegraNegocioException.class ).when(service).validar(lancamentoASalvar);
		
		// Execução e Verificação
		Assertions.catchThrowableOfType( () -> service.salvar(lancamentoASalvar) , RegraNegocioException.class);
		Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
		
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		
		// Cenário
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		
		Mockito.doNothing().when(service).validar(lancamentoSalvo);
		
		Mockito.when( repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);
		
		// Execução
		service.atualizar(lancamentoSalvo);
		
		// Verificação
		Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);
				
	}
	
	@Test
	public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
		
		// Cenário
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
				
		// Execução e Verificação
		Assertions.catchThrowableOfType( () -> service.atualizar(lancamentoASalvar) , NullPointerException.class);
		Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
				
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		
		// Cenário
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
				
		// Execução
		service.deletar(lancamento);
		
		// Verificação
		Mockito.verify(repository).delete(lancamento);
				
	}
	
	@Test
	public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {
		
		// Cenário
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
				
		// Execução
		Assertions.catchThrowableOfType( () -> service.deletar(lancamento) , NullPointerException.class);
		
		// Verificação
		Mockito.verify(repository, Mockito.never()).delete(lancamento);
				
	}
	
	@Test
	public void deveFiltrarLancamentos() {
		
		// Cenário
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
				
		List<Lancamento> lista = Arrays.asList(lancamento);
		Mockito.when( repository.findAll(Mockito.any(Example.class)) ).thenReturn(lista);
		
		// Execução
		List<Lancamento> resultado = service.buscar(lancamento);
		
		// Verificação
		Assertions.assertThat(resultado).isNotEmpty().hasSize(1).contains(lancamento);
		
	}
	
	@Test
	public void deveAtualizarOStatusDeUmLancamento() {
		
		// Cenário
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		
		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		Mockito.doReturn(lancamento).when(service).atualizar(lancamento);
		
		// Execução
		service.atualizarStatus(lancamento, novoStatus);
		
		// Verificação
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
		Mockito.verify(service).atualizar(lancamento);		
		
	}
	
	@Test
	public void deveObterUmLancamentoPorId() {
		
		// Cenário
		Long id = 1l;
		
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));
		
		// Execução
		Optional<Lancamento> resultado = service.obterPorId(id); 
		
		// Verificação
		Assertions.assertThat(resultado.isPresent()).isTrue();
		
	}
	
	@Test
	public void deveRetornarVazioQuandoOLancamentoNaoExiste() {
		
		// Cenário
		Long id = 1l;
		
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(id);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
		
		// Execução
		Optional<Lancamento> resultado = service.obterPorId(id); 
		
		// Verificação
		Assertions.assertThat(resultado.isPresent()).isFalse();
		
	}
	
	@Test
	public void deveLancarErroAoValidarUmLancamento() {
		
		Lancamento lancamento = new Lancamento();
		
		Throwable erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma descrição válida.");
		
		lancamento.setDescricao("Pagamento de Salários");
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um mês válido.");
		
		lancamento.setMes(10);
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um ano válido.");
		
		lancamento.setAno(2020);
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um usuário.");
		
		lancamento.setUsuario(new Usuario());
		lancamento.getUsuario().setId(1l);
		
		erro = Assertions.catchThrowable( () -> service.validar(lancamento) );
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um valor válido.");
		
	}
	
}