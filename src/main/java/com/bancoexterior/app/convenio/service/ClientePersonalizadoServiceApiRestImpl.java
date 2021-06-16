package com.bancoexterior.app.convenio.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.convenio.dto.ClienteDatosBasicoRequest;
import com.bancoexterior.app.convenio.dto.ClienteDatosBasicosResponse;
import com.bancoexterior.app.convenio.dto.ClienteRequest;
import com.bancoexterior.app.convenio.dto.ClienteResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.ClientesPersonalizados;
import com.bancoexterior.app.convenio.model.DatosClientes;
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
	
	@Value("${des.ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${des.SocketTimeout}")
    private int socketTimeout;
    
    @Value("${des.clientesPersonalizados.urlConsulta}")
    private String urlConsulta;
    
    @Value("${des.clientesPersonalizados.urlActualizar}")
    private String urlActualizar;
    
    @Value("${des.datosbasicos.urlConsultaDatosBasicos}")
    private String urlConsultaDatosBasicos;
    
	
    public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }

	@Override
	public List<ClientesPersonalizados> listaClientesPersonalizados(ClienteRequest clienteRequest)
			throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		ClienteResponse clienteResponse = new ClienteResponse();
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl(urlConsulta);
	
		log.info("antes de llamarte WS en consultar listaClientesPersonalizados");
		retorno = wsService.post(wsrequest);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista clientes personalizados");
				try {
					clienteResponse = mapper.jsonToClass(retorno.getBody(), ClienteResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
				log.info(clienteResponse.getResultado().getCodigo());
				return clienteResponse.getListaClientes();
			} else {
				if (retorno.getStatus() == 422) {
					log.info("entro en error 422 en listaClientesPersonalizados");
					try {
						Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
						String mensaje = resultado.getDescripcion();
						throw new CustomException(mensaje);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			throw new CustomException("No hubo conexion con el micreoservicio clientesPersonalizados");
			
		}
		return null;
	}

	@Override
	public ClientesPersonalizados buscarClientesPersonalizados(ClienteRequest clienteRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		ClienteResponse clienteResponse = new ClienteResponse();
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl(urlConsulta);
		
		log.info("antes de llamarte WS en consultar buscarClientesPersonalizados");
		retorno = wsService.post(wsrequest);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscarClientesPersonalizados");
				try {
					clienteResponse = mapper.jsonToClass(retorno.getBody(), ClienteResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
				log.info(clienteResponse.getResultado().getCodigo());
				if(clienteResponse.getResultado().getCodigo().equals("0000")){
	            	log.info("Respusta codigo 0000 si existe el limite");
	            	return clienteResponse.getListaClientes().get(0);
	            }else {
	            	return null;
	            }
			} else {
				if (retorno.getStatus() == 422) {
					log.info("entro en error 422 en buscarClientesPersonalizados");
					try {
						Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
						String error = resultado.getDescripcion();
						throw new CustomException(error);
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			throw new CustomException("No hubo conexion con el micreoservicio clientesPersonalizados");
			
		}
		return null;
	}

	@Override
	public String actualizar(ClienteRequest clienteRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		Response response = new Response();  
		String respuesta;
		String error;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl(urlActualizar);
			
		log.info("antes de llamarte WS en actualizar");
		retorno = wsService.put(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en Actualizar el cliente por codigo");
				try {
					response = mapper.jsonToClass(retorno.getBody(), Response.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
				respuesta = response.getResultado().getDescripcion();
				return respuesta;
				
				
			}else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400 || retorno.getStatus() == 600) {
					log.info("Respusta codigo " +retorno.getStatus()+ "en Actualizar el cliente por codigo");
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
			throw new CustomException("No hubo conexion con el micreoservicio");
		}
		return null;
	}

	@Override
	public String crear(ClienteRequest clienteRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		Response response = new Response();  
		String respuesta;
		String error;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl(urlActualizar);
		log.info("antes de llamarte WS en crear");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en crear el clientePersonalizado por codigo");
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
			throw new CustomException("No hubo conexion con el micreoservicio");
		}
		
		
		return null;
	}

	public WSResponse consultarWs(ClienteRequest clienteRequest) {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl(urlConsulta);
		log.info("antes de llamarte WS en consultar");
		retorno = wsService.post(wsrequest);
		return retorno;
	}

	@Override
	public DatosClientes buscarDatosBasicos(ClienteDatosBasicoRequest clienteDatosBasicoRequest)
			throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		
		ClienteDatosBasicosResponse clienteDatosBasicosResponse = new ClienteDatosBasicosResponse();
		String clienteDatosBasicoRequestJSON;
		clienteDatosBasicoRequestJSON = new Gson().toJson(clienteDatosBasicoRequest);
		wsrequest.setBody(clienteDatosBasicoRequestJSON);
		wsrequest.setUrl(urlConsultaDatosBasicos);
		log.info("antes de llamarte WS en buscarDatosBasicos ");
		retorno = wsService.post(wsrequest);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscarDatosBasicos");
				try {
					clienteDatosBasicosResponse  = mapper.jsonToClass(retorno.getBody(), ClienteDatosBasicosResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
				log.info(clienteDatosBasicosResponse.getResultado().getCodigo());
				if(clienteDatosBasicosResponse.getResultado().getCodigo().equals("0000")){
	            	log.info("Respusta codigo 0000 si existe el limite");
	            	return clienteDatosBasicosResponse.getDatosCliente();
	            }else {
	            	return null;
	            }
				
			} else {
				if (retorno.getStatus() == 422) {
					log.info("entro en error 422 en buscarDatosBasicos");
					try {
						Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
						String error = response.getResultado().getDescripcion();
						throw new CustomException(error);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			throw new CustomException("No hubo conexion con el micreoservicio clientesPersonalizados");
			
		}
		return null;
	}

	@Override
	public ClienteResponse listaClientesPaginacion(ClienteRequest clienteRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		ClienteResponse clienteResponse = new ClienteResponse();
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl(urlConsulta);
			
		log.info("antes de llamarte WS en listaClientesPaginacion");
		retorno = wsService.post(wsrequest);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en listaClientesPaginacion");
				try {
					clienteResponse = mapper.jsonToClass(retorno.getBody(), ClienteResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
				log.info(clienteResponse.getResultado().getCodigo());
				if(clienteResponse.getResultado().getCodigo().equals("0000")){
	            	log.info("Respusta codigo 0000 si de clienteResponse");
	            	return clienteResponse;
	            }else {
	            	return null;
	            }
			} else {
				if (retorno.getStatus() == 422) {
					log.info("entro en error 422 en listaClientesPaginacion");
					try {
						Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
						String mensaje = resultado.getDescripcion();
						throw new CustomException(mensaje);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} else {
			throw new CustomException("No hubo conexion con el micreoservicio clientesPersonalizados");
			
		}
		return null;
	}



}
