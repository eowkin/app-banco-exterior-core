package com.bancoexterior.app.convenio.apiRest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.convenio.dto.LimitesPersonalizadosRequest;
import com.bancoexterior.app.convenio.services.restApi.IWSService;
import com.bancoexterior.app.convenio.services.restApi.model.WSRequest;
import com.bancoexterior.app.convenio.services.restApi.model.WSResponse;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class LimitesPersonalizadosServiceApiRestImpl implements ILimitesPersonalizadosServiceApiRest{

	@Autowired
	private IWSService wsService;
	
	@Override
	public WSResponse consultarWs(LimitesPersonalizadosRequest limitesPersonalizadosRequest) {
		WSRequest wsrequest = new WSRequest();
		WSResponse retorno;
		String limitesPersonalizadosRequestJSON;
		limitesPersonalizadosRequestJSON = new Gson().toJson(limitesPersonalizadosRequest);
		log.info("limitesPersonalizadosRequestJSON: "+limitesPersonalizadosRequestJSON);
		
		wsrequest.setBody(limitesPersonalizadosRequestJSON);
		wsrequest.setConnectTimeout(10000);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(10000);
			
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//wsrequest.setUrl("http://172.19.148.48:7108/api/des/V1/parametros/monedas/consultas");
		                  //https://172.19.148.51:8443/api/des/V1/parametros/limites/consultas 
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/limitesdivisasclientes/consultas");
			
		//retorno: WSResponse [statusText=, status=200, body={"resultado":{"codigo":"0000","descripcion":"Operacion Exitosa."},"monedas":[{"codMoneda":"EUR","descripcion":"EURO Europa","codAlterno":"222","flagActivo":true,"codUsuario":"E33333","fechaModificacion":"2021-05-07 21:24:07"}]}, exitoso=true, httpRetorno=kong.unirest.StringResponse@7451891e, httpError=null, error=null, idConstructor=1]
		log.info("antes de llamarte WS en consultar");
		retorno = wsService.post(wsrequest);
		return retorno;
	}

	@Override
	public WSResponse actulaizarWs(LimitesPersonalizadosRequest limitesPersonalizadosRequest) {
		WSRequest wsrequest = new WSRequest();
		WSResponse retorno;
		String limitesPersonalizadosRequestJSON;
		limitesPersonalizadosRequestJSON = new Gson().toJson(limitesPersonalizadosRequest);
		log.info("limitesPersonalizadosRequestJSON: "+limitesPersonalizadosRequestJSON);
		
		wsrequest.setBody(limitesPersonalizadosRequestJSON);
		wsrequest.setConnectTimeout(10000);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(10000);
			
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//wsrequest.setUrl("http://172.19.148.48:7108/api/des/V1/parametros/monedas/consultas");
		                  //https://172.19.148.51:8443/api/des/V1/parametros/limites/consultas 
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/limitesdivisasclientes");
			
		//retorno: WSResponse [statusText=, status=200, body={"resultado":{"codigo":"0000","descripcion":"Operacion Exitosa."},"monedas":[{"codMoneda":"EUR","descripcion":"EURO Europa","codAlterno":"222","flagActivo":true,"codUsuario":"E33333","fechaModificacion":"2021-05-07 21:24:07"}]}, exitoso=true, httpRetorno=kong.unirest.StringResponse@7451891e, httpError=null, error=null, idConstructor=1]
		log.info("antes de llamarte WS en actualizar");
		retorno = wsService.put(wsrequest);
		return retorno;
	}

	@Override
	public WSResponse crearWs(LimitesPersonalizadosRequest limitesPersonalizadosRequest) {
		WSRequest wsrequest = new WSRequest();
		WSResponse retorno;
		String limitesPersonalizadosRequestJSON;
		limitesPersonalizadosRequestJSON = new Gson().toJson(limitesPersonalizadosRequest);
		log.info("limitesPersonalizadosRequestJSON: "+limitesPersonalizadosRequestJSON);
		
		wsrequest.setBody(limitesPersonalizadosRequestJSON);
		wsrequest.setConnectTimeout(10000);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(10000);
			
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//wsrequest.setUrl("http://172.19.148.48:7108/api/des/V1/parametros/monedas/consultas");
		                  //https://172.19.148.51:8443/api/des/V1/parametros/limites/consultas 
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/limitesdivisasclientes");
			
		//retorno: WSResponse [statusText=, status=200, body={"resultado":{"codigo":"0000","descripcion":"Operacion Exitosa."},"monedas":[{"codMoneda":"EUR","descripcion":"EURO Europa","codAlterno":"222","flagActivo":true,"codUsuario":"E33333","fechaModificacion":"2021-05-07 21:24:07"}]}, exitoso=true, httpRetorno=kong.unirest.StringResponse@7451891e, httpError=null, error=null, idConstructor=1]
		log.info("antes de llamarte WS en crear");
		retorno = wsService.post(wsrequest);
		return retorno;
	}

}
