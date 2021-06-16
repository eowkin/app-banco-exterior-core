package com.bancoexterior.app.convenio.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.convenio.dto.AgenciaRequest;
import com.bancoexterior.app.convenio.dto.AgenciaResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.Agencia;
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
public class AgenciaServiceApiRestImpl implements IAgenciaServiceApiRest{

	@Autowired
	private IWSService wsService;
	
	@Autowired 
	private Mapper mapper;
	
	@Value("${des.ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${des.SocketTimeout}")
    private int socketTimeout;
    
    @Value("${des.agencia.urlConsulta}")
    private String urlConsulta;
    
    @Value("${des.agencia.urlActualizar}")
    private String urlActualizar;
	

    public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }
    
    
	@Override
	public List<Agencia> listaAgencias(AgenciaRequest agenciaRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		AgenciaResponse agenciaResponse = new AgenciaResponse();
		String agenciaRequestJSON;
		agenciaRequestJSON = new Gson().toJson(agenciaRequest);
		wsrequest.setBody(agenciaRequestJSON);
		wsrequest.setUrl(urlConsulta);								
		log.info("antes de llamarte WS en consultar listaAgencias");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista agencias");
	            try {
					agenciaResponse = mapper.jsonToClass(retorno.getBody(), AgenciaResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info(agenciaResponse.getResultado().getCodigo());
	            return agenciaResponse.getListaAgencias();
			}else {
				if (retorno.getStatus() == 422) {
					log.info("entro en error 422 en buscar la lista agencias");
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
			throw new CustomException("No hubo conexion con el micreoservicio agencias");
		}
		
		return null;
	}

	@Override
	public Agencia buscarAgencia(AgenciaRequest agenciaRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		AgenciaResponse agenciaResponse = new AgenciaResponse();
		String agenciaRequestJSON;
		agenciaRequestJSON = new Gson().toJson(agenciaRequest);
		wsrequest.setBody(agenciaRequestJSON);
		wsrequest.setUrl(urlConsulta);								
		log.info("antes de llamarte WS en consultar buscarAgencia");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscarAgencia");
	            try {
					agenciaResponse = mapper.jsonToClass(retorno.getBody(), AgenciaResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info(agenciaResponse.getResultado().getCodigo());
	            if(agenciaResponse.getResultado().getCodigo().equals("0000")){
	            	log.info("Respusta codigo 0000 si existe la agencia");
	            	return agenciaResponse.getListaAgencias().get(0);
	            }else {
	            	return null;
	            }
			}else {
				if (retorno.getStatus() == 422) {
					log.info("entro en error 422 en buscarAgencia");
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
			throw new CustomException("No hubo conexion con el micreoservicio agencias");
		}
		return null;
	}

	@Override
	public String actualizar(AgenciaRequest agenciaRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		Response response = new Response();
		Resultado resultado = new Resultado();
		String respuesta;
		String error;
		String agenciaRequestJSON;
		agenciaRequestJSON = new Gson().toJson(agenciaRequest);
		wsrequest.setBody(agenciaRequestJSON);
		wsrequest.setUrl(urlActualizar);								
		log.info("antes de llamarte WS en actualizar");
		retorno = wsService.put(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en Actualizar agencia por codigo");
				try {
					resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				respuesta = resultado.getDescripcion();
				return respuesta;
				
				
			}else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400 || retorno.getStatus() == 600) {
					log.info("Respusta error   Actualizar la agencia por codigo");
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
			throw new CustomException("No hubo conexion con el micreoservicio agencias");
		}
		return null;
	}

	@Override
	public String crear(AgenciaRequest agenciaRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		Resultado resultado = new Resultado();
		String respuesta;
		String error;
		String agenciaRequestJSON;
		agenciaRequestJSON = new Gson().toJson(agenciaRequest);
		wsrequest.setBody(agenciaRequestJSON);
		wsrequest.setUrl(urlActualizar);								
		log.info("antes de llamarte WS en creear");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en crear la agencia por codigo");
				try {
					resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
					log.info("resultado: "+resultado);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				respuesta = resultado.getDescripcion();
				return respuesta;
				
				
			}else {
				if (retorno.getStatus() == 422) {
					log.info("Respusta codigo " +retorno.getStatus()+ "en crear la agencia por codigo");
					try {
						
						Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
						error = response.getResultado().getDescripcion();
						throw new CustomException(error);
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}else {
					if (retorno.getStatus() == 400 || retorno.getStatus() == 600) {
						try {
							resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
							error = resultado.getDescripcion();
							throw new CustomException(error);
							
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					}
				}
			}
		}else {
			throw new CustomException("No hubo conexion con el micreoservicio agencias");
		}
		return null;
	}

}
