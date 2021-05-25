package com.bancoexterior.app.convenio.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bancoexterior.app.convenio.apiRest.ILimitesGeneralesServiceApirest;
import com.bancoexterior.app.convenio.apiRest.IMonedaServiceApiRest;
import com.bancoexterior.app.convenio.dto.LimiteRequest;
import com.bancoexterior.app.convenio.dto.LimiteResponse;
import com.bancoexterior.app.convenio.dto.MonedaResponse;
import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.dto.TasaResponse;
import com.bancoexterior.app.convenio.model.LimitesGenerales;
import com.bancoexterior.app.convenio.model.Moneda;
import com.bancoexterior.app.convenio.model.Tasa;
import com.bancoexterior.app.convenio.response.Response;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.convenio.services.ILimitesGeneralesService;
import com.bancoexterior.app.convenio.services.IMonedaService;
import com.bancoexterior.app.convenio.services.restApi.model.WSResponse;
import com.bancoexterior.app.util.Mapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/limitesGenerales")
public class LimitesGeneralesController {

	@Autowired
	private ILimitesGeneralesServiceApirest limitesGeneralesServiceApirest;
	
	@Autowired
	private IMonedaServiceApiRest monedaServiceApiRest;
	
	@Autowired
	private IMonedaService monedaService;
	
	@Autowired
	private ILimitesGeneralesService limitesGeneralesService;
	
	@Autowired 
	private Mapper mapper;
	
	
	@GetMapping("/index")
	public String index(Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a index listaLimitesWs");
		
		List<LimitesGenerales> listaLimitesGenerales = new ArrayList<>();
		
		LimiteRequest limiteRequest = new LimiteRequest(); 
		limiteRequest.setIdUsuario("test");
		limiteRequest.setIdSesion("20210101121213");
		limiteRequest.setCodUsuario("E66666");
		limiteRequest.setCanal("8");
		LimitesGenerales limite = new LimitesGenerales();
		//limite.setFlagActivo(false);
		limiteRequest.setLimite(limite);
		
		LimiteResponse limiteResponse = new LimiteResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = limitesGeneralesServiceApirest.consultarWs(limiteRequest);
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista tasa");
	            try {
					limiteResponse = mapper.jsonToClass(respuesta.getBody(), LimiteResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("limiteResponse: "+limiteResponse);
	            log.info(limiteResponse.getResultado().getCodigo());
	            listaLimitesGenerales = limiteResponse.getLimites();
	            model.addAttribute("listaLimitesGenerales", listaLimitesGenerales);
	    		return "convenio/limitesGenerales/listaLimitesGenerales";
			}else {
				if (respuesta.getStatus() == 422) {
					log.info("entro en error 422");
					try {
						resultado = mapper.jsonToClass(respuesta.getBody(), Resultado.class);
						log.info("resultado: "+resultado);
					} catch (IOException e) {
						e.printStackTrace();
					}
					redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
					return "convenio/tasa/listaTasas";
				}
			}
		}
		
		return "convenio/limitesGenerales/listaLimitesGenerales";
	}
	
	@GetMapping("/detalle/{codMoneda}/{tipoTransaccion}/{tipoCliente}")
	public String detalleWs(@PathVariable("codMoneda") String codMoneda, @PathVariable("tipoTransaccion") String tipoTransaccion,
			@PathVariable("tipoCliente") String tipoCliente, LimitesGenerales limitesGenerales ,Model model, RedirectAttributes redirectAttributes) {
		log.info("detalleWs");
		log.info(codMoneda);
		log.info(tipoTransaccion);
		log.info(tipoCliente);
		
		LimitesGenerales limitesGeneralesEdit = new LimitesGenerales();
				
		
		LimiteRequest limiteRequest = new LimiteRequest(); 
		limiteRequest.setIdUsuario("test");
		limiteRequest.setIdSesion("20210101121213");
		limiteRequest.setCodUsuario("E66666");
		limiteRequest.setCanal("8");
		LimitesGenerales limite = new LimitesGenerales();
		limite.setCodMoneda(codMoneda);
		limite.setTipoTransaccion(tipoTransaccion);
		limite.setTipoCliente(tipoCliente);
		limiteRequest.setLimite(limite);
		
		Response response = new Response();
		LimiteResponse limiteResponse = new LimiteResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = limitesGeneralesServiceApirest.consultarWs(limiteRequest);
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar limite por codigo");
				try {
					limiteResponse = mapper.jsonToClass(respuesta.getBody(), LimiteResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	           
	            log.info("limiteResponse: "+limiteResponse);
	            log.info(limiteResponse.getResultado().getCodigo());
	            if(limiteResponse.getResultado().getCodigo().equals("0000")){
	            	log.info("Respusta codigo 0000 si existe el limite");
	            	limitesGeneralesEdit = limiteResponse.getLimites().get(0);
	            	model.addAttribute("limitesGenerales", limitesGeneralesEdit);
	            	return "convenio/limitesGenerales/formLimitesGeneralesDetalle";
	            }else{
	            	//if(monedaResponse.getResultado().getCodigo().equals("0000")){
	            	if(limiteResponse.getResultado().getCodigo().equals("0001")){
	            		log.info("Respusta codigo 0001 recurso no encontrado");
	            		resultado = limiteResponse.getResultado();
	            		redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
	            		return "redirect:/limitesGenerales/index";
	            	}
	            }
			}if(respuesta.getStatus() == 422) {
				log.info("Respusta codigo 422 validacion");
        		
				try {
					response = mapper.jsonToClass(respuesta.getBody(), Response.class);
					log.info("response: "+response);
				} catch (IOException e) {
					e.printStackTrace();
				} 
				
        		redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion());
        		return "redirect:/limitesGenerales/index";
			}
		}
		
		return "";
	
	}
	
	
	@GetMapping("/edit/{codMoneda}/{tipoTransaccion}/{tipoCliente}")
	public String editarWs(@PathVariable("codMoneda") String codMoneda, @PathVariable("tipoTransaccion") String tipoTransaccion,
			@PathVariable("tipoCliente") String tipoCliente, LimitesGenerales limitesGenerales ,Model model, RedirectAttributes redirectAttributes) {
		log.info("editarWs");
		log.info(codMoneda);
		log.info(tipoTransaccion);
		log.info(tipoCliente);
		
		LimitesGenerales limitesGeneralesEdit = new LimitesGenerales();
				
		
		LimiteRequest limiteRequest = new LimiteRequest(); 
		limiteRequest.setIdUsuario("test");
		limiteRequest.setIdSesion("20210101121213");
		limiteRequest.setCodUsuario("E66666");
		limiteRequest.setCanal("8");
		LimitesGenerales limite = new LimitesGenerales();
		limite.setCodMoneda(codMoneda);
		limite.setTipoTransaccion(tipoTransaccion);
		limite.setTipoCliente(tipoCliente);
		limiteRequest.setLimite(limite);
		
		Response response = new Response();
		LimiteResponse limiteResponse = new LimiteResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = limitesGeneralesServiceApirest.consultarWs(limiteRequest);
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar limite por codigo");
				try {
					limiteResponse = mapper.jsonToClass(respuesta.getBody(), LimiteResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	           
	            log.info("limiteResponse: "+limiteResponse);
	            log.info(limiteResponse.getResultado().getCodigo());
	            if(limiteResponse.getResultado().getCodigo().equals("0000")){
	            	log.info("Respusta codigo 0000 si existe el limite");
	            	limitesGeneralesEdit = limiteResponse.getLimites().get(0);
	            	model.addAttribute("limitesGenerales", limitesGeneralesEdit);
	            	return "convenio/limitesGenerales/formLimitesGeneralesEdit";
	            }else{
	            	//if(monedaResponse.getResultado().getCodigo().equals("0000")){
	            	if(limiteResponse.getResultado().getCodigo().equals("0001")){
	            		log.info("Respusta codigo 0001 recurso no encontrado");
	            		resultado = limiteResponse.getResultado();
	            		redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
	            		return "redirect:/limitesGenerales/index";
	            	}
	            }
			}
		}
		
		return "";
	
	}	
	
	@PostMapping("/guardar")
	public String guardarWs(LimitesGenerales limitesGenerales, BindingResult result,  RedirectAttributes redirectAttributes) {
		log.info("guardarWs");
		log.info("limitesGenerales: "+limitesGenerales);
		
		
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				log.info("Ocurrio un error: " + error.getDefaultMessage());
			}
		
			return "convenio/limitesGenerales/formLimitesGeneralesEdit";
		}
		
		LimiteRequest limiteRequest = new LimiteRequest(); 
		limiteRequest.setIdUsuario("test");
		limiteRequest.setIdSesion("20210101121213");
		limiteRequest.setCodUsuario("E66666");
		limiteRequest.setCanal("8");
		limiteRequest.setLimite(limitesGenerales);
		
		Response response = new Response();
		LimiteResponse limiteResponse = new LimiteResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = limitesGeneralesServiceApirest.actualizarWs(limiteRequest);
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar limite por codigo");
				try {
					response = mapper.jsonToClass(respuesta.getBody(), Response.class);
					log.info("resultado: "+resultado);
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//redirectAttributes.addFlashAttribute("mensaje", "Registro actualizado");
				redirectAttributes.addFlashAttribute("mensaje", " Codigo :" +response.getResultado().getCodigo() +", descripcion: "+response.getResultado().getDescripcion());
				return "redirect:/limitesGenerales/index";
			}else {
				if (respuesta.getStatus() == 422) {
					log.info("entro en error 422");
					try {
						response = mapper.jsonToClass(respuesta.getBody(), Response.class);
						log.info("response: "+response);
					} catch (IOException e) {
						e.printStackTrace();
					}
					redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
					result.addError(new ObjectError("codMoneda", " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion()));
					return "convenio/limitesGenerales/formLimitesGeneralesEdit";
						   
				}
			}
		}else {
			return "redirect:/";
		}	
		return "convenio/limitesGenerales/formLimitesGeneralesEdit";
	}
	
	@PostMapping("/save")
	public String saveWs(LimitesGenerales limitesGenerales, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		log.info("saveWs");
		log.info("limitesGenerales: "+limitesGenerales);
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
			
			MonedaResponse monedaResponse = new MonedaResponse();
			WSResponse respuesta = monedaServiceApiRest.consultar(monedasRequest);
			log.info("responseMoneda: "+respuesta);
			log.info("respuesta.getBody(): "+respuesta.getBody());
			log.info("retorno.getStatus(): "+respuesta.getStatus());
			log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
			
			if(respuesta.isExitoso()) {
				if(respuesta.getStatus() == 200) {
					log.info("Respusta codigo 200 en buscar la lista moneda");
		            try {
						monedaResponse = mapper.jsonToClass(respuesta.getBody(), MonedaResponse.class);
					} catch (IOException e) {
						e.printStackTrace();
					}
		            log.info("monedaResponse: "+monedaResponse);
		            log.info(monedaResponse.getResultado().getCodigo());
		            listaMonedas = monedaResponse.getMonedas();
		            model.addAttribute("listaMonedas", listaMonedas);
		            return "convenio/limitesGenerales/formLimitesGenerales";
				}
			}
			
		}
		
		LimiteRequest limiteRequest = new LimiteRequest(); 
		limiteRequest.setIdUsuario("test");
		limiteRequest.setIdSesion("20210101121213");
		limiteRequest.setCodUsuario("E66666");
		limiteRequest.setCanal("8");
		limitesGenerales.setFlagActivo(true);
		limiteRequest.setLimite(limitesGenerales);
		log.info("limitesGenerales: "+limitesGenerales);
		
		Response response = new Response();
		LimiteResponse limiteResponse = new LimiteResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = limitesGeneralesServiceApirest.crearWs(limiteRequest);
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar limite por codigo");
				try {
					response = mapper.jsonToClass(respuesta.getBody(), Response.class);
					log.info("resultado: "+resultado);
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//redirectAttributes.addFlashAttribute("mensaje", "Registro actualizado");
				redirectAttributes.addFlashAttribute("mensaje", " Codigo :" +response.getResultado().getCodigo() +", descripcion: "+response.getResultado().getDescripcion());
				return "redirect:/limitesGenerales/index";
			}else {
				if (respuesta.getStatus() == 422 || respuesta.getStatus() == 400) {
					log.info("entro en error con codigo: "+respuesta.getStatus());
					MonedaResponse monedaResponse = new MonedaResponse();
					WSResponse respuestaMoneda = monedaServiceApiRest.consultar(monedasRequest);
					log.info("respuestaMoneda: "+respuestaMoneda);
					log.info("respuestaMoneda.getBody(): "+respuestaMoneda.getBody());
					log.info("respuestaMoneda.getStatus(): "+respuestaMoneda.getStatus());
					log.info("respuestaMoneda.isExitoso(): "+respuestaMoneda.isExitoso());
					
					if(respuestaMoneda.isExitoso()) {
						if(respuestaMoneda.getStatus() == 200) {
							log.info("Respusta codigo 200 en buscar la lista moneda");
				            try {
								monedaResponse = mapper.jsonToClass(respuestaMoneda.getBody(), MonedaResponse.class);
							} catch (IOException e) {
								e.printStackTrace();
							}
				            log.info("monedaResponse: "+monedaResponse);
				            log.info(monedaResponse.getResultado().getCodigo());
				            listaMonedas = monedaResponse.getMonedas();
				            model.addAttribute("listaMonedas", listaMonedas);
				            
						}
					}
					
					try {
						response = mapper.jsonToClass(respuesta.getBody(), Response.class);
						log.info("response: "+response);
					} catch (IOException e) {
						e.printStackTrace();
					}
					redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
					result.addError(new ObjectError("codMoneda", " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion()));
					return "convenio/limitesGenerales/formLimitesGenerales";
						   
				}
			}
		}else {
			return "redirect:/";
		}
		return "convenio/limitesGenerales/formLimitesGenerales";
	}	
	
	@GetMapping("/activar/{codMoneda}/{tipoTransaccion}/{tipoCliente}")
	public String activarWs(@PathVariable("codMoneda") String codMoneda, @PathVariable("tipoTransaccion") String tipoTransaccion,
			@PathVariable("tipoCliente") String tipoCliente, LimitesGenerales limitesGenerales ,Model model, RedirectAttributes redirectAttributes) {
		log.info("activarWs");
		log.info(codMoneda);
		log.info(tipoTransaccion);
		log.info(tipoCliente);
		
		LimitesGenerales limitesGeneralesEdit = new LimitesGenerales();
				
		
		LimiteRequest limiteRequest = new LimiteRequest(); 
		limiteRequest.setIdUsuario("test");
		limiteRequest.setIdSesion("20210101121213");
		limiteRequest.setCodUsuario("E66666");
		limiteRequest.setCanal("8");
		LimitesGenerales limite = new LimitesGenerales();
		limite.setCodMoneda(codMoneda);
		limite.setTipoTransaccion(tipoTransaccion);
		limite.setTipoCliente(tipoCliente);
		limiteRequest.setLimite(limite);
		
		Response response = new Response();
		LimiteResponse limiteResponse = new LimiteResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = limitesGeneralesServiceApirest.consultarWs(limiteRequest);
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar limite por codigo");
				try {
					limiteResponse = mapper.jsonToClass(respuesta.getBody(), LimiteResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	           
	            log.info("limiteResponse: "+limiteResponse);
	            log.info(limiteResponse.getResultado().getCodigo());
	            if(limiteResponse.getResultado().getCodigo().equals("0000")){
	            	log.info("Respusta codigo 0000 si existe la modena");
	            	limitesGeneralesEdit = limiteResponse.getLimites().get(0);
	            	limitesGeneralesEdit.setFlagActivo(true);
	            	limiteRequest.setLimite(limitesGeneralesEdit);
	            	WSResponse respuestaActualizar = limitesGeneralesServiceApirest.actualizarWs(limiteRequest);
	            	log.info("respuestaActualizar: "+respuestaActualizar);
	            	log.info("respuestaActualizar.getBody(): "+respuestaActualizar.getBody());
	            	log.info("respuestaActualizar.getStatus(): "+respuestaActualizar.getStatus());
	        		log.info("respuestaActualizar.isExitoso(): "+respuestaActualizar.isExitoso());
	        		if(respuestaActualizar.isExitoso()) {
	        			if(respuestaActualizar.getStatus() == 200) {
	        				log.info("Respusta codigo 200 en Actualizar el limiteGeneralizado por codigo");
	        				try {
        						response = mapper.jsonToClass(respuestaActualizar.getBody(), Response.class);
        						log.info("response: "+response);
        						
        						
        					} catch (IOException e) {
        						e.printStackTrace();
        					}
        					redirectAttributes.addFlashAttribute("mensaje", " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion());
        					return "redirect:/limitesGenerales/index";
	        				
	        				
	        			}else {
	        				if (respuestaActualizar.getStatus() == 422 || respuestaActualizar.getStatus() == 400) {
	        					log.info("Respusta codigo " +respuestaActualizar.getStatus()+ "en Actualizar la moneda por codigo");
	        					try {
	        						response = mapper.jsonToClass(respuestaActualizar.getBody(), Response.class);
	        						log.info("response: "+response);
	        						
	        						
	        					} catch (IOException e) {
	        						e.printStackTrace();
	        					}
	        					redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion());
	        					return "redirect:/limitesGenerales/index";
	        				}
	        			}
	        		}	
	            }else{
	            	//if(monedaResponse.getResultado().getCodigo().equals("0000")){
	            	if(limiteResponse.getResultado().getCodigo().equals("0001")){
	            		log.info("Respusta codigo 0001 recurso no encontrado");
	            		resultado = limiteResponse.getResultado();
	            		redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
	            		return "redirect:/limitesGenerales/index";
	            	}
	            }
			}
		}	
		
		
		return "redirect:/limitesGenerales/index";
	}
	
	
	@GetMapping("/desactivar/{codMoneda}/{tipoTransaccion}/{tipoCliente}")
	public String desactivarWs(@PathVariable("codMoneda") String codMoneda, @PathVariable("tipoTransaccion") String tipoTransaccion,
			@PathVariable("tipoCliente") String tipoCliente, LimitesGenerales limitesGenerales ,Model model, RedirectAttributes redirectAttributes) {
		log.info("desactivarWs");
		log.info(codMoneda);
		log.info(tipoTransaccion);
		log.info(tipoCliente);
		
		LimitesGenerales limitesGeneralesEdit = new LimitesGenerales();
				
		
		LimiteRequest limiteRequest = new LimiteRequest(); 
		limiteRequest.setIdUsuario("test");
		limiteRequest.setIdSesion("20210101121213");
		limiteRequest.setCodUsuario("E66666");
		limiteRequest.setCanal("8");
		LimitesGenerales limite = new LimitesGenerales();
		limite.setCodMoneda(codMoneda);
		limite.setTipoTransaccion(tipoTransaccion);
		limite.setTipoCliente(tipoCliente);
		limiteRequest.setLimite(limite);
		
		Response response = new Response();
		LimiteResponse limiteResponse = new LimiteResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = limitesGeneralesServiceApirest.consultarWs(limiteRequest);
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar limite por codigo");
				try {
					limiteResponse = mapper.jsonToClass(respuesta.getBody(), LimiteResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	           
	            log.info("limiteResponse: "+limiteResponse);
	            log.info(limiteResponse.getResultado().getCodigo());
	            if(limiteResponse.getResultado().getCodigo().equals("0000")){
	            	log.info("Respusta codigo 0000 si existe la modena");
	            	limitesGeneralesEdit = limiteResponse.getLimites().get(0);
	            	limitesGeneralesEdit.setFlagActivo(false);
	            	limiteRequest.setLimite(limitesGeneralesEdit);
	            	WSResponse respuestaActualizar = limitesGeneralesServiceApirest.actualizarWs(limiteRequest);
	            	log.info("respuestaActualizar: "+respuestaActualizar);
	            	log.info("respuestaActualizar.getBody(): "+respuestaActualizar.getBody());
	            	log.info("respuestaActualizar.getStatus(): "+respuestaActualizar.getStatus());
	        		log.info("respuestaActualizar.isExitoso(): "+respuestaActualizar.isExitoso());
	        		if(respuestaActualizar.isExitoso()) {
	        			if(respuestaActualizar.getStatus() == 200) {
	        				log.info("Respusta codigo 200 en Actualizar la moneda por codigo");
	        				try {
        						response = mapper.jsonToClass(respuestaActualizar.getBody(), Response.class);
        						log.info("response: "+response);
        						
        						
        					} catch (IOException e) {
        						e.printStackTrace();
        					}
        					redirectAttributes.addFlashAttribute("mensaje", " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion());
        					return "redirect:/limitesGenerales/index";
	        				
	        				
	        			}else {
	        				if (respuestaActualizar.getStatus() == 422 || respuestaActualizar.getStatus() == 400) {
	        					log.info("Respusta codigo " +respuestaActualizar.getStatus()+ "en Actualizar la moneda por codigo");
	        					try {
	        						response = mapper.jsonToClass(respuestaActualizar.getBody(), Response.class);
	        						log.info("response: "+response);
	        						
	        						
	        					} catch (IOException e) {
	        						e.printStackTrace();
	        					}
	        					redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion());
	        					return "redirect:/limitesGenerales/index";
	        				}
	        			}
	        		}	
	            }else{
	            	//if(monedaResponse.getResultado().getCodigo().equals("0000")){
	            	if(limiteResponse.getResultado().getCodigo().equals("0001")){
	            		log.info("Respusta codigo 0001 recurso no encontrado");
	            		resultado = limiteResponse.getResultado();
	            		redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
	            		return "redirect:/monedas/index";
	            	}
	            }
			}
		}	
		
		
		return "redirect:/limitesGenerales/listaLimitesGenerales";
	}
	
	@GetMapping("/formLimitesGenerales")
	public String formLimitesGenerales(LimitesGenerales limitesGenerales,  Model model) {
		List<Moneda> listaMonedas = new ArrayList<>();
		
		MonedasRequest monedasRequest = new MonedasRequest();
		monedasRequest.setIdUsuario("test");
		monedasRequest.setIdSesion("20210101121213");
		monedasRequest.setCodUsuario("E66666");
		monedasRequest.setCanal("8");
		Moneda moneda = new Moneda();
		moneda.setFlagActivo(true);
		monedasRequest.setMoneda(moneda);
		
		//monedaService.consultaMonedasApiRest(monedasRequest);
		MonedaResponse monedaResponse = new MonedaResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = monedaServiceApiRest.consultar(monedasRequest);
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista moneda");
	            try {
					monedaResponse = mapper.jsonToClass(respuesta.getBody(), MonedaResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("monedaResponse: "+monedaResponse);
	            log.info(monedaResponse.getResultado().getCodigo());
	            listaMonedas = monedaResponse.getMonedas();
	            model.addAttribute("listaMonedas", listaMonedas);
	    		return "convenio/limitesGenerales/formLimitesGenerales";
			}
		}
		return "convenio/limitesGenerales/formLimitesGenerales";
	}
	
}
