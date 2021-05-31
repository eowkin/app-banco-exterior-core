package com.bancoexterior.app.convenio.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class Agencia implements Serializable{
	
	

	@JsonProperty("codAgencia")
	private String codAgencia;
	
	@JsonProperty("nombreAgencia")
	private String nombreAgencia;
	
	@JsonProperty("codUsuario")
	private String codUsuario;
		
	@JsonProperty("flagActivo")
	private Boolean flagActivo;
	
	@JsonProperty("flagDivisa")
	private Boolean flagDivisa;
	
	@JsonProperty("fechaModificacion")
	private String fechaModificacion;
	
	//@JsonProperty("fechaModificacion")
	//@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	//private Date fechaModificacion;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
}
