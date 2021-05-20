package com.bancoexterior.app.convenio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bancoexterior.app.convenio.model.LimitesGenerales;
import com.bancoexterior.app.convenio.model.Moneda;
import com.bancoexterior.app.convenio.services.ILimitesGeneralesService;
import com.bancoexterior.app.convenio.services.IMonedaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/limitesGenerales")
public class LimitesGeneralesController {

	@Autowired
	private IMonedaService monedaService;
	
	@Autowired
	private ILimitesGeneralesService limitesGeneralesService;
	
	@GetMapping("/index")
	public String index(Model model) {
		
		List<LimitesGenerales> listaLimitesGenerales = limitesGeneralesService.buscarTodas();
		
		for (LimitesGenerales limitesGenerales : listaLimitesGenerales) {
			log.info("limitesGenerales: "+limitesGenerales);
		}
		
		model.addAttribute("listaLimitesGenerales", listaLimitesGenerales);
		return "convenio/limitesGenerales/listaLimitesGenerales";
	}
	
	@GetMapping("/formLimitesGenerales")
	public String formLimitesGenerales(Model model) {
		
		List<Moneda> listaMonedas = monedaService.buscarTodas();
		
		for (Moneda moneda : listaMonedas) {
			log.info("moneda: "+moneda);
		}
		model.addAttribute("listaMonedas", listaMonedas);
		
		return "convenio/limitesGenerales/formLimitesGenerales";
	}
	
}
