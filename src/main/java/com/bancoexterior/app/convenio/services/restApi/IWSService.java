package com.bancoexterior.app.convenio.services.restApi;

import com.bancoexterior.app.convenio.services.restApi.model.WSRequest;
import com.bancoexterior.app.convenio.services.restApi.model.WSResponse;

public interface  IWSService {
	WSResponse post(WSRequest request);
	WSResponse put(WSRequest request);
}
