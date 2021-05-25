package com.bancoexterior.app.convenio.apiRest;

import com.bancoexterior.app.convenio.dto.LimitesPersonalizadosRequest;
import com.bancoexterior.app.convenio.services.restApi.model.WSResponse;

public interface ILimitesPersonalizadosServiceApiRest {
	public WSResponse consultarWs(LimitesPersonalizadosRequest limitesPersonalizadosRequest);
	
	public WSResponse actulaizarWs(LimitesPersonalizadosRequest limitesPersonalizadosRequest);
	
	public WSResponse crearWs(LimitesPersonalizadosRequest limitesPersonalizadosRequest);
}
