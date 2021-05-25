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

import com.bancoexterior.app.convenio.apiRest.ILimitesPersonalizadosServiceApiRest;
import com.bancoexterior.app.convenio.dto.LimiteResponse;
import com.bancoexterior.app.convenio.dto.LimitesPersonalizadosRequest;
import com.bancoexterior.app.convenio.dto.LimitesPersonalizadosResponse;
import com.bancoexterior.app.convenio.model.ClientesPersonalizados;
import com.bancoexterior.app.convenio.model.LimitesPersonalizados;
import com.bancoexterior.app.convenio.model.Moneda;
import com.bancoexterior.app.convenio.response.Response;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.convenio.services.IClientesPersonalizadosService;
import com.bancoexterior.app.convenio.services.ILimitesPersonalizadosService;
import com.bancoexterior.app.convenio.services.IMonedaService;
import com.bancoexterior.app.convenio.services.restApi.model.WSResponse;
import com.bancoexterior.app.util.Mapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/limitesPersonalizados")
public class LimitesPersonalizadosController {

	@Autowired
	private ILimitesPersonalizadosServiceApiRest limitesPersonalizadosServiceApiRest; 
	
	@Autowired 
	private Mapper mapper;
	
	@Autowired
	private IClientesPersonalizadosService clientesPersonalizadosService;
	
	@Autowired
	private IMonedaService monedaService;
	
	
	@Autowired
	private ILimitesPersonalizadosService limitesPersonalizadosService;
	
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
		
		LimitesPersonalizadosResponse limitesPersonalizadosResponse = new LimitesPersonalizadosResponse();
		
		Resultado resultado = new Resultado();
		WSResponse respuesta = limitesPersonalizadosServiceApiRest.consultarWs(limitesPersonalizadosRequest);
		log.info("responseLimitesPersonalizados: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista limites personalizados");
	            try {
					limitesPersonalizadosResponse= mapper.jsonToClass(respuesta.getBody(), LimitesPersonalizadosResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("limitesPersonalizadosResponse: "+limitesPersonalizadosResponse);
	            log.info(limitesPersonalizadosResponse.getResultado().getCodigo());
	            listaLimitesPersonalizados = limitesPersonalizadosResponse.getLimitesPersonalizados();
	            model.addAttribute("listaLimitesPersonalizados", listaLimitesPersonalizados);
	    		return "convenio/limitesPersonalizados/listaLimitesPersonalizados";
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
		
		return "convenio/limitesPersonalizados/listaLimitesPersonalizados";
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
		
		Response response = new Response();
		LimitesPersonalizadosResponse limitesPersonalizadosResponse = new LimitesPersonalizadosResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = limitesPersonalizadosServiceApiRest.consultarWs(limitesPersonalizadosRequest);
		
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar limitePersonalizado por codigo");
				try {
					limitesPersonalizadosResponse = mapper.jsonToClass(respuesta.getBody(), LimitesPersonalizadosResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	           
	            log.info("limitesPersonalizadosResponse: "+limitesPersonalizadosResponse);
	            log.info(limitesPersonalizadosResponse.getResultado().getCodigo());
	            if(limitesPersonalizadosResponse.getResultado().getCodigo().equals("0000")){
	            	log.info("Respusta codigo 0000 si existe la modena");
	            	limitesPersonalizadosEdit = limitesPersonalizadosResponse.getLimitesPersonalizados().get(0);
	            	limitesPersonalizadosEdit.setFlagActivo(true);
	            	limitesPersonalizadosRequest.setLimiteCliente(limitesPersonalizadosEdit);
	            	WSResponse respuestaActualizar = limitesPersonalizadosServiceApiRest.actulaizarWs(limitesPersonalizadosRequest);
	            	log.info("respuestaActualizar: "+respuestaActualizar);
	            	log.info("respuestaActualizar.getBody(): "+respuestaActualizar.getBody());
	            	log.info("respuestaActualizar.getStatus(): "+respuestaActualizar.getStatus());
	        		log.info("respuestaActualizar.isExitoso(): "+respuestaActualizar.isExitoso());
	        		if(respuestaActualizar.isExitoso()) {
	        			if(respuestaActualizar.getStatus() == 200) {
	        				log.info("Respusta codigo 200 en Actualizar el limitePersonalizado por codigo");
	        				try {
        						response = mapper.jsonToClass(respuestaActualizar.getBody(), Response.class);
        						log.info("response: "+response);
        						
        						
        					} catch (IOException e) {
        						e.printStackTrace();
        					}
        					redirectAttributes.addFlashAttribute("mensaje", " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion());
        					return "redirect:/limitesPersonalizados/index";
	        				
	        				
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
	        					return "redirect:/limitesPersonalizados/index";
	        				}
	        			}
	        		}	
	            }else{
	            	//if(monedaResponse.getResultado().getCodigo().equals("0000")){
	            	if(limitesPersonalizadosResponse.getResultado().getCodigo().equals("0001")){
	            		log.info("Respusta codigo 0001 recurso no encontrado");
	            		resultado = limitesPersonalizadosResponse.getResultado();
	            		redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
	            		return "redirect://limitesPersonalizados/index";
	            	}
	            }
			}
		}
		
		
		
		return "redirect:/limitesPersonalizados/index";
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
		
		Response response = new Response();
		LimitesPersonalizadosResponse limitesPersonalizadosResponse = new LimitesPersonalizadosResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = limitesPersonalizadosServiceApiRest.consultarWs(limitesPersonalizadosRequest);
		
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar limitePersonalizado por codigo");
				try {
					limitesPersonalizadosResponse = mapper.jsonToClass(respuesta.getBody(), LimitesPersonalizadosResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	           
	            log.info("limitesPersonalizadosResponse: "+limitesPersonalizadosResponse);
	            log.info(limitesPersonalizadosResponse.getResultado().getCodigo());
	            if(limitesPersonalizadosResponse.getResultado().getCodigo().equals("0000")){
	            	log.info("Respusta codigo 0000 si existe el limite");
	            	limitesPersonalizadosEdit = limitesPersonalizadosResponse.getLimitesPersonalizados().get(0);
	            	model.addAttribute("limitesPersonalizados", limitesPersonalizadosEdit);
	            	return "convenio/limitesPersonalizados/formLimitesPersonalizadosEdit";
	            }else{
	            	//if(monedaResponse.getResultado().getCodigo().equals("0000")){
	            	if(limitesPersonalizadosResponse.getResultado().getCodigo().equals("0001")){
	            		log.info("Respusta codigo 0001 recurso no encontrado");
	            		resultado = limitesPersonalizadosResponse.getResultado();
	            		redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
	            		return "redirect:/limitesPersonalizados/index";
	            	}
	            }
			}
		}	
					
				
		
		return "";
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
		
		Response response = new Response();
		LimitesPersonalizadosResponse limitesPersonalizadosResponse = new LimitesPersonalizadosResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = limitesPersonalizadosServiceApiRest.consultarWs(limitesPersonalizadosRequest);
		
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar limitePersonalizado por codigo");
				try {
					limitesPersonalizadosResponse = mapper.jsonToClass(respuesta.getBody(), LimitesPersonalizadosResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	           
	            log.info("limitesPersonalizadosResponse: "+limitesPersonalizadosResponse);
	            log.info(limitesPersonalizadosResponse.getResultado().getCodigo());
	            if(limitesPersonalizadosResponse.getResultado().getCodigo().equals("0000")){
	            	log.info("Respusta codigo 0000 si existe la modena");
	            	limitesPersonalizadosEdit = limitesPersonalizadosResponse.getLimitesPersonalizados().get(0);
	            	limitesPersonalizadosEdit.setFlagActivo(false);
	            	limitesPersonalizadosRequest.setLimiteCliente(limitesPersonalizadosEdit);
	            	WSResponse respuestaActualizar = limitesPersonalizadosServiceApiRest.actulaizarWs(limitesPersonalizadosRequest);
	            	log.info("respuestaActualizar: "+respuestaActualizar);
	            	log.info("respuestaActualizar.getBody(): "+respuestaActualizar.getBody());
	            	log.info("respuestaActualizar.getStatus(): "+respuestaActualizar.getStatus());
	        		log.info("respuestaActualizar.isExitoso(): "+respuestaActualizar.isExitoso());
	        		if(respuestaActualizar.isExitoso()) {
	        			if(respuestaActualizar.getStatus() == 200) {
	        				log.info("Respusta codigo 200 en Actualizar el limitePersonalizado por codigo");
	        				try {
        						response = mapper.jsonToClass(respuestaActualizar.getBody(), Response.class);
        						log.info("response: "+response);
        						
        						
        					} catch (IOException e) {
        						e.printStackTrace();
        					}
        					redirectAttributes.addFlashAttribute("mensaje", " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion());
        					return "redirect:/limitesPersonalizados/index";
	        				
	        				
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
	        					return "redirect:/limitesPersonalizados/index";
	        				}
	        			}
	        		}	
	            }else{
	            	//if(monedaResponse.getResultado().getCodigo().equals("0000")){
	            	if(limitesPersonalizadosResponse.getResultado().getCodigo().equals("0001")){
	            		log.info("Respusta codigo 0001 recurso no encontrado");
	            		resultado = limitesPersonalizadosResponse.getResultado();
	            		redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
	            		return "redirect://limitesPersonalizados/index";
	            	}
	            }
			}
		}
		
		
		
		return "redirect:/limitesPersonalizados/index";
	}
	
	@GetMapping("/formLimitesPersonalizados")
	public String formClientePersonalizado(Model model) {
		
		List<ClientesPersonalizados> listaClientesPersonalizados = clientesPersonalizadosService.buscarTodos();
		
		for (ClientesPersonalizados clientesPersonalizados : listaClientesPersonalizados) {
			log.info("clientesPersonalizados: "+clientesPersonalizados);
		}
		
		List<Moneda> listaMonedas = monedaService.buscarTodas();
		for (Moneda moneda : listaMonedas) {
			log.info("moneda: "+moneda);
		}
		
		model.addAttribute("listaMonedas", listaMonedas);
		model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
		return "convenio/limitesPersonalizados/formLimitesPersonalizados";
	}
	
}
