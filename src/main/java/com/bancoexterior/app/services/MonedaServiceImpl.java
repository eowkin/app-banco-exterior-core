package com.bancoexterior.app.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;


import java.lang.reflect.Type;

import com.bancoexterior.app.entities.Moneda;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class MonedaServiceImpl implements IMonedaService{

	
	private List<Moneda> listaMoneda = new ArrayList<>();
	

	
	
	String monedasJson  = "[{\r\n"
			+ "            \"codMoneda\": \"EUR\",\r\n"
			+ "            \"descripcion\": \"EURO Europa\",\r\n"
			+ "            \"codAlterno\": \"222\",\r\n"
			+ "            \"flagActivo\": true,\r\n"
			+ "            \"codUsuario\": \"E33333\",\r\n"
			+ "            \"fechaModificacion\": \"2021-05-07\"\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "            \"codMoneda\": \"USD\",\r\n"
			+ "            \"descripcion\": \"Dolar Estadounidense.\",\r\n"
			+ "            \"codAlterno\": \"999\",\r\n"
			+ "            \"flagActivo\": true,\r\n"
			+ "            \"codUsuario\": \"E33333\",\r\n"
			+ "            \"fechaModificacion\": \"2021-05-07\"\r\n"
			+ "        }]";
	
	
	public MonedaServiceImpl() {
		super();
		System.out.println(monedasJson);
		Type listType = new TypeToken<ArrayList<Moneda>>(){}.getType();
		listaMoneda = new Gson().fromJson(monedasJson, listType);
		
		
			 
	}

	@Override
	public List<Moneda> buscarTodas() {
		
		return listaMoneda;
	}

	@Override
	public Moneda findById(String codMoneda) {
		for (Moneda moneda : listaMoneda) {
			if(moneda.getCodMoneda().equals(codMoneda))
				return moneda;
		}
		return null;
	}

	@Override
	public void guardar(Moneda moneda) {
		listaMoneda.add(moneda);
		
	}

}
