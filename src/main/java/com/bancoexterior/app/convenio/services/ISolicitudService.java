package com.bancoexterior.app.convenio.services;

import java.util.List;

import com.bancoexterior.app.convenio.model.Solicitud;

public interface ISolicitudService {

	public List<Solicitud> bucarPorAprobar();
	
	public List<Solicitud> buscarTodas();
	
	public Solicitud findById(String codOperacion);
}
