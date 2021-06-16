package com.bancoexterior.app.convenio.service;

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
		wsrequest.setBody(limitesPersonalizadosRequestJSON);
		wsrequest.setUrl(urlConsulta);
		log.info("antes de llamarte WS en listaLimitesPersonalizados");
		retorno = wsService.post(wsrequest);
		
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				try {
					limiteResponse = mapper.jsonToClass(retorno.getBody(), LimitesPersonalizadosResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            return limiteResponse.getLimitesPersonalizados();
			}else {
				if (retorno.getStatus() == 422) {
					try {
						Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
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
		wsrequest.setBody(limitesPersonalizadosRequestJSON);
		wsrequest.setUrl(urlConsulta);
			
		log.info("antes de llamarte WS en consultar");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				try {
					limiteResponse = mapper.jsonToClass(retorno.getBody(), LimitesPersonalizadosResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            if(limiteResponse.getResultado().getCodigo().equals("0000")){
	            	return limiteResponse.getLimitesPersonalizados().get(0);
	            }else {
	            	return null;
	            }
			}else {
				if (retorno.getStatus() == 422) {
					try {
						Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
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
		wsrequest.setBody(limitesPersonalizadosRequestJSON);
		wsrequest.setUrl(urlActualizar);
		log.info("antes de llamarte WS en actualizar");
		retorno = wsService.put(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				try {
					response = mapper.jsonToClass(retorno.getBody(), Response.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
				respuesta = response.getResultado().getDescripcion();
				return respuesta;
				
				
			}else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400 || retorno.getStatus() == 600) {
					try {
						response = mapper.jsonToClass(retorno.getBody(), Response.class);
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
		wsrequest.setBody(limitesPersonalizadosRequestJSON);
		wsrequest.setUrl(urlActualizar);
			
		log.info("antes de llamarte WS en crear");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				try {
					response = mapper.jsonToClass(retorno.getBody(), Response.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
				respuesta = response.getResultado().getDescripcion();
				return respuesta;
				
				
			}else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400 || retorno.getStatus() == 600) {
					try {
						response = mapper.jsonToClass(retorno.getBody(), Response.class);
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