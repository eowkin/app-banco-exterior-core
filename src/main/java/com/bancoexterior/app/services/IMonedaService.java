package com.bancoexterior.app.services;

import java.util.List;

import com.bancoexterior.app.entities.Moneda;



public interface IMonedaService {

	public List<Moneda> buscarTodas();
	
	public Moneda findById(String codMoneda);
	
	public void guardar(Moneda moneda);
}
