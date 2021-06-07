package com.bancoexterior.app.convenio.apiRest;


import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.convenio.dto.MonedaResponse;
import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.Moneda;
import com.bancoexterior.app.convenio.response.Response;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.convenio.services.restApi.IWSService;
import com.bancoexterior.app.convenio.services.restApi.model.WSRequest;
import com.bancoexterior.app.convenio.services.restApi.model.WSResponse;
import com.bancoexterior.app.util.Mapper;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class MonedaServiceApiRestImpl implements IMonedaServiceApiRest{

		
    
    @Autowired
	private IWSService wsService;
    
    @Autowired 
	private Mapper mapper;
	


	




	@Override
	public List<Moneda> listaMonedas(MonedasRequest monedasRequest) throws CustomException {
		WSRequest wsrequest = new WSRequest();
		WSResponse retorno;
		String monedasRequestJSON;
		MonedaResponse monedaResponse = new MonedaResponse();
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		log.info("monedasRequestJSON: "+monedasRequestJSON);
		
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setConnectTimeout(10000);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(10000);
			
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//wsrequest.setUrl("http://172.19.148.48:7108/api/des/V1/parametros/monedas/consultas");
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/monedas/consultas");
			
		//retorno: WSResponse [statusText=, status=200, body={"resultado":{"codigo":"0000","descripcion":"Operacion Exitosa."},"monedas":[{"codMoneda":"EUR","descripcion":"EURO Europa","codAlterno":"222","flagActivo":true,"codUsuario":"E33333","fechaModificacion":"2021-05-07 21:24:07"}]}, exitoso=true, httpRetorno=kong.unirest.StringResponse@7451891e, httpError=null, error=null, idConstructor=1]
		log.info("antes de llamarte WS en consultar");
		retorno = wsService.post(wsrequest);
		
		log.info("retorno: "+retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista moneda");
	            try {
					monedaResponse = mapper.jsonToClass(retorno.getBody(), MonedaResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("monedaResponse: "+monedaResponse);
	            log.info(monedaResponse.getResultado().getCodigo());
	            return monedaResponse.getMonedas();
				
				
			}else {
				if(retorno.getStatus() == 422) {
					log.info("entro en error 422");
					try {
						Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
						log.info("resultado: "+resultado);
						//String mensaje = " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion();
						String mensaje = resultado.getDescripcion();
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





	@Override
	public boolean existe(MonedasRequest monedasRequest) throws CustomException {
		WSRequest wsrequest = new WSRequest();
		WSResponse retorno;
		String monedasRequestJSON;
		MonedaResponse monedaResponse = new MonedaResponse();
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		log.info("monedasRequestJSON: "+monedasRequestJSON);
		
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setConnectTimeout(10000);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(10000);
			
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//wsrequest.setUrl("http://172.19.148.48:7108/api/des/V1/parametros/monedas/consultas");
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/monedas/consultas");
			
		//retorno: WSResponse [statusText=, status=200, body={"resultado":{"codigo":"0000","descripcion":"Operacion Exitosa."},"monedas":[{"codMoneda":"EUR","descripcion":"EURO Europa","codAlterno":"222","flagActivo":true,"codUsuario":"E33333","fechaModificacion":"2021-05-07 21:24:07"}]}, exitoso=true, httpRetorno=kong.unirest.StringResponse@7451891e, httpError=null, error=null, idConstructor=1]
		log.info("antes de llamarte WS en consultar");
		retorno = wsService.post(wsrequest);
		
		log.info("retorno: "+retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista moneda");
	            try {
					monedaResponse = mapper.jsonToClass(retorno.getBody(), MonedaResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("monedaResponse: "+monedaResponse);
	            log.info(monedaResponse.getResultado().getCodigo());
	            if(monedaResponse.getResultado().getCodigo().equals("0000")){
	            	log.info("Respusta codigo 0000 si existe la modena");
	            	return true;
	            }else {
	            	return false;
	            }
				
				
			}else {
				if(retorno.getStatus() == 422) {
					log.info("entro en error 422");
					try {
						Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
						log.info("resultado: "+resultado);
						//String mensaje = " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion();
						String mensaje = resultado.getDescripcion();
						throw new CustomException(mensaje);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else {
			throw new CustomException("No hubo conexion con el micreoservicio");
		}
		
		return false;
	}





	@Override
	public String actualizar(MonedasRequest monedasRequest) throws CustomException{
		WSRequest wsrequest = new WSRequest();
		WSResponse retorno;
		Response response = new Response();
		String respuesta;
		String error;
		String monedasRequestJSON;
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		log.info("monedasRequestJSON: "+monedasRequestJSON);
		
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setConnectTimeout(10000);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(10000);
			
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//wsrequest.setUrl("http://172.19.148.48:7108/api/des/V1/parametros/monedas/consultas");
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/monedas");
		//retorno: WSResponse [statusText=, status=200, body={"resultado":{"codigo":"0000","descripcion":"Operacion Exitosa."},"monedas":[{"codMoneda":"EUR","descripcion":"EURO Europa","codAlterno":"222","flagActivo":true,"codUsuario":"E33333","fechaModificacion":"2021-05-07 21:24:07"}]}, exitoso=true, httpRetorno=kong.unirest.StringResponse@7451891e, httpError=null, error=null, idConstructor=1]
		log.info("antes de llamarte WS en actualizarWs");
		retorno = wsService.put(wsrequest);
		log.info("retorno: "+retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en actualizar la lista moneda");
	            try {
					response = mapper.jsonToClass(retorno.getBody(), Response.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("monedaResponse: "+response);
	            //respuesta =" Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion();
	            respuesta = response.getResultado().getDescripcion();
				return respuesta;
				
			}else {
				if(retorno.getStatus() == 422 || retorno.getStatus() == 400 || retorno.getStatus() == 600) {
					log.info("entro en error 422");
					try {
						Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
						log.info("resultado: "+resultado);
						//error = " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion();
						error = resultado.getDescripcion();
						throw new CustomException(error);
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





	@Override
	public Moneda buscarMoneda(MonedasRequest monedasRequest) throws CustomException {
		WSRequest wsrequest = new WSRequest();
		WSResponse retorno;
		String monedasRequestJSON;
		MonedaResponse monedaResponse = new MonedaResponse();
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		log.info("monedasRequestJSON: "+monedasRequestJSON);
		
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setConnectTimeout(10000);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(10000);
			
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//wsrequest.setUrl("http://172.19.148.48:7108/api/des/V1/parametros/monedas/consultas");
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/monedas/consultas");
			
		//retorno: WSResponse [statusText=, status=200, body={"resultado":{"codigo":"0000","descripcion":"Operacion Exitosa."},"monedas":[{"codMoneda":"EUR","descripcion":"EURO Europa","codAlterno":"222","flagActivo":true,"codUsuario":"E33333","fechaModificacion":"2021-05-07 21:24:07"}]}, exitoso=true, httpRetorno=kong.unirest.StringResponse@7451891e, httpError=null, error=null, idConstructor=1]
		log.info("antes de llamarte WS en consultar");
		retorno = wsService.post(wsrequest);
		
		log.info("retorno: "+retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista moneda");
	            try {
					monedaResponse = mapper.jsonToClass(retorno.getBody(), MonedaResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("monedaResponse: "+monedaResponse);
	            log.info(monedaResponse.getResultado().getCodigo());
	            if(monedaResponse.getResultado().getCodigo().equals("0000")){
	            	log.info("Respusta codigo 0000 si existe la modena");
	            	return monedaResponse.getMonedas().get(0);
	            }else {
	            	return null;
	            }
				
				
			}else {
				if(retorno.getStatus() == 422) {
					log.info("entro en error 422");
					try {
						Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
						log.info("resultado: "+resultado);
						//String mensaje = " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion();
						String mensaje = resultado.getDescripcion();
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





	@Override
	public String crear(MonedasRequest monedasRequest) throws CustomException {
		WSRequest wsrequest = new WSRequest();
		WSResponse retorno;
		Response response = new Response();
		String respuesta;
		String error;
		String monedasRequestJSON;
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		log.info("monedasRequestJSON: "+monedasRequestJSON);
		
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setConnectTimeout(10000);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(10000);
			
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//wsrequest.setUrl("http://172.19.148.48:7108/api/des/V1/parametros/monedas/consultas");
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/monedas");
			
		//retorno: WSResponse [statusText=, status=200, body={"resultado":{"codigo":"0000","descripcion":"Operacion Exitosa."},"monedas":[{"codMoneda":"EUR","descripcion":"EURO Europa","codAlterno":"222","flagActivo":true,"codUsuario":"E33333","fechaModificacion":"2021-05-07 21:24:07"}]}, exitoso=true, httpRetorno=kong.unirest.StringResponse@7451891e, httpError=null, error=null, idConstructor=1]
		log.info("antes de llamarte WS en crearWs");
		retorno = wsService.post(wsrequest);
		log.info("retorno: "+retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en actualizar la lista moneda");
	            try {
					response = mapper.jsonToClass(retorno.getBody(), Response.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("monedaResponse: "+response);
	            //respuesta =" Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion();
	            respuesta = response.getResultado().getDescripcion();
				return respuesta;
				
			}else {
				if(retorno.getStatus() == 422 || retorno.getStatus() == 400 || retorno.getStatus() == 600) {
					log.info("entro en error 422");
					try {
						//Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
						//log.info("resultado: "+resultado);
						//error = " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion();
						response = mapper.jsonToClass(retorno.getBody(), Response.class);
						log.info("response: "+response);
						//error = " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion();
						error = response.getResultado().getDescripcion();
						throw new CustomException(error);
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
