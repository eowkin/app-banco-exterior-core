package com.bancoexterior.app.convenio.apiRest;


import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	
    @Value("${des.ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${des.SocketTimeout}")
    private int socketTimeout;
    
    @Value("${des.moneda.urlConsulta}")
    private String urlConsulta;
    
    @Value("${des.moneda.urlActualizar}")
    private String urlActualizar;

	
    public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }



	@Override
	public List<Moneda> listaMonedas(MonedasRequest monedasRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String monedasRequestJSON;
		MonedaResponse monedaResponse = new MonedaResponse();
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		log.info("monedasRequestJSON: "+monedasRequestJSON);
		
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setUrl(urlConsulta);
		log.info("antes de llamarte WS en consultar lista moneda");
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
					log.info("entro en error 422 en buscar la lista moneda");
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
			throw new CustomException("No hubo conexion con el micreoservicio Monedas");
		}
		return null;
	}





	@Override
	public boolean existe(MonedasRequest monedasRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String monedasRequestJSON;
		MonedaResponse monedaResponse = new MonedaResponse();
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		log.info("monedasRequestJSON: "+monedasRequestJSON);
		
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setUrl(urlConsulta);
			
		
		log.info("antes de llamarte WS en consultar existe moneda");
		retorno = wsService.post(wsrequest);
		
		log.info("retorno: "+retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar existe moneda");
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
					log.info("entro en error 422 en existe moneda");
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
			throw new CustomException("No hubo conexion con el micreoservicio Monedas");
		}
		
		return false;
	}





	@Override
	public String actualizar(MonedasRequest monedasRequest) throws CustomException{
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		Response response = new Response();
		String respuesta;
		String error;
		String monedasRequestJSON;
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		log.info("monedasRequestJSON: "+monedasRequestJSON);
		
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setUrl(urlActualizar);
		log.info("antes de llamarte WS en actualizarWs");
		retorno = wsService.put(wsrequest);
		log.info("retorno: "+retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en actualizar en actualizarWs");
	            try {
					response = mapper.jsonToClass(retorno.getBody(), Response.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("monedaResponse: "+response);
	            respuesta = response.getResultado().getDescripcion();
				return respuesta;
				
			}else {
				if(retorno.getStatus() == 422 || retorno.getStatus() == 400 || retorno.getStatus() == 600) {
					log.info("entro en error 422 en en actualizarWs");
					try {
						Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
						log.info("resultado: "+resultado);
						error = resultado.getDescripcion();
						throw new CustomException(error);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else {
			throw new CustomException("No hubo conexion con el micreoservicio Monedas");
		}
		return null;
	}





	@Override
	public Moneda buscarMoneda(MonedasRequest monedasRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String monedasRequestJSON;
		MonedaResponse monedaResponse = new MonedaResponse();
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		log.info("monedasRequestJSON: "+monedasRequestJSON);
		
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setUrl(urlConsulta);
	
		log.info("antes de llamarte WS en buscarMoneda");
		retorno = wsService.post(wsrequest);
		
		log.info("retorno: "+retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscarMoneda");
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
					log.info("entro en error 422 en buscarMoneda");
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
			throw new CustomException("No hubo conexion con el micreoservicio Monedas");
		}
		
		return null;
	}





	@Override
	public String crear(MonedasRequest monedasRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		Response response = new Response();
		String respuesta;
		String error;
		String monedasRequestJSON;
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		log.info("monedasRequestJSON: "+monedasRequestJSON);
		
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setUrl(urlActualizar);
		
		log.info("antes de llamarte WS en crearWs");
		retorno = wsService.post(wsrequest);
		log.info("retorno: "+retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en crearWs");
	            try {
					response = mapper.jsonToClass(retorno.getBody(), Response.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("monedaResponse: "+response);
	            respuesta = response.getResultado().getDescripcion();
				return respuesta;
				
			}else {
				if(retorno.getStatus() == 422 || retorno.getStatus() == 400 || retorno.getStatus() == 600) {
					log.info("entro en error 422 en crearWs");
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
			throw new CustomException("No hubo conexion con el micreoservicio Monedas");
		}
		return null;
	}

}
