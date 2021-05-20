package com.bancoexterior.app.convenio.services;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bancoexterior.app.convenio.model.Tasa;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class TasaServiceImpl implements ITasaService{
	
	private List<Tasa> listaTasas = new ArrayList<>();

	String tasasJson = "[\r\n"
			+ "        {\r\n"
			+ "            \"codMonedaOrigen\": \"USD\",\r\n"
			+ "            \"codMonedaDestino\": \"EUR\",\r\n"
			+ "            \"tipoOperacion\": 1,\r\n"
			+ "            \"montoTasaCompra\": 6655.12340000,\r\n"
			+ "            \"montoTasaVenta\": 1666.56700000,\r\n"
			+ "            \"codUsuario\": \"TESS\",\r\n"
			+ "            \"fechaModificacion\": \"2021-05-09\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"codMonedaOrigen\": \"USD\",\r\n"
			+ "            \"codMonedaDestino\": \"EUR\",\r\n"
			+ "            \"tipoOperacion\": 0,\r\n"
			+ "            \"montoTasaCompra\": 666.12340000,\r\n"
			+ "            \"montoTasaVenta\": 11.56780000,\r\n"
			+ "            \"codUsuario\": \"TESS\",\r\n"
			+ "            \"fechaModificacion\": \"2021-05-09\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"codMonedaOrigen\": \"VES\",\r\n"
			+ "            \"codMonedaDestino\": \"USD\",\r\n"
			+ "            \"tipoOperacion\": 0,\r\n"
			+ "            \"montoTasaCompra\": 666.12340000,\r\n"
			+ "            \"montoTasaVenta\": 11.56780000,\r\n"
			+ "            \"codUsuario\": \"TESTSS\",\r\n"
			+ "            \"fechaModificacion\": \"2021-05-09\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"codMonedaOrigen\": \"VES\",\r\n"
			+ "            \"codMonedaDestino\": \"EUR\",\r\n"
			+ "            \"tipoOperacion\": 1,\r\n"
			+ "            \"montoTasaCompra\": 6655.12340000,\r\n"
			+ "            \"montoTasaVenta\": 1666.56780000,\r\n"
			+ "            \"codUsuario\": \"TESS\",\r\n"
			+ "            \"fechaModificacion\": \"2021-05-09\"\r\n"
			+ "        }\r\n"
			+ "    ]";
	
	
	
	
	public TasaServiceImpl() {
		super();
		System.out.println(tasasJson);
		Type listType = new TypeToken<ArrayList<Tasa>>(){}.getType();
		listaTasas = new Gson().fromJson(tasasJson, listType);
	}




	@Override
	public List<Tasa> buscarTodas() {
		
		return listaTasas;
	}

}
