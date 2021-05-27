package com.bancoexterior.app.convenio.apiRest;

import java.util.List;

import com.bancoexterior.app.convenio.dto.ClienteRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.ClientesPersonalizados;
import com.bancoexterior.app.convenio.services.restApi.model.WSResponse;


public interface IClientePersonalizadoServiceApiRest {

	public WSResponse consultarWs(ClienteRequest clienteRequest);
	
	public List<ClientesPersonalizados> listaClientesPersonalizados(ClienteRequest clienteRequest) throws CustomException;
	
	public ClientesPersonalizados buscarClientesPersonalizados(ClienteRequest clienteRequest) throws CustomException;
	
	public String actualizar(ClienteRequest clienteRequest) throws CustomException;
	
	public String crear(ClienteRequest clienteRequest) throws CustomException;
}
