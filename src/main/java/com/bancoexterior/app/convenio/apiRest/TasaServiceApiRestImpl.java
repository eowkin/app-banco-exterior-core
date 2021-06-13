package com.bancoexterior.app.convenio.apiRest;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.convenio.dto.TasaRequest;
import com.bancoexterior.app.convenio.dto.TasaResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.Tasa;
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
public class TasaServiceApiRestImpl implements ITasaServiceApiRest{

	@Autowired
	private IWSService wsService;
	
	@Autowired 
	private Mapper mapper;
    
	@Value("${des.ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${des.SocketTimeout}")
    private int socketTimeout;
    
    @Value("${des.tasa.urlConsulta}")
    private String urlConsulta;
    
    @Value("${des.tasa.urlActualizar}")
    private String urlActualizar;
	
	public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }
	
	@Override
	public List<Tasa> listaTasas(TasaRequest tasaRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		TasaResponse tasaResponse = new TasaResponse();
		String tasaRequestJSON;
		tasaRequestJSON = new Gson().toJson(tasaRequest);
		log.info("tasaRequestJSON: "+tasaRequestJSON);
		
		wsrequest.setBody(tasaRequestJSON);
		wsrequest.setUrl(urlConsulta);

		log.info("antes de llamarte WS en consultar la lista de tasas");
		retorno = wsService.post(wsrequest);
		log.info("retorno: "+retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista de tasas");
	            try {
					tasaResponse = mapper.jsonToClass(retorno.getBody(), TasaResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("tasaResponse: "+tasaResponse);
	            log.info(tasaResponse.getResultado().getCodigo());
	            return tasaResponse.getTasa();
			}else {
				if (retorno.getStatus() == 422) {
					log.info("entro en error 422 en la lista de tasas");
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
			throw new CustomException("No hubo conexion con el micreoservicio tasa");
		}
		return null;
	}


	@Override
	public Tasa buscarTasa(TasaRequest tasaRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		TasaResponse tasaResponse = new TasaResponse();
		String tasaRequestJSON;
		tasaRequestJSON = new Gson().toJson(tasaRequest);
		log.info("tasaRequestJSON: "+tasaRequestJSON);
		
		wsrequest.setBody(tasaRequestJSON);
		wsrequest.setUrl(urlConsulta);
			
		log.info("antes de llamarte WS en buscarTasa");
		retorno = wsService.post(wsrequest);
		log.info("retorno: "+retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la tasa");
	            try {
					tasaResponse = mapper.jsonToClass(retorno.getBody(), TasaResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("tasaResponse: "+tasaResponse);
	            log.info(tasaResponse.getResultado().getCodigo());
	            if(tasaResponse.getResultado().getCodigo().equals("0000")){
	            	log.info("Respusta codigo 0000 si existe la tasa");
	            	return tasaResponse.getTasa().get(0);
	            }else {
	            	return null;
	            }
			}else {
				if (retorno.getStatus() == 422) {
					log.info("entro en error 422 en buscarTasa");
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
			throw new CustomException("No hubo conexion con el micreoservicio tasa");
		}
		return null;
	}


	@Override
	public String actualizar(TasaRequest tasaRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		Response response = new Response();
		Resultado resultado = new Resultado();
		String respuesta;
		String error;
		String tasaRequestJSON;
		tasaRequestJSON = new Gson().toJson(tasaRequest);
		log.info("tasaRequestJSON: "+tasaRequestJSON);
		
		wsrequest.setBody(tasaRequestJSON);
		wsrequest.setUrl(urlActualizar);
		
		log.info("antes de llamarte WS en actualizar");
		retorno = wsService.put(wsrequest);
		log.info("retorno: "+retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en Actualizar la tasa por codigo");
				try {
					resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
					log.info("resultado: "+resultado);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				respuesta = resultado.getDescripcion();
				return respuesta;
				
			}else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400 || retorno.getStatus() == 600) {
					log.info("Respusta codigo " +retorno.getStatus()+ "en Actualizar la tasa por codigo");
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
			throw new CustomException("No hubo conexion con el micreoservicio tasa");
		}	
		return null;
	}


	@Override
	public String crear(TasaRequest tasaRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		Response response = new Response();
		Resultado resultado = new Resultado();
		String respuesta;
		String error;
		String tasaRequestJSON;
		tasaRequestJSON = new Gson().toJson(tasaRequest);
		log.info("tasaRequestJSON: "+tasaRequestJSON);
		
		wsrequest.setBody(tasaRequestJSON);
		wsrequest.setUrl(urlActualizar);
		
		log.info("antes de llamarte WS en crear");
		retorno = wsService.post(wsrequest);
		log.info("retorno: "+retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en crear la tasa por codigo");
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
					log.info("Respusta codigo " +retorno.getStatus()+ "en craer la tasa por codigo");
					try {
						response = mapper.jsonToClass(retorno.getBody(), Response.class);
						log.info("response: "+response);
						error = response.getResultado().getDescripcion();
						throw new CustomException(error);
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}if (retorno.getStatus() == 400 || retorno.getStatus() == 600) {
					log.info("Respusta codigo " +retorno.getStatus()+ "en craer la tasa por codigo");
					try {
						resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
						error = resultado.getDescripcion();
						throw new CustomException(error);
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}
		}else {
			throw new CustomException("No hubo conexion con el micreoservicio tasa");
		}
		return null;
	}

}
