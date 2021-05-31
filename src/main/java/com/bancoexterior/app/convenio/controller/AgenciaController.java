package com.bancoexterior.app.convenio.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bancoexterior.app.convenio.apiRest.IAgenciaServiceApiRest;
import com.bancoexterior.app.convenio.dto.AgenciaRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.Agencia;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/agencias")
public class AgenciaController {

	@Autowired IAgenciaServiceApiRest agenciaServiceApiRest; 
	
	
	
	
	@GetMapping("/index")
	public String index(Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a index agenciasWs");
		
		List<Agencia> listaAgencias = new ArrayList<>();
		AgenciaRequest agenciaRequest = new AgenciaRequest();
		agenciaRequest.setIdUsuario("test");
		agenciaRequest.setIdSesion("20210101121213");
		agenciaRequest.setCodUsuario("E66666");
		agenciaRequest.setCanal("8");
		Agencia agencia = new Agencia();
		agencia.setFlagDivisa(true);
		agenciaRequest.setAgencia(agencia);
		
		try {
			listaAgencias = agenciaServiceApiRest.listaAgencias(agenciaRequest);
			model.addAttribute("listaAgencias", listaAgencias);
			return "convenio/agencia/listaAgencias";
		} catch (CustomException e) {
			
			log.error("error: "+e);
			return "redirect:/";
		}
		
		
	}	
	
	@GetMapping("/activar/{codAgencia}")
	public String activarWs(@PathVariable("codAgencia") String codAgencia, Model model,
			RedirectAttributes redirectAttributes) {
		log.info("activarWs");
		log.info("codAgencia: " + codAgencia);
		
	
		Agencia agenciaEdit = new Agencia(); 
		AgenciaRequest agenciaRequest = new AgenciaRequest();
		agenciaRequest.setIdUsuario("test");
		agenciaRequest.setIdSesion("20210101121213");
		agenciaRequest.setCodUsuario("E66666");
		agenciaRequest.setCanal("8");
		Agencia agencia = new Agencia();
		agencia.setCodAgencia(codAgencia);
		agencia.setFlagDivisa(true);
		agenciaRequest.setAgencia(agencia);
		
		try {
			agenciaEdit = agenciaServiceApiRest.buscarAgencia(agenciaRequest);
			agenciaEdit.setFlagActivo(true);
			agenciaRequest.setAgencia(agenciaEdit);
			String respuesta = agenciaServiceApiRest.actualizar(agenciaRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/agencias/index";
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/agencias/index";
		}
		
		
	}	
	
	
	@GetMapping("/desactivar/{codAgencia}")
	public String desactivarWs(@PathVariable("codAgencia") String codAgencia, Model model,
			RedirectAttributes redirectAttributes) {
		log.info("activarWs");
		log.info("codAgencia: " + codAgencia);
		
		
		Agencia agenciaEdit = new Agencia(); 
		AgenciaRequest agenciaRequest = new AgenciaRequest();
		agenciaRequest.setIdUsuario("test");
		agenciaRequest.setIdSesion("20210101121213");
		agenciaRequest.setCodUsuario("E66666");
		agenciaRequest.setCanal("8");
		Agencia agencia = new Agencia();
		agencia.setCodAgencia(codAgencia);
		agencia.setFlagDivisa(true);
		agenciaRequest.setAgencia(agencia);
		
		try {
			agenciaEdit = agenciaServiceApiRest.buscarAgencia(agenciaRequest);
			agenciaEdit.setFlagActivo(false);
			agenciaRequest.setAgencia(agenciaEdit);
			String respuesta = agenciaServiceApiRest.actualizar(agenciaRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/agencias/index";
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/agencias/index";
		}
		
		
	}
	
	
	@GetMapping("/edit/{codAgencia}")
	public String editarWs(@PathVariable("codAgencia") String codAgencia, 
			Agencia agencia, Model model, RedirectAttributes redirectAttributes) {
		log.info("editarWs");
		log.info("codAgencia: "+codAgencia);
		
		
		Agencia agenciaEdit = new Agencia(); 
		AgenciaRequest agenciaRequest = new AgenciaRequest();
		agenciaRequest.setIdUsuario("test");
		agenciaRequest.setIdSesion("20210101121213");
		agenciaRequest.setCodUsuario("E66666");
		agenciaRequest.setCanal("8");
		Agencia agenciaBuscar = new Agencia();
		agenciaBuscar.setFlagDivisa(true);
		agenciaBuscar.setCodAgencia(agencia.getCodAgencia());
		agenciaRequest.setAgencia(agenciaBuscar);
		
		try {
			agenciaEdit = agenciaServiceApiRest.buscarAgencia(agenciaRequest);
			if(agenciaEdit != null) {
				model.addAttribute("agencia", agenciaEdit);
				return "convenio/agencia/formAgenciaEdit";
			}else {
				redirectAttributes.addFlashAttribute("mensajeError", " Codigo : 0001 descripcion: Operacion Exitosa.La consulta no arrojo resultado.");
				return "redirect:/agencias/index";
			}
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/agencias/index";
		}
		
		
	}
	
	@PostMapping("/guardar")
	public String guardarWs(Agencia agencia, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		log.info("guardarWs");
		log.info("agencia: "+agencia);
		
		
		AgenciaRequest agenciaRequest = new AgenciaRequest();
		agenciaRequest.setIdUsuario("test");
		agenciaRequest.setIdSesion("20210101121213");
		agenciaRequest.setCodUsuario("E66666");
		agenciaRequest.setCanal("8");
		agencia.setFlagActivo(true);
		agencia.setFlagDivisa(true);
		agenciaRequest.setAgencia(agencia);
	
		
		try {
			
			String respuesta = agenciaServiceApiRest.actualizar(agenciaRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/agencias/index";
		} catch (CustomException e) {
			log.error("error: "+e);
			result.addError(new ObjectError("codMoneda", " Codigo :" +e.getMessage()));
			return "convenio/agencia/formAgenciaEdit";
		}
		
	}
	
	
	@GetMapping("/formBuscarAgencia")
	public String fromBuscarAgencia(Agencia agencia, Model model, RedirectAttributes redirectAttributes) {
		log.info("fromBuscarAgencia");
		
		List<Agencia> listaAgencias = new ArrayList<>();
		AgenciaRequest agenciaRequest = new AgenciaRequest();
		agenciaRequest.setIdUsuario("test");
		agenciaRequest.setIdSesion("20210101121213");
		agenciaRequest.setCodUsuario("E66666");
		agenciaRequest.setCanal("8");
		Agencia agenciaBuscar = new Agencia();
		agenciaBuscar.setFlagDivisa(false);
		agenciaRequest.setAgencia(agenciaBuscar);
		
		try {
			listaAgencias = agenciaServiceApiRest.listaAgencias(agenciaRequest);
			model.addAttribute("listaAgencias", listaAgencias);
			return "convenio/agencia/formBuscarAgencia";
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/agencias/index";
		}
		
	}
	
	@GetMapping("/formAgencia")
	public String fromAgencia(Agencia agencia,  Model model) {
		
		return "convenio/agencia/formAgencia";
	}
	
	@GetMapping("/searchCrear")
	public String searchCrear(Agencia agencia, Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a searchCrear clientesPersonalizadosWs");
		log.info(agencia.getCodAgencia());
		
		List<Agencia> listaAgencias = new ArrayList<>();
		Agencia agenciaEdit = new Agencia(); 
		AgenciaRequest agenciaRequest = new AgenciaRequest();
		agenciaRequest.setIdUsuario("test");
		agenciaRequest.setIdSesion("20210101121213");
		agenciaRequest.setCodUsuario("E66666");
		agenciaRequest.setCanal("8");
		Agencia agenciaBuscar = new Agencia();
		agenciaBuscar.setFlagDivisa(false);
		agenciaBuscar.setCodAgencia(agencia.getCodAgencia());
		agenciaRequest.setAgencia(agenciaBuscar);
		
		
		try {
			agenciaEdit = agenciaServiceApiRest.buscarAgencia(agenciaRequest);
			if(agenciaEdit != null) {
				model.addAttribute("agencia", agenciaEdit);
				return "convenio/agencia/formAgencia";
			}else {
				Agencia agenciaBuscarCargar = new Agencia();
				agenciaBuscarCargar.setFlagDivisa(false);
				agenciaRequest.setAgencia(agenciaBuscarCargar);
				listaAgencias = agenciaServiceApiRest.listaAgencias(agenciaRequest);
				model.addAttribute("listaAgencias", listaAgencias);
				model.addAttribute("mensajeError", " Codigo : 0001 descripcion: Operacion Exitosa.La consulta no arrojo resultado.");
				return "convenio/agencia/formBuscarAgencia";
			}
			
			
		} catch (CustomException e) {
			log.error("error: "+e);
			Agencia agenciaBuscarCargar = new Agencia();
			agenciaBuscarCargar.setFlagDivisa(false);
			agenciaRequest.setAgencia(agenciaBuscarCargar);
			try {
				listaAgencias = agenciaServiceApiRest.listaAgencias(agenciaRequest);
				model.addAttribute("listaAgencias", listaAgencias);
				model.addAttribute("mensajeError", e.getMessage());
				return "convenio/agencia/formBuscarAgencia";
			} catch (CustomException e1) {
				// TODO Auto-generated catch block
				log.error("error: "+e1);
				model.addAttribute("mensajeError", e1.getMessage());
				return "convenio/agencia/formBuscarAgencia";
			}
			
		}
		
		
	}	
	
	
	@PostMapping("/save")
	public String saveWs(Agencia agencia, Model model, RedirectAttributes redirectAttributes) {
		log.info("saveWs");
		log.info("agencia: "+agencia);
		
		
		AgenciaRequest agenciaRequest = new AgenciaRequest();
		agenciaRequest.setIdUsuario("test");
		agenciaRequest.setIdSesion("20210101121213");
		agenciaRequest.setCodUsuario("E66666");
		agenciaRequest.setCanal("8");
		agencia.setFlagActivo(true);
		agencia.setFlagDivisa(true);
		agenciaRequest.setAgencia(agencia);
		
		
		
		try {
			String respuesta = agenciaServiceApiRest.crear(agenciaRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/agencias/index";
			
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError",e.getMessage());
			return "redirect:/agencias/index";
			
			//model.addAttribute("mensajeError", e.getMessage());
			//return "convenio/agencia/formBuscarAgencia";
		}
		
		
	}	
	
	
	@GetMapping("/search")
	public String search(@ModelAttribute("agenciaSearch") Agencia agenciaSearch,
			Agencia agencia, Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a search agenciasWs");
		log.info(agenciaSearch.getCodAgencia());
		
		List<Agencia> listaAgencias = new ArrayList<>();
		AgenciaRequest agenciaRequest = new AgenciaRequest();
		agenciaRequest.setIdUsuario("test");
		agenciaRequest.setIdSesion("20210101121213");
		agenciaRequest.setCodUsuario("E66666");
		agenciaRequest.setCanal("8");
		Agencia agenciaBuscar = new Agencia();
		agenciaBuscar.setFlagDivisa(true);
		if(!agenciaSearch.getCodAgencia().equals("")){
			agenciaBuscar.setCodAgencia(agenciaSearch.getCodAgencia());
		}	
		agenciaRequest.setAgencia(agenciaBuscar);
		
		try {
			listaAgencias = agenciaServiceApiRest.listaAgencias(agenciaRequest);
			log.info("lista: "+listaAgencias.isEmpty());
			if(!listaAgencias.isEmpty()) {
				model.addAttribute("listaAgencias", listaAgencias);
				return "convenio/agencia/listaAgencias";
			}else {
				//redirectAttributes.addFlashAttribute("mensajeError", " Codigo : 0001 descripcion: Operacion Exitosa.La consulta no arrojo resultado.");
				model.addAttribute("listaAgencias", listaAgencias);
				model.addAttribute("mensajeError", " Codigo : 0001 descripcion: Operacion Exitosa.La consulta no arrojo resultado.");
				return "convenio/agencia/listaAgencias";
			}
		} catch (CustomException e) {
			log.error("error: "+e);
			model.addAttribute("listaAgencias", listaAgencias);
			model.addAttribute("mensajeError", e.getMessage());
			return "convenio/agencia/listaAgencias";
		}
		
		
		
		
		
		
		
	}	
	
	
	
		
	
	
	@ModelAttribute
	public void setGenericos(Model model) {
		Agencia agenciaSearch = new Agencia();
		model.addAttribute("agenciaSearch", agenciaSearch);
	}
}
