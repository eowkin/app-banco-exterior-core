package com.bancoexterior.app.convenio.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bancoexterior.app.convenio.apiRest.ILimitesGeneralesServiceApirest;
import com.bancoexterior.app.convenio.dto.LimiteRequest;
import com.bancoexterior.app.convenio.dto.LimiteResponse;
import com.bancoexterior.app.convenio.dto.MonedaResponse;
import com.bancoexterior.app.convenio.dto.TasaResponse;
import com.bancoexterior.app.convenio.model.LimitesGenerales;
import com.bancoexterior.app.convenio.model.Moneda;
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
	public String formLimitesGenerales(Model model) {
		
		List<Moneda> listaMonedas = monedaService.buscarTodas();
		
		for (Moneda moneda : listaMonedas) {
			log.info("moneda: "+moneda);
		}
		model.addAttribute("listaMonedas", listaMonedas);
		
		return "convenio/limitesGenerales/formLimitesGenerales";
	}
	
}
