package com.bancoexterior.app.convenio.services;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bancoexterior.app.convenio.model.LimitesGenerales;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


@Service
public class LimitesGeneralesImpl implements ILimitesGeneralesService{

	private List<LimitesGenerales> listaLimitesGenerales = new ArrayList<>();
	
	String limitesGeneralesJson = "[\r\n"
			+ "    {\r\n"
			+ "        \"codMoneda\": \"EUR\",\r\n"
			+ "        \"tipoTransaccion\": \"V\",\r\n"
			+ "        \"tipoCliente\": \"J\",\r\n"
			+ "        \"montoMin\": 390.50,\r\n"
			+ "        \"montoMax\": 391.50,\r\n"
			+ "        \"montoTope\": 392.50,\r\n"
			+ "        \"montoMensual\": 393.50,\r\n"
			+ "        \"montoDiario\": 394.50,\r\n"
			+ "        \"montoBanco\": 395.50,\r\n"
			+ "        \"codUsuario\": \"E44444\",\r\n"
			+ "        \"flagActivo\": true,\r\n"
			+ "        \"fechaModificacion\": \"2021-05-07\"\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "        \"codMoneda\": \"USD\",\r\n"
			+ "        \"tipoTransaccion\": \"V\",\r\n"
			+ "        \"tipoCliente\": \"N\",\r\n"
			+ "        \"montoMin\": 100.56,\r\n"
			+ "        \"montoMax\": 200.56,\r\n"
			+ "        \"montoTope\": 300.56,\r\n"
			+ "        \"montoMensual\": 400.56,\r\n"
			+ "        \"montoDiario\": 500.56,\r\n"
			+ "        \"montoBanco\": 600.56,\r\n"
			+ "        \"codUsuario\": \"E66666\",\r\n"
			+ "        \"flagActivo\": true,\r\n"
			+ "        \"fechaModificacion\": \"2021-04-27\"\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "        \"codMoneda\": \"USD\",\r\n"
			+ "        \"tipoTransaccion\": \"V\",\r\n"
			+ "        \"tipoCliente\": \"J\",\r\n"
			+ "        \"montoMin\": 1100.56,\r\n"
			+ "        \"montoMax\": 1200.56,\r\n"
			+ "        \"montoTope\": 1300.56,\r\n"
			+ "        \"montoMensual\": 1400.56,\r\n"
			+ "        \"montoDiario\": 1500.56,\r\n"
			+ "        \"montoBanco\": 1600.56,\r\n"
			+ "        \"codUsuario\": \"E44444\",\r\n"
			+ "        \"flagActivo\": true,\r\n"
			+ "        \"fechaModificacion\": \"2021-04-27\"\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "        \"codMoneda\": \"USD\",\r\n"
			+ "        \"tipoTransaccion\": \"C\",\r\n"
			+ "        \"tipoCliente\": \"J\",\r\n"
			+ "        \"montoMin\": 101.50,\r\n"
			+ "        \"montoMax\": 111.50,\r\n"
			+ "        \"montoTope\": 121.50,\r\n"
			+ "        \"montoMensual\": 131.50,\r\n"
			+ "        \"montoDiario\": 141.50,\r\n"
			+ "        \"montoBanco\": 151.50,\r\n"
			+ "        \"codUsuario\": \"E44444\",\r\n"
			+ "        \"flagActivo\": true,\r\n"
			+ "        \"fechaModificacion\": \"2021-04-30\"\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "        \"codMoneda\": \"EUR\",\r\n"
			+ "        \"tipoTransaccion\": \"C\",\r\n"
			+ "        \"tipoCliente\": \"J\",\r\n"
			+ "        \"montoMin\": 190.50,\r\n"
			+ "        \"montoMax\": 191.50,\r\n"
			+ "        \"montoTope\": 192.50,\r\n"
			+ "        \"montoMensual\": 193.50,\r\n"
			+ "        \"montoDiario\": 194.50,\r\n"
			+ "        \"montoBanco\": 195.50,\r\n"
			+ "        \"codUsuario\": \"E44444\",\r\n"
			+ "        \"flagActivo\": false,\r\n"
			+ "        \"fechaModificacion\": \"2021-05-02\"\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "        \"codMoneda\": \"EUR\",\r\n"
			+ "        \"tipoTransaccion\": \"C\",\r\n"
			+ "        \"tipoCliente\": \"N\",\r\n"
			+ "        \"montoMin\": 290.50,\r\n"
			+ "        \"montoMax\": 291.50,\r\n"
			+ "        \"montoTope\": 292.50,\r\n"
			+ "        \"montoMensual\": 293.50,\r\n"
			+ "        \"montoDiario\": 294.50,\r\n"
			+ "        \"montoBanco\": 295.50,\r\n"
			+ "        \"codUsuario\": \"E44444\",\r\n"
			+ "        \"flagActivo\": true,\r\n"
			+ "        \"fechaModificacion\": \"2021-05-02\"\r\n"
			+ "    },\r\n"
			+ "    {\r\n"
			+ "        \"codMoneda\": \"EUR\",\r\n"
			+ "        \"tipoTransaccion\": \"V\",\r\n"
			+ "        \"tipoCliente\": \"N\",\r\n"
			+ "        \"montoMin\": 390.50,\r\n"
			+ "        \"montoMax\": 391.50,\r\n"
			+ "        \"montoTope\": 392.50,\r\n"
			+ "        \"montoMensual\": 393.50,\r\n"
			+ "        \"montoDiario\": 394.50,\r\n"
			+ "        \"montoBanco\": 395.50,\r\n"
			+ "        \"codUsuario\": \"E44444\",\r\n"
			+ "        \"flagActivo\": true,\r\n"
			+ "        \"fechaModificacion\": \"2021-05-02\"\r\n"
			+ "    }\r\n"
			+ "]";
	
	
	
	public LimitesGeneralesImpl() {
		super();
		System.out.println(limitesGeneralesJson);
		Type listType = new TypeToken<ArrayList<LimitesGenerales>>(){}.getType();
		listaLimitesGenerales = new Gson().fromJson(limitesGeneralesJson, listType);
	}

	@Override
	public List<LimitesGenerales> buscarTodas() {
		return listaLimitesGenerales;
	}

}
