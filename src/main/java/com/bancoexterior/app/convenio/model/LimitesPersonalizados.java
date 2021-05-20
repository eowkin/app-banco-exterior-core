package com.bancoexterior.app.convenio.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class LimitesPersonalizados {

	private String codigoIbs;
	
	private String codMoneda;
	
	private String tipoTransaccion;
	
	private BigDecimal montoMin;
	
	private BigDecimal montoMax;
	
	private BigDecimal montoTope;
	
	private BigDecimal montoMensual;
	
	private BigDecimal montoDiario;
	
	private Boolean flagActivo;
	
	private String codUsuario;
	
	private Date fechaModificacion;
}
