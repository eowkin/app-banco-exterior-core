package com.bancoexterior.app.convenio.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class Movimiento implements Serializable{

	@JsonProperty("codOperacion")
	private String codOperacion;
	
	@JsonProperty("fechaOperacion")
	private String fechaOperacion;

	@JsonProperty("codigoIbs")
	private String codigoIbs;
	
	@JsonProperty("nroIdCliente")
	private String nroIdCliente;
	
	@JsonProperty("cuentaNacional")
	private String cuentaNacional;
	
	@JsonProperty("cuentaDivisa")
	private String cuentaDivisa;
	
	@JsonProperty("tipoTransaccion")
	private String tipoTransaccion;
	
	@JsonProperty("tipoTransaccionCliente")
	private String tipoTransaccionCliente;
	
	@JsonProperty("codMoneda")
	private String codMoneda;
	
	@JsonProperty("montoDivisa")
	private BigDecimal montoDivisa;
	
	@JsonProperty("montoBsCliente")
	private BigDecimal montoBsCliente;
	
	@JsonProperty("tasaCliente")
	private BigDecimal tasaCliente;
	
	@JsonProperty("tasaOperacion")
	private BigDecimal tasaOperacion;
	
	@JsonProperty("montoBsOperacion")
	private BigDecimal montoBsOperacion;
	
	@JsonProperty("referenciaDebito")
	private String referenciaDebito;
	
	@JsonProperty("referenciaCredito")
	private String referenciaCredito;
	 
	@JsonProperty("descripcion")
	private String descripcion;
	
	@JsonProperty("estatus")
	private Integer estatus;
	
	private BigDecimal nuevaTasaCliente;
	
	private String fecha;
	
	private Integer paginaActual;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
