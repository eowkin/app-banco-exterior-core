package com.bancoexterior.app.services;

import java.util.List;

import com.bancoexterior.app.model.Solicitud;

public interface ISolicitudService {

	public List<Solicitud> bucarPorAprobar();
	
	public List<Solicitud> buscarTodas();
	
	
}
