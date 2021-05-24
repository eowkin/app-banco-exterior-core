package com.bancoexterior.app.convenio.apiRest;


import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.services.restApi.model.WSResponse;

public interface IMonedaServiceApiRest {

	
	
	public WSResponse consultar(MonedasRequest monedasRequest);
	
	public WSResponse actualizarWs(MonedasRequest monedasRequest);
	
	public WSResponse crearWs(MonedasRequest monedasRequest);
	
	
}
