package com.bancoexterior.app.convenio.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class Tasa {
	
	@NotEmpty(message = "Codigo moneda no puede ser vacia")
	@JsonProperty("codMonedaOrigen")
	@Size(min = 1, max = 3 , message = "Codigo moneda origen debe tener maximo 3 caracteres")
	private String codMonedaOrigen;
	
	
	@NotEmpty(message = "Codigo moneda no puede ser vacia")
	@JsonProperty("codMonedaDestino")
	@Size(min = 1, max = 3 , message = "Codigo moneda destino debe tener maximo 3 caracteres")
	private String codMonedaDestino;
	
	@JsonProperty("tipoOperacion")
	private Integer tipoOperacion;
	
	@JsonProperty("montoTasaCompra")
	@Digits(integer=13, fraction=4, message = "El monte tasa compra debe ser numerico y tener maximo 13 digitos y 8 decimales")
	private BigDecimal montoTasaCompra;
	
	@JsonProperty("montoTasaVenta")
	@Digits(integer=13, fraction=4, message = "El monte tasa venta debe ser numerico y tener maximo 13 digitos y 8 decimales")
	private BigDecimal montoTasaVenta;
	
	@JsonProperty("codUsuario")
	private String codUsuario;
	
	@JsonProperty("fechaModificacion")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	//private Date fechaModificacion;
	private Timestamp fechaModificacion;
}
