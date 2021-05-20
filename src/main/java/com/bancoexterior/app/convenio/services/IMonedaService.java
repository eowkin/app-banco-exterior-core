package com.bancoexterior.app.convenio.services;

import java.util.List;

import com.bancoexterior.app.convenio.model.Moneda;





public interface IMonedaService {

	public List<Moneda> buscarTodas();
	
	public Moneda findById(String codMoneda);
	
	public void guardar(Moneda moneda);
}
