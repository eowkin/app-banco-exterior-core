package com.bancoexterior.app.convenio.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bancoexterior.app.convenio.apiRest.IAgenciaServiceApiRest;
import com.bancoexterior.app.convenio.dto.AgenciaRequest;
import com.bancoexterior.app.convenio.dto.AgenciaResponse;
import com.bancoexterior.app.convenio.dto.ClienteResponse;
import com.bancoexterior.app.convenio.model.Agencia;
import com.bancoexterior.app.convenio.response.Response;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.convenio.services.IAgenciaService;
import com.bancoexterior.app.convenio.services.restApi.model.WSResponse;
import com.bancoexterior.app.util.Mapper;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/agencias")
public class AgenciaController {

	@Autowired IAgenciaServiceApiRest agenciaServiceApiRest; 
	
	@Autowired 
	private Mapper mapper;
	
	@Autowired
	private IAgenciaService agenciaService;
	
	@GetMapping("/index")
	public String index(Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a index agenciasWs");
		
		List<Agencia> listaAgencias = new ArrayList<>();
		AgenciaRequest agenciaRequest = new AgenciaRequest();
		agenciaRequest.setIdUsuario("test");
		agenciaRequest.setIdSesion("20210101121213");
		agenciaRequest.setCodUsuario("E66666");
		agenciaRequest.setCanal("8");
		Agencia agencia = new Agencia();
		agencia.setFlagDivisa(true);
		agenciaRequest.setAgencia(agencia);
		
		
				
		AgenciaResponse agenciaResponse = new AgenciaResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = agenciaServiceApiRest.consultarWs(agenciaRequest);
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista agencias");
	            try {
					agenciaResponse= mapper.jsonToClass(respuesta.getBody(), AgenciaResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("agenciaResponse: "+agenciaResponse);
	            log.info(agenciaResponse.getResultado().getCodigo());
	            listaAgencias = agenciaResponse.getListaAgencias();
	            model.addAttribute("listaAgencias", listaAgencias);
	    		return "convenio/agencia/listaAgencias";
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
					return "convenio/agencia/listaAgencias";
				}
			}
		}else {
			log.info("error conectar microservicio consultarWs Agencias");
			return "redirect:/";
		}
		
		return "convenio/agencia/listaAgencias";
	}
	
	
	@GetMapping("/edit/{codAgencia}")
	public String editarWs(@PathVariable("codAgencia") String codAgencia, 
			Agencia agencia, Model model, RedirectAttributes redirectAttributes) {
		log.info("editarWs");
		log.info("codAgencia: "+codAgencia);
		
		List<Agencia> listaAgencias = new ArrayList<>();
		Agencia agenciaEdit = new Agencia(); 
		AgenciaRequest agenciaRequest = new AgenciaRequest();
		agenciaRequest.setIdUsuario("test");
		agenciaRequest.setIdSesion("20210101121213");
		agenciaRequest.setCodUsuario("E66666");
		agenciaRequest.setCanal("8");
		Agencia agenciaBuscar = new Agencia();
		agenciaBuscar.setFlagDivisa(true);
		agenciaBuscar.setCodAgencia(agencia.getCodAgencia());
		agenciaRequest.setAgencia(agenciaBuscar);
		
		AgenciaResponse agenciaResponse = new AgenciaResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = agenciaServiceApiRest.consultarWs(agenciaRequest);
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista agencias");
	            try {
					agenciaResponse= mapper.jsonToClass(respuesta.getBody(), AgenciaResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("agenciaResponse: "+agenciaResponse);
	            log.info(agenciaResponse.getResultado().getCodigo());
	            if (agenciaResponse.getResultado().getCodigo().equals("0000")) {
					log.info("Respusta codigo 0000 si existe la ageancia");
					agenciaEdit = agenciaResponse.getListaAgencias().get(0); 
					model.addAttribute("agencia", agenciaEdit);
					return "convenio/agencia/formAgenciaEdit";
				}else {
						if (agenciaResponse.getResultado().getCodigo().equals("0001")||agenciaResponse.getResultado().getCodigo().equals("0002")) {
							log.info("Respusta codigo 0001 recurso no encontrado");
							resultado = agenciaResponse.getResultado();
							model.addAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
							redirectAttributes.addFlashAttribute("mensajeError"," Codigo :" + resultado.getCodigo() + " descripcion: " + resultado.getDescripcion());
							return "redirect:/agencias/index";
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
					model.addAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
					redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
					return "redirect:/agencias/index";
				}
			}
		}else {
			log.info("error conectar microservicio consultarWs Agencias");
			return "redirect:/";
		}
		
		
		
		return "convenio/agencia/formAgenciaEdit";
	}
	
	
	@PostMapping("/guardar")
	public String guardarWs(Agencia agencia, Model model, RedirectAttributes redirectAttributes) {
		log.info("guardarWs");
		log.info("agencia: "+agencia);
		
		List<Agencia> listaAgencias = new ArrayList<>();
		Agencia agenciaEdit = new Agencia(); 
		AgenciaRequest agenciaRequest = new AgenciaRequest();
		agenciaRequest.setIdUsuario("test");
		agenciaRequest.setIdSesion("20210101121213");
		agenciaRequest.setCodUsuario("E66666");
		agenciaRequest.setCanal("8");
		agencia.setFlagActivo(true);
		agencia.setFlagDivisa(true);
		agenciaRequest.setAgencia(agencia);
		
		Response response = new Response();
		AgenciaResponse agenciaResponse = new AgenciaResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = agenciaServiceApiRest.actualizarWs(agenciaRequest);
		log.info("respuestaAgencia: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				
				log.info("Respusta codigo 200 en actualizar agencias");
				try {
					resultado = mapper.jsonToClass(respuesta.getBody(), Resultado.class);
					log.info("resultado: " + resultado);

				} catch (IOException e) {
					e.printStackTrace();
				}
				redirectAttributes.addFlashAttribute("mensaje",
						" Codigo :" + resultado.getCodigo() + " descripcion: "
								+ resultado.getDescripcion());
				return "redirect:/agencias/index";
			}else {
				if (respuesta.getStatus() == 422 || respuesta.getStatus() == 400) {
					log.info("Respusta codigo " + respuesta.getStatus()
							+ "en Actualizar la agencia por codigo");
					try {
						resultado = mapper.jsonToClass(respuesta.getBody(), Resultado.class);
						log.info("response: " + response);

					} catch (IOException e) {
						e.printStackTrace();
					}
					redirectAttributes.addFlashAttribute("mensajeError",
							" Codigo :" + resultado.getCodigo() + " descripcion: "
									+ resultado.getDescripcion());
					return "redirect:/agencias/index";
				}
			}
			
		}else {
			log.info("error conectar microservicio crearWs Agencias");
			return "redirect:/";
		}	
		
		
		return "redirect:/agencias/index";
	}
	
	
	
	@PostMapping("/save")
	public String saveWs(Agencia agencia, Model model, RedirectAttributes redirectAttributes) {
		log.info("saveWs");
		log.info("agencia: "+agencia);
		
		List<Agencia> listaAgencias = new ArrayList<>();
		Agencia agenciaEdit = new Agencia(); 
		AgenciaRequest agenciaRequest = new AgenciaRequest();
		agenciaRequest.setIdUsuario("test");
		agenciaRequest.setIdSesion("20210101121213");
		agenciaRequest.setCodUsuario("E66666");
		agenciaRequest.setCanal("8");
		agencia.setFlagActivo(true);
		agencia.setFlagDivisa(true);
		agenciaRequest.setAgencia(agencia);
		
		Response response = new Response();
		AgenciaResponse agenciaResponse = new AgenciaResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = agenciaServiceApiRest.crearWs(agenciaRequest);
		log.info("respuestaAgencia: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista agencias");
				log.info("Respusta codigo 200 en actualizar agencias");
				try {
					resultado = mapper.jsonToClass(respuesta.getBody(), Resultado.class);
					log.info("resultado: " + resultado);

				} catch (IOException e) {
					e.printStackTrace();
				}
				redirectAttributes.addFlashAttribute("mensaje",
						" Codigo :" + resultado.getCodigo() + " descripcion: "
								+ resultado.getDescripcion());
				return "redirect:/agencias/index";
			}else {
				if (respuesta.getStatus() == 422 || respuesta.getStatus() == 400) {
					log.info("Respusta codigo " + respuesta.getStatus()
							+ "en Actualizar la agencia por codigo");
					try {
						resultado = mapper.jsonToClass(respuesta.getBody(), Resultado.class);
						log.info("response: " + response);

					} catch (IOException e) {
						e.printStackTrace();
					}
					redirectAttributes.addFlashAttribute("mensajeError",
							" Codigo :" + resultado.getCodigo() + " descripcion: "
									+ resultado.getDescripcion());
					return "redirect:/agencias/index";
				}
			}
			
		}else {
			log.info("error conectar microservicio crearWs Agencias");
			return "redirect:/";
		}	
		
		
		return "convenio/agencia/listaAgencias";
	}
	
	
	@GetMapping("/activar/{codAgencia}")
	public String activarWs(@PathVariable("codAgencia") String codAgencia, Model model,
			RedirectAttributes redirectAttributes) {
		log.info("activarWs");
		log.info("codAgencia: " + codAgencia);
		
		List<Agencia> listaAgencias = new ArrayList<>();
		Agencia agenciaEdit = new Agencia(); 
		AgenciaRequest agenciaRequest = new AgenciaRequest();
		agenciaRequest.setIdUsuario("test");
		agenciaRequest.setIdSesion("20210101121213");
		agenciaRequest.setCodUsuario("E66666");
		agenciaRequest.setCanal("8");
		Agencia agencia = new Agencia();
		agencia.setCodAgencia(codAgencia);
		agencia.setFlagDivisa(true);
		agenciaRequest.setAgencia(agencia);
		
		Response response = new Response();
		AgenciaResponse agenciaResponse = new AgenciaResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = agenciaServiceApiRest.consultarWs(agenciaRequest);
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista agencias");
	            try {
					agenciaResponse= mapper.jsonToClass(respuesta.getBody(), AgenciaResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("agenciaResponse: "+agenciaResponse);
	            log.info(agenciaResponse.getResultado().getCodigo());
	            if (agenciaResponse.getResultado().getCodigo().equals("0000")) {
					log.info("Respusta codigo 0000 si existe la ageancia");
					listaAgencias = agenciaResponse.getListaAgencias();
					agenciaEdit = listaAgencias.get(0);
					agenciaEdit.setFlagActivo(true);
					
					agenciaRequest.setAgencia(agenciaEdit);
					WSResponse respuestaActualizar = agenciaServiceApiRest.actualizarWs(agenciaRequest);
					log.info("respuestaActualizar: "+respuestaActualizar);
					log.info("respuestaActualizar.getBody(): "+respuestaActualizar.getBody());
					log.info("respuestaActualizar.getStatus(): "+respuestaActualizar.getStatus());
					log.info("respuestaActualizar.isExitoso(): "+respuestaActualizar.isExitoso());
					
					if(respuestaActualizar.isExitoso()) {
						if(respuestaActualizar.getStatus() == 200) {
							log.info("Respusta codigo 200 en actualizar agencias");
							try {
								resultado = mapper.jsonToClass(respuestaActualizar.getBody(), Resultado.class);
								log.info("resultado: " + resultado);

							} catch (IOException e) {
								e.printStackTrace();
							}
							redirectAttributes.addFlashAttribute("mensaje",
									" Codigo :" + resultado.getCodigo() + " descripcion: "
											+ resultado.getDescripcion());
							return "redirect:/agencias/index";
						}else {
							if (respuestaActualizar.getStatus() == 422 || respuestaActualizar.getStatus() == 400) {
								log.info("Respusta codigo " + respuestaActualizar.getStatus()
										+ "en Actualizar la agencia por codigo");
								try {
									response = mapper.jsonToClass(respuestaActualizar.getBody(), Response.class);
									log.info("response: " + response);

								} catch (IOException e) {
									e.printStackTrace();
								}
								redirectAttributes.addFlashAttribute("mensajeError",
										" Codigo :" + response.getResultado().getCodigo() + " descripcion: "
												+ response.getResultado().getDescripcion());
								return "redirect:/agencias/index";
							}
						}		
					}else{
						log.info("error conectar microservicio actualizarWs Agencias");
						return "redirect:/";
					}
					
					
				}else {
						if (agenciaResponse.getResultado().getCodigo().equals("0001")) {
							log.info("Respusta codigo 0001 recurso no encontrado");
							resultado = agenciaResponse.getResultado();
							redirectAttributes.addFlashAttribute("mensajeError",
									" Codigo :" + resultado.getCodigo() + " descripcion: " + resultado.getDescripcion());
							return "redirect:/agencias/index";
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
					return "redirect:/agencias/index";
				}
			}
		}else {
			log.info("error conectar microservicio consultarWs Agencias");
			return "redirect:/";
		}
		
		return "convenio/agencia/listaAgencias";
	}
	
	@GetMapping("/desactivar/{codAgencia}")
	public String desactivarWs(@PathVariable("codAgencia") String codAgencia, Model model,
			RedirectAttributes redirectAttributes) {
		log.info("activarWs");
		log.info("codAgencia: " + codAgencia);
		
		List<Agencia> listaAgencias = new ArrayList<>();
		Agencia agenciaEdit = new Agencia(); 
		AgenciaRequest agenciaRequest = new AgenciaRequest();
		agenciaRequest.setIdUsuario("test");
		agenciaRequest.setIdSesion("20210101121213");
		agenciaRequest.setCodUsuario("E66666");
		agenciaRequest.setCanal("8");
		Agencia agencia = new Agencia();
		agencia.setCodAgencia(codAgencia);
		agencia.setFlagDivisa(true);
		agenciaRequest.setAgencia(agencia);
		
		Response response = new Response();
		AgenciaResponse agenciaResponse = new AgenciaResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = agenciaServiceApiRest.consultarWs(agenciaRequest);
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista agencias");
	            try {
					agenciaResponse= mapper.jsonToClass(respuesta.getBody(), AgenciaResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("agenciaResponse: "+agenciaResponse);
	            log.info(agenciaResponse.getResultado().getCodigo());
	            if (agenciaResponse.getResultado().getCodigo().equals("0000")) {
					log.info("Respusta codigo 0000 si existe la ageancia");
					listaAgencias = agenciaResponse.getListaAgencias();
					agenciaEdit = listaAgencias.get(0);
					agenciaEdit.setFlagActivo(false);
					
					agenciaRequest.setAgencia(agenciaEdit);
					WSResponse respuestaActualizar = agenciaServiceApiRest.actualizarWs(agenciaRequest);
					log.info("respuestaActualizar: "+respuestaActualizar);
					log.info("respuestaActualizar.getBody(): "+respuestaActualizar.getBody());
					log.info("respuestaActualizar.getStatus(): "+respuestaActualizar.getStatus());
					log.info("respuestaActualizar.isExitoso(): "+respuestaActualizar.isExitoso());
					
					if(respuestaActualizar.isExitoso()) {
						if(respuestaActualizar.getStatus() == 200) {
							log.info("Respusta codigo 200 en actualizar agencias");
							try {
								resultado = mapper.jsonToClass(respuestaActualizar.getBody(), Resultado.class);
								log.info("resultado: " + resultado);

							} catch (IOException e) {
								e.printStackTrace();
							}
							redirectAttributes.addFlashAttribute("mensaje",
									" Codigo :" + resultado.getCodigo() + " descripcion: "
											+ resultado.getDescripcion());
							return "redirect:/agencias/index";
						}else {
							if (respuestaActualizar.getStatus() == 422 || respuestaActualizar.getStatus() == 400) {
								log.info("Respusta codigo " + respuestaActualizar.getStatus()
										+ "en Actualizar la agencia por codigo");
								try {
									response = mapper.jsonToClass(respuestaActualizar.getBody(), Response.class);
									log.info("response: " + response);

								} catch (IOException e) {
									e.printStackTrace();
								}
								redirectAttributes.addFlashAttribute("mensajeError",
										" Codigo :" + response.getResultado().getCodigo() + " descripcion: "
												+ response.getResultado().getDescripcion());
								return "redirect:/agencias/index";
							}
						}		
					}else{
						log.info("error conectar microservicio actualizarWs Agencias");
						return "redirect:/";
					}
					
					
				}else {
						if (agenciaResponse.getResultado().getCodigo().equals("0001")) {
							log.info("Respusta codigo 0001 recurso no encontrado");
							resultado = agenciaResponse.getResultado();
							redirectAttributes.addFlashAttribute("mensajeError",
									" Codigo :" + resultado.getCodigo() + " descripcion: " + resultado.getDescripcion());
							return "redirect:/agencias/index";
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
					return "redirect:/agencias/index";
				}
			}
		}else {
			log.info("error conectar microservicio consultarWs Agencias");
			return "redirect:/";
		}
		
		return "convenio/agencia/listaAgencias";
	}
	
	
	@GetMapping("/formAgencia")
	public String fromAgencia(Agencia agencia,  Model model) {
		
		return "convenio/agencia/formAgencia";
	}
	
	@GetMapping("/formBuscarAgencia")
	public String fromBuscarAgencia(Agencia agencia) {
		
		return "convenio/agencia/formBuscarAgencia";
	}
	
	@GetMapping("/search")
	public String search(@ModelAttribute("agenciaSearch") Agencia agenciaSearch,
			Agencia agencia, Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a search clientesPersonalizadosWs");
		log.info(agenciaSearch.getCodAgencia());
		
		List<Agencia> listaAgencias = new ArrayList<>();
		AgenciaRequest agenciaRequest = new AgenciaRequest();
		agenciaRequest.setIdUsuario("test");
		agenciaRequest.setIdSesion("20210101121213");
		agenciaRequest.setCodUsuario("E66666");
		agenciaRequest.setCanal("8");
		Agencia agenciaBuscar = new Agencia();
		agenciaBuscar.setFlagDivisa(true);
		if(!agenciaSearch.getCodAgencia().equals("")){
			agenciaBuscar.setCodAgencia(agenciaSearch.getCodAgencia());
		}	
		agenciaRequest.setAgencia(agenciaBuscar);
		
		AgenciaResponse agenciaResponse = new AgenciaResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = agenciaServiceApiRest.consultarWs(agenciaRequest);
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista agencias");
	            try {
					agenciaResponse= mapper.jsonToClass(respuesta.getBody(), AgenciaResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("agenciaResponse: "+agenciaResponse);
	            log.info(agenciaResponse.getResultado().getCodigo());
	            if (agenciaResponse.getResultado().getCodigo().equals("0000")) {
					log.info("Respusta codigo 0000 si existe la ageancia");
					listaAgencias = agenciaResponse.getListaAgencias();
					model.addAttribute("listaAgencias", listaAgencias);
					return "convenio/agencia/listaAgencias";
				}else {
						if (agenciaResponse.getResultado().getCodigo().equals("0001")) {
							log.info("Respusta codigo 0001 recurso no encontrado");
							resultado = agenciaResponse.getResultado();
							redirectAttributes.addFlashAttribute("mensajeError",
									" Codigo :" + resultado.getCodigo() + " descripcion: " + resultado.getDescripcion());
							return "redirect:/agencias/index";
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
					return "redirect:/agencias/index";
				}
			}
		}else {
			log.info("error conectar microservicio consultarWs Agencias");
			return "redirect:/";
		}

		return "convenio/agencia/listaAgencias";
	}
	
	@GetMapping("/searchCrear")
	public String searchCrear(Agencia agencia, Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a searchCrear clientesPersonalizadosWs");
		log.info(agencia.getCodAgencia());
		
		List<Agencia> listaAgencias = new ArrayList<>();
		Agencia agenciaEdit = new Agencia(); 
		AgenciaRequest agenciaRequest = new AgenciaRequest();
		agenciaRequest.setIdUsuario("test");
		agenciaRequest.setIdSesion("20210101121213");
		agenciaRequest.setCodUsuario("E66666");
		agenciaRequest.setCanal("8");
		Agencia agenciaBuscar = new Agencia();
		agenciaBuscar.setFlagDivisa(false);
		agenciaBuscar.setCodAgencia(agencia.getCodAgencia());
		agenciaRequest.setAgencia(agenciaBuscar);
		
		AgenciaResponse agenciaResponse = new AgenciaResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = agenciaServiceApiRest.consultarWs(agenciaRequest);
		log.info("responseMoneda: "+respuesta);
		log.info("respuesta.getBody(): "+respuesta.getBody());
		log.info("retorno.getStatus(): "+respuesta.getStatus());
		log.info("respuesta.isExitoso(): "+respuesta.isExitoso());
		
		if(respuesta.isExitoso()) {
			if(respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista agencias");
	            try {
					agenciaResponse= mapper.jsonToClass(respuesta.getBody(), AgenciaResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
	            log.info("agenciaResponse: "+agenciaResponse);
	            log.info(agenciaResponse.getResultado().getCodigo());
	            if (agenciaResponse.getResultado().getCodigo().equals("0000")) {
					log.info("Respusta codigo 0000 si existe la ageancia");
					agenciaEdit = agenciaResponse.getListaAgencias().get(0); 
					model.addAttribute("agencia", agenciaEdit);
					return "convenio/agencia/formAgencia";
				}else {
						if (agenciaResponse.getResultado().getCodigo().equals("0001")||agenciaResponse.getResultado().getCodigo().equals("0002")) {
							log.info("Respusta codigo 0001 recurso no encontrado");
							resultado = agenciaResponse.getResultado();
							model.addAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
							redirectAttributes.addFlashAttribute("mensajeError"," Codigo :" + resultado.getCodigo() + " descripcion: " + resultado.getDescripcion());
							return "convenio/agencia/formBuscarAgencia";
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
					model.addAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
					redirectAttributes.addFlashAttribute("mensajeError", " Codigo :" +resultado.getCodigo() +" descripcion: "+resultado.getDescripcion());
					return "convenio/agencia/formBuscarAgencia";
				}
			}
		}else {
			log.info("error conectar microservicio consultarWs Agencias");
			return "redirect:/";
		}

		return "convenio/agencia/listaAgencias";
	}
	
	
	
	@ModelAttribute
	public void setGenericos(Model model) {
		Agencia agenciaSearch = new Agencia();
		model.addAttribute("agenciaSearch", agenciaSearch);
	}
}
