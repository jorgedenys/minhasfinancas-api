package com.jdsjara.minhasfinancas.model.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jdsjara.minhasfinancas.model.entity.Lancamento;
import com.jdsjara.minhasfinancas.model.enums.StatusLancamento;
import com.jdsjara.minhasfinancas.model.enums.TipoLancamento;
 
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {
	
	@Query( value =
			" SELECT "
		  + " sum(l.valor) "
		  + " FROM Lancamento l"
		  + " join l.usuario u"
		  + " WHERE u.id = :idUsuario"
		  + "   and l.tipo = :tipo "
		  + "   and l.status = :status "
		  + " GROUP BY u "
		  )
	BigDecimal obterSaldoPorTipoLancamentoEUsuarioEStatus(
			@Param("idUsuario") Long idUsuario,
			@Param("tipo") TipoLancamento tipo,
			@Param("status") StatusLancamento status);

}