package com.bancoexterior.app.convenio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bancoexterior.app.convenio.model.ClientesPersonalizados;
import com.bancoexterior.app.convenio.services.IClientesPersonalizadosService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/clientesPersonalizados")
public class ClientesPersonalizadosController {

	
	@Autowired
	private IClientesPersonalizadosService clientePersonalizadosService;
	
	
	@GetMapping("/index")
	public String index(Model model) {
		
		List<ClientesPersonalizados> listaClientesPersonalizados = clientePersonalizadosService.buscarTodos();
		for (ClientesPersonalizados clientesPersonalizados : listaClientesPersonalizados) {
			log.info("clientesPersonalizados: "+clientesPersonalizados);
		}
		model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
		
		return "convenio/clientesPersonalizados/listaClientesPersonalizados";
	}
	
	@GetMapping("/formClientePersonalizado")
	public String formClientePersonalizado(Model model) {
		
		return "convenio/clientesPersonalizados/formClientesPersonalizados";
	}
}
