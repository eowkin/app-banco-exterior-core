package com.bancoexterior.app.convenio.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bancoexterior.app.convenio.apiRest.IMonedaServiceApiRest;
import com.bancoexterior.app.convenio.apiRest.ITasaServiceApiRest;
import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.dto.TasaRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.Moneda;
import com.bancoexterior.app.convenio.model.Tasa;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/tasas")
public class TasaController {

	@Autowired
	private ITasaServiceApiRest tasaServiceApiRest; 
	
	@Autowired
	private IMonedaServiceApiRest monedaServiceApiRest;
	
	@GetMapping("/index")
	public String index(Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a index listaTasasWs");
		
		List<Tasa> listaTasas = new ArrayList<>();
		
		TasaRequest tasaRequest = new TasaRequest();
		tasaRequest.setIdUsuario("test");
		tasaRequest.setIdSesion("20210101121213");
		tasaRequest.setCodUsuario("E66666");
		tasaRequest.setCanal("8");
		Tasa tasa = new Tasa();
		tasaRequest.setTasa(tasa);
	
		try {
			listaTasas = tasaServiceApiRest.listaTasas(tasaRequest);
			model.addAttribute("listaTasas", listaTasas);
			return "convenio/tasa/listaTasas";
		} catch (CustomException e) {
			
			log.error("error: "+e);
			return "redirect:/";
		}
		
		
		
	}	
	
	@GetMapping("/edit/{codMonedaOrigen}/{codMonedaDestino}/{tipoOperacion}")
	public String editarWs(@PathVariable("codMonedaOrigen") String codMonedaOrigen, 
			@PathVariable("codMonedaDestino") String codMonedaDestino, @PathVariable("tipoOperacion") Integer tipoOperacion,
			Tasa tasa, Model model, RedirectAttributes redirectAttributes) {
		log.info("editarWs");
		log.info("codMonedaOrigen: "+codMonedaOrigen);
		log.info("codMonedaDestino: "+codMonedaDestino);
		log.info("tipoOperacion: "+tipoOperacion);
		
		
		Tasa tasaEdit = new Tasa();
		
		
		
		TasaRequest tasaRequest = new TasaRequest();
		tasaRequest.setIdUsuario("test");
		tasaRequest.setIdSesion("20210101121213");
		tasaRequest.setCodUsuario("E66666");
		tasaRequest.setCanal("8");
		Tasa tasaBuscar = new Tasa();
		tasaBuscar.setCodMonedaOrigen(codMonedaOrigen);
		tasaBuscar.setCodMonedaDestino(codMonedaDestino);
		tasaBuscar.setTipoOperacion(tipoOperacion);
		tasaRequest.setTasa(tasaBuscar);
		
		try {
			tasaEdit = tasaServiceApiRest.buscarTasa(tasaRequest);
			if(tasaEdit != null) {
				model.addAttribute("tasa", tasaEdit);
            	return "convenio/tasa/formTasaEdit";
			}else {
				redirectAttributes.addFlashAttribute("mensajeError", " Codigo : 0001 descripcion: Operacion Exitosa.La consulta no arrojo resultado.");
				return "redirect:/tasas/index";
			}
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/tasas/index";
		}
		
		
		
	}
	
	
	@PostMapping("/guardar")
	public String guardarWs(Tasa tasa, BindingResult result,  RedirectAttributes redirectAttributes) {
		log.info("guardarWs");
		log.info("tasa: "+tasa);
		
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				log.info("Ocurrio un error: " + error.getDefaultMessage());
			}
		
			return "convenio/tasa/formTasaEdit";
		}
		
		TasaRequest tasaRequest = new TasaRequest();
		tasaRequest.setIdUsuario("test");
		tasaRequest.setIdSesion("20210101121213");
		tasaRequest.setCodUsuario("E66666");
		tasaRequest.setCanal("8");
		tasaRequest.setTasa(tasa);
		
		try {
			
			String respuesta = tasaServiceApiRest.actualizar(tasaRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/tasas/index";
		} catch (CustomException e) {
			log.error("error: "+e);
			result.addError(new ObjectError("codMoneda", " Codigo :" +e.getMessage()));
			return "convenio/tasa/formTasaEdit";
		}
		
		
		
	}
	
	@GetMapping("/formTasa")
	public String formTasa(Tasa tasa, Model model, RedirectAttributes redirectAttributes) {
		
		List<Moneda> listaMonedas = new ArrayList<>();
		
		MonedasRequest monedasRequest = new MonedasRequest();
		monedasRequest.setIdUsuario("test");
		monedasRequest.setIdSesion("20210101121213");
		monedasRequest.setCodUsuario("E66666");
		monedasRequest.setCanal("8");
		Moneda moneda = new Moneda();
		moneda.setFlagActivo(true);
		monedasRequest.setMoneda(moneda);
		
		try {
			listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
			model.addAttribute("listaMonedas", listaMonedas);
			return "convenio/tasa/formTasa";
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/tasas/index";
		}
		
		
		
	}	
	
	@PostMapping("/save")
	public String saveWs(Tasa tasa, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		log.info("save");
		log.info("tasa: "+tasa);
		List<Moneda> listaMonedas = new ArrayList<>();
		MonedasRequest monedasRequest = new MonedasRequest();
		monedasRequest.setIdUsuario("test");
		monedasRequest.setIdSesion("20210101121213");
		monedasRequest.setCodUsuario("E66666");
		monedasRequest.setCanal("8");
		Moneda moneda = new Moneda();
		moneda.setFlagActivo(true);
		monedasRequest.setMoneda(moneda);
		
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				log.info("Ocurrio un error: " + error.getDefaultMessage());
			}
			try {
				listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
				model.addAttribute("listaMonedas", listaMonedas);
				return "convenio/tasa/formTasa";
			} catch (CustomException e) {
				log.error("error: "+e);
				result.addError(new ObjectError("codMoneda", " Codigo :" +e.getMessage()));
				return "convenio/tasa/formTasa";
			}
		}
		
		TasaRequest tasaRequest = new TasaRequest();
		tasaRequest.setIdUsuario("test");
		tasaRequest.setIdSesion("20210101121213");
		tasaRequest.setCodUsuario("E66666");
		tasaRequest.setCanal("8");
		tasaRequest.setTasa(tasa);
		
		try {
			String respuesta = tasaServiceApiRest.crear(tasaRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/tasas/index";
		} catch (CustomException e) {
			log.error("error: "+e);
			try {
				listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
				model.addAttribute("listaMonedas", listaMonedas);
				result.addError(new ObjectError("codMoneda", " Codigo :" +e.getMessage()));
				return "convenio/tasa/formTasa";
			} catch (CustomException e1) {
				log.error("error: "+e1);
				result.addError(new ObjectError("codMoneda", " Codigo :" +e1.getMessage()));
				return "convenio/tasa/formTasa";
			}
			
		}
		
		
	}	
	
	
	
	
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dataFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dataFormat, false));
	}
	
	
}
