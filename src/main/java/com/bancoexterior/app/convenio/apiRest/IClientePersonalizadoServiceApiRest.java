package com.bancoexterior.app.convenio.apiRest;

import com.bancoexterior.app.convenio.dto.ClienteRequest;
import com.bancoexterior.app.convenio.services.restApi.model.WSResponse;

public interface IClientePersonalizadoServiceApiRest {

	public WSResponse consultarWs(ClienteRequest clienteRequest);
	
	public WSResponse actualizarWs(ClienteRequest clienteRequest);
	
	public WSResponse crearWs(ClienteRequest clienteRequest);
}
