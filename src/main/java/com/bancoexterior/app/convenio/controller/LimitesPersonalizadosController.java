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

import com.bancoexterior.app.convenio.apiRest.IClientePersonalizadoServiceApiRest;
import com.bancoexterior.app.convenio.apiRest.ILimitesPersonalizadosServiceApiRest;
import com.bancoexterior.app.convenio.apiRest.IMonedaServiceApiRest;
import com.bancoexterior.app.convenio.dto.ClienteRequest;
import com.bancoexterior.app.convenio.dto.LimitesPersonalizadosRequest;
import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.ClientesPersonalizados;
import com.bancoexterior.app.convenio.model.LimitesPersonalizados;
import com.bancoexterior.app.convenio.model.Moneda;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/limitesPersonalizados")
public class LimitesPersonalizadosController {

	@Autowired
	private ILimitesPersonalizadosServiceApiRest limitesPersonalizadosServiceApiRest; 
	
	@Autowired
	private IClientePersonalizadoServiceApiRest clientePersonalizadoServiceApiRest;
	
	@Autowired
	private IMonedaServiceApiRest monedaServiceApiRest; 
	
	
	
	
	@GetMapping("/index")
	public String index(Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a index listaLimitesPersonalizadosWs");
		List<LimitesPersonalizados> listaLimitesPersonalizados = new ArrayList<>();
		
		LimitesPersonalizadosRequest limitesPersonalizadosRequest = new LimitesPersonalizadosRequest();
		limitesPersonalizadosRequest.setIdUsuario("test");
		limitesPersonalizadosRequest.setIdSesion("20210101121213");
		limitesPersonalizadosRequest.setCodUsuario("E66666");
		limitesPersonalizadosRequest.setCanal("8");
		LimitesPersonalizados limiteCliente = new LimitesPersonalizados();
		//limiteCliente.setFlagActivo(false);
		limitesPersonalizadosRequest.setLimiteCliente(limiteCliente);
		
		try {
			listaLimitesPersonalizados = limitesPersonalizadosServiceApiRest.listaLimitesPersonalizados(limitesPersonalizadosRequest);
			model.addAttribute("listaLimitesPersonalizados", listaLimitesPersonalizados);
			return "convenio/limitesPersonalizados/listaLimitesPersonalizados";
		} catch (CustomException e) {
			
			log.error("error: "+e);
			return "redirect:/";
		}
		
		
		
	}
	
	
	@GetMapping("/activar/{codigoIbs}/{codMoneda}/{tipoTransaccion}")
	public String activarWs(@PathVariable("codigoIbs") String codigoIbs, @PathVariable("codMoneda") String codMoneda, 
			@PathVariable("tipoTransaccion") String tipoTransaccion,LimitesPersonalizados limitesPersonalizados,Model model, RedirectAttributes redirectAttributes) {
		log.info("activarWs");
		log.info(codigoIbs);
		log.info(codMoneda);
		log.info(tipoTransaccion);
		
		LimitesPersonalizados limitesPersonalizadosEdit = new LimitesPersonalizados();
		
		LimitesPersonalizadosRequest limitesPersonalizadosRequest = new LimitesPersonalizadosRequest();
		limitesPersonalizadosRequest.setIdUsuario("test");
		limitesPersonalizadosRequest.setIdSesion("20210101121213");
		limitesPersonalizadosRequest.setCodUsuario("E66666");
		limitesPersonalizadosRequest.setCanal("8");
		LimitesPersonalizados limitesP = new LimitesPersonalizados();
		limitesP.setCodigoIbs(codigoIbs);
		limitesP.setCodMoneda(codMoneda);
		limitesP.setTipoTransaccion(tipoTransaccion);
		limitesPersonalizadosRequest.setLimiteCliente(limitesP);
		
		try {
			limitesPersonalizadosEdit = limitesPersonalizadosServiceApiRest.buscarLimitesPersonalizados(limitesPersonalizadosRequest);
			limitesPersonalizadosEdit.setFlagActivo(true);
			limitesPersonalizadosRequest.setLimiteCliente(limitesPersonalizadosEdit);
			String respuesta = limitesPersonalizadosServiceApiRest.actualizar(limitesPersonalizadosRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/limitesPersonalizados/index";
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/limitesPersonalizados/index";
		}
	
		
	}
	
	@GetMapping("/desactivar/{codigoIbs}/{codMoneda}/{tipoTransaccion}")
	public String desactivarWs(@PathVariable("codigoIbs") String codigoIbs, @PathVariable("codMoneda") String codMoneda, 
			@PathVariable("tipoTransaccion") String tipoTransaccion,LimitesPersonalizados limitesPersonalizados,Model model, RedirectAttributes redirectAttributes) {
		log.info("activarWs");
		log.info(codigoIbs);
		log.info(codMoneda);
		log.info(tipoTransaccion);
		
		LimitesPersonalizados limitesPersonalizadosEdit = new LimitesPersonalizados();
		
		LimitesPersonalizadosRequest limitesPersonalizadosRequest = new LimitesPersonalizadosRequest();
		limitesPersonalizadosRequest.setIdUsuario("test");
		limitesPersonalizadosRequest.setIdSesion("20210101121213");
		limitesPersonalizadosRequest.setCodUsuario("E66666");
		limitesPersonalizadosRequest.setCanal("8");
		LimitesPersonalizados limitesP = new LimitesPersonalizados();
		limitesP.setCodigoIbs(codigoIbs);
		limitesP.setCodMoneda(codMoneda);
		limitesP.setTipoTransaccion(tipoTransaccion);
		limitesPersonalizadosRequest.setLimiteCliente(limitesP);
		
		try {
			limitesPersonalizadosEdit = limitesPersonalizadosServiceApiRest.buscarLimitesPersonalizados(limitesPersonalizadosRequest);
			limitesPersonalizadosEdit.setFlagActivo(false);
			limitesPersonalizadosRequest.setLimiteCliente(limitesPersonalizadosEdit);
			String respuesta = limitesPersonalizadosServiceApiRest.actualizar(limitesPersonalizadosRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/limitesPersonalizados/index";
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/limitesPersonalizados/index";
		}
	
		
	}
	
	
	@GetMapping("/edit/{codigoIbs}/{codMoneda}/{tipoTransaccion}")
	public String editarWs(@PathVariable("codigoIbs") String codigoIbs, @PathVariable("codMoneda") String codMoneda, 
			@PathVariable("tipoTransaccion") String tipoTransaccion,LimitesPersonalizados limitesPersonalizados,Model model, RedirectAttributes redirectAttributes) {
		log.info("editarWs");
		log.info(codigoIbs);
		log.info(codMoneda);
		log.info(tipoTransaccion);
		
		LimitesPersonalizados limitesPersonalizadosEdit = new LimitesPersonalizados();
		
		
		
		LimitesPersonalizadosRequest limitesPersonalizadosRequest = new LimitesPersonalizadosRequest();
		limitesPersonalizadosRequest.setIdUsuario("test");
		limitesPersonalizadosRequest.setIdSesion("20210101121213");
		limitesPersonalizadosRequest.setCodUsuario("E66666");
		limitesPersonalizadosRequest.setCanal("8");
		LimitesPersonalizados limitesP = new LimitesPersonalizados();
		limitesP.setCodigoIbs(codigoIbs);
		limitesP.setCodMoneda(codMoneda);
		limitesP.setTipoTransaccion(tipoTransaccion);
		limitesPersonalizadosRequest.setLimiteCliente(limitesP);
	
		try {
			limitesPersonalizadosEdit = limitesPersonalizadosServiceApiRest.buscarLimitesPersonalizados(limitesPersonalizadosRequest);
			if(limitesPersonalizadosEdit != null) {
				model.addAttribute("limitesPersonalizados", limitesPersonalizadosEdit);
				return "convenio/limitesPersonalizados/formLimitesPersonalizadosEdit";
			}else {
				redirectAttributes.addFlashAttribute("mensajeError", " Codigo : 0001 descripcion: Operacion Exitosa.La consulta no arrojo resultado.");
				return "redirect:/limitesPersonalizados/index";
			}
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/limitesPersonalizados/index";
		}
		
		
	}
	
	
	@PostMapping("/guardar")
	public String guardarWs(LimitesPersonalizados limitesPersonalizados, BindingResult result,  RedirectAttributes redirectAttributes) {
		log.info("guardarWs");
		log.info("limitesPersonalizados", limitesPersonalizados);
		
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				log.info("Ocurrio un error: " + error.getDefaultMessage());
			}
		
			return "convenio/limitesPersonalizados/formLimitesPersonalizadosEdit";
		}
		
		LimitesPersonalizadosRequest limitesPersonalizadosRequest = new LimitesPersonalizadosRequest();
		limitesPersonalizadosRequest.setIdUsuario("test");
		limitesPersonalizadosRequest.setIdSesion("20210101121213");
		limitesPersonalizadosRequest.setCodUsuario("E66666");
		limitesPersonalizadosRequest.setCanal("8");
		limitesPersonalizadosRequest.setLimiteCliente(limitesPersonalizados);
		
		try {
			
			String respuesta = limitesPersonalizadosServiceApiRest.actualizar(limitesPersonalizadosRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/limitesPersonalizados/index";
		} catch (CustomException e) {
			log.error("error: "+e);
			result.addError(new ObjectError("codMoneda", " Codigo :" +e.getMessage()));
			return "convenio/limitesPersonalizados/formLimitesPersonalizadosEdit";
		}
	
		
	}
	
	
	@GetMapping("/formLimitesPersonalizados")
	public String formClientePersonalizado(LimitesPersonalizados limitesPersonalizados,  Model model, RedirectAttributes redirectAttributes) {
		log.info("formClientePersonalizado");
		
		List<ClientesPersonalizados> listaClientesPersonalizados = new ArrayList<>();
		List<Moneda> listaMonedas = new ArrayList<>();
		
		
		ClienteRequest clienteRequest = new ClienteRequest(); 
		clienteRequest.setIdUsuario("test");
		clienteRequest.setIdSesion("20210101121213");
		clienteRequest.setCodUsuario("E66666");
		clienteRequest.setCanal("8");
		ClientesPersonalizados clientesPersonalizados = new ClientesPersonalizados();
		clientesPersonalizados.setFlagActivo(true);
		clienteRequest.setCliente(clientesPersonalizados);
		
		MonedasRequest monedasRequest = new MonedasRequest();
		monedasRequest.setIdUsuario("test");
		monedasRequest.setIdSesion("20210101121213");
		monedasRequest.setCodUsuario("E66666");
		monedasRequest.setCanal("8");
		Moneda moneda = new Moneda();
		moneda.setFlagActivo(true);
		monedasRequest.setMoneda(moneda);
		
		try {
			listaClientesPersonalizados = clientePersonalizadoServiceApiRest.listaClientesPersonalizados(clienteRequest);
			model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
			listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
			model.addAttribute("listaMonedas", listaMonedas);
    		return "convenio/limitesPersonalizados/formLimitesPersonalizados";
		} catch (CustomException e) {
			// TODO Auto-generated catch block
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/limitestesPersonalizados/index";
		}
		
	}	
	
	
	@PostMapping("/save")
	public String saveWs(LimitesPersonalizados limitesPersonalizados, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		log.info("saveWs");
		log.info("limitesPersonalizados: "+limitesPersonalizados);
		
		List<ClientesPersonalizados> listaClientesPersonalizados = new ArrayList<>();
		List<Moneda> listaMonedas = new ArrayList<>();
		
		
		ClienteRequest clienteRequest = new ClienteRequest(); 
		clienteRequest.setIdUsuario("test");
		clienteRequest.setIdSesion("20210101121213");
		clienteRequest.setCodUsuario("E66666");
		clienteRequest.setCanal("8");
		ClientesPersonalizados clientesPersonalizados = new ClientesPersonalizados();
		clientesPersonalizados.setFlagActivo(true);
		clienteRequest.setCliente(clientesPersonalizados);
		
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
				listaClientesPersonalizados = clientePersonalizadoServiceApiRest.listaClientesPersonalizados(clienteRequest);
				model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
				listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
				model.addAttribute("listaMonedas", listaMonedas);
	    		return "convenio/limitesPersonalizados/formLimitesPersonalizados";
			} catch (CustomException e) {
				log.error("error: "+e);
				result.addError(new ObjectError("codMoneda", " Codigo :" +e.getMessage()));
				return "convenio/limitesPersonalizados/formLimitesPersonalizados";
			}
			
		}
		
		LimitesPersonalizadosRequest limitesPersonalizadosRequest = new LimitesPersonalizadosRequest();
		limitesPersonalizadosRequest.setIdUsuario("test");
		limitesPersonalizadosRequest.setIdSesion("20210101121213");
		limitesPersonalizadosRequest.setCodUsuario("E66666");
		limitesPersonalizadosRequest.setCanal("8");
		limitesPersonalizados.setFlagActivo(true);
		limitesPersonalizadosRequest.setLimiteCliente(limitesPersonalizados);
		
		try {
			String respuesta = limitesPersonalizadosServiceApiRest.crear(limitesPersonalizadosRequest);
			log.info(respuesta);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/limitesPersonalizados/index";
		} catch (CustomException e) {
			log.error("error: "+e);
			try {
				listaClientesPersonalizados = clientePersonalizadoServiceApiRest.listaClientesPersonalizados(clienteRequest);
				model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
				listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
				model.addAttribute("listaMonedas", listaMonedas);
				result.addError(new ObjectError("codMoneda", " Codigo :" +e.getMessage()));
	    		return "convenio/limitesPersonalizados/formLimitesPersonalizados";
			} catch (CustomException e1) {
				log.error("error: "+e1);
				result.addError(new ObjectError("codMoneda", " Codigo :" +e1.getMessage()));
				return "convenio/limitesPersonalizados/formLimitesPersonalizados";
			}
			
		}

		
		
	
	}
	
	@GetMapping("/search")
	public String search(@ModelAttribute("limitesPersonalizadosSearch") LimitesPersonalizados limitesPersonalizadosSearch, 
			Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a search limitesPersonalizadosWs");
		log.info(limitesPersonalizadosSearch.getCodigoIbs());
		
		List<LimitesPersonalizados> listaLimitesPersonalizados = new ArrayList<>();
		
		LimitesPersonalizadosRequest limitesPersonalizadosRequest = new LimitesPersonalizadosRequest();
		limitesPersonalizadosRequest.setIdUsuario("test");
		limitesPersonalizadosRequest.setIdSesion("20210101121213");
		limitesPersonalizadosRequest.setCodUsuario("E66666");
		limitesPersonalizadosRequest.setCanal("8");
		LimitesPersonalizados limite = new LimitesPersonalizados();
		if(!limitesPersonalizadosSearch.getCodigoIbs().equals(""))
			limite.setCodigoIbs(limitesPersonalizadosSearch.getCodigoIbs());
		limitesPersonalizadosRequest.setLimiteCliente(limite);
		
		try {
			listaLimitesPersonalizados = limitesPersonalizadosServiceApiRest.listaLimitesPersonalizados(limitesPersonalizadosRequest);
			log.info("lista: "+listaLimitesPersonalizados.isEmpty());
			if(!listaLimitesPersonalizados.isEmpty()) {
				model.addAttribute("listaLimitesPersonalizados", listaLimitesPersonalizados);
	    		return "convenio/limitesPersonalizados/listaLimitesPersonalizados";
			}else {
				//redirectAttributes.addFlashAttribute("mensajeError", " Codigo : 0001 descripcion: Operacion Exitosa.La consulta no arrojo resultado.");
				model.addAttribute("listaLimitesPersonalizados", listaLimitesPersonalizados);
				model.addAttribute("mensajeError", " Codigo : 0001 descripcion: Operacion Exitosa.La consulta no arrojo resultado.");
				return "convenio/limitesPersonalizados/listaLimitesPersonalizados";
			}
			
		} catch (CustomException e) {
			
			log.error("error: "+e);
			model.addAttribute("listaLimitesPersonalizados", listaLimitesPersonalizados);
			model.addAttribute("mensajeError", e.getMessage());
			return "convenio/limitesPersonalizados/listaLimitesPersonalizados";
		}
		
	}	
	
	
	
	@ModelAttribute
	public void setGenericos(Model model) {
		LimitesPersonalizados limitesPersonalizadosSearch = new LimitesPersonalizados();
		model.addAttribute("limitesPersonalizadosSearch", limitesPersonalizadosSearch); 
	}
	
	
}
