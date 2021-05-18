package com.bancoexterior.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bancoexterior.app.entities.Moneda;
import com.bancoexterior.app.model.ClientesPersonalizados;
import com.bancoexterior.app.model.LimitesPersonalizados;

import com.bancoexterior.app.services.IClientesPersonalizadosService;
import com.bancoexterior.app.services.ILimitesPersonalizadosService;
import com.bancoexterior.app.services.IMonedaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/limitesPersonalizados")
public class LimitesPersonalizadosController {

	@Autowired
	private IClientesPersonalizadosService clientesPersonalizadosService;
	
	@Autowired
	private IMonedaService monedaService;
	
	
	@Autowired
	private ILimitesPersonalizadosService limitesPersonalizadosService;
	
	@GetMapping("/index")
	public String index(Model model) {
		
		List<LimitesPersonalizados> listaLimitesPersonalizados = limitesPersonalizadosService.buscarTodas();
		for (LimitesPersonalizados limitesPersonalizados : listaLimitesPersonalizados) {
			log.info("limitesPersonalizados: "+limitesPersonalizados);
		}		
		
		model.addAttribute("listaLimitesPersonalizados", listaLimitesPersonalizados);
		
		return "convenio/limitesPersonalizados/listaLimitesPersonalizados";
	}
	
	@GetMapping("/formLimitesPersonalizados")
	public String formClientePersonalizado(Model model) {
		
		List<ClientesPersonalizados> listaClientesPersonalizados = clientesPersonalizadosService.buscarTodos();
		
		for (ClientesPersonalizados clientesPersonalizados : listaClientesPersonalizados) {
			log.info("clientesPersonalizados: "+clientesPersonalizados);
		}
		
		List<Moneda> listaMonedas = monedaService.buscarTodas();
		for (Moneda moneda : listaMonedas) {
			log.info("moneda: "+moneda);
		}
		
		model.addAttribute("listaMonedas", listaMonedas);
		model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
		return "convenio/limitesPersonalizados/formLimitesPersonalizados";
	}
	
}
