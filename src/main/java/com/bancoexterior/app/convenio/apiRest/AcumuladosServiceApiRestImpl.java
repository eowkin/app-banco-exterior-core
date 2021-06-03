package com.bancoexterior.app.convenio.apiRest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.convenio.dto.AcumuladoRequest;
import com.bancoexterior.app.convenio.dto.AcumuladoResponse;
import com.bancoexterior.app.convenio.dto.MovimientosResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.response.Response;
import com.bancoexterior.app.convenio.services.restApi.IWSService;
import com.bancoexterior.app.convenio.services.restApi.model.WSRequest;
import com.bancoexterior.app.convenio.services.restApi.model.WSResponse;
import com.bancoexterior.app.util.Mapper;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class AcumuladosServiceApiRestImpl implements IAcumuladosServiceApiRest{

	@Autowired
	private IWSService wsService;
    
    @Autowired 
	private Mapper mapper;
	
	@Override
	public String consultarAcumulados(AcumuladoRequest acumuladoRequest) throws CustomException {
		WSRequest wsrequest = new WSRequest();
		WSResponse retorno;
		String acumuladoRequestJSON;
		AcumuladoResponse acumuladoResponse = new AcumuladoResponse();
		acumuladoRequestJSON = new Gson().toJson(acumuladoRequest);
		log.info("acumuladoRequestJSON: "+acumuladoRequestJSON);
		
		wsrequest.setBody(acumuladoRequestJSON);
		wsrequest.setConnectTimeout(15000);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(15000);
			
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//wsrequest.setUrl("http://172.19.148.48:7108/api/des/V1/parametros/monedas/consultas");
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/montosacumulados");
			
		//retorno: WSResponse [statusText=, status=200, body={"resultado":{"codigo":"0000","descripcion":"Operacion Exitosa."},"monedas":[{"codMoneda":"EUR","descripcion":"EURO Europa","codAlterno":"222","flagActivo":true,"codUsuario":"E33333","fechaModificacion":"2021-05-07 21:24:07"}]}, exitoso=true, httpRetorno=kong.unirest.StringResponse@7451891e, httpError=null, error=null, idConstructor=1]
		log.info("antes de llamarte WS en consultar");
		retorno = wsService.post(wsrequest);
		log.info("retorno: "+retorno);
		return null;
	}

	@Override
	public AcumuladoResponse consultarAcumuladosDiariosBanco(AcumuladoRequest acumuladoRequest) throws CustomException {
		WSRequest wsrequest = new WSRequest();
		WSResponse retorno;
		String acumuladoRequestJSON;
		AcumuladoResponse acumuladoResponse = new AcumuladoResponse();
		acumuladoRequestJSON = new Gson().toJson(acumuladoRequest);
		log.info("acumuladoRequestJSON: "+acumuladoRequestJSON);
		
		wsrequest.setBody(acumuladoRequestJSON);
		wsrequest.setConnectTimeout(15000);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(15000);
			
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//wsrequest.setUrl("http://172.19.148.48:7108/api/des/V1/parametros/monedas/consultas");
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/montosacumulados");
			
		//retorno: WSResponse [statusText=, status=200, body={"resultado":{"codigo":"0000","descripcion":"Operacion Exitosa."},"monedas":[{"codMoneda":"EUR","descripcion":"EURO Europa","codAlterno":"222","flagActivo":true,"codUsuario":"E33333","fechaModificacion":"2021-05-07 21:24:07"}]}, exitoso=true, httpRetorno=kong.unirest.StringResponse@7451891e, httpError=null, error=null, idConstructor=1]
		log.info("antes de llamarte WS en consultar");
		retorno = wsService.post(wsrequest);
		log.info("retorno: "+retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar el movimientosResponse");
	            try {
	            	acumuladoResponse = mapper.jsonToClass(retorno.getBody(), AcumuladoResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("acumuladoResponse: "+acumuladoResponse);
	            log.info(acumuladoResponse.getResultado().getCodigo());
	            return acumuladoResponse;
	            /*
	            if(movimientosResponse.getResultado().getCodigo().equals("0000")){
	            	log.info("Respusta codigo 0000 si existe la modena");
	            	return movimientosResponse;
	            }else {
	            	return null;
	            }*/
				
				
			}else {
				if(retorno.getStatus() == 422) {
					log.info("entro en error 422");
					try {
						Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
						log.info("response: "+response);
						String mensaje = " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado() .getDescripcion();
						throw new CustomException(mensaje);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else {
			throw new CustomException("No hubo conexion con el micreoservicio");
		}
		return null;
	}
	
	

}
