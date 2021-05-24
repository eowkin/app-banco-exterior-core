package com.bancoexterior.app.convenio.repository.restApi;

import java.util.List;

import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.model.Moneda;

public interface IMonedaRepositoryRest {
	
	public List<Moneda> bucarMonedasRestApi(MonedasRequest monedasRequest);

}
