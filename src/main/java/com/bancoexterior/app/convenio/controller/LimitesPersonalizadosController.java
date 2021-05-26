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

import com.bancoexterior.app.convenio.apiRest.IClientePersonalizadoServiceApiRest;
import com.bancoexterior.app.convenio.apiRest.ILimitesPersonalizadosServiceApiRest;
import com.bancoexterior.app.convenio.apiRest.IMonedaServiceApiRest;
import com.bancoexterior.app.convenio.dto.ClienteRequest;
import com.bancoexterior.app.convenio.dto.ClienteResponse;
import com.bancoexterior.app.convenio.dto.LimiteResponse;
import com.bancoexterior.app.convenio.dto.LimitesPersonalizadosRequest;
import com.bancoexterior.app.convenio.dto.LimitesPersonalizadosResponse;
import com.bancoexterior.app.convenio.dto.MonedaResponse;
import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.model.ClientesPersonalizados;
import com.bancoexterior.app.convenio.model.LimitesGenerales;
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
	private IClientePersonalizadoServiceApiRest clientePersonalizadoServiceApiRest;
	
	@Autowired
	private IMonedaServiceApiRest monedaServiceApiRest; 
	
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
		}else {
			log.info("error conectar microservicio consultarWs limitesPersonalizados");
			return "redirect:/";
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
		
		log.info("limitesPersonalizadosMoneda: "+respuesta);
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
	        		}else {
	        			log.info("error conectar microservicio actualizarWs limitesPersonalizados");
	        			return "redirect:/";
	        		}	
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
		}else {
			log.info("error conectar microservicio consultarWs limitesPersonalizados");
			return "redirect:/";
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
		}else {
			log.info("error conectar microservicio consultarWs limitesPersonalizados");
			return "redirect:/";
		}	
					
				
		
		return "";
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
		
		Response response = new Response();
		LimitesPersonalizadosResponse limitesPersonalizadosResponse = new LimitesPersonalizadosResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = limitesPersonalizadosServiceApiRest.actulaizarWs(limitesPersonalizadosRequest);
		
		log.info("responseLimitesPersonalizados: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en actualizar limitePersonalizado por codigo");
				try {
					response = mapper.jsonToClass(respuesta.getBody(), Response.class);
					log.info("resultado: "+resultado);
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//redirectAttributes.addFlashAttribute("mensaje", "Registro actualizado");
				redirectAttributes.addFlashAttribute("mensaje", " Codigo :" +response.getResultado().getCodigo() +", descripcion: "+response.getResultado().getDescripcion());
				return "redirect:/limitesPersonalizados/index";
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
					return "convenio/limitesPersonalizados/formLimitesPersonalizadosEdit";
						   
				}
			}
				
		}else {
			log.info("error conectar microservicio actualizarWs limitesPersonalizados");
			return "redirect:/";
		}	
		return "convenio/limitesPersonalizados/formLimitesPersonalizadosEdit";
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
	        		}else {
	        			log.info("error conectar microservicio actualizarWs limitesPersonalizados");
	        			return "redirect:/";
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
		}else {
			log.info("error conectar microservicio consultarWs limitesPersonalizados");
			return "redirect:/";
		}
		
		
		
		return "redirect:/limitesPersonalizados/index";
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
		
		ClienteResponse clienteResponse = new ClienteResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = clientePersonalizadoServiceApiRest.consultarWs(clienteRequest);
		log.info("responseCliente: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista clientes personalizados");
				try {
					clienteResponse= mapper.jsonToClass(respuesta.getBody(), ClienteResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("clienteResponse: "+clienteResponse);
	            log.info(clienteResponse.getResultado().getCodigo());
	            listaClientesPersonalizados = clienteResponse.getListaClientes();
	            model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
	            
	            MonedasRequest monedasRequest = new MonedasRequest();
	    		monedasRequest.setIdUsuario("test");
	    		monedasRequest.setIdSesion("20210101121213");
	    		monedasRequest.setCodUsuario("E66666");
	    		monedasRequest.setCanal("8");
	    		Moneda moneda = new Moneda();
	    		moneda.setFlagActivo(true);
	    		monedasRequest.setMoneda(moneda);
	            
	    		MonedaResponse monedaResponse = new MonedaResponse();
	    		Resultado resultadoMoneda = new Resultado();
	    		WSResponse respuestaMoneda = monedaServiceApiRest.consultar(monedasRequest);
	    		log.info("responseMoneda: "+respuestaMoneda);
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
	    	    		return "convenio/limitesPersonalizados/formLimitesPersonalizados";
	    			}else {
	    				if (respuesta.getStatus() == 422) {
	    					log.info("entro en error 422");
	    					try {
	    						resultadoMoneda = mapper.jsonToClass(respuestaMoneda.getBody(), Resultado.class);
	    						log.info("resultadoMoneda: "+resultadoMoneda);
	    					} catch (IOException e) {
	    						e.printStackTrace();
	    					}
	    					redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultadoMoneda.getCodigo() +" descripcion: "+resultadoMoneda.getDescripcion());
	    					return "redirect:/limitestesPersonalizados/index";
	    				}
	    			}	
	    		}else {
	    			log.info("error conectar microservicio consultarWs monedas");
	    			return "redirect:/";
	    		}	
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
					return "redirect:/limitestesPersonalizados/index";
				}
			}	
		}else {
			log.info("error conectar microservicio consultarWs clientesPersonalizados");
			return "redirect:/";
		}
		
		return "convenio/limitesPersonalizados/formLimitesPersonalizados";
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
		ClienteResponse clienteResponse = new ClienteResponse();
		Resultado resultado = new Resultado();
		
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
			
			WSResponse respuesta = clientePersonalizadoServiceApiRest.consultarWs(clienteRequest);
			log.info("responseCliente: "+respuesta);
			log.info("respuesta.getBody(): "+respuesta.getBody());
			log.info("retorno.getStatus(): "+respuesta.getStatus());
			log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
			
			if(respuesta.isExitoso()) {
				if(respuesta.getStatus() == 200) {
					log.info("Respusta codigo 200 en buscar la lista clientes personalizados");
					try {
						clienteResponse= mapper.jsonToClass(respuesta.getBody(), ClienteResponse.class);
					} catch (IOException e) {
						e.printStackTrace();
					}
		            log.info("clienteResponse: "+clienteResponse);
		            log.info(clienteResponse.getResultado().getCodigo());
		            listaClientesPersonalizados = clienteResponse.getListaClientes();
		            model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
		            
		            MonedaResponse monedaResponse = new MonedaResponse();
		    		Resultado resultadoMoneda = new Resultado();
		    		WSResponse respuestaMoneda = monedaServiceApiRest.consultar(monedasRequest);
		    		log.info("responseMoneda: "+respuestaMoneda);
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
		    	    		return "convenio/limitesPersonalizados/formLimitesPersonalizados";
		    			}else {
		    				if (respuestaMoneda.getStatus() == 422) {
		    					log.info("entro en error 422");
		    					try {
		    						resultadoMoneda = mapper.jsonToClass(respuestaMoneda.getBody(), Resultado.class);
		    						log.info("resultadoMoneda: "+resultadoMoneda);
		    					} catch (IOException e) {
		    						e.printStackTrace();
		    					}
		    					redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultadoMoneda.getCodigo() +" descripcion: "+resultadoMoneda.getDescripcion());
		    					return "convenio/limitesPersonalizados/formLimitesPersonalizados";
		    				}
		    			}	
		    		}else {
		    			log.info("error conectar microservicio consultarWs monedas");
		    			return "redirect:/";
		    		}
		            
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
						return "convenio/limitesPersonalizados/formLimitesPersonalizados";
					}
				}
			}else {
				log.info("error conectar microservicio consultarWs clientesPersonalizados");
				return "redirect:/";
			}	
		}
		
		
		
		
		LimitesPersonalizadosRequest limitesPersonalizadosRequest = new LimitesPersonalizadosRequest();
		limitesPersonalizadosRequest.setIdUsuario("test");
		limitesPersonalizadosRequest.setIdSesion("20210101121213");
		limitesPersonalizadosRequest.setCodUsuario("E66666");
		limitesPersonalizadosRequest.setCanal("8");
		limitesPersonalizados.setFlagActivo(true);
		limitesPersonalizadosRequest.setLimiteCliente(limitesPersonalizados);
		
		Response response = new Response();
		LimitesPersonalizadosResponse limitesPersonalizadosResponse = new LimitesPersonalizadosResponse();
		Resultado resultadoCrear = new Resultado();
		WSResponse respuesta = limitesPersonalizadosServiceApiRest.crearWs(limitesPersonalizadosRequest);
		
		log.info("responseLimitesPersonalizados: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en crear limitePersonalizado por codigo");
				try {
					response = mapper.jsonToClass(respuesta.getBody(), Response.class);
					log.info("response: "+response);
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//redirectAttributes.addFlashAttribute("mensaje", "Registro actualizado");
				redirectAttributes.addFlashAttribute("mensaje", " Codigo :" +response.getResultado().getCodigo() +", descripcion: "+response.getResultado().getDescripcion());
				return "redirect:/limitesPersonalizados/index";
			}else {
				if (respuesta.getStatus() == 422 || respuesta.getStatus() == 400) {
					log.info("entro en error codigo: "+respuesta.getStatus());
					WSResponse respuestaCliente = clientePersonalizadoServiceApiRest.consultarWs(clienteRequest);
					log.info("responseCliente: "+respuestaCliente);
					log.info("respuestaCliente.getBody(): "+respuestaCliente.getBody());
					log.info("respuestaCliente.getStatus(): "+respuestaCliente.getStatus());
					log.info("respuestaCliente.isExitoso(): "+respuestaCliente.isExitoso());
					
					if(respuestaCliente.isExitoso()) {
						if(respuestaCliente.getStatus() == 200) {
							log.info("Respusta codigo 200 en buscar la lista clientes personalizados");
							try {
								clienteResponse= mapper.jsonToClass(respuestaCliente.getBody(), ClienteResponse.class);
							} catch (IOException e) {
								e.printStackTrace();
							}
				            log.info("clienteResponse: "+clienteResponse);
				            log.info(clienteResponse.getResultado().getCodigo());
				            listaClientesPersonalizados = clienteResponse.getListaClientes();
				            model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
				            
				            MonedaResponse monedaResponse = new MonedaResponse();
				    		Resultado resultadoMoneda = new Resultado();
				    		WSResponse respuestaMoneda = monedaServiceApiRest.consultar(monedasRequest);
				    		log.info("responseMoneda: "+respuestaMoneda);
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
				    	    		//return "convenio/limitesPersonalizados/formLimitesPersonalizados";
				    			}else {
				    				if (respuestaMoneda.getStatus() == 422) {
				    					log.info("entro en error 422");
				    					try {
				    						resultadoMoneda = mapper.jsonToClass(respuestaMoneda.getBody(), Resultado.class);
				    						log.info("resultadoMoneda: "+resultadoMoneda);
				    					} catch (IOException e) {
				    						e.printStackTrace();
				    					}
				    					redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultadoMoneda.getCodigo() +" descripcion: "+resultadoMoneda.getDescripcion());
				    					return "convenio/limitesPersonalizados/formLimitesPersonalizados";
				    				}
				    			}	
				    		}else {
				    			log.info("error conectar microservicio consultarWs monedas");
				    			return "redirect:/";
				    		}
				            
						}else {
							if (respuestaCliente.getStatus() == 422) {
								log.info("entro en error 422");
								try {
									resultado = mapper.jsonToClass(respuestaCliente.getBody(), Resultado.class);
									log.info("resultado: "+resultado);
								} catch (IOException e) {
									e.printStackTrace();
								}
								redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
								return "convenio/limitesPersonalizados/formLimitesPersonalizados";
							}
						}
					}else {
						log.info("error conectar microservicio consultarWs clientesPersonalizados");
						return "redirect:/";
					}
					
					
					
					try {
						response = mapper.jsonToClass(respuesta.getBody(), Response.class);
						log.info("response: "+response);
					} catch (IOException e) {
						e.printStackTrace();
					}
					redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
					result.addError(new ObjectError("codMoneda", " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion()));
					return "convenio/limitesPersonalizados/formLimitesPersonalizados";
						   
				}
			}
				
		}else {
			log.info("error conectar microservicio crearWs limitesPersonalizados");
			return "redirect:/";
		}
		
		return "convenio/limitesPersonalizados/formLimitesPersonalizados";
	}
	
}
