package com.bancoexterior.app.convenio.apiRest;

import com.bancoexterior.app.convenio.dto.LimiteRequest;
import com.bancoexterior.app.convenio.services.restApi.model.WSResponse;

public interface ILimitesGeneralesServiceApirest {
	public WSResponse consultarWs(LimiteRequest limiteRequest);
	
	public WSResponse actualizarWs(LimiteRequest limiteRequest);
	
	public WSResponse crearWs(LimiteRequest limiteRequest);
}
