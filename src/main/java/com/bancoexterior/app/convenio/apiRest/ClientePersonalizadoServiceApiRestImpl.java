package com.bancoexterior.app.convenio.apiRest;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.convenio.dto.ClienteRequest;
import com.bancoexterior.app.convenio.dto.ClienteResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.ClientesPersonalizados;
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
public class ClientePersonalizadoServiceApiRestImpl implements IClientePersonalizadoServiceApiRest{

	
	@Autowired
	private IWSService wsService;
	
	@Autowired 
	private Mapper mapper;
	
	

	@Override
	public List<ClientesPersonalizados> listaClientesPersonalizados(ClienteRequest clienteRequest)
			throws CustomException {
		WSRequest wsrequest = new WSRequest();
		WSResponse retorno;
		ClienteResponse clienteResponse = new ClienteResponse();
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		log.info("clienteRequestJSON: "+clienteRequestJSON);
		
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setConnectTimeout(10000);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(10000);
			
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//wsrequest.setUrl("http://172.19.148.48:7108/api/des/V1/parametros/monedas/consultas");
		                  //https://172.19.148.51:8443/api/des/V1/parametros/limites/consultas 
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/clientes/consultas");
			
		//retorno: WSResponse [statusText=, status=200, body={"resultado":{"codigo":"0000","descripcion":"Operacion Exitosa."},"monedas":[{"codMoneda":"EUR","descripcion":"EURO Europa","codAlterno":"222","flagActivo":true,"codUsuario":"E33333","fechaModificacion":"2021-05-07 21:24:07"}]}, exitoso=true, httpRetorno=kong.unirest.StringResponse@7451891e, httpError=null, error=null, idConstructor=1]
		log.info("antes de llamarte WS en consultar");
		retorno = wsService.post(wsrequest);
		log.info("retorno: "+retorno);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista clientes personalizados");
				try {
					clienteResponse = mapper.jsonToClass(retorno.getBody(), ClienteResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
				log.info("clienteResponse: " + clienteResponse);
				log.info(clienteResponse.getResultado().getCodigo());
				
				return clienteResponse.getListaClientes();
			} else {
				if (retorno.getStatus() == 422) {
					log.info("entro en error 422");
					try {
						Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
						log.info("resultado: "+resultado);
						String mensaje = " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion();
						throw new CustomException(mensaje);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			log.info("error conectar microservicio consultarWs clientesPersonalizados");
			
		}
		return null;
	}

	@Override
	public ClientesPersonalizados buscarClientesPersonalizados(ClienteRequest clienteRequest) throws CustomException {
		WSRequest wsrequest = new WSRequest();
		WSResponse retorno;
		ClienteResponse clienteResponse = new ClienteResponse();
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		log.info("clienteRequestJSON: "+clienteRequestJSON);
		
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setConnectTimeout(10000);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(10000);
			
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//wsrequest.setUrl("http://172.19.148.48:7108/api/des/V1/parametros/monedas/consultas");
		                  //https://172.19.148.51:8443/api/des/V1/parametros/limites/consultas 
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/clientes/consultas");
			
		//retorno: WSResponse [statusText=, status=200, body={"resultado":{"codigo":"0000","descripcion":"Operacion Exitosa."},"monedas":[{"codMoneda":"EUR","descripcion":"EURO Europa","codAlterno":"222","flagActivo":true,"codUsuario":"E33333","fechaModificacion":"2021-05-07 21:24:07"}]}, exitoso=true, httpRetorno=kong.unirest.StringResponse@7451891e, httpError=null, error=null, idConstructor=1]
		log.info("antes de llamarte WS en consultar");
		retorno = wsService.post(wsrequest);
		log.info("retorno: "+retorno);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista clientes personalizados");
				try {
					clienteResponse = mapper.jsonToClass(retorno.getBody(), ClienteResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
				log.info("clienteResponse: " + clienteResponse);
				log.info(clienteResponse.getResultado().getCodigo());
				if(clienteResponse.getResultado().getCodigo().equals("0000")){
	            	log.info("Respusta codigo 0000 si existe el limite");
	            	return clienteResponse.getListaClientes().get(0);
	            }else {
	            	return null;
	            }
			} else {
				if (retorno.getStatus() == 422) {
					log.info("entro en error 422");
					try {
						Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
						log.info("response: "+response);
						String error = " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion();
						throw new CustomException(error);
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			log.info("error conectar microservicio consultarWs clientesPersonalizados");
			
		}
		return null;
	}

	@Override
	public String actualizar(ClienteRequest clienteRequest) throws CustomException {
		WSRequest wsrequest = new WSRequest();
		WSResponse retorno;
		Response response = new Response();  
		String respuesta;
		String error;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		log.info("clienteRequestJSON: "+clienteRequestJSON);
		
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setConnectTimeout(10000);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(10000);
			
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//wsrequest.setUrl("http://172.19.148.48:7108/api/des/V1/parametros/monedas/consultas");
		                  //https://172.19.148.51:8443/api/des/V1/parametros/limites/consultas 
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/clientes");
			
		//retorno: WSResponse [statusText=, status=200, body={"resultado":{"codigo":"0000","descripcion":"Operacion Exitosa."},"monedas":[{"codMoneda":"EUR","descripcion":"EURO Europa","codAlterno":"222","flagActivo":true,"codUsuario":"E33333","fechaModificacion":"2021-05-07 21:24:07"}]}, exitoso=true, httpRetorno=kong.unirest.StringResponse@7451891e, httpError=null, error=null, idConstructor=1]
		log.info("antes de llamarte WS en actualizar");
		retorno = wsService.put(wsrequest);
		log.info("retorno: "+retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en Actualizar el limiteGenerales por codigo");
				try {
					response = mapper.jsonToClass(retorno.getBody(), Response.class);
					log.info("response: "+response);
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				respuesta =" Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion();
				return respuesta;
				
				
			}else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400 || retorno.getStatus() == 600) {
					log.info("Respusta codigo " +retorno.getStatus()+ "en Actualizar la moneda por codigo");
					try {
						response = mapper.jsonToClass(retorno.getBody(), Response.class);
						log.info("response: "+response);
						error = " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion();
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
	public String crear(ClienteRequest clienteRequest) throws CustomException {
		WSRequest wsrequest = new WSRequest();
		WSResponse retorno;
		Response response = new Response();  
		String respuesta;
		String error;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		log.info("clienteRequestJSON: "+clienteRequestJSON);
		
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setConnectTimeout(10000);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(10000);
			
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//wsrequest.setUrl("http://172.19.148.48:7108/api/des/V1/parametros/monedas/consultas");
		                  //https://172.19.148.51:8443/api/des/V1/parametros/limites/consultas 
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/clientes");
			
		//retorno: WSResponse [statusText=, status=200, body={"resultado":{"codigo":"0000","descripcion":"Operacion Exitosa."},"monedas":[{"codMoneda":"EUR","descripcion":"EURO Europa","codAlterno":"222","flagActivo":true,"codUsuario":"E33333","fechaModificacion":"2021-05-07 21:24:07"}]}, exitoso=true, httpRetorno=kong.unirest.StringResponse@7451891e, httpError=null, error=null, idConstructor=1]
		log.info("antes de llamarte WS en crear");
		retorno = wsService.post(wsrequest);
		log.info("retorno: "+retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en crear el clientePersonalizado por codigo");
				try {
					response = mapper.jsonToClass(retorno.getBody(), Response.class);
					log.info("response: "+response);
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				respuesta =" Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion();
				return respuesta;
				
				
			}else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400 || retorno.getStatus() == 600) {
					log.info("Respusta codigo " +retorno.getStatus()+ "en Actualizar la moneda por codigo");
					try {
						response = mapper.jsonToClass(retorno.getBody(), Response.class);
						log.info("response: "+response);
						error = " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion();
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

	public WSResponse consultarWs(ClienteRequest clienteRequest) {
		WSRequest wsrequest = new WSRequest();
		WSResponse retorno;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		log.info("clienteRequestJSON: "+clienteRequestJSON);
		
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setConnectTimeout(10000);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(10000);
			
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
		//wsrequest.setUrl("http://172.19.148.48:7108/api/des/V1/parametros/monedas/consultas");
		                  //https://172.19.148.51:8443/api/des/V1/parametros/limites/consultas 
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/clientes/consultas");
			
		//retorno: WSResponse [statusText=, status=200, body={"resultado":{"codigo":"0000","descripcion":"Operacion Exitosa."},"monedas":[{"codMoneda":"EUR","descripcion":"EURO Europa","codAlterno":"222","flagActivo":true,"codUsuario":"E33333","fechaModificacion":"2021-05-07 21:24:07"}]}, exitoso=true, httpRetorno=kong.unirest.StringResponse@7451891e, httpError=null, error=null, idConstructor=1]
		log.info("antes de llamarte WS en consultar");
		retorno = wsService.post(wsrequest);
		return retorno;
	}



}
