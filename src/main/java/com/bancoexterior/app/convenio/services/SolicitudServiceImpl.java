package com.bancoexterior.app.convenio.services;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bancoexterior.app.convenio.model.Solicitud;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class SolicitudServiceImpl implements ISolicitudService{

	private List<Solicitud> listaSolicitudesPorAprobar = new ArrayList<>();
	
	private List<Solicitud> listaSolicitudesTodas = new ArrayList<>();
	
	
	
	
	String solicitudesPorAprobar = "[{\r\n"
			+ "                   \"codOperacion\": \"CA-20210429201040-0A980B2BB9\",\r\n"
			+ "                   \"fechaOperacion\": \"2021-04-29\",\r\n"
			+ "                   \"codigoIbs\": \"12345678\",\r\n"
			+ "                   \"nroIdCliente\": \"J12345678\",\r\n"
			+ "                   \"cuentaNacional\": \"01150010261005153603\",\r\n"
			+ "                   \"cuentaDivisa\": \"01150010261005153603\",\r\n"
			+ "                   \"tipoTransaccion\": \"C\",\r\n"
			+ "                   \"codMoneda\": \"USD\",\r\n"
			+ "                   \"montoDivisa\": 2.00,\r\n"
			+ "                   \"montoBs\": 24.66,\r\n"
			+ "                   \"tasaCliente\": 12.33,\r\n"
			+ "                   \"referenciaDebito\": \"---Por Aprobar---\",\r\n"
			+ "                   \"referenciaCredito\": \"---Por Aprobar---\",\r\n"
			+ "                   \"descripcion\": \"prueba\",\r\n"
			+ "                   \"estatus\": 0\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "    \"codOperacion\": \"CA-20210429200959-1BFACA61B3\",\r\n"
			+ "    \"fechaOperacion\": \"2021-04-29\",\r\n"
			+ "    \"codigoIbs\": \"12345678\",\r\n"
			+ "    \"nroIdCliente\": \"J12345678\",\r\n"
			+ "    \"cuentaNacional\": \"01150010261005153603\",\r\n"
			+ "    \"cuentaDivisa\": \"01150010261005153603\",\r\n"
			+ "    \"tipoTransaccion\": \"C\",\r\n"
			+ "    \"codMoneda\": \"USD\",\r\n"
			+ "    \"montoDivisa\": 2.00,\r\n"
			+ "    \"montoBs\": 24.66,\r\n"
			+ "    \"tasaCliente\": 12.33,\r\n"
			+ "    \"referenciaDebito\": \"---Por Aprobar---\",\r\n"
			+ "    \"referenciaCredito\": \"---Por Aprobar---\",\r\n"
			+ "    \"descripcion\": \"prueba\",\r\n"
			+ "    \"estatus\": 0\r\n"
			+ " }]";
	
	
	String solicitudesTodasJson = "[{\r\n"
			+ "\"codOperacion\": \"CA-20210429201139-45117DD2D9\",\r\n"
			+ "\"fechaOperacion\": \"2021-04-29\",\r\n"
			+ "\"codigoIbs\": \"12345678\",\r\n"
			+ "\"nroIdCliente\": \"J12345678\",\r\n"
			+ "\"cuentaNacional\": \"01150010261005153603\",\r\n"
			+ "\"cuentaDivisa\": \"01150010261005153603\",\r\n"
			+ "\"tipoTransaccion\": \"C\",\r\n"
			+ "\"codMoneda\": \"USD\",\r\n"
			+ "\"montoDivisa\": 2.00,\r\n"
			+ "\"montoBs\": 24.66,\r\n"
			+ "\"tasaCliente\": 12.33,\r\n"
			+ "\"referenciaDebito\": \"0000000258904\",\r\n"
			+ "\"referenciaCredito\": \"0000000258904\",\r\n"
			+ "\"descripcion\": \"---Prueba---\",\r\n"
			+ "\"estatus\": 1\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "\"codOperacion\": \"CA-20210429201040-0A980B2BB9\",\r\n"
			+ "\"fechaOperacion\": \"2021-04-29\",\r\n"
			+ "\"codigoIbs\": \"12345678\",\r\n"
			+ "\"nroIdCliente\": \"J12345678\",\r\n"
			+ "\"cuentaNacional\": \"01150010261005153603\",\r\n"
			+ "\"cuentaDivisa\": \"01150010261005153603\",\r\n"
			+ "\"tipoTransaccion\": \"C\",\r\n"
			+ "\"codMoneda\": \"USD\",\r\n"
			+ "\"montoDivisa\": 2.00,\r\n"
			+ "\"montoBs\": 24.66,\r\n"
			+ "\"tasaCliente\": 12.33,\r\n"
			+ "\"referenciaDebito\": \"0000000258903\",\r\n"
			+ "\"referenciaCredito\": \"0000000258903\",\r\n"
			+ "\"descripcion\": \"---Prueba---\",\r\n"
			+ "\"estatus\": 1\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "\"codOperacion\": \"CA-20210429200959-1BFACA61B3\",\r\n"
			+ "\"fechaOperacion\": \"2021-04-29\",\r\n"
			+ "\"codigoIbs\": \"12345678\",\r\n"
			+ "\"nroIdCliente\": \"J12345678\",\r\n"
			+ "\"cuentaNacional\": \"01150010261005153603\",\r\n"
			+ "\"cuentaDivisa\": \"01150010261005153603\",\r\n"
			+ "\"tipoTransaccion\": \"C\",\r\n"
			+ "\"codMoneda\": \"USD\",\r\n"
			+ "\"montoDivisa\": 2.00,\r\n"
			+ "\"montoBs\": 24.66,\r\n"
			+ "\"tasaCliente\": 12.33,\r\n"
			+ "\"referenciaDebito\": \"0000000258902\",\r\n"
			+ "\"referenciaCredito\": \"0000000258902\",\r\n"
			+ "\"descripcion\": \"---Prueba---\",\r\n"
			+ "\"estatus\": 1\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "\"codOperacion\": \"CA-20210429194313-F39699BC4F\",\r\n"
			+ "\"fechaOperacion\": \"2021-04-29\",\r\n"
			+ "\"codigoIbs\": \"12345678\",\r\n"
			+ "\"nroIdCliente\": \"J12345678\",\r\n"
			+ "\"cuentaNacional\": \"01150010261005153603\",\r\n"
			+ "\"cuentaDivisa\": \"01150010261005153603\",\r\n"
			+ "\"tipoTransaccion\": \"C\",\r\n"
			+ "\"codMoneda\": \"USD\",\r\n"
			+ "\"montoDivisa\": 2.00,\r\n"
			+ "\"montoBs\": 24.66,\r\n"
			+ "\"tasaCliente\": 12.33,\r\n"
			+ "\"referenciaDebito\": \"0000000258901\",\r\n"
			+ "\"referenciaCredito\": \"0000000258901\",\r\n"
			+ "\"descripcion\": \"---Prueba---\",\r\n"
			+ "\"estatus\": 1\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "\"codOperacion\": \"CA-20210429194313-F39699BC4F\",\r\n"
			+ "\"fechaOperacion\": \"2021-04-29\",\r\n"
			+ "\"codigoIbs\": \"12345678\",\r\n"
			+ "\"nroIdCliente\": \"J12345678\",\r\n"
			+ "\"cuentaNacional\": \"01150010261005153603\",\r\n"
			+ "\"cuentaDivisa\": \"01150010261005153603\",\r\n"
			+ "\"tipoTransaccion\": \"C\",\r\n"
			+ "\"codMoneda\": \"USD\",\r\n"
			+ "\"montoDivisa\": 2.00,\r\n"
			+ "\"montoBs\": 24.66,\r\n"
			+ "\"tasaCliente\": 12.33,\r\n"
			+ "\"referenciaDebito\": \"0000000258901\",\r\n"
			+ "\"referenciaCredito\": \"0000000258901\",\r\n"
			+ "\"descripcion\": \"---Prueba---\",\r\n"
			+ "\"estatus\": 2\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "\"codOperacion\": \"CA-20210429194313-F39699BC4F\",\r\n"
			+ "\"fechaOperacion\": \"2021-04-29\",\r\n"
			+ "\"codigoIbs\": \"12345678\",\r\n"
			+ "\"nroIdCliente\": \"J12345678\",\r\n"
			+ "\"cuentaNacional\": \"01150010261005153603\",\r\n"
			+ "\"cuentaDivisa\": \"01150010261005153603\",\r\n"
			+ "\"tipoTransaccion\": \"C\",\r\n"
			+ "\"codMoneda\": \"USD\",\r\n"
			+ "\"montoDivisa\": 2.00,\r\n"
			+ "\"montoBs\": 24.66,\r\n"
			+ "\"tasaCliente\": 12.33,\r\n"
			+ "\"referenciaDebito\": \"---Rechazada---\",\r\n"
			+ "\"referenciaCredito\": \"---Rechazada---\",\r\n"
			+ "\"descripcion\": \"---Prueba---\",\r\n"
			+ "\"estatus\": 3\r\n"
			+ "        },\r\n"
			+ "        {\r\n"
			+ "\"codOperacion\": \"CA-20210429194313-F39699BC4F\",\r\n"
			+ "\"fechaOperacion\": \"2021-04-29\",\r\n"
			+ "\"codigoIbs\": \"12345678\",\r\n"
			+ "\"nroIdCliente\": \"J12345678\",\r\n"
			+ "\"cuentaNacional\": \"01150010261005153603\",\r\n"
			+ "\"cuentaDivisa\": \"01150010261005153603\",\r\n"
			+ "\"tipoTransaccion\": \"C\",\r\n"
			+ "\"codMoneda\": \"USD\",\r\n"
			+ "\"montoDivisa\": 2.00,\r\n"
			+ "\"montoBs\": 24.66,\r\n"
			+ "\"tasaCliente\": 12.33,\r\n"
			+ "\"referenciaDebito\": \"---Rechazada---\",\r\n"
			+ "\"referenciaCredito\": \"---Rechazada---\",\r\n"
			+ "\"descripcion\": \"---Prueba---\",\r\n"
			+ "\"estatus\": 4\r\n"
			+ "  }\r\n"
			+ "\r\n"
			+ "\r\n"
			+ "]";
	
	
	
	
	
	
	public SolicitudServiceImpl() {
		super();
		
		
		System.out.println(solicitudesPorAprobar);
		Type listType = new TypeToken<ArrayList<Solicitud>>(){}.getType();
		listaSolicitudesPorAprobar = new Gson().fromJson(solicitudesPorAprobar, listType);
		
		System.out.println(solicitudesTodasJson);
		Type listType1 = new TypeToken<ArrayList<Solicitud>>(){}.getType();
		listaSolicitudesTodas = new Gson().fromJson(solicitudesTodasJson, listType1);
		
	}

	@Override
	public List<Solicitud> bucarPorAprobar() {
		return listaSolicitudesPorAprobar;
	}

	@Override
	public List<Solicitud> buscarTodas() {
		return listaSolicitudesTodas;
	}

	@Override
	public Solicitud findById(String codOperacion) {
		for (Solicitud solicitud : listaSolicitudesPorAprobar) {
			if(solicitud.getCodOperacion().equals(codOperacion)) {
				return solicitud;
			}
		}
		return null;
	}

}
