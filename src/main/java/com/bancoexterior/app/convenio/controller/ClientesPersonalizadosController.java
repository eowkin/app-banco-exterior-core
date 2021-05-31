package com.bancoexterior.app.convenio.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.bancoexterior.app.convenio.dto.ClienteDatosBasicoRequest;
import com.bancoexterior.app.convenio.dto.ClienteRequest;
import com.bancoexterior.app.convenio.dto.LimitesPersonalizadosRequest;
import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.ClientesPersonalizados;
import com.bancoexterior.app.convenio.model.DatosClientes;
import com.bancoexterior.app.convenio.model.LimitesPersonalizados;
import com.bancoexterior.app.convenio.model.Moneda;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/clientesPersonalizados")
public class ClientesPersonalizadosController {

	@Autowired
	private IClientePersonalizadoServiceApiRest clientePersonalizadoServiceApiRest;

	@Autowired
	private ILimitesPersonalizadosServiceApiRest limitesPersonalizadosServiceApiRest;
	
	@Autowired
	private IMonedaServiceApiRest monedaServiceApiRest;
	
	
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
	
	@GetMapping("/edit/{codigoIbs}")
	public String editarWs(@PathVariable("codigoIbs") String codigoIbs, 
			ClientesPersonalizados clientesPersonalizados, Model model, RedirectAttributes redirectAttributes) {
		log.info("editarWs");
		log.info("codigoIbs: " + codigoIbs);

		ClientesPersonalizados clientesPersonalizadosEdit = new ClientesPersonalizados();

		ClienteRequest clienteRequest = new ClienteRequest();
		clienteRequest.setIdUsuario("test");
		clienteRequest.setIdSesion("20210101121213");
		clienteRequest.setCodUsuario("E66666");
		clienteRequest.setCanal("8");
		ClientesPersonalizados clientesPersonalizadosBuscar = new ClientesPersonalizados();
		clientesPersonalizadosBuscar.setCodigoIbs(codigoIbs);
		clienteRequest.setCliente(clientesPersonalizadosBuscar);
		
		try {
			clientesPersonalizadosEdit = clientePersonalizadoServiceApiRest.buscarClientesPersonalizados(clienteRequest);
			if(clientesPersonalizadosEdit != null) {
				model.addAttribute("clientesPersonalizados", clientesPersonalizadosEdit);
				return "convenio/clientesPersonalizados/formClientesPersonalizadosEdit";
			}else {
				redirectAttributes.addFlashAttribute("mensajeError", " Codigo : 0001 descripcion: Operacion Exitosa.La consulta no arrojo resultado.");
				return "redirect:/clientesPersonalizados/index";
			}
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/agencias/index";
		}
	}
	
	@GetMapping("/verLimites/{codigoIbs}")
	public String verLimitesWs(@PathVariable("codigoIbs") String codigoIbs, 
			ClientesPersonalizados clientesPersonalizados, Model model, RedirectAttributes redirectAttributes) {
		log.info("verLimitesWs");
		log.info("codigoIbs: " + codigoIbs);

		
		List<LimitesPersonalizados> listaLimitesPersonalizados = new ArrayList<>();
		
		LimitesPersonalizadosRequest limitesPersonalizadosRequest = new LimitesPersonalizadosRequest();
		limitesPersonalizadosRequest.setIdUsuario("test");
		limitesPersonalizadosRequest.setIdSesion("20210101121213");
		limitesPersonalizadosRequest.setCodUsuario("E66666");
		limitesPersonalizadosRequest.setCanal("8");
		LimitesPersonalizados limite = new LimitesPersonalizados();
		limite.setCodigoIbs(codigoIbs);
		limitesPersonalizadosRequest.setLimiteCliente(limite);
		
		ClientesPersonalizados clientesPersonalizadosEdit = new ClientesPersonalizados();

		ClienteRequest clienteRequest = new ClienteRequest();
		clienteRequest.setIdUsuario("test");
		clienteRequest.setIdSesion("20210101121213");
		clienteRequest.setCodUsuario("E66666");
		clienteRequest.setCanal("8");
		ClientesPersonalizados clientesPersonalizadosBuscar = new ClientesPersonalizados();
		clientesPersonalizadosBuscar.setCodigoIbs(codigoIbs);
		clienteRequest.setCliente(clientesPersonalizadosBuscar);
			
		try {
			
			clientesPersonalizadosEdit = clientePersonalizadoServiceApiRest.buscarClientesPersonalizados(clienteRequest);
			if(clientesPersonalizadosEdit != null) {
				listaLimitesPersonalizados = limitesPersonalizadosServiceApiRest.listaLimitesPersonalizados(limitesPersonalizadosRequest);
				log.info("lista: "+listaLimitesPersonalizados.isEmpty());
				if(!listaLimitesPersonalizados.isEmpty()) {
					model.addAttribute("listaLimitesPersonalizados", listaLimitesPersonalizados);
					model.addAttribute("codigoIbs", codigoIbs);
		    		return "convenio/clientesPersonalizados/listaLimitesPersonalizados";
				}else {
					//redirectAttributes.addFlashAttribute("mensajeError", " Codigo : 0001 descripcion: Operacion Exitosa.La consulta no arrojo resultado.");
					model.addAttribute("listaLimitesPersonalizados", listaLimitesPersonalizados);
					model.addAttribute("codigoIbs", codigoIbs);
					model.addAttribute("mensajeError", " Codigo : 0001 descripcion: Operacion Exitosa.La consulta no arrojo resultado.");
					return "convenio/clientesPersonalizados/listaLimitesPersonalizados";
				}
			}else {
				redirectAttributes.addFlashAttribute("mensajeError", " Codigo : 0001 descripcion: Operacion Exitosa.La consulta no arrojo resultado.");
				return "redirect:/clientesPersonalizados/index";
			}		
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/clientesPersonalizados/index";
		}
	}
	
	
	@GetMapping("/editLimiteCliente/{codigoIbs}/{codMoneda}/{tipoTransaccion}")
	public String editarLimiteClienteWs(@PathVariable("codigoIbs") String codigoIbs, @PathVariable("codMoneda") String codMoneda, 
			@PathVariable("tipoTransaccion") String tipoTransaccion,LimitesPersonalizados limitesPersonalizados,Model model, RedirectAttributes redirectAttributes) {
		log.info("editarLimiteClienteWs");
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
				return "convenio/clientesPersonalizados/formLimitesPersonalizadosEdit";
			}else {
				redirectAttributes.addFlashAttribute("mensajeError", " Codigo : 0001 descripcion: Operacion Exitosa.La consulta no arrojo resultado.");
				return "redirect:/clientesPersonalizados/index";
			}
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/clientesPersonalizados/index";
		}
		
		
	}
	
	
	@PostMapping("/guardarLimiteCliente")
	public String guardarLimiteClienteWs(LimitesPersonalizados limitesPersonalizados, BindingResult result,  RedirectAttributes redirectAttributes) {
		log.info("guardarWs");
		log.info("limitesPersonalizados", limitesPersonalizados);
		
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				log.info("Ocurrio un error: " + error.getDefaultMessage());
			}
		
			return "convenio/clientesPersonalizados/formLimitesPersonalizadosEdit";
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
			//return "redirect:/limitesPersonalizados/index";
			return "redirect:/clientesPersonalizados/verLimites/"+limitesPersonalizados.getCodigoIbs();
		} catch (CustomException e) {
			log.error("error: "+e);
			result.addError(new ObjectError("codMoneda", " Codigo :" +e.getMessage()));
			return "convenio/clientesPersonalizados/formLimitesPersonalizadosEdit";
		}
	
		
	}
	
	
	
	@GetMapping("/formLimiteClientePersonalizado/{codigoIbs}")
	public String formLimiteClientePersonalizado(@PathVariable("codigoIbs") String codigoIbs,LimitesPersonalizados limitesPersonalizados,  Model model, RedirectAttributes redirectAttributes) {
		
		log.info("formLimiteClientePersonalizado");
		log.info("codigoIbs: " + codigoIbs);
		List<Moneda> listaMonedas = new ArrayList<>();
		ClientesPersonalizados clientesPersonalizadosEdit = new ClientesPersonalizados();

		ClienteRequest clienteRequest = new ClienteRequest();
		clienteRequest.setIdUsuario("test");
		clienteRequest.setIdSesion("20210101121213");
		clienteRequest.setCodUsuario("E66666");
		clienteRequest.setCanal("8");
		ClientesPersonalizados clientesPersonalizadosBuscar = new ClientesPersonalizados();
		clientesPersonalizadosBuscar.setCodigoIbs(codigoIbs);
		clienteRequest.setCliente(clientesPersonalizadosBuscar);
		
		MonedasRequest monedasRequest = new MonedasRequest();
		monedasRequest.setIdUsuario("test");
		monedasRequest.setIdSesion("20210101121213");
		monedasRequest.setCodUsuario("E66666");
		monedasRequest.setCanal("8");
		Moneda moneda = new Moneda();
		moneda.setFlagActivo(true);
		monedasRequest.setMoneda(moneda);
			
		try {
			
			clientesPersonalizadosEdit = clientePersonalizadoServiceApiRest.buscarClientesPersonalizados(clienteRequest);
			if(clientesPersonalizadosEdit != null) {
				listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
				limitesPersonalizados.setCodigoIbs(codigoIbs);
				model.addAttribute("listaMonedas", limitesPersonalizados);
				model.addAttribute("listaMonedas", listaMonedas);
	    		return "convenio/clientesPersonalizados/formLimitesPersonalizados";
			}else {
				redirectAttributes.addFlashAttribute("mensajeError", " Codigo : 0001 descripcion: Operacion Exitosa.La consulta no arrojo resultado.");
				return "redirect:/clientesPersonalizados/index";
			}
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/clientesPersonalizados/index";
		}	
	}
	
	@PostMapping("/saveLimiteCliente")
	public String saveLimiteClienteWs(LimitesPersonalizados limitesPersonalizados, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		log.info("saveLimiteClienteWs");
		log.info("limitesPersonalizados: "+limitesPersonalizados);
		
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
				//model.addAttribute("codigoIbs", limitesPersonalizados.getCodigoIbs());
	    		return "convenio/clientesPersonalizados/formLimitesPersonalizados";
	    		//clientesPersonalizados/formLimiteClientePersonalizado/{codigoIbs}
			} catch (CustomException e) {
				log.error("error: "+e);
				result.addError(new ObjectError("codMoneda", " Codigo :" +e.getMessage()));
				return "convenio/clientesPersonalizados/formLimitesPersonalizados";
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
			return "redirect:/clientesPersonalizados/verLimites/"+limitesPersonalizados.getCodigoIbs();
		} catch (CustomException e) {
			log.error("error: "+e);
			try {
				listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
				model.addAttribute("listaMonedas", listaMonedas);
				result.addError(new ObjectError("codMoneda", " Codigo :" +e.getMessage()));
	    		return "convenio/clientesPersonalizados/formLimitesPersonalizados";
			} catch (CustomException e1) {
				log.error("error: "+e1);
				result.addError(new ObjectError("codMoneda", " Codigo :" +e1.getMessage()));
				return "convenio/clientesPersonalizados/formLimitesPersonalizados";
			}
		
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
	
	@GetMapping("/searchCrear")
	public String searchCrear(ClientesPersonalizados clientesPersonalizados,
			Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		log.info("si me llamo a search clientesPersonalizadosWs");
		log.info("codigoIbs: "+clientesPersonalizados.getCodigoIbs());
		log.info("nroIdCliente: "+clientesPersonalizados.getNroIdCliente());
		log.info("request: "+request.getRemoteAddr());

		 
		
		ClienteDatosBasicoRequest clienteDatosBasicoRequest = new ClienteDatosBasicoRequest();
		clienteDatosBasicoRequest.setIdUsuario("test");
		clienteDatosBasicoRequest.setIdSesion("20210101121213");
		clienteDatosBasicoRequest.setCodUsuario("E66666");
		clienteDatosBasicoRequest.setCanal("8");
		clienteDatosBasicoRequest.setIp(request.getRemoteAddr());
		if(!clientesPersonalizados.getCodigoIbs().equals(""))
			clienteDatosBasicoRequest.setCodigoIbs(clientesPersonalizados.getCodigoIbs());
		
		if(!clientesPersonalizados.getNroIdCliente().equals(""))
			clienteDatosBasicoRequest.setNroIdCliente(clientesPersonalizados.getNroIdCliente());
		
		
		
		try {
			DatosClientes datosClientes = clientePersonalizadoServiceApiRest.buscarDatosBasicos(clienteDatosBasicoRequest);
			
			if(datosClientes != null) {
				clientesPersonalizados.setCodigoIbs(datosClientes.getCodIbs());
				clientesPersonalizados.setNroIdCliente(datosClientes.getNroIdCliente());
				clientesPersonalizados.setNombreRif(datosClientes.getNombreLegal());
				model.addAttribute("clientesPersonalizados", clientesPersonalizados);
				return "convenio/clientesPersonalizados/formClientesPersonalizados";
			}else {
				//redirectAttributes.addFlashAttribute("mensajeError", " Codigo : 0001 descripcion: Operacion Exitosa.La consulta no arrojo resultado.");
				model.addAttribute("mensajeError", " Codigo : 0001 descripcion: Operacion Exitosa.La consulta no arrojo resultado.");
				return "convenio/clientesPersonalizados/formClientesPersonalizadosBuscar";
			}
			
			
		} catch (CustomException e) {
			log.error("error: "+e);
			model.addAttribute("mensajeError", e.getMessage());
			return "convenio/clientesPersonalizados/formClientesPersonalizadosBuscar";
		}
		
	}	

	@PostMapping("/guardar")
	public String guardarWs(ClientesPersonalizados clientesPersonalizados, Model model, RedirectAttributes redirectAttributes) {
		log.info("saveWs");
		log.info("clientesPersonalizados: "+clientesPersonalizados);
		
		ClienteRequest clienteRequest = new ClienteRequest();
		clienteRequest.setIdUsuario("test");
		clienteRequest.setIdSesion("20210101121213");
		clienteRequest.setCodUsuario("E66666");
		clienteRequest.setCanal("8");
		clientesPersonalizados.setFlagActivo(true);
		clienteRequest.setCliente(clientesPersonalizados);
		
		try {
			String respuesta = clientePersonalizadoServiceApiRest.actualizar(clienteRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/clientesPersonalizados/index";
			
		} catch (CustomException e) {
			log.error("error: "+e);
			//redirectAttributes.addFlashAttribute("mensajeError",e.getMessage());
			model.addAttribute("mensajeError",e.getMessage());
			return "convenio/clientesPersonalizados/formClientesPersonalizadosEdit";
			//model.addAttribute("mensajeError", e.getMessage());
			//return "convenio/agencia/formBuscarAgencia";
		}
	}
	
	
	@PostMapping("/save")
	public String saveWs(ClientesPersonalizados clientesPersonalizados, Model model, RedirectAttributes redirectAttributes) {
		log.info("saveWs");
		log.info("clientesPersonalizados: "+clientesPersonalizados);
		
		ClienteRequest clienteRequest = new ClienteRequest();
		clienteRequest.setIdUsuario("test");
		clienteRequest.setIdSesion("20210101121213");
		clienteRequest.setCodUsuario("E66666");
		clienteRequest.setCanal("8");
		clientesPersonalizados.setFlagActivo(true);
		clienteRequest.setCliente(clientesPersonalizados);
		
		try {
			String respuesta = clientePersonalizadoServiceApiRest.crear(clienteRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/clientesPersonalizados/index";
			
		} catch (CustomException e) {
			log.error("error: "+e);
			//redirectAttributes.addFlashAttribute("mensajeError",e.getMessage());
			model.addAttribute("mensajeError",e.getMessage());
			return "convenio/clientesPersonalizados/formClientesPersonalizados";
			
			//model.addAttribute("mensajeError", e.getMessage());
			//return "convenio/agencia/formBuscarAgencia";
		}
		
		
		
	}
	
	
	@GetMapping("/formClientePersonalizado")
	public String formClientePersonalizado(Model model) {

		return "convenio/clientesPersonalizados/formClientesPersonalizados";
	}
	
	
	@GetMapping("/formClientePersonalizadoBuscar")
	public String formClientePersonalizadoBuscar(ClientesPersonalizados clientesPersonalizados) {

		return "convenio/clientesPersonalizados/formClientesPersonalizadosBuscar";
	}
	
	

	@ModelAttribute
	public void setGenericos(Model model) {
		ClientesPersonalizados clientesPersonalizadosSearch = new ClientesPersonalizados();
		model.addAttribute("clientesPersonalizadosSearch", clientesPersonalizadosSearch);
	}
}
