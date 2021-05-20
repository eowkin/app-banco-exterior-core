package com.bancoexterior.app.convenio.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class Moneda {

	
	private String codMoneda;
	
	
	private String descripcion;
	
	
	private String codAlterno;
	
	
	private Boolean flagActivo;
	
	
	private String codUsuario;
	
	
	private Date fechaModificacion;
	
	
}
