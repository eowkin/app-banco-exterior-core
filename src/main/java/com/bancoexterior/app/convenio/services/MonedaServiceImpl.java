package com.bancoexterior.app.convenio.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.lang.reflect.Type;

import com.bancoexterior.app.convenio.controller.MonedaController;
import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.model.Moneda;
import com.bancoexterior.app.convenio.repository.restApi.IMonedaRepositoryRest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import jdk.internal.org.jline.utils.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MonedaServiceImpl implements IMonedaService{

	
	@Autowired
	private IMonedaRepositoryRest repoRest;
	
	
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

	@Override
	public List<Moneda> consultaMonedasApiRest(MonedasRequest monedasRequest){

		
		//log.info(monedasRequest);
		//int valor = 1/0;
		repoRest.bucarMonedasRestApi(monedasRequest);
		return null;
	}

	@Override
	public List<Moneda> consultaMonedasApi(MonedasRequest MonedasRequest) {
		// TODO Auto-generated method stub
		return null;
	}

}
