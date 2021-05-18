package com.bancoexterior.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bancoexterior.app.entities.Moneda;
import com.bancoexterior.app.model.Tasa;
import com.bancoexterior.app.services.IMonedaService;
import com.bancoexterior.app.services.ITasaService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/tasas")
public class TasaController {

	
	@Autowired
	private IMonedaService monedaService;
	
	@Autowired
	private ITasaService tasaService;
	
	
	@GetMapping("/index")
	public String index(Model model) {
		
		List<Tasa> listaTasas = tasaService.buscarTodas();
		
		for (Tasa tasa : listaTasas) {
			log.info("tasa: "+tasa);
		}
		
		model.addAttribute("listaTasas", listaTasas);
		
		return "convenio/tasa/listaTasas";
	}
	
	@GetMapping("/formTasa")
	public String formTasa(Tasa tasa, Model model) {
		
		List<Moneda> listaMonedas = monedaService.buscarTodas();
		
		for (Moneda moneda : listaMonedas) {
			log.info("moneda: "+moneda);
		}
		model.addAttribute("listaMonedas", listaMonedas);
		
		return "convenio/tasa/formTasa";
	}
	
	@PostMapping("/save")
	public String guardar(Tasa tasa, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		log.info("save");
		log.info("tasa: "+tasa);
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				log.info("Ocurrio un error: " + error.getDefaultMessage());
			}
			List<Moneda> listaMonedas = monedaService.buscarTodas();
			
			for (Moneda moneda : listaMonedas) {
				log.info("moneda: "+moneda);
			}
			model.addAttribute("listaMonedas", listaMonedas);
			
			return "convenio/tasa/formTasa";
		}
		redirectAttributes.addFlashAttribute("mensaje", "Registro Guardado");
		return "redirect:/tasas/index";
	}
	
	
	
	
}
