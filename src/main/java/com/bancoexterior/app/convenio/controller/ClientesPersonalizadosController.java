package com.bancoexterior.app.convenio.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bancoexterior.app.convenio.apiRest.IClientePersonalizadoServiceApiRest;
import com.bancoexterior.app.convenio.dto.ClienteRequest;
import com.bancoexterior.app.convenio.dto.ClienteResponse;
import com.bancoexterior.app.convenio.dto.LimitesPersonalizadosResponse;
import com.bancoexterior.app.convenio.model.ClientesPersonalizados;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.convenio.services.IClientesPersonalizadosService;
import com.bancoexterior.app.convenio.services.restApi.model.WSResponse;
import com.bancoexterior.app.util.Mapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/clientesPersonalizados")
public class ClientesPersonalizadosController {

	@Autowired
	private IClientePersonalizadoServiceApiRest clientePersonalizadoServiceApiRest; 
	
	@Autowired 
	private Mapper mapper;

	
	@Autowired
	private IClientesPersonalizadosService clientePersonalizadosService;
	
	
	@GetMapping("/index")
	public String index(Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a index clientesPersonalizadosWs");
		
		List<ClientesPersonalizados> listaClientesPersonalizados = new ArrayList<>();
		
		ClienteRequest clienteRequest = new ClienteRequest(); 
		clienteRequest.setIdUsuario("test");
		clienteRequest.setIdSesion("20210101121213");
		clienteRequest.setCodUsuario("E66666");
		clienteRequest.setCanal("8");
		ClientesPersonalizados clientesPersonalizados = new ClientesPersonalizados(); 
		clienteRequest.setCliente(clientesPersonalizados);
		
		ClienteResponse clienteResponse = new ClienteResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = clientePersonalizadoServiceApiRest.consultarWs(clienteRequest);
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista clientes personalizados");
	            try {
					clienteResponse= mapper.jsonToClass(respuesta.getBody(), ClienteResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("clienteResponse: "+clienteResponse);
	            log.info(clienteResponse.getResultado().getCodigo());
	            listaClientesPersonalizados = clienteResponse.getListaClientes();
	            model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
	    		return "convenio/clientesPersonalizados/listaClientesPersonalizados";
			}else {
				if (respuesta.getStatus() == 422) {
					log.info("entro en error 422");
					try {
						resultado = mapper.jsonToClass(respuesta.getBody(), Resultado.class);
						log.info("resultado: "+resultado);
					} catch (IOException e) {
						e.printStackTrace();
					}
					redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
					return "convenio/clientesPersonalizados/listaClientesPersonalizados";
				}
			}
		}
		
		return "convenio/clientesPersonalizados/listaClientesPersonalizados";
	}
	
	@GetMapping("/formClientePersonalizado")
	public String formClientePersonalizado(Model model) {
		
		return "convenio/clientesPersonalizados/formClientesPersonalizados";
	}
}
