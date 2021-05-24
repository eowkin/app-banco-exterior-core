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
import com.bancoexterior.app.convenio.apiRest.ITasaServiceApiRest;
import com.bancoexterior.app.convenio.dto.MonedaResponse;
import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.dto.TasaRequest;
import com.bancoexterior.app.convenio.dto.TasaResponse;
import com.bancoexterior.app.convenio.model.Moneda;
import com.bancoexterior.app.convenio.model.Tasa;
import com.bancoexterior.app.convenio.response.Response;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.convenio.services.IMonedaService;
import com.bancoexterior.app.convenio.services.ITasaService;
import com.bancoexterior.app.convenio.services.restApi.model.WSResponse;
import com.bancoexterior.app.util.Mapper;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/tasas")
public class TasaController {

	@Autowired
	private ITasaServiceApiRest tasaServiceApiRest; 
	
	@Autowired
	private IMonedaServiceApiRest monedaServiceApiRest;
	
	@Autowired 
	private Mapper mapper;

	
	@GetMapping("/index")
	public String index(Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a index listaTasasWs");
		
		List<Tasa> listaTasas = new ArrayList<>();
		
		TasaRequest tasaRequest = new TasaRequest();
		tasaRequest.setIdUsuario("test");
		tasaRequest.setIdSesion("20210101121213");
		tasaRequest.setCodUsuario("E66666");
		tasaRequest.setCanal("8");
		Tasa tasa = new Tasa();
		tasaRequest.setTasa(tasa);
		
		TasaResponse tasaResponse = new TasaResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = tasaServiceApiRest.consultarWs(tasaRequest);
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista tasa");
	            try {
					tasaResponse = mapper.jsonToClass(respuesta.getBody(), TasaResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("tasaResponse: "+tasaResponse);
	            log.info(tasaResponse.getResultado().getCodigo());
	            listaTasas = tasaResponse.getTasa();
	            model.addAttribute("listaTasas", listaTasas);
	    		return "convenio/tasa/listaTasas";
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
		
		//model.addAttribute("listMonedas", listMonedas);
		return "redirect:/";
		
		
		
		
		
		
		
		
		//model.addAttribute("listaTasas", listaTasas);
		
		//return "convenio/tasa/listaTasas";
	}
	
	
	@GetMapping("/edit/{codMonedaOrigen}/{codMonedaDestino}/{tipoOperacion}")
	public String editarWs(@PathVariable("codMonedaOrigen") String codMonedaOrigen, 
			@PathVariable("codMonedaDestino") String codMonedaDestino, @PathVariable("tipoOperacion") Integer tipoOperacion,
			Tasa tasa, Model model, RedirectAttributes redirectAttributes) {
		log.info("editarWs");
		log.info("codMonedaOrigen: "+codMonedaOrigen);
		log.info("codMonedaDestino: "+codMonedaDestino);
		log.info("tipoOperacion: "+tipoOperacion);
		
		List<Tasa> listaTasas = new ArrayList<>();
		Tasa tasaEdit = new Tasa();
		
		
		
		TasaRequest tasaRequest = new TasaRequest();
		tasaRequest.setIdUsuario("test");
		tasaRequest.setIdSesion("20210101121213");
		tasaRequest.setCodUsuario("E66666");
		tasaRequest.setCanal("8");
		Tasa tasaBuscar = new Tasa();
		tasaBuscar.setCodMonedaOrigen(codMonedaOrigen);
		tasaBuscar.setCodMonedaDestino(codMonedaDestino);
		tasaBuscar.setTipoOperacion(tipoOperacion);
		tasaRequest.setTasa(tasaBuscar);
		
		TasaResponse tasaResponse = new TasaResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = tasaServiceApiRest.consultarWs(tasaRequest);
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar tasa por codigo");
				try {
					tasaResponse = mapper.jsonToClass(respuesta.getBody(), TasaResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("tasaResponse: "+tasaResponse);
	            log.info(tasaResponse.getResultado().getCodigo());
	            if(tasaResponse.getResultado().getCodigo().equals("0000")) {
	            	log.info("Respusta codigo 0000 si existe la tasa");
	            	tasaEdit = tasaResponse.getTasa().get(0);
	            	model.addAttribute("tasa", tasaEdit);
	            	return "convenio/tasa/formTasaEdit";
	            }else {
	            	
	            	if(tasaResponse.getResultado().getCodigo().equals("0001")){
	            		log.info("Respusta codigo 0001 recurso no encontrado");
	            		resultado = tasaResponse.getResultado();
	            		redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
	            		return "redirect:/tasas/index";
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
					return "redirect:/tasas/index";
				}
			}
		}	
		
		
		
		return "";
	}
	
	
	@PostMapping("/guardar")
	public String guardarWs(Tasa tasa, BindingResult result,  RedirectAttributes redirectAttributes) {
		log.info("guardarWs");
		log.info("tasa: "+tasa);
		
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				log.info("Ocurrio un error: " + error.getDefaultMessage());
			}
		
			return "convenio/tasa/formTasaEdit";
		}
		
		TasaRequest tasaRequest = new TasaRequest();
		tasaRequest.setIdUsuario("test");
		tasaRequest.setIdSesion("20210101121213");
		tasaRequest.setCodUsuario("E66666");
		tasaRequest.setCanal("8");
		tasaRequest.setTasa(tasa);
		
		Response response = new Response();
		Resultado resultado = new Resultado();
		WSResponse respuesta = tasaServiceApiRest.actualizarWs(tasaRequest);
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en Actualizar la tasa por codigo");
				try {
					resultado = mapper.jsonToClass(respuesta.getBody(), Resultado.class);
					log.info("resultado: "+resultado);
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//redirectAttributes.addFlashAttribute("mensaje", "Registro actualizado");
				redirectAttributes.addFlashAttribute("mensaje", " Codigo :" +resultado.getCodigo() +", descripcion: "+resultado.getDescripcion());
				return "redirect:/tasas/index";
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
					return "convenio/tasa/formTasaEdit";
						   
				}
			}
		}else {
			return "redirect:/";
		}
		
		
		
		return "convenio/tasa/formTasaEdit";
	}
	
	
	@GetMapping("/formTasa")
	public String formTasa(Tasa tasa, Model model) {
		
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
	    		return "convenio/tasa/formTasa";
			}
		}
		return "convenio/tasa/formTasa";
	}
	
	@PostMapping("/save")
	public String saveWs(Tasa tasa, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		log.info("save");
		log.info("tasa: "+tasa);
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
		    		return "convenio/tasa/formTasa";
				}
			}
		}
		
		
		TasaRequest tasaRequest = new TasaRequest();
		tasaRequest.setIdUsuario("test");
		tasaRequest.setIdSesion("20210101121213");
		tasaRequest.setCodUsuario("E66666");
		tasaRequest.setCanal("8");
		tasaRequest.setTasa(tasa);
		
		Response response = new Response();
		Resultado resultado = new Resultado();
		WSResponse respuesta = tasaServiceApiRest.crearWs(tasaRequest);
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en Crear la tasa por codigo");
				try {
					resultado = mapper.jsonToClass(respuesta.getBody(), Resultado.class);
					log.info("resultado: "+resultado);
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//redirectAttributes.addFlashAttribute("mensaje", "Registro actualizado");
				redirectAttributes.addFlashAttribute("mensaje", " Codigo :" +resultado.getCodigo() +", descripcion: "+resultado.getDescripcion());
				return "redirect:/tasas/index";
			}else {
				if (respuesta.getStatus() == 422) {
					log.info("entro en error crear tasa codigo : "+respuesta.getStatus());
					try {
						response = mapper.jsonToClass(respuesta.getBody(), Response.class);
						log.info("response: "+response);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					
					//monedaService.consultaMonedasApiRest(monedasRequest);
					MonedaResponse monedaResponse = new MonedaResponse();
					Resultado resultado422 = new Resultado();
					WSResponse respuesta422 = monedaServiceApiRest.consultar(monedasRequest);
					log.info("respuesta422: "+respuesta422);
					log.info("respuesta422.getBody(): "+respuesta422.getBody());
					log.info("respuesta422.getStatus(): "+respuesta422.getStatus());
					log.info("respuesta422.isExitoso(): "+respuesta422.isExitoso());
					
					if(respuesta422.isExitoso()) {
						if(respuesta422.getStatus() == 200) {
							log.info("Respusta codigo 200 en buscar la lista moneda");
				            try {
								monedaResponse = mapper.jsonToClass(respuesta422.getBody(), MonedaResponse.class);
							} catch (IOException e) {
								e.printStackTrace();
							}
				            log.info("monedaResponse: "+monedaResponse);
				            log.info(monedaResponse.getResultado().getCodigo());
				            listaMonedas = monedaResponse.getMonedas();
				            model.addAttribute("listaMonedas", listaMonedas);
				    		//return "convenio/tasa/formTasa";
						}
					}
					
					//redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
					result.addError(new ObjectError("codMoneda", " Codigo :" +response.getResultado().getCodigo() +" descripcion: "+response.getResultado().getDescripcion()));
					return "convenio/tasa/formTasa";
						   
				}else {
					if (respuesta.getStatus() == 400) {
						log.info("entro en error crear tasa codigo : "+respuesta.getStatus());
						try {
							resultado = mapper.jsonToClass(respuesta.getBody(), Resultado.class);
							log.info("resultado: "+resultado);
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						
						//monedaService.consultaMonedasApiRest(monedasRequest);
						MonedaResponse monedaResponse = new MonedaResponse();
						Resultado resultado422 = new Resultado();
						WSResponse respuesta422 = monedaServiceApiRest.consultar(monedasRequest);
						log.info("respuesta422: "+respuesta422);
						log.info("respuesta422.getBody(): "+respuesta422.getBody());
						log.info("respuesta422.getStatus(): "+respuesta422.getStatus());
						log.info("respuesta422.isExitoso(): "+respuesta422.isExitoso());
						
						if(respuesta422.isExitoso()) {
							if(respuesta422.getStatus() == 200) {
								log.info("Respusta codigo 200 en buscar la lista moneda");
					            try {
									monedaResponse = mapper.jsonToClass(respuesta422.getBody(), MonedaResponse.class);
								} catch (IOException e) {
									e.printStackTrace();
								}
					            log.info("monedaResponse: "+monedaResponse);
					            log.info(monedaResponse.getResultado().getCodigo());
					            listaMonedas = monedaResponse.getMonedas();
					            model.addAttribute("listaMonedas", listaMonedas);
					    		//return "convenio/tasa/formTasa";
							}
						}
						
						//redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
						result.addError(new ObjectError("codMoneda", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion()));
						return "convenio/tasa/formTasa";
							   
					}
				}
			}
		}else {
			return "redirect:/";
		}
		
		
		
		//redirectAttributes.addFlashAttribute("mensaje", "Registro Guardado");
		return "redirect:/tasas/index";
	}
	
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dataFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dataFormat, false));
	}
	
	
}
