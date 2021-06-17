package com.bancoexterior.app.convenio.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.convenio.dto.AprobarRechazarRequest;
import com.bancoexterior.app.convenio.dto.AprobarRechazarResponse;
import com.bancoexterior.app.convenio.dto.MovimientosRequest;
import com.bancoexterior.app.convenio.dto.MovimientosResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.Movimiento;
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
public class MovimientosApiRestImpl implements IMovimientosApiRest{

	@Autowired
	private IWSService wsService;
    
    @Autowired 
	private Mapper mapper;
    
    @Value("${des.ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${des.SocketTimeout}")
    private int socketTimeout;
    
    @Value("${des.movimientos.consultarMovimientosPorAprobar}")
    private String urlConsultarMovimientosPorAprobar;
    
    @Value("${des.movimientos.consultarMovimientosPorAprobarVenta}")
    private String urlConsultarMovimientosPorAprobarVenta;
    
    @Value("${des.movimientos.consultarMovimientos}")
    private String urlConsultarMovimientos;
    
    @Value("${des.movimientos.compra.actualizar}")
    private String urlActualizarMovimientosCompra;
    
    @Value("${des.movimientos.venta.actualizar}")
    private String urlActualizarMovimientosVenta;
    
    
    public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }

	
	@Override
	public MovimientosResponse consultarMovimientosPorAprobar(MovimientosRequest movimientosRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String movimientosRequestJSON;
		MovimientosResponse movimientosResponse = new MovimientosResponse();
		movimientosRequestJSON = new Gson().toJson(movimientosRequest);
		wsrequest.setBody(movimientosRequestJSON);
		//wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/consultasmovimientos?sort=codMoneda,desc&sort=tasaCliente,asc&sort=montoDivisa,asc");
		wsrequest.setUrl(urlConsultarMovimientosPorAprobar);
			
		log.info("antes de llamarte WS en consultar");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				try {
	            	movimientosResponse = mapper.jsonToClass(retorno.getBody(), MovimientosResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            return movimientosResponse;
			}else {
				if(retorno.getStatus() == 422) {
					try {
						Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
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

	@Override
	public MovimientosResponse consultarMovimientosPorAprobarVenta(MovimientosRequest movimientosRequest)
			throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String movimientosRequestJSON;
		MovimientosResponse movimientosResponse = new MovimientosResponse();
		movimientosRequestJSON = new Gson().toJson(movimientosRequest);
		wsrequest.setBody(movimientosRequestJSON);
		//wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/consultasmovimientos?sort=codMoneda,desc&sort=tasaCliente,desc&sort=montoDivisa,desc");
		wsrequest.setUrl(urlConsultarMovimientosPorAprobarVenta);
		log.info("antes de llamarte WS en consultar");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				try {
	            	movimientosResponse = mapper.jsonToClass(retorno.getBody(), MovimientosResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            return movimientosResponse;
			}else {
				if(retorno.getStatus() == 422) {
					try {
						Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
						String mensaje = " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado() .getDescripcion();
						throw new CustomException(mensaje);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else {
			throw new CustomException("No hubo conexion con el micreoservicio movimientos");
		}
		return null;
	}
	
	
	
	@Override
	public MovimientosResponse consultarMovimientos(MovimientosRequest movimientosRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String movimientosRequestJSON;
		MovimientosResponse movimientosResponse = new MovimientosResponse();
		movimientosRequestJSON = new Gson().toJson(movimientosRequest);
		wsrequest.setBody(movimientosRequestJSON);
		//wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/consultasmovimientos");
		wsrequest.setUrl(urlConsultarMovimientos);
		log.info("antes de llamarte WS en consultar");
		retorno = wsService.post(wsrequest);
		log.info("retorno: "+retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				try {
	            	movimientosResponse = mapper.jsonToClass(retorno.getBody(), MovimientosResponse.class);
	            	
				} catch (IOException e) {
					e.printStackTrace();
				}
	            return movimientosResponse;
			}else {
				if(retorno.getStatus() == 422) {
					try {
						Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
						String mensaje = " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado() .getDescripcion();
						throw new CustomException(mensaje);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else {
			throw new CustomException("No hubo conexion con el micreoservicio movimientos");
		}
		return null;
	}

	@Override
	public String rechazarCompra(AprobarRechazarRequest aprobarRechazarRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		Response response = new Response();
		Resultado resultado = new Resultado();
		String respuesta;
		String error;
		AprobarRechazarResponse aprobarRechazarResponse = new AprobarRechazarResponse();
		String aprobarRechazarRequestJSON;
		aprobarRechazarRequestJSON = new Gson().toJson(aprobarRechazarRequest);
		wsrequest.setBody(aprobarRechazarRequestJSON);								 
		//wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/aprobacionescompras");
		wsrequest.setUrl(urlActualizarMovimientosCompra);
		log.info("antes de llamarte WS en rechazarCompraSolicitud");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				try {
					aprobarRechazarResponse = mapper.jsonToClass(retorno.getBody(), AprobarRechazarResponse.class);
					if(aprobarRechazarResponse.getResultado().getCodigo().equals("0000")){
		            	resultado = aprobarRechazarResponse.getResultado();
		            	respuesta =" Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion();
						return respuesta;
		            }else {
		            	resultado = aprobarRechazarResponse.getResultado();
		            	respuesta =" Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion();
						return respuesta;
		            }
				} catch (IOException e) {
					e.printStackTrace();
				}
				    
				
				
				
			}else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400) {
					try {
						response = mapper.jsonToClass(retorno.getBody(), Response.class);
						error = " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion();
						throw new CustomException(error);
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}else {
					if (retorno.getStatus() == 500) {
						try {
							aprobarRechazarResponse = mapper.jsonToClass(retorno.getBody(), AprobarRechazarResponse.class);
						} catch (IOException e) {
							e.printStackTrace();
						}
						resultado = aprobarRechazarResponse.getResultado();
						error = " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion();
						throw new CustomException(error);
							
						
						
					}
				}
			}
		}else {
			throw new CustomException("No hubo conexion con el micreoservicio movimientos");
		}
		return null;
	}

	@Override
	public String aprobarCompra(AprobarRechazarRequest aprobarRechazarRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		Response response = new Response();
		Resultado resultado = new Resultado();
		String respuesta;
		String error;
		AprobarRechazarResponse aprobarRechazarResponse = new AprobarRechazarResponse();
		String aprobarRechazarRequestJSON;
		aprobarRechazarRequestJSON = new Gson().toJson(aprobarRechazarRequest);
		wsrequest.setBody(aprobarRechazarRequestJSON);
		wsrequest.setUrl(urlActualizarMovimientosCompra);
		log.info("antes de llamarte WS en aprobarCompraSolicitud");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				try {
					aprobarRechazarResponse = mapper.jsonToClass(retorno.getBody(), AprobarRechazarResponse.class);
					if(aprobarRechazarResponse.getResultado().getCodigo().equals("0000")){
		            	
		            	resultado = aprobarRechazarResponse.getResultado();
		            	respuesta =" Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion();
						return respuesta;
		            }else {
		            	resultado = aprobarRechazarResponse.getResultado();
		            	respuesta =" Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion();
						return respuesta;
		            }
				} catch (IOException e) {
					e.printStackTrace();
				}
				    
				
				
				
			}else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400) {
					try {
						response = mapper.jsonToClass(retorno.getBody(), Response.class);
						error = " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion();
						throw new CustomException(error);
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}else {
					if (retorno.getStatus() == 500) {
						try {
							aprobarRechazarResponse = mapper.jsonToClass(retorno.getBody(), AprobarRechazarResponse.class);
						} catch (IOException e) {
							e.printStackTrace();
						}
						resultado = aprobarRechazarResponse.getResultado();
						error = " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion();
						throw new CustomException(error);
							
						
						
					}
				}
			}
		}else {
			throw new CustomException("No hubo conexion con el micreoservicio movimientos");
		}
		return null;
	}

	@Override
	public String rechazarVenta(AprobarRechazarRequest aprobarRechazarRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		Response response = new Response();
		Resultado resultado = new Resultado();
		String respuesta;
		String error;
		AprobarRechazarResponse aprobarRechazarResponse = new AprobarRechazarResponse();
		String aprobarRechazarRequestJSON;
		aprobarRechazarRequestJSON = new Gson().toJson(aprobarRechazarRequest);
		wsrequest.setBody(aprobarRechazarRequestJSON);
		//wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/aprobacionesventas");
		wsrequest.setUrl(urlActualizarMovimientosVenta);
		
		log.info("antes de llamarte WS en rechazarVentaSolicitud");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				try {
					aprobarRechazarResponse = mapper.jsonToClass(retorno.getBody(), AprobarRechazarResponse.class);
					if(aprobarRechazarResponse.getResultado().getCodigo().equals("0000")){
		            	
		            	resultado = aprobarRechazarResponse.getResultado();
		            	respuesta =" Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion();
						return respuesta;
		            }else {
		            	resultado = aprobarRechazarResponse.getResultado();
		            	respuesta =" Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion();
						return respuesta;
		            }
				} catch (IOException e) {
					e.printStackTrace();
				}
				    
				
				
				
			}else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400) {
					try {
						response = mapper.jsonToClass(retorno.getBody(), Response.class);
						error = " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion();
						throw new CustomException(error);
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}else {
					if (retorno.getStatus() == 500) {
						try {
							aprobarRechazarResponse = mapper.jsonToClass(retorno.getBody(), AprobarRechazarResponse.class);
						} catch (IOException e) {
							e.printStackTrace();
						}
						resultado = aprobarRechazarResponse.getResultado();
						error = " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion();
						throw new CustomException(error);
							
						
						
					}
				}
			}
		}else {
			throw new CustomException("No hubo conexion con el micreoservicio movimientos");
		}
		return null;
	}

	@Override
	public String aprobarVenta(AprobarRechazarRequest aprobarRechazarRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		Response response = new Response();
		Resultado resultado = new Resultado();
		String respuesta;
		String error;
		AprobarRechazarResponse aprobarRechazarResponse = new AprobarRechazarResponse();
		String aprobarRechazarRequestJSON;
		aprobarRechazarRequestJSON = new Gson().toJson(aprobarRechazarRequest);
		wsrequest.setBody(aprobarRechazarRequestJSON);										 
		//wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/aprobacionesventas");
		wsrequest.setUrl(urlActualizarMovimientosVenta);
		log.info("antes de llamarte WS en aprobarVentaSolicitud");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				try {
					aprobarRechazarResponse = mapper.jsonToClass(retorno.getBody(), AprobarRechazarResponse.class);
					if(aprobarRechazarResponse.getResultado().getCodigo().equals("0000")){
		            	
		            	resultado = aprobarRechazarResponse.getResultado();
		            	respuesta =" Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion();
						return respuesta;
		            }else {
		            	resultado = aprobarRechazarResponse.getResultado();
		            	respuesta =" Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion();
						return respuesta;
		            }
				} catch (IOException e) {
					e.printStackTrace();
				}
				 
		     }else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400) {
					try {
						response = mapper.jsonToClass(retorno.getBody(), Response.class);
						error = " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion();
						throw new CustomException(error);
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}else {
					if (retorno.getStatus() == 500) {
						try {
							aprobarRechazarResponse = mapper.jsonToClass(retorno.getBody(), AprobarRechazarResponse.class);
						} catch (IOException e) {
							e.printStackTrace();
						}
						resultado = aprobarRechazarResponse.getResultado();
						error = " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion();
						throw new CustomException(error);
							
						
						
					}
				}
			}
		}else {
			throw new CustomException("No hubo conexion con el micreoservicio movimientos");
		}
		return null;
	}

	@Override
	public List<Movimiento> getListaMovimientos(MovimientosRequest movimientosRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String movimientosRequestJSON;
		MovimientosResponse movimientosResponse = new MovimientosResponse();
		movimientosRequestJSON = new Gson().toJson(movimientosRequest);
		wsrequest.setBody(movimientosRequestJSON);
		//wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/consultasmovimientos");
		wsrequest.setUrl(urlConsultarMovimientos);
		log.info("antes de llamarte WS buscarListaMovimientos");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				try {
	            	movimientosResponse = mapper.jsonToClass(retorno.getBody(), MovimientosResponse.class);
	            	
				} catch (IOException e) {
					e.printStackTrace();
				}
	            return movimientosResponse.getMovimientos();
	       	}else {
				if(retorno.getStatus() == 422) {
					try {
						Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
						String mensaje = " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado() .getDescripcion();
						throw new CustomException(mensaje);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else {
			throw new CustomException("No hubo conexion con el micreoservicio movimientos");
		}
		return null;
	}

	

}
