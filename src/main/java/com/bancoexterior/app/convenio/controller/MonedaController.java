package com.bancoexterior.app.convenio.controller;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

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
import com.bancoexterior.app.convenio.dto.MonedaResponse;
import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.model.Moneda;
import com.bancoexterior.app.convenio.response.Response;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.convenio.services.IMonedaService;
import com.bancoexterior.app.convenio.services.restApi.model.WSResponse;
import com.bancoexterior.app.util.Mapper;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/monedas")
public class MonedaController {

	@Autowired
	private IMonedaServiceApiRest monedaServiceApiRest;
	
	@Autowired 
	private Mapper mapper;
		
	@GetMapping("/index")
	public String listaMonedasWs(Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a index listaMonedasWs");
		
		List<Moneda> listMonedas = new ArrayList<>();
		
		MonedasRequest monedasRequest = new MonedasRequest();
		monedasRequest.setIdUsuario("test");
		monedasRequest.setIdSesion("20210101121213");
		monedasRequest.setCodUsuario("E66666");
		monedasRequest.setCanal("8");
		Moneda moneda = new Moneda();
		//moneda.setCodMoneda("EUR1");
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
	            listMonedas = monedaResponse.getMonedas();
	            model.addAttribute("listMonedas", listMonedas);
	    		return "convenio/moneda/listaMonedas";
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
					return "convenio/moneda/listaMonedas";
				}
			}
		}
		
		//model.addAttribute("listMonedas", listMonedas);
		return "redirect:/";
	}
	
	
	
	
	
	
	@GetMapping("/formMoneda")
	public String formMoneda(Moneda moneda, Model model) {
		
		return "convenio/moneda/formMoneda";
	}	
	
	
	@PostMapping("/save")
	public String saveWs(@Valid  Moneda moneda, BindingResult result, RedirectAttributes redirectAttributes) {
		
		log.info("saveWs");
		log.info("moneda: "+moneda);
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				log.info("Ocurrio un error: " + error.getDefaultMessage());
			}
		
			return "convenio/moneda/formMoneda";
		}
		
		MonedasRequest monedasRequest = new MonedasRequest();
		monedasRequest.setIdUsuario("test");
		monedasRequest.setIdSesion("20210101121213");
		monedasRequest.setCodUsuario("E66666");
		monedasRequest.setCanal("8");
		Moneda monedaBuscar = new Moneda();
		monedaBuscar.setCodMoneda(moneda.getCodMoneda());
		monedasRequest.setMoneda(monedaBuscar);
		
		MonedaResponse monedaResponse = new MonedaResponse();
		Response response = new Response();
		Resultado resultado = new Resultado();
		WSResponse respuesta = monedaServiceApiRest.consultar(monedasRequest);
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar moneda por codigo");
	            try {
					monedaResponse = mapper.jsonToClass(respuesta.getBody(), MonedaResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            
	            log.info("monedaResponse: "+monedaResponse);
	            log.info(monedaResponse.getResultado().getCodigo());
	            if(monedaResponse.getResultado().getCodigo().equals("0000")){
	            	result.addError(new ObjectError("codMoneda", "El codigo de moneda ya existe"));
	            	return "convenio/moneda/formMoneda";
	            }else {
	            	log.info("save");
	        		log.info("moneda: "+moneda);
	        		moneda.setFlagActivo(true);
	        		moneda.setCodUsuario("E44444");
	        		moneda.setFechaModificacion(new Date());
	            	monedasRequest.setMoneda(moneda);
	            	WSResponse respuestaCrear = monedaServiceApiRest.crearWs(monedasRequest);
	            	log.info("respuestaCrear: "+respuestaCrear);
	            	log.info("respuestaCrear.getBody(): "+respuestaCrear.getBody());
	            	log.info("respuestaCrear.getStatus(): "+respuestaCrear.getStatus());
	        		log.info("respuestaCrear.isExitoso(): "+respuestaCrear.isExitoso());
	        		if(respuestaCrear.isExitoso()) {
	        			if(respuestaCrear.getStatus() == 200) {
	        				log.info("Respusta codigo 200 en Crear la moneda por codigo");
	        				try {
        						response = mapper.jsonToClass(respuestaCrear.getBody(), Response.class);
        						log.info("response: "+response);
        						
        						
        					} catch (IOException e) {
        						e.printStackTrace();
        					}
        					redirectAttributes.addFlashAttribute("mensaje", " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion());
        					return "redirect:/monedas/index";
	        			}else {
	        				if (respuestaCrear.getStatus() == 422 || respuestaCrear.getStatus() == 400) {
	        					log.info("Respusta codigo " +respuestaCrear.getStatus()+ "en Crear la moneda por codigo");
	        					try {
	        						response = mapper.jsonToClass(respuestaCrear.getBody(), Response.class);
	        						log.info("response: "+response);
	        						
	        						
	        					} catch (IOException e) {
	        						e.printStackTrace();
	        					}
	        					redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion());
	        					return "redirect:/monedas/index";
	        				}
	        			}
	        		}	
	            }
			}
		}
		
		
		
		//monedaService.guardar(moneda);
		//redirectAttributes.addFlashAttribute("mensaje", "Registro Guardado");
		return "redirect:/monedas/index";  
	}
	
	
	
	@GetMapping("/edit/{codMoneda}")
	public String editarWs(@PathVariable("codMoneda") String codMoneda, Moneda moneda, Model model, RedirectAttributes redirectAttributes) {
		log.info("editarWs");
		log.info(codMoneda);
		
		List<Moneda> listMonedas = new ArrayList<>();
		Moneda monedaEdit = new Moneda();
		
		
		MonedasRequest monedasRequest = new MonedasRequest();
		monedasRequest.setIdUsuario("test");
		monedasRequest.setIdSesion("20210101121213");
		monedasRequest.setCodUsuario("E66666");
		monedasRequest.setCanal("8");
		Moneda monedaBuscar = new Moneda();
		monedaBuscar.setCodMoneda(codMoneda);
		monedasRequest.setMoneda(monedaBuscar);
		
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
				log.info("Respusta codigo 200 en buscar moneda por codigo");
	            try {
					monedaResponse = mapper.jsonToClass(respuesta.getBody(), MonedaResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            
	            log.info("monedaResponse: "+monedaResponse);
	            log.info(monedaResponse.getResultado().getCodigo());
	            if(monedaResponse.getResultado().getCodigo().equals("0000")){
	            	log.info("Respusta codigo 0000 si existe la modena");
	            	log.info("Respusta codigo 0000 si existe la modena");
	            	monedaEdit = monedaResponse.getMonedas().get(0);
	            	model.addAttribute("moneda", monedaEdit);
	        		log.info("monedaEdit: "+monedaEdit);
	        		return "convenio/moneda/formMonedaEdit";
	            }else {
	            	//if(monedaResponse.getResultado().getCodigo().equals("0000")){
	            	if(monedaResponse.getResultado().getCodigo().equals("0001")){
	            		log.info("Respusta codigo 0001 recurso no encontrado");
	            		resultado = monedaResponse.getResultado();
	            		redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
	            		return "redirect:/monedas/index";
	            	}
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
					return "redirect:/monedas/index";
				}
			}
		}
		
		
		log.info("nuca llego aqui");
		//Moneda monedaEdit = monedaService.findById(codMoneda);
		model.addAttribute("moneda", monedaEdit);
		log.info("monedaEdit: "+monedaEdit);
		return "convenio/moneda/formMonedaEdit";
		
	}
	
	
	@PostMapping("/guardar")
	public String guardarWs(@Valid  Moneda moneda, BindingResult result,  RedirectAttributes redirectAttributes) {
		
		log.info("guardar");
		log.info("moneda: "+moneda);
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				log.info("Ocurrio un error: " + error.getDefaultMessage());
			}
		
			return "convenio/moneda/formMonedaEdit";
		}	
		
		MonedasRequest monedasRequest = new MonedasRequest();
		monedasRequest.setIdUsuario("test");
		monedasRequest.setIdSesion("20210101121213");
		monedasRequest.setCodUsuario("E66666");
		monedasRequest.setCanal("8");
		moneda.setCodUsuario("E66666");
		monedasRequest.setMoneda(moneda);
		
		
		Response response = new Response();
		Resultado resultado = new Resultado();
		WSResponse respuesta = monedaServiceApiRest.actualizarWs(monedasRequest);
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en Actualizar la moneda por codigo");
				try {
					response = mapper.jsonToClass(respuesta.getBody(), Response.class);
					log.info("response: "+response);
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//redirectAttributes.addFlashAttribute("mensaje", "Registro actualizado");
				redirectAttributes.addFlashAttribute("mensaje", " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion());
				return "redirect:/monedas/index";
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
					return "convenio/moneda/formMonedaEdit";
				}
			}
		}
		 
		return "convenio/moneda/formMonedaEdit";
	}
	
	@GetMapping("/desactivar/{codMoneda}")
	public String desactivarWs(@PathVariable("codMoneda") String codMoneda, Moneda moneda, Model model, RedirectAttributes redirectAttributes) {
		log.info("desactivarWs");
		log.info(codMoneda);
		
		List<Moneda> listMonedas = new ArrayList<>();
		Moneda monedaEdit = new Moneda();
		
		
		MonedasRequest monedasRequest = new MonedasRequest();
		monedasRequest.setIdUsuario("test");
		monedasRequest.setIdSesion("20210101121213");
		monedasRequest.setCodUsuario("E66666");
		monedasRequest.setCanal("8");
		Moneda monedaBuscar = new Moneda();
		monedaBuscar.setCodMoneda(codMoneda);
		monedasRequest.setMoneda(monedaBuscar);
		
		MonedaResponse monedaResponse = new MonedaResponse();
		
		Response response = new Response();
		Resultado resultado = new Resultado();
		WSResponse respuesta = monedaServiceApiRest.consultar(monedasRequest);
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la moneda por codigo");
	            try {
					monedaResponse = mapper.jsonToClass(respuesta.getBody(), MonedaResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	           
	            log.info("monedaResponse: "+monedaResponse);
	            log.info(monedaResponse.getResultado().getCodigo());
	            if(monedaResponse.getResultado().getCodigo().equals("0000")){
	            	log.info("Respusta codigo 0000 si existe la modena");
	            	monedaEdit = monedaResponse.getMonedas().get(0);
	            	monedaEdit.setFlagActivo(false);
	            	monedasRequest.setMoneda(monedaEdit);
	            	WSResponse respuestaActualizar = monedaServiceApiRest.actualizarWs(monedasRequest);
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
        					return "redirect:/monedas/index";
	        				
	        				
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
	        					return "redirect:/monedas/index";
	        				}
	        			}
	        		}	
	            }else{
	            	//if(monedaResponse.getResultado().getCodigo().equals("0000")){
	            	if(monedaResponse.getResultado().getCodigo().equals("0001")){
	            		log.info("Respusta codigo 0001 recurso no encontrado");
	            		resultado = monedaResponse.getResultado();
	            		redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
	            		return "redirect:/monedas/index";
	            	}
	            }
			}else{
				//if(respuesta.getStatus() == 200)
				if (respuesta.getStatus() == 422) {
					log.info("Respusta codigo " +respuesta.getStatus()+ "en buscar la moneda por codigo");
					try {
						
						resultado = mapper.jsonToClass(respuesta.getBody(), Resultado.class);
						log.info("resultado: "+resultado);
						
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
					return "redirect:/monedas/index";
				}
			}
		}//if(respuesta.isExitoso())
		
		//Moneda monedaEdit = monedaService.findById(codMoneda);
		//model.addAttribute("moneda", monedaEdit);
		//redirectAttributes.addFlashAttribute("mensaje", "Registro actualizado");
		return "redirect:/monedas/index";
		
	}
	
	
	@GetMapping("/activar/{codMoneda}")
	public String activarWs(@PathVariable("codMoneda") String codMoneda, Moneda moneda, Model model, RedirectAttributes redirectAttributes) {
		log.info("activarWs");
		log.info(codMoneda);
		
		List<Moneda> listMonedas = new ArrayList<>();
		Moneda monedaEdit = new Moneda();
		
		
		MonedasRequest monedasRequest = new MonedasRequest();
		monedasRequest.setIdUsuario("test");
		monedasRequest.setIdSesion("20210101121213");
		monedasRequest.setCodUsuario("E66666");
		monedasRequest.setCanal("8");
		Moneda monedaBuscar = new Moneda();
		monedaBuscar.setCodMoneda(codMoneda);
		monedasRequest.setMoneda(monedaBuscar);
		
		MonedaResponse monedaResponse = new MonedaResponse();
		
		Response response = new Response();
		Resultado resultado = new Resultado();
		WSResponse respuesta = monedaServiceApiRest.consultar(monedasRequest);
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la moneda por codigo");
	            try {
					monedaResponse = mapper.jsonToClass(respuesta.getBody(), MonedaResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	           
	            log.info("monedaResponse: "+monedaResponse);
	            log.info(monedaResponse.getResultado().getCodigo());
	            if(monedaResponse.getResultado().getCodigo().equals("0000")){
	            	log.info("Respusta codigo 0000 si existe la modena");
	            	monedaEdit = monedaResponse.getMonedas().get(0);
	            	monedaEdit.setFlagActivo(true);
	            	monedasRequest.setMoneda(monedaEdit);
	            	WSResponse respuestaActualizar = monedaServiceApiRest.actualizarWs(monedasRequest);
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
        					return "redirect:/monedas/index";
	        				
	        				
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
	        					return "redirect:/monedas/index";
	        				}
	        			}
	        		}	
	            }else{
	            	//if(monedaResponse.getResultado().getCodigo().equals("0000")){
	            	if(monedaResponse.getResultado().getCodigo().equals("0001")){
	            		log.info("Respusta codigo 0001 recurso no encontrado");
	            		resultado = monedaResponse.getResultado();
	            		redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
	            		return "redirect:/monedas/index";
	            	}
	            }
			}else{
				//if(respuesta.getStatus() == 200)
				if (respuesta.getStatus() == 422) {
					log.info("Respusta codigo " +respuesta.getStatus()+ "en buscar la moneda por codigo");
					try {
						
						resultado = mapper.jsonToClass(respuesta.getBody(), Resultado.class);
						log.info("resultado: "+resultado);
						
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
					return "redirect:/monedas/index";
				}
			}
		}//if(respuesta.isExitoso())
		
		//Moneda monedaEdit = monedaService.findById(codMoneda);
		//model.addAttribute("moneda", monedaEdit);
		//redirectAttributes.addFlashAttribute("mensaje", "Registro actualizado");
		return "redirect:/monedas/index";
		
	}
	
	
	
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dataFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dataFormat, false));
	}
	

	
}
