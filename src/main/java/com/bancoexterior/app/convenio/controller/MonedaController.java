package com.bancoexterior.app.convenio.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bancoexterior.app.convenio.model.Moneda;
import com.bancoexterior.app.convenio.services.IMonedaService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/monedas")
public class MonedaController {

	@Autowired
	private IMonedaService monedaService;
	
	@GetMapping("/index")
	public String listaMonedas(Model model) {
		log.info("si me llamo a index");
		List<Moneda> listMonedas = monedaService.buscarTodas();
		for (Moneda moneda : listMonedas) {
			log.info("moneda: "+ moneda);
		}
		
		model.addAttribute("listMonedas", listMonedas);
		return "convenio/moneda/listaMonedas";
	}
	
	@GetMapping("/formMoneda")
	public String formMoneda(Moneda moneda, Model model) {
		
		return "convenio/moneda/formMoneda";
	}	
	
	
	@PostMapping("/save")
	public String save(Moneda moneda, RedirectAttributes redirectAttributes) {
		
		log.info("save");
		log.info("moneda: "+moneda);
		moneda.setFlagActivo(true);
		moneda.setCodUsuario("E44444");
		moneda.setFechaModificacion(new Date());
		monedaService.guardar(moneda);
		redirectAttributes.addFlashAttribute("mensaje", "Registro Guardado");
		return "redirect:/monedas/index";  
	}
	
	
	
	@GetMapping("/edit/{codMoneda}")
	public String editar(@PathVariable("codMoneda") String codMoneda, Moneda moneda, Model model) {
		
		log.info(codMoneda);
		Moneda monedaEdit = monedaService.findById(codMoneda);
		model.addAttribute("moneda", monedaEdit);
		log.info("monedaEdit: "+monedaEdit);
		return "convenio/moneda/formMonedaEdit";
		
	}
	
	
	@PostMapping("/guardar")
	public String guardar(Moneda moneda, RedirectAttributes redirectAttributes) {
		
		log.info("guardar");
		log.info("moneda: "+moneda);
		moneda.setFlagActivo(true);
		moneda.setCodUsuario("E44444");
		moneda.setFechaModificacion(new Date());
		//monedaService.guardar(moneda);
		redirectAttributes.addFlashAttribute("mensaje", "Registro actualizado");
		return "redirect:/monedas/index";  
	}
	
	
	
	
	
	

	
}
