package com.bancoexterior.app.convenio.apiRest;

import com.bancoexterior.app.convenio.dto.MovimientosRequest;
import com.bancoexterior.app.convenio.dto.MovimientosResponse;
import com.bancoexterior.app.convenio.exception.CustomException;

public interface IMovimientosApiRest {

	public MovimientosResponse consultarMovimientosPorAprobar(MovimientosRequest movimientosRequest) throws CustomException;
	
	public MovimientosResponse consultarMovimientos(MovimientosRequest movimientosRequest) throws CustomException;
}
