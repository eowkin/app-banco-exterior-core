package com.bancoexterior.app.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class Tasa {
	
	private String codMonedaOrigen;
	
	private String codMonedaDestino;
	
	private Integer tipoOperacion;
	
	private BigDecimal montoTasaCompra;
	
	private BigDecimal montoTasaVenta;
	
	private String codUsuario;
	
	private Date fechaModificacion;

}
