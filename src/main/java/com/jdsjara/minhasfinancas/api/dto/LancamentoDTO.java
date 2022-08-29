package com.jdsjara.minhasfinancas.api.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
public class LancamentoDTO {
	
	private Long id;
	private String descricao;
	private Integer mes;
	private Integer ano;
	private BigDecimal valor;
	private Long idUsuario;
	private String tipo;
	private String status;
	
}