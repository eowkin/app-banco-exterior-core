package com.bancoexterior.app.convenio.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class LimitesGenerales {
	
	
	private String codMoneda;
	
	private String tipoTransaccion;
	
	private String tipoCliente;
	
	private BigDecimal montoMin;
	
	private BigDecimal montoMax;
	
	private BigDecimal montoTope;
	
	private BigDecimal montoMensual;
	
	private BigDecimal montoDiario;
	
	private BigDecimal montoBanco;
	
	private String codUsuario;
	
	private Boolean flagActivo;
	
	private Date fechaModificacion;

}
