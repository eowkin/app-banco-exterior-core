package com.bancoexterior.app.convenio.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bancoexterior.app.convenio.apiRest.IClientePersonalizadoServiceApiRest;
import com.bancoexterior.app.convenio.dto.ClienteRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.ClientesPersonalizados;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/clientesPersonalizados")
public class ClientesPersonalizadosController {

	@Autowired
	private IClientePersonalizadoServiceApiRest clientePersonalizadoServiceApiRest;

	
	
	
	@GetMapping("/index")
	public String index(Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a index clientesPersonalizadosWs");

		

		ClienteRequest clienteRequest = new ClienteRequest();
		clienteRequest.setIdUsuario("test");
		clienteRequest.setIdSesion("20210101121213");
		clienteRequest.setCodUsuario("E66666");
		clienteRequest.setCanal("8");
		ClientesPersonalizados clientesPersonalizados = new ClientesPersonalizados();
		clienteRequest.setCliente(clientesPersonalizados);

		try {
			List<ClientesPersonalizados> listaClientesPersonalizados = clientePersonalizadoServiceApiRest.listaClientesPersonalizados(clienteRequest);
			model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
			return "convenio/clientesPersonalizados/listaClientesPersonalizados";
		} catch (CustomException e) {
			log.error("error: "+e);
			return "redirect:/";
		}
		
		
		
	}	
	
	@GetMapping("/activar/{codigoIbs}")
	public String activarWs(@PathVariable("codigoIbs") String codigoIbs, Model model,
			RedirectAttributes redirectAttributes) {
		log.info("activarWs");
		log.info("codigoIbs: " + codigoIbs);

		ClientesPersonalizados clientesPersonalizadosEdit = new ClientesPersonalizados();

		ClienteRequest clienteRequest = new ClienteRequest();
		clienteRequest.setIdUsuario("test");
		clienteRequest.setIdSesion("20210101121213");
		clienteRequest.setCodUsuario("E66666");
		clienteRequest.setCanal("8");
		ClientesPersonalizados clientesPersonalizados = new ClientesPersonalizados();
		clientesPersonalizados.setCodigoIbs(codigoIbs);
		clienteRequest.setCliente(clientesPersonalizados);
		
		try {
			clientesPersonalizadosEdit = clientePersonalizadoServiceApiRest.buscarClientesPersonalizados(clienteRequest);
			clientesPersonalizadosEdit.setFlagActivo(true);
			clienteRequest.setCliente(clientesPersonalizadosEdit);
			String respuesta = clientePersonalizadoServiceApiRest.actualizar(clienteRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/clientesPersonalizados/index";
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/clientesPersonalizados/index";
		}
		
	}
	
	@GetMapping("/desactivar/{codigoIbs}")
	public String desactivarWs(@PathVariable("codigoIbs") String codigoIbs, Model model,
			RedirectAttributes redirectAttributes) {
		log.info("activarWs");
		log.info("codigoIbs: " + codigoIbs);

		ClientesPersonalizados clientesPersonalizadosEdit = new ClientesPersonalizados();

		ClienteRequest clienteRequest = new ClienteRequest();
		clienteRequest.setIdUsuario("test");
		clienteRequest.setIdSesion("20210101121213");
		clienteRequest.setCodUsuario("E66666");
		clienteRequest.setCanal("8");
		ClientesPersonalizados clientesPersonalizados = new ClientesPersonalizados();
		clientesPersonalizados.setCodigoIbs(codigoIbs);
		clienteRequest.setCliente(clientesPersonalizados);
		
		try {
			clientesPersonalizadosEdit = clientePersonalizadoServiceApiRest.buscarClientesPersonalizados(clienteRequest);
			clientesPersonalizadosEdit.setFlagActivo(false);
			clienteRequest.setCliente(clientesPersonalizadosEdit);
			String respuesta = clientePersonalizadoServiceApiRest.actualizar(clienteRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/clientesPersonalizados/index";
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/clientesPersonalizados/index";
		}
		
	}
	
	
	@GetMapping("/search")
	public String search(
			@ModelAttribute("clientesPersonalizadosSearch") ClientesPersonalizados clientesPersonalizadosSearch,
			Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a search clientesPersonalizadosWs");
		log.info(clientesPersonalizadosSearch.getCodigoIbs());

		List<ClientesPersonalizados> listaClientesPersonalizados = new ArrayList<>();

		ClienteRequest clienteRequest = new ClienteRequest();
		clienteRequest.setIdUsuario("test");
		clienteRequest.setIdSesion("20210101121213");
		clienteRequest.setCodUsuario("E66666");
		clienteRequest.setCanal("8");
		ClientesPersonalizados clientesPersonalizados = new ClientesPersonalizados();
		if (!clientesPersonalizadosSearch.getCodigoIbs().equals(""))
			clientesPersonalizados.setCodigoIbs(clientesPersonalizadosSearch.getCodigoIbs());
		clienteRequest.setCliente(clientesPersonalizados);
		
		try {
			listaClientesPersonalizados = clientePersonalizadoServiceApiRest.listaClientesPersonalizados(clienteRequest);
			log.info("lista: "+listaClientesPersonalizados.isEmpty());
			
			if(!listaClientesPersonalizados.isEmpty()) {
				model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
				return "convenio/clientesPersonalizados/listaClientesPersonalizados";
			}else {
				//redirectAttributes.addFlashAttribute("mensajeError", " Codigo : 0001 descripcion: Operacion Exitosa.La consulta no arrojo resultado.");
				model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
				model.addAttribute("mensajeError", " Codigo : 0001 descripcion: Operacion Exitosa.La consulta no arrojo resultado.");
				return "convenio/clientesPersonalizados/listaClientesPersonalizados";
			}
			
		} catch (CustomException e) {
			
			log.error("error: "+e);
			model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
			model.addAttribute("mensajeError", e.getMessage());
			return "convenio/clientesPersonalizados/listaClientesPersonalizados";
		}
		
		
	}	
	
	@GetMapping("/formClientePersonalizado")
	public String formClientePersonalizado(Model model) {

		return "convenio/clientesPersonalizados/formClientesPersonalizados";
	}
	
	
	

	@ModelAttribute
	public void setGenericos(Model model) {
		ClientesPersonalizados clientesPersonalizadosSearch = new ClientesPersonalizados();
		model.addAttribute("clientesPersonalizadosSearch", clientesPersonalizadosSearch);
	}
}
