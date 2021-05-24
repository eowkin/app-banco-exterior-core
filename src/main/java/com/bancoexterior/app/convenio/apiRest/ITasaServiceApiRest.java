package com.bancoexterior.app.convenio.apiRest;


import com.bancoexterior.app.convenio.dto.TasaRequest;
import com.bancoexterior.app.convenio.services.restApi.model.WSResponse;

public interface ITasaServiceApiRest {

	public WSResponse consultarWs(TasaRequest tasaRequest);
	
	public WSResponse actualizarWs(TasaRequest tasaRequest);
	
	public WSResponse crearWs(TasaRequest tasaRequest);
}
