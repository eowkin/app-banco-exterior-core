package com.bancoexterior.app.convenio.services;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bancoexterior.app.convenio.model.Agencia;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AgenciaServiceImpl implements IAgenciaService{

	private List<Agencia> listaAgencias = new ArrayList<>();
	
	String agenciasJson = "\r\n"
			+ "[\r\n"
			+ " {\r\n"
			+ "   \"codUsuario\": \"E4354\",\r\n"
			+ "   \"fechaModificacion\": \"2021-04-24\",\r\n"
			+ "   \"flagActivo\": true,\r\n"
			+ "   \"codAgencia\": \"037\",\r\n"
			+ "   \"nombreAgencia\": \"ACARIGUA\"\r\n"
			+ "  },\r\n"
			+ "  {\r\n"
			+ "   \"codUsuario\": \"E4354\",\r\n"
			+ "   \"fechaModificacion\": \"2021-04-24\",\r\n"
			+ "   \"flagActivo\": true,\r\n"
			+ "   \"codAgencia\": \"014\",\r\n"
			+ "   \"nombreAgencia\": \"ACARIGUA LLANO MALL\"\r\n"
			+ "  },\r\n"
			+ "  {\r\n"
			+ "   \"codUsuario\": \"e4354\",\r\n"
			+ "   \"fechaModificacion\": \"2021-04-02\",\r\n"
			+ "   \"flagActivo\": true,\r\n"
			+ "   \"codAgencia\": \"123\",\r\n"
			+ "   \"nombreAgencia\": \"Agencia Prueba\"\r\n"
			+ "  }\r\n"
			+ "]";
	
	
	
	
	
	
	public AgenciaServiceImpl() {
		super();
		log.info(agenciasJson);
		Type listType = new TypeToken<ArrayList<Agencia>>(){}.getType();
		listaAgencias = new Gson().fromJson(agenciasJson, listType);
	}






	@Override
	public List<Agencia> buscarTodas() {

		return listaAgencias;
	}

}
