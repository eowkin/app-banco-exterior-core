package com.bancoexterior.app.services;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bancoexterior.app.model.LimitesPersonalizados;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


@Service
public class LimitesPersonalizadosServiceImpl implements ILimitesPersonalizadosService{

	private List<LimitesPersonalizados> listaLimitesPersonalizados = new ArrayList<>();
	
	String limitesPersonalizadosJson = "[\r\n"
			+ "\r\n"
			+ "    {\r\n"
			+ "        \"codigoIbs\": \"1234567\",\r\n"
			+ "        \"codMoneda\": \"EUR\",\r\n"
			+ "        \"tipoTransaccion\": \"C\",\r\n"
			+ "        \"montoMin\": 10.50,\r\n"
			+ "        \"montoMax\": 11.50,\r\n"
			+ "        \"montoTope\": 12.50,\r\n"
			+ "        \"montoMensual\": 13.50,\r\n"
			+ "        \"montoDiario\": 14.50,\r\n"
			+ "        \"codUsuario\": \"E55555\",\r\n"
			+ "        \"flagActivo\": true,\r\n"
			+ "        \"fechaModificacion\": \"2021-05-07\"\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "        \"codigoIbs\": \"1234567\",\r\n"
			+ "        \"codMoneda\": \"EUR\",\r\n"
			+ "        \"tipoTransaccion\": \"V\",\r\n"
			+ "        \"montoMin\": 20.50,\r\n"
			+ "        \"montoMax\": 21.50,\r\n"
			+ "        \"montoTope\": 22.50,\r\n"
			+ "        \"montoMensual\": 23.50,\r\n"
			+ "        \"montoDiario\": 24.50,\r\n"
			+ "        \"codUsuario\": \"E55555\",\r\n"
			+ "        \"flagActivo\": true,\r\n"
			+ "        \"fechaModificacion\": \"2021-05-07\"\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "        \"codigoIbs\": \"1234567\",\r\n"
			+ "        \"codMoneda\": \"USD\",\r\n"
			+ "        \"tipoTransaccion\": \"V\",\r\n"
			+ "        \"montoMin\": 120.50,\r\n"
			+ "        \"montoMax\": 121.50,\r\n"
			+ "        \"montoTope\": 122.50,\r\n"
			+ "        \"montoMensual\": 123.50,\r\n"
			+ "        \"montoDiario\": 124.50,\r\n"
			+ "        \"codUsuario\": \"E44444\",\r\n"
			+ "        \"flagActivo\": true,\r\n"
			+ "        \"fechaModificacion\": \"2021-05-07\"\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "        \"codigoIbs\": \"1234567\",\r\n"
			+ "        \"codMoneda\": \"USD\",\r\n"
			+ "        \"tipoTransaccion\": \"C\",\r\n"
			+ "        \"montoMin\": 220.50,\r\n"
			+ "        \"montoMax\": 221.50,\r\n"
			+ "        \"montoTope\": 222.50,\r\n"
			+ "        \"montoMensual\": 223.50,\r\n"
			+ "        \"montoDiario\": 224.50,\r\n"
			+ "        \"codUsuario\": \"E44444\",\r\n"
			+ "        \"flagActivo\": true,\r\n"
			+ "        \"fechaModificacion\": \"2021-05-07\"\r\n"
			+ "    }\r\n"
			+ "\r\n"
			+ "]";
	
	
	
	
	public LimitesPersonalizadosServiceImpl() {
		super();
		System.out.println(limitesPersonalizadosJson);
		Type listType = new TypeToken<ArrayList<LimitesPersonalizados>>(){}.getType();
		listaLimitesPersonalizados = new Gson().fromJson(limitesPersonalizadosJson, listType);
	}




	@Override
	public List<LimitesPersonalizados> buscarTodas() {
		return listaLimitesPersonalizados;
	}

}
