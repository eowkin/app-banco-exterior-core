package com.bancoexterior.app.convenio.apiRest;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.convenio.dto.AcumuladoCompraVentaResponse;
import com.bancoexterior.app.convenio.dto.AcumuladoRequest;
import com.bancoexterior.app.convenio.dto.AcumuladoResponse;
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
	
    @Value("${des.ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${des.SocketTimeout}")
    private int socketTimeout;
    
    @Value("${des.acumulados.urlConsulta}")
    private String urlConsulta;
    
    
    public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }
    
	@Override
	public String consultarAcumulados(AcumuladoRequest acumuladoRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String acumuladoRequestJSON;
		acumuladoRequestJSON = new Gson().toJson(acumuladoRequest);
		log.info("acumuladoRequestJSON: "+acumuladoRequestJSON);
		
		wsrequest.setBody(acumuladoRequestJSON);
		wsrequest.setUrl(urlConsulta);
			
		log.info("antes de llamarte WS en consultarAcumulados");
		retorno = wsService.post(wsrequest);
		log.info("retorno: "+retorno);
		return null;
	}

	@Override
	public AcumuladoResponse consultarAcumuladosDiariosBanco(AcumuladoRequest acumuladoRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String acumuladoRequestJSON;
		AcumuladoResponse acumuladoResponse = new AcumuladoResponse();
		acumuladoRequestJSON = new Gson().toJson(acumuladoRequest);
		log.info("acumuladoRequestJSON: "+acumuladoRequestJSON);
		
		wsrequest.setBody(acumuladoRequestJSON);
		wsrequest.setUrl(urlConsulta);
			
		log.info("antes de llamarte WS en consultarAcumuladosDiariosBanco");
		retorno = wsService.post(wsrequest);
		log.info("retorno: "+retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en consultarAcumuladosDiariosBanco");
	            try {
	            	acumuladoResponse = mapper.jsonToClass(retorno.getBody(), AcumuladoResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("acumuladoResponse: "+acumuladoResponse);
	            log.info(acumuladoResponse.getResultado().getCodigo());
	            return acumuladoResponse;
			}else {
				if(retorno.getStatus() == 422) {
					log.info("entro en error 422 en consultarAcumuladosDiariosBanco");
					try {
						Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
						log.info("response: "+response);
						String mensaje = response.getResultado() .getDescripcion();
						throw new CustomException(mensaje);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else {
			throw new CustomException("No hubo conexion con el micreoservicio acumulados");
		}
		return null;
	}

	@Override
	public AcumuladoCompraVentaResponse consultarAcumuladosCompraVenta(AcumuladoRequest acumuladoRequest)
			throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String acumuladoRequestJSON;
		AcumuladoCompraVentaResponse acumuladoCompraVentaResponse = new AcumuladoCompraVentaResponse();
		acumuladoRequestJSON = new Gson().toJson(acumuladoRequest);
		log.info("acumuladoRequestJSON: "+acumuladoRequestJSON);
		
		wsrequest.setBody(acumuladoRequestJSON);
		wsrequest.setUrl(urlConsulta);
			
		log.info("antes de llamarte WS en consultarAcumuladosCompraVenta");
		retorno = wsService.post(wsrequest);
		log.info("retorno: "+retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info("Respusta codigo 200 en consultarAcumuladosCompraVenta");
	            try {
	            	acumuladoCompraVentaResponse = mapper.jsonToClass(retorno.getBody(), AcumuladoCompraVentaResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("acumuladoCompraVentaResponse: "+acumuladoCompraVentaResponse);
	            log.info(acumuladoCompraVentaResponse.getResultado().getCodigo());
	            return acumuladoCompraVentaResponse;
			}else {
				if(retorno.getStatus() == 422) {
					log.info("entro en error 422 en consultarAcumuladosCompraVenta");
					try {
						Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
						log.info("response: "+response);
						//String mensaje = " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado() .getDescripcion();
						String mensaje = response.getResultado() .getDescripcion();
						throw new CustomException(mensaje);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}else {
			throw new CustomException("No hubo conexion con el micreoservicio acumulados");
		}
		return null;
	}
	
	

}
