package com.bancoexterior.app.convenio.services;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;


import com.bancoexterior.app.convenio.model.ClientesPersonalizados;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClientesPersonalizadosServiceImpl implements IClientesPersonalizadosService{

	private List<ClientesPersonalizados> listaClientesPersonalizados = new ArrayList<>();
	
	String clientesPersonalizadosJson = "[\r\n"
			+ "{\r\n"
			+ "    \"codigoIbs\": \"64583920\",\r\n"
			+ "    \"nroIdCliente\": \"J-987654321\",\r\n"
			+ "    \"nombreRif\": \"nombre-prueba\",\r\n"
			+ "    \"codUsuario\": \"E66666\",\r\n"
			+ "    \"flagActivo\": true,\r\n"
			+ "    \"fechaModificacion\": \"2021-05-01\"\r\n"
			+ "},\r\n"
			+ "{\r\n"
			+ "    \"codigoIbs\": \"7654321\",\r\n"
			+ "    \"nroIdCliente\": \"v-31640514\",\r\n"
			+ "    \"nombreRif\": \"nombre-prueba\",\r\n"
			+ "    \"codUsuario\": \"E55555\",\r\n"
			+ "    \"flagActivo\": true,\r\n"
			+ "    \"fechaModificacion\": \"2021-05-01\"\r\n"
			+ "},\r\n"
			+ "{\r\n"
			+ "    \"codigoIbs\": \"109364859\",\r\n"
			+ "    \"nroIdCliente\": \"J-87e622134\",\r\n"
			+ "    \"nombreRif\": \"nombre-prueba\",\r\n"
			+ "    \"codUsuario\": \"E66666\",\r\n"
			+ "    \"flagActivo\": true,\r\n"
			+ "    \"fechaModificacion\": \"2021-05-01\"\r\n"
			+ "},\r\n"
			+ "{\r\n"
			+ "    \"codigoIbs\": \"1234567\",\r\n"
			+ "    \"nroIdCliente\": \"v-14405316\",\r\n"
			+ "    \"nombreRif\": \"nombre-prueba\",\r\n"
			+ "    \"codUsuario\": \"E55555\",\r\n"
			+ "    \"flagActivo\": true,\r\n"
			+ "    \"fechaModificacion\": \"2021-05-07\"\r\n"
			+ "},\r\n"
			+ "{\r\n"
			+ "    \"codigoIbs\": \"63243242\",\r\n"
			+ "    \"nroIdCliente\": \"J-sajhs292y22\",\r\n"
			+ "    \"nombreRif\": \"nombre-prueba\",\r\n"
			+ "    \"codUsuario\": \"E55555\",\r\n"
			+ "    \"flagActivo\": true,\r\n"
			+ "    \"fechaModificacion\": \"2021-05-07\"\r\n"
			+ "},\r\n"
			+ "{\r\n"
			+ "    \"codigoIbs\": \"48392058\",\r\n"
			+ "    \"nroIdCliente\": \"J-123456789\",\r\n"
			+ "    \"nombreRif\": \"nombre-prueba\",\r\n"
			+ "    \"codUsuario\": \"test\",\r\n"
			+ "    \"flagActivo\": false,\r\n"
			+ "    \"fechaModificacion\": \"2021-05-07\"\r\n"
			+ "}\r\n"
			+ "]";
	
	
	
	
	public ClientesPersonalizadosServiceImpl() {
		super();
		log.info(clientesPersonalizadosJson);
		Type listType = new TypeToken<ArrayList<ClientesPersonalizados>>(){}.getType();
		listaClientesPersonalizados = new Gson().fromJson(clientesPersonalizadosJson, listType);
		
	}




	@Override
	public List<ClientesPersonalizados> buscarTodos() {
		return listaClientesPersonalizados;
	}

}
