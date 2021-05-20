package com.bancoexterior.app.convenio.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class Agencia {
	
	private String codAgencia;
	
	private String nombreAgencia;
	
	private String codUsuario;
		
	private Boolean flagActivo;
	
	private Date fechaModificacion;

}
