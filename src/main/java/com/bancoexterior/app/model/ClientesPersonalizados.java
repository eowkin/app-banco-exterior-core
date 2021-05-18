package com.bancoexterior.app.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor @NoArgsConstructor
public class ClientesPersonalizados {

	
	private String codigoIbs;
	
	private String nroIdCliente;
	
	private String nombreRif;
	
	private String codUsuario;
	
	private Boolean flagActivo;
	
	private Date fechaModificacion;
	
}
