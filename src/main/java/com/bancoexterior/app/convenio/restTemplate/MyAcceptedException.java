package com.bancoexterior.app.convenio.restTemplate;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;



@ResponseStatus(HttpStatus.ACCEPTED)
public class MyAcceptedException extends RuntimeException{
	private static final long serialVersionUID = 7626621872124381457L;
	public MyAcceptedException(String msg)
	{
		super(msg);
	}
}
