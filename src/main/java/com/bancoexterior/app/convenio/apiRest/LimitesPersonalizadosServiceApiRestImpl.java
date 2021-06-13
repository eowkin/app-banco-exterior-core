package com.bancoexterior.app.convenio.apiRest;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import com.bancoexterior.app.convenio.dto.LimitesPersonalizadosRequest;
import com.bancoexterior.app.convenio.dto.LimitesPersonalizadosResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.LimitesPersonalizados;
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
public class LimitesPersonalizadosServiceApiRestImpl implements ILimitesPersonalizadosServiceApiRest{

	@Autowired
	private IWSService wsService;
	
	@Autowired 
	private Mapper mapper;
	
	@Value("${des.ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${des.SocketTimeout}")
    private int socketTimeout;
    
    @Value("${des.limitesPersonalizados.urlConsulta}")
    private String urlConsulta;
    
    @Value("${des.limitesPersonalizados.urlActualizar}")
    private String urlActualizar;

    public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }

    
	@Override
	public List<LimitesPersonalizados> listaLimitesPersonalizados(
			LimitesPersonalizadosRequest limitesPersonalizadosRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		LimitesPersonalizadosResponse limiteResponse = new LimitesPersonalizadosResponse();
		String limitesPersonalizadosRequestJSON;
		limitesPersonalizadosRequestJSON = new Gson().toJson(limitesPersonalizadosRequest);
		log.info("limitesPersonalizadosRequestJSON: "+limitesPersonalizadosRequestJSON);
		
		wsrequest.setBody(limitesPersonalizadosRequestJSON);
		wsrequest.setUrl(urlConsulta);
		
		log.info("antes de llamarte WS en listaLimitesPersonalizados");
		retorno = wsService.post(wsrequest);
		
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista limites personalizados");
	            try {
					limiteResponse = mapper.jsonToClass(retorno.getBody(), LimitesPersonalizadosResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("limiteResponse: "+limiteResponse);
	            log.info(limiteResponse.getResultado().getCodigo());
	            return limiteResponse.getLimitesPersonalizados();
			}else {
				if (retorno.getStatus() == 422) {
					log.info("entro en error 422 en listaLimitesPersonalizados");
					try {
						Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
						log.info("resultado: "+resultado);
						String mensaje = resultado.getDescripcion();
						throw new CustomException(mensaje);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}
		}else {
			throw new CustomException("No hubo conexion con el micreoservicio limites personalizados");
		}
		return null;
	}

	@Override
	public LimitesPersonalizados buscarLimitesPersonalizados(LimitesPersonalizadosRequest limitesPersonalizadosRequest)
			throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		LimitesPersonalizadosResponse limiteResponse = new LimitesPersonalizadosResponse();
		String limitesPersonalizadosRequestJSON;
		limitesPersonalizadosRequestJSON = new Gson().toJson(limitesPersonalizadosRequest);
		log.info("limitesPersonalizadosRequestJSON: "+limitesPersonalizadosRequestJSON);
		
		wsrequest.setBody(limitesPersonalizadosRequestJSON);
		wsrequest.setUrl(urlConsulta);
			
		log.info("antes de llamarte WS en consultar");
		retorno = wsService.post(wsrequest);
		
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscarLimitesPersonalizados");
	            try {
					limiteResponse = mapper.jsonToClass(retorno.getBody(), LimitesPersonalizadosResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("limiteResponse: "+limiteResponse);
	            log.info(limiteResponse.getResultado().getCodigo());
	            if(limiteResponse.getResultado().getCodigo().equals("0000")){
	            	log.info("Respusta codigo 0000 si existe el limite");
	            	return limiteResponse.getLimitesPersonalizados().get(0);
	            }else {
	            	return null;
	            }
			}else {
				if (retorno.getStatus() == 422) {
					log.info("entro en error 422 en buscarLimitesPersonalizados");
					try {
						Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
						log.info("resultado: "+resultado);
						String mensaje = resultado.getDescripcion();
						throw new CustomException(mensaje);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}
		}else {
			throw new CustomException("No hubo conexion con el micreoservicio limitesPersonalizados");
		}
		return null;
	}

	@Override
	public String actualizar(LimitesPersonalizadosRequest limitesPersonalizadosRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		Response response = new Response();  
		String respuesta;
		String error;
		String limitesPersonalizadosRequestJSON;
		limitesPersonalizadosRequestJSON = new Gson().toJson(limitesPersonalizadosRequest);
		log.info("limitesPersonalizadosRequestJSON: "+limitesPersonalizadosRequestJSON);
		
		wsrequest.setBody(limitesPersonalizadosRequestJSON);
		wsrequest.setUrl(urlActualizar);
			
		log.info("antes de llamarte WS en actualizar");
		retorno = wsService.put(wsrequest);
		log.info("retorno: "+retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en Actualizar el limitePersonalizado por codigo");
				try {
					response = mapper.jsonToClass(retorno.getBody(), Response.class);
					log.info("response: "+response);
				} catch (IOException e) {
					e.printStackTrace();
				}
				respuesta = response.getResultado().getDescripcion();
				return respuesta;
				
				
			}else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400 || retorno.getStatus() == 600) {
					log.info("Respusta codigo " +retorno.getStatus()+ "en Actualizar el limite personalizado por codigo");
					try {
						response = mapper.jsonToClass(retorno.getBody(), Response.class);
						log.info("response: "+response);
						error = response.getResultado().getDescripcion();
						throw new CustomException(error);
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}
		}else {
			throw new CustomException("No hubo conexion con el micreoservicio limites personalizados");
		}
		return null;
	}

	@Override
	public String crear(LimitesPersonalizadosRequest limitesPersonalizadosRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		Response response = new Response();  
		String respuesta;
		String error;
		String limitesPersonalizadosRequestJSON;
		limitesPersonalizadosRequestJSON = new Gson().toJson(limitesPersonalizadosRequest);
		log.info("limitesPersonalizadosRequestJSON: "+limitesPersonalizadosRequestJSON);
		
		wsrequest.setBody(limitesPersonalizadosRequestJSON);
		wsrequest.setUrl(urlActualizar);
			
		log.info("antes de llamarte WS en crear");
		retorno = wsService.post(wsrequest);
		log.info("retorno: "+retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en crear el limitepersonalizado por codigo");
				try {
					response = mapper.jsonToClass(retorno.getBody(), Response.class);
					log.info("response: "+response);
				} catch (IOException e) {
					e.printStackTrace();
				}
				respuesta = response.getResultado().getDescripcion();
				return respuesta;
				
				
			}else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400 || retorno.getStatus() == 600) {
					log.info("Respusta codigo " +retorno.getStatus()+ "en Crear el limitePersonalizado por codigo");
					try {
						response = mapper.jsonToClass(retorno.getBody(), Response.class);
						log.info("response: "+response);
						error = response.getResultado().getDescripcion();
						throw new CustomException(error);
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}
		}else {
			throw new CustomException("No hubo conexion con el micreoservicio limites personalizados");
		}
		return null;
	}

}
