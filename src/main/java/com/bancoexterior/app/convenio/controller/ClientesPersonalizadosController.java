package com.bancoexterior.app.convenio.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.bancoexterior.app.convenio.dto.ClienteResponse;
import com.bancoexterior.app.convenio.dto.LimitesPersonalizadosRequest;
import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.ClientesPersonalizados;
import com.bancoexterior.app.convenio.model.DatosClientes;
import com.bancoexterior.app.convenio.model.DatosPaginacion;
import com.bancoexterior.app.convenio.model.LimitesPersonalizados;
import com.bancoexterior.app.convenio.model.Moneda;
import com.bancoexterior.app.util.LibreriaUtil;

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
	
	@Autowired
	private LibreriaUtil libreriaUtil; 
	
	@Value("${des.canal}")
    private String canal;	
	
	@Value("${des.clientesPersonalizados.numeroRegistroPage}")
    private int numeroRegistroPage;
	
	
	
	@GetMapping("/index/{page}")
	public String index(@PathVariable("page") int page,Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a index clientesPersonalizadosWs");
		log.info("page: "+page);
		

		ClienteRequest clienteRequest = getClienteRequest();
		clienteRequest.setNumeroPagina(page);
		clienteRequest.setTamanoPagina(numeroRegistroPage);
		ClientesPersonalizados clientesPersonalizados = new ClientesPersonalizados();
		clienteRequest.setCliente(clientesPersonalizados);
		List<ClientesPersonalizados> listaClientesPersonalizados = new ArrayList<>();
		DatosPaginacion datosPaginacion = new DatosPaginacion(0,0,0,0);
		try {
			
			ClienteResponse clienteResponse = clientePersonalizadoServiceApiRest.listaClientesPaginacion(clienteRequest);
			
			
			if(clienteResponse != null) {
				listaClientesPersonalizados = clienteResponse.getListaClientes();
				for (ClientesPersonalizados clientesPersonalizados2 : listaClientesPersonalizados) {
					if(clientesPersonalizados2.getFechaModificacion() != null) {
						String[] arrOfStr = clientesPersonalizados2.getFechaModificacion().split(" ", 2);
						clientesPersonalizados2.setFechaModificacion(arrOfStr[0]);
					}
				}
				log.info("listaClientesPersonalizados: "+listaClientesPersonalizados);
				datosPaginacion = clienteResponse.getDatosPaginacion();
				log.info("datosPaginacion: "+datosPaginacion);
				model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
				model.addAttribute("datosPaginacion", datosPaginacion);
				return "convenio/clientesPersonalizados/listaClientesPersonalizados";
			}else {
				datosPaginacion.setTotalPaginas(0);
				model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
				model.addAttribute("datosPaginacion", datosPaginacion);
				//redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
				return "convenio/clientesPersonalizados/listaClientesPersonalizados";
			}
			
			
		} catch (CustomException e) {
			log.error("error: "+e);
			log.error("se vino por aqui");
			
			model.addAttribute("datosPaginacion", datosPaginacion);
			model.addAttribute("mensajeError", e.getMessage());
			return "convenio/clientesPersonalizados/listaClientesPersonalizados";
			//redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			//return "redirect:/";
		}
		
		
		
	}	
	
	@GetMapping("/activar/{codigoIbs}/{page}")
	public String activarWs(@PathVariable("codigoIbs") String codigoIbs, @PathVariable("page") int page, Model model,
			RedirectAttributes redirectAttributes) {
		log.info("activarWs");
		log.info("codigoIbs: " + codigoIbs);
		log.info("page: " + page);

		ClientesPersonalizados clientesPersonalizadosEdit = new ClientesPersonalizados();

		ClienteRequest clienteRequest = getClienteRequest();
		clienteRequest.setNumeroPagina(1);
		clienteRequest.setTamanoPagina(numeroRegistroPage);
		ClientesPersonalizados clientesPersonalizados = new ClientesPersonalizados();
		clientesPersonalizados.setCodigoIbs(codigoIbs);
		clienteRequest.setCliente(clientesPersonalizados);
		
		try {
			clientesPersonalizadosEdit = clientePersonalizadoServiceApiRest.buscarClientesPersonalizados(clienteRequest);
			clientesPersonalizadosEdit.setFlagActivo(true);
			clienteRequest.setCliente(clientesPersonalizadosEdit);
			String respuesta = clientePersonalizadoServiceApiRest.actualizar(clienteRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/clientesPersonalizados/index/"+page;
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/clientesPersonalizados/index/"+page;
		}
		
	}
	
	@GetMapping("/desactivar/{codigoIbs}/{page}")
	public String desactivarWs(@PathVariable("codigoIbs") String codigoIbs, @PathVariable("page") int page, Model model,
			RedirectAttributes redirectAttributes) {
		log.info("desactivarWs");
		log.info("codigoIbs: " + codigoIbs);
		log.info("page: " + page);

		ClientesPersonalizados clientesPersonalizadosEdit = new ClientesPersonalizados();

		ClienteRequest clienteRequest = getClienteRequest();
		clienteRequest.setNumeroPagina(1);
		clienteRequest.setTamanoPagina(numeroRegistroPage);
		ClientesPersonalizados clientesPersonalizados = new ClientesPersonalizados();
		clientesPersonalizados.setCodigoIbs(codigoIbs);
		clienteRequest.setCliente(clientesPersonalizados);
		
		try {
			clientesPersonalizadosEdit = clientePersonalizadoServiceApiRest.buscarClientesPersonalizados(clienteRequest);
			clientesPersonalizadosEdit.setFlagActivo(false);
			clienteRequest.setCliente(clientesPersonalizadosEdit);
			String respuesta = clientePersonalizadoServiceApiRest.actualizar(clienteRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/clientesPersonalizados/index/"+page;
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/clientesPersonalizados/index/"+page;
		}
		
	}
	
	@GetMapping("/edit/{codigoIbs}")
	public String editarWs(@PathVariable("codigoIbs") String codigoIbs, 
			ClientesPersonalizados clientesPersonalizados, Model model, RedirectAttributes redirectAttributes) {
		log.info("editarWs");
		log.info("codigoIbs: " + codigoIbs);

		ClientesPersonalizados clientesPersonalizadosEdit = new ClientesPersonalizados();

		ClienteRequest clienteRequest = getClienteRequest();
		ClientesPersonalizados clientesPersonalizadosBuscar = new ClientesPersonalizados();
		clientesPersonalizadosBuscar.setCodigoIbs(codigoIbs);
		clienteRequest.setCliente(clientesPersonalizadosBuscar);
		
		try {
			clientesPersonalizadosEdit = clientePersonalizadoServiceApiRest.buscarClientesPersonalizados(clienteRequest);
			if(clientesPersonalizadosEdit != null) {
				model.addAttribute("clientesPersonalizados", clientesPersonalizadosEdit);
				return "convenio/clientesPersonalizados/formClientesPersonalizadosEdit";
			}else {
				redirectAttributes.addFlashAttribute("mensajeError", "Operacion Exitosa.La consulta no arrojo resultado.");
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
		
		LimitesPersonalizadosRequest limitesPersonalizadosRequest = getLimitesPersonalizadosRequest();
		LimitesPersonalizados limite = new LimitesPersonalizados();
		limite.setCodigoIbs(codigoIbs);
		limitesPersonalizadosRequest.setLimiteCliente(limite);
		
		ClientesPersonalizados clientesPersonalizadosEdit = new ClientesPersonalizados();

		ClienteRequest clienteRequest = getClienteRequest();
		clienteRequest.setNumeroPagina(1);
		clienteRequest.setTamanoPagina(numeroRegistroPage);
		ClientesPersonalizados clientesPersonalizadosBuscar = new ClientesPersonalizados();
		clientesPersonalizadosBuscar.setCodigoIbs(codigoIbs);
		clienteRequest.setCliente(clientesPersonalizadosBuscar);
			
		try {
			
			clientesPersonalizadosEdit = clientePersonalizadoServiceApiRest.buscarClientesPersonalizados(clienteRequest);
			if(clientesPersonalizadosEdit != null) {
				listaLimitesPersonalizados = limitesPersonalizadosServiceApiRest.listaLimitesPersonalizados(limitesPersonalizadosRequest);
				log.info("lista: "+listaLimitesPersonalizados.isEmpty());
				if(!listaLimitesPersonalizados.isEmpty()) {
					
					for (LimitesPersonalizados limitesPersonalizados : listaLimitesPersonalizados) {
						if(limitesPersonalizados.getFechaModificacion() != null) {
							String[] arrOfStr = limitesPersonalizados.getFechaModificacion().split(" ", 2);
							limitesPersonalizados.setFechaModificacion(arrOfStr[0]);
						}
					}
					model.addAttribute("listaLimitesPersonalizados", listaLimitesPersonalizados);
					model.addAttribute("codigoIbs", codigoIbs);
		    		return "convenio/clientesPersonalizados/listaLimitesPersonalizados";
				}else {
					//redirectAttributes.addFlashAttribute("mensajeError", " Codigo : 0001 descripcion: Operacion Exitosa.La consulta no arrojo resultado.");
					model.addAttribute("listaLimitesPersonalizados", listaLimitesPersonalizados);
					model.addAttribute("codigoIbs", codigoIbs);
					model.addAttribute("mensajeError", "Operacion Exitosa.La consulta no arrojo resultado.");
					return "convenio/clientesPersonalizados/listaLimitesPersonalizados";
				}
			}else {
				redirectAttributes.addFlashAttribute("mensajeError", "Operacion Exitosa.La consulta no arrojo resultado.");
				return "redirect:/clientesPersonalizados/index/1";
			}		
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/clientesPersonalizados/index/1";
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
		
		
		
		LimitesPersonalizadosRequest limitesPersonalizadosRequest = getLimitesPersonalizadosRequest();
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
				redirectAttributes.addFlashAttribute("mensajeError", "Operacion Exitosa.La consulta no arrojo resultado.");
				return "redirect:/clientesPersonalizados/index";
			}
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/clientesPersonalizados/index";
		}
		
		
	}
	
	
	@PostMapping("/guardarLimiteCliente")
	public String guardarLimiteClienteWs(LimitesPersonalizados limitesPersonalizados, BindingResult result,
			RedirectAttributes redirectAttributes, Model model) {
		log.info("guardarWs");
		log.info("limitesPersonalizados", limitesPersonalizados);
		List<String> listaError = new ArrayList<>();
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				//log.info("error.hashCode(): "+error.);
				log.info("error.hashCode(): "+error.hashCode());
				log.info("error.getCode(): "+error.getCode());
				if(error.getCode().equals("typeMismatch")) {
					listaError.add("Los valores de los montos debe ser numerico");
				}
				log.info("getObjectName(): "+error.getObjectName());
				log.info("Ocurrio un error: " + error.getDefaultMessage());
			}
			model.addAttribute("listaError", listaError);
			return "convenio/clientesPersonalizados/formLimitesPersonalizadosEdit";
		}
		
		//firstBigDecimal.compareTo(secondBigDecimal) < 0 // "<"
		//firstBigDecimal.compareTo(secondBigDecimal) > 0 // ">"    
		//firstBigDecimal.compareTo(secondBigDecimal) == 0 // "=="  
		//firstBigDecimal.compareTo(secondBigDecimal) >= 0 // ">="
		  log.info("Comparar tamaño:" + limitesPersonalizados.getMontoMax().compareTo(limitesPersonalizados.getMontoMin())); 
		  if(limitesPersonalizados.getMontoMax().compareTo(limitesPersonalizados.getMontoMin()) < 0) { 
			  listaError.add("El monto mínimo no debe ser mayor al monto máximo");
			  model.addAttribute("listaError", listaError);
			  result.addError(new  ObjectError("codMoneda", " El monto mínimo no debe ser mayor al monto máximo"));
			  
			  return "convenio/clientesPersonalizados/formLimitesPersonalizadosEdit"; 
		  }
		 
		  log.info("Comparar tamaño:" + limitesPersonalizados.getMontoMensual().compareTo(limitesPersonalizados.getMontoDiario())); 
		  if(limitesPersonalizados.getMontoMensual().compareTo(limitesPersonalizados.getMontoDiario()) < 0) { 
			  result.addError(new  ObjectError("codMoneda", " El monto diario no debe ser mayor al mensual"));
			  listaError.add("El monto diario no debe ser mayor al mensual");
			  model.addAttribute("listaError", listaError);
			  return "convenio/clientesPersonalizados/formLimitesPersonalizadosEdit"; 
		  }
		
		
		LimitesPersonalizadosRequest limitesPersonalizadosRequest = getLimitesPersonalizadosRequest();
		limitesPersonalizadosRequest.setLimiteCliente(limitesPersonalizados);
		
		try {
			
			String respuesta = limitesPersonalizadosServiceApiRest.actualizar(limitesPersonalizadosRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			//return "redirect:/limitesPersonalizados/index";
			return "redirect:/clientesPersonalizados/verLimites/"+limitesPersonalizados.getCodigoIbs();
		} catch (CustomException e) {
			log.error("error: "+e);
			result.addError(new ObjectError("codMoneda", " Codigo :" +e.getMessage()));
			listaError.add(e.getMessage());
			  model.addAttribute("listaError", listaError);
			return "convenio/clientesPersonalizados/formLimitesPersonalizadosEdit";
		}
	
		
	}
	
	
	
	@GetMapping("/formLimiteClientePersonalizado/{codigoIbs}")
	public String formLimiteClientePersonalizado(@PathVariable("codigoIbs") String codigoIbs,LimitesPersonalizados limitesPersonalizados,  Model model, RedirectAttributes redirectAttributes) {
		
		log.info("formLimiteClientePersonalizado");
		log.info("codigoIbs: " + codigoIbs);
		List<Moneda> listaMonedas = new ArrayList<>();
		ClientesPersonalizados clientesPersonalizadosEdit = new ClientesPersonalizados();

		ClienteRequest clienteRequest = getClienteRequest();
		clienteRequest.setNumeroPagina(1);
		clienteRequest.setTamanoPagina(numeroRegistroPage);
		ClientesPersonalizados clientesPersonalizadosBuscar = new ClientesPersonalizados();
		clientesPersonalizadosBuscar.setCodigoIbs(codigoIbs);
		clienteRequest.setCliente(clientesPersonalizadosBuscar);
		
		MonedasRequest monedasRequest = getMonedasRequest();
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
				redirectAttributes.addFlashAttribute("mensajeError", "Operacion Exitosa.La consulta no arrojo resultado.");
				return "redirect:/clientesPersonalizados/index/1";
			}
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/clientesPersonalizados/index/1";
		}	
	}
	
	@PostMapping("/saveLimiteCliente")
	public String saveLimiteClienteWs(LimitesPersonalizados limitesPersonalizados, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		log.info("saveLimiteClienteWs");
		log.info("limitesPersonalizados: "+limitesPersonalizados);
		
		List<Moneda> listaMonedas = new ArrayList<>();
		List<String> listaError = new ArrayList<>();
		MonedasRequest monedasRequest = getMonedasRequest();
		Moneda moneda = new Moneda();
		moneda.setFlagActivo(true);
		monedasRequest.setMoneda(moneda);
		
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				log.info("Ocurrio un error: " + error.getDefaultMessage());
				log.info("error.getCode(): "+error.getCode());
				if(error.getCode().equals("typeMismatch")) {
					listaError.add("Los valores de los montos debe ser numerico");
				}
			}
			
			try {
				listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
				model.addAttribute("listaMonedas", listaMonedas);
				model.addAttribute("listaError", listaError);
	    		return "convenio/clientesPersonalizados/formLimitesPersonalizados";
	    		//clientesPersonalizados/formLimiteClientePersonalizado/{codigoIbs}
			} catch (CustomException e) {
				log.error("error: "+e);
				result.addError(new ObjectError("codMoneda", " Codigo :" +e.getMessage()));
				 listaError.add(e.getMessage());
				 model.addAttribute("listaError", e.getMessage());
				return "convenio/clientesPersonalizados/formLimitesPersonalizados";
			}
			
		}
		
		log.info("Comparar tamaño:" + limitesPersonalizados.getMontoMax().compareTo(limitesPersonalizados.getMontoMin())); 
		  if(limitesPersonalizados.getMontoMax().compareTo(limitesPersonalizados.getMontoMin()) < 0) { 
			  log.info("Se metio por aqui compa:");
			  try {
				  	log.info("Se metio por aqui compa 1.1");
				  	listaError.add("El monto mínimo no debe ser mayor al monto máximo");
					log.info("listaError: "+listaError);
					listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
					model.addAttribute("listaMonedas", listaMonedas);
					model.addAttribute("listaError", listaError);
					result.addError(new  ObjectError("codMoneda", " El monto mínimo no debe ser mayor al monto máximo"));
					return "convenio/clientesPersonalizados/formLimitesPersonalizados";
		    		//clientesPersonalizados/formLimiteClientePersonalizado/{codigoIbs}
				} catch (CustomException e) {
					log.error("error: "+e);
					result.addError(new ObjectError("codMoneda", " Codigo :" +e.getMessage()));
					 listaError.add(e.getMessage());
					 model.addAttribute("listaError", listaError);
					return "convenio/clientesPersonalizados/formLimitesPersonalizados";
				}
		  }
		 
		  log.info("Comparar tamaño:" + limitesPersonalizados.getMontoMensual().compareTo(limitesPersonalizados.getMontoDiario())); 
		  if(limitesPersonalizados.getMontoMensual().compareTo(limitesPersonalizados.getMontoDiario()) < 0) { 
			  log.info("Se metio por aqui compa 2:");
			  try {
					listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
					model.addAttribute("listaMonedas", listaMonedas);
					listaError.add("El monto diario no debe ser mayor al mensual");
			        model.addAttribute("listaError", listaError);
			        result.addError(new  ObjectError("codMoneda", " El monto diario no debe ser mayor al mensual"));
		    		return "convenio/clientesPersonalizados/formLimitesPersonalizados";
		    		//clientesPersonalizados/formLimiteClientePersonalizado/{codigoIbs}
				} catch (CustomException e) {
					log.error("error: "+e);
					result.addError(new ObjectError("codMoneda", " Codigo :" +e.getMessage()));
					 listaError.add(e.getMessage());
					 model.addAttribute("listaError", listaError);
					return "convenio/clientesPersonalizados/formLimitesPersonalizados";
				}
		  }
		
		LimitesPersonalizadosRequest limitesPersonalizadosRequest = getLimitesPersonalizadosRequest();
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
				listaError.add(e.getMessage());
				model.addAttribute("listaError", listaError);
	    		return "convenio/clientesPersonalizados/formLimitesPersonalizados";
			} catch (CustomException e1) {
				log.error("error: "+e1);
				result.addError(new ObjectError("codMoneda", " Codigo :" +e1.getMessage()));
				listaError.add(e1.getMessage());
				model.addAttribute("listaError", listaError);
				return "convenio/clientesPersonalizados/formLimitesPersonalizados";
			}
		
		}
	}	
	
	
	@GetMapping("/activarLimiteCliente/{codigoIbs}/{codMoneda}/{tipoTransaccion}")
	public String activarLimiteClienteWs(@PathVariable("codigoIbs") String codigoIbs, @PathVariable("codMoneda") String codMoneda, 
			@PathVariable("tipoTransaccion") String tipoTransaccion,LimitesPersonalizados limitesPersonalizados,Model model, RedirectAttributes redirectAttributes) {
		log.info("activarLimiteClienteWs");
		log.info(codigoIbs);
		log.info(codMoneda);
		log.info(tipoTransaccion);
		
		LimitesPersonalizados limitesPersonalizadosEdit = new LimitesPersonalizados();
		
		LimitesPersonalizadosRequest limitesPersonalizadosRequest = getLimitesPersonalizadosRequest();
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
			return "redirect:/clientesPersonalizados/verLimites/"+codigoIbs;
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/clientesPersonalizados/verLimites/"+codigoIbs;
		}
	
		
	}
	
	@GetMapping("/desactivarLimiteCliente/{codigoIbs}/{codMoneda}/{tipoTransaccion}")
	public String desactivarLimiteClienteWs(@PathVariable("codigoIbs") String codigoIbs, @PathVariable("codMoneda") String codMoneda, 
			@PathVariable("tipoTransaccion") String tipoTransaccion,LimitesPersonalizados limitesPersonalizados,Model model, RedirectAttributes redirectAttributes) {
		log.info("activarWs");
		log.info(codigoIbs);
		log.info(codMoneda);
		log.info(tipoTransaccion);
		
		LimitesPersonalizados limitesPersonalizadosEdit = new LimitesPersonalizados();
		
		LimitesPersonalizadosRequest limitesPersonalizadosRequest = getLimitesPersonalizadosRequest();
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
			return "redirect:/clientesPersonalizados/verLimites/"+codigoIbs;
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/clientesPersonalizados/verLimites/"+codigoIbs;
		}
	
		
	}
	
	@GetMapping("/search")
	public String search(
			@ModelAttribute("clientesPersonalizadosSearch") ClientesPersonalizados clientesPersonalizadosSearch,
			Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a search clientesPersonalizadosWs");
		log.info(clientesPersonalizadosSearch.getCodigoIbs());

		

		ClienteRequest clienteRequest = getClienteRequest();
		clienteRequest.setNumeroPagina(1);
		clienteRequest.setTamanoPagina(numeroRegistroPage);
		ClientesPersonalizados clientesPersonalizados = new ClientesPersonalizados();
		if (!clientesPersonalizadosSearch.getCodigoIbs().equals(""))
			clientesPersonalizados.setCodigoIbs(clientesPersonalizadosSearch.getCodigoIbs());
		clienteRequest.setCliente(clientesPersonalizados);
		List<ClientesPersonalizados> listaClientesPersonalizados = new ArrayList<>();
		DatosPaginacion datosPaginacion = new DatosPaginacion(0,0,0,0);
		
		try {
			ClienteResponse clienteResponse = clientePersonalizadoServiceApiRest.listaClientesPaginacion(clienteRequest);
			
			
			if(clienteResponse != null) {
				listaClientesPersonalizados = clienteResponse.getListaClientes();
				log.info("listaClientesPersonalizados: "+listaClientesPersonalizados);
				log.info("lista: "+listaClientesPersonalizados.isEmpty());
				
				if(!listaClientesPersonalizados.isEmpty()) {
					for (ClientesPersonalizados clientesPersonalizados2 : listaClientesPersonalizados) {
						if(clientesPersonalizados2.getFechaModificacion() != null) {
							String[] arrOfStr = clientesPersonalizados2.getFechaModificacion().split(" ", 2);
							clientesPersonalizados2.setFechaModificacion(arrOfStr[0]);
						}
					}
					datosPaginacion = clienteResponse.getDatosPaginacion();
					log.info("datosPaginacion: "+datosPaginacion);
					model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
					model.addAttribute("datosPaginacion", datosPaginacion);
					return "convenio/clientesPersonalizados/listaClientesPersonalizados";
				}else {
					datosPaginacion.setTotalPaginas(0);
					model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
					model.addAttribute("datosPaginacion", datosPaginacion);
					model.addAttribute("mensajeError", "Operacion Exitosa.La consulta no arrojo resultado.");
					return "convenio/clientesPersonalizados/listaClientesPersonalizados";
				}
				
				
			}else {
				datosPaginacion.setTotalPaginas(0);
				model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
				model.addAttribute("datosPaginacion", datosPaginacion);
				model.addAttribute("mensajeError", "Operacion Exitosa.La consulta no arrojo resultado.");
				return "convenio/clientesPersonalizados/listaClientesPersonalizados";
			}
			
		} catch (CustomException e) {
			
			log.error("error: "+e);
			datosPaginacion.setTotalPaginas(0);
			model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
			model.addAttribute("datosPaginacion", datosPaginacion);
			model.addAttribute("mensajeError", e.getMessage());
			return "convenio/clientesPersonalizados/listaClientesPersonalizados";
		}
		
		
	}	
	
	
	@GetMapping("/searchNroIdCliente")
	public String searchNroIdCliente(
			@ModelAttribute("clientesPersonalizadosSearch") ClientesPersonalizados clientesPersonalizadosSearch,
			Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a search clientesPersonalizadosWs");
		log.info(clientesPersonalizadosSearch.getCodigoIbs());

	
		ClienteRequest clienteRequest = getClienteRequest();
		clienteRequest.setNumeroPagina(1);
		clienteRequest.setTamanoPagina(numeroRegistroPage);
		ClientesPersonalizados clientesPersonalizados = new ClientesPersonalizados();
		if (!clientesPersonalizadosSearch.getNroIdCliente().equals(""))
			clientesPersonalizados.setNroIdCliente(clientesPersonalizadosSearch.getNroIdCliente());
		clienteRequest.setCliente(clientesPersonalizados);
		
		List<ClientesPersonalizados> listaClientesPersonalizados = new ArrayList<>();
		DatosPaginacion datosPaginacion = new DatosPaginacion(0,0,0,0);
		
		try {
			ClienteResponse clienteResponse = clientePersonalizadoServiceApiRest.listaClientesPaginacion(clienteRequest);
			
			
			if(clienteResponse != null) {
				listaClientesPersonalizados = clienteResponse.getListaClientes();
				log.info("listaClientesPersonalizados: "+listaClientesPersonalizados);
				log.info("lista: "+listaClientesPersonalizados.isEmpty());
				
				if(!listaClientesPersonalizados.isEmpty()) {
					for (ClientesPersonalizados clientesPersonalizados2 : listaClientesPersonalizados) {
						if(clientesPersonalizados2.getFechaModificacion() != null) {
							String[] arrOfStr = clientesPersonalizados2.getFechaModificacion().split(" ", 2);
							clientesPersonalizados2.setFechaModificacion(arrOfStr[0]);
						}
					}
					datosPaginacion = clienteResponse.getDatosPaginacion();
					log.info("datosPaginacion: "+datosPaginacion);
					model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
					model.addAttribute("datosPaginacion", datosPaginacion);
					return "convenio/clientesPersonalizados/listaClientesPersonalizados";
				}else {
					datosPaginacion.setTotalPaginas(0);
					model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
					model.addAttribute("datosPaginacion", datosPaginacion);
					model.addAttribute("mensajeError", "Operacion Exitosa.La consulta no arrojo resultado.");
					return "convenio/clientesPersonalizados/listaClientesPersonalizados";
				}
				
				
			}else {
				datosPaginacion.setTotalPaginas(0);
				model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
				model.addAttribute("datosPaginacion", datosPaginacion);
				model.addAttribute("mensajeError", "Operacion Exitosa.La consulta no arrojo resultado.");
				return "convenio/clientesPersonalizados/listaClientesPersonalizados";
			}
			
			
			
			
		} catch (CustomException e) {
			
			log.error("error: "+e);
			datosPaginacion.setTotalPaginas(0);
			model.addAttribute("datosPaginacion", datosPaginacion);
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

		 
		
		ClienteDatosBasicoRequest clienteDatosBasicoRequest = getClienteDatosBasicoRequest();
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
				model.addAttribute("mensajeError", "Operacion Exitosa.La consulta no arrojo resultado.");
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
		
		ClienteRequest clienteRequest = getClienteRequest();
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
		
		ClienteRequest clienteRequest = getClienteRequest();
		clientesPersonalizados.setFlagActivo(true);
		clienteRequest.setCliente(clientesPersonalizados);
		
		try {
			String respuesta = clientePersonalizadoServiceApiRest.crear(clienteRequest);
			//redirectAttributes.addFlashAttribute("mensaje", respuesta);
			//return "redirect:/clientesPersonalizados/index";
			redirectAttributes.addFlashAttribute("mensaje", respuesta+ " Puede crear limites Personalizados al cliiente nuevo");
			return "redirect:/clientesPersonalizados/formLimiteClientePersonalizado/"+clientesPersonalizados.getCodigoIbs();
			
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
	
	public ClienteRequest getClienteRequest() {
		ClienteRequest clienteRequest = new ClienteRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		clienteRequest.setIdUsuario(userName);
		clienteRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		clienteRequest.setCodUsuario(userName);
		clienteRequest.setCanal(canal);
		return clienteRequest;
	}
	
	public ClienteDatosBasicoRequest getClienteDatosBasicoRequest() {
		ClienteDatosBasicoRequest clienteDatosBasicoRequest = new ClienteDatosBasicoRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		clienteDatosBasicoRequest.setIdUsuario(userName);
		clienteDatosBasicoRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		clienteDatosBasicoRequest.setCodUsuario(userName);
		clienteDatosBasicoRequest.setCanal(canal);
		return clienteDatosBasicoRequest;
	}
	
	public LimitesPersonalizadosRequest getLimitesPersonalizadosRequest() {
		LimitesPersonalizadosRequest limitesPersonalizadosRequest = new LimitesPersonalizadosRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		limitesPersonalizadosRequest.setIdUsuario(userName);
		limitesPersonalizadosRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		limitesPersonalizadosRequest.setCodUsuario(userName);
		limitesPersonalizadosRequest.setCanal(canal);
		return limitesPersonalizadosRequest;
	}

	public MonedasRequest getMonedasRequest() {
		MonedasRequest monedasRequest = new MonedasRequest();
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		monedasRequest.setIdUsuario(userName);
		monedasRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		monedasRequest.setCodUsuario(userName);
		monedasRequest.setCanal(canal);
		return monedasRequest;
	}
	
	@ModelAttribute
	public void setGenericos(Model model, HttpServletRequest request) {
		ClientesPersonalizados clientesPersonalizadosSearch = new ClientesPersonalizados();
		model.addAttribute("clientesPersonalizadosSearch", clientesPersonalizadosSearch);
		String uri = request.getRequestURI();
		log.info("uri: "+uri);
		String[] arrUri = uri.split("/");

		arrUri[0] = "Home";
		model.addAttribute("arrUri", arrUri);
	}
}
