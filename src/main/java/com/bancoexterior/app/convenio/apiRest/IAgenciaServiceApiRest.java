package com.bancoexterior.app.convenio.apiRest;



import com.bancoexterior.app.convenio.dto.AgenciaRequest;
import com.bancoexterior.app.convenio.services.restApi.model.WSResponse;

public interface IAgenciaServiceApiRest {

	public WSResponse consultarWs(AgenciaRequest agenciaRequest);
	
	public WSResponse actualizarWs(AgenciaRequest agenciaRequest);
	
	public WSResponse crearWs(AgenciaRequest agenciaRequest);
	
	
}
