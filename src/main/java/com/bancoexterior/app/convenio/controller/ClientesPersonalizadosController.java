package com.bancoexterior.app.convenio.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bancoexterior.app.convenio.apiRest.IClientePersonalizadoServiceApiRest;
import com.bancoexterior.app.convenio.dto.ClienteRequest;
import com.bancoexterior.app.convenio.dto.ClienteResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.ClientesPersonalizados;
import com.bancoexterior.app.convenio.response.Response;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.convenio.services.IClientesPersonalizadosService;
import com.bancoexterior.app.convenio.services.restApi.model.WSResponse;
import com.bancoexterior.app.util.Mapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/clientesPersonalizados")
public class ClientesPersonalizadosController {

	@Autowired
	private IClientePersonalizadoServiceApiRest clientePersonalizadoServiceApiRest;

	@Autowired
	private Mapper mapper;

	@Autowired
	private IClientesPersonalizadosService clientePersonalizadosService;

	
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
				model.addAttribute("listaLimitesGenerales", listaClientesPersonalizados);
				model.addAttribute("mensajeError", " Codigo : 0001 descripcion: Operacion Exitosa.La consulta no arrojo resultado.");
				return "convenio/limitesGenerales/listaLimitesGenerales";
			}
			
		} catch (CustomException e) {
			
			log.error("error: "+e);
			model.addAttribute("listaLimitesGenerales", listaClientesPersonalizados);
			model.addAttribute("mensajeError", e.getMessage());
			return "convenio/limitesGenerales/listaLimitesGenerales";
		}
		
		
	}	
	
	@GetMapping("/formClientePersonalizado")
	public String formClientePersonalizado(Model model) {

		return "convenio/clientesPersonalizados/formClientesPersonalizados";
	}
	
	@GetMapping("/index1")
	public String index1(Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a index clientesPersonalizadosWs");

		List<ClientesPersonalizados> listaClientesPersonalizados = new ArrayList<>();

		ClienteRequest clienteRequest = new ClienteRequest();
		clienteRequest.setIdUsuario("test");
		clienteRequest.setIdSesion("20210101121213");
		clienteRequest.setCodUsuario("E66666");
		clienteRequest.setCanal("8");
		ClientesPersonalizados clientesPersonalizados = new ClientesPersonalizados();
		clienteRequest.setCliente(clientesPersonalizados);

		ClienteResponse clienteResponse = new ClienteResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = clientePersonalizadoServiceApiRest.consultarWs(clienteRequest);
		log.info("responseMoneda: " + respuesta);
		log.info("respuesta.getBody(): " + respuesta.getBody());
		log.info("retorno.getStatus(): " + respuesta.getStatus());
		log.info("respuesta.isExitoso(): " + respuesta.isExitoso());

		if (respuesta.isExitoso()) {
			if (respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista clientes personalizados");
				try {
					clienteResponse = mapper.jsonToClass(respuesta.getBody(), ClienteResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
				log.info("clienteResponse: " + clienteResponse);
				log.info(clienteResponse.getResultado().getCodigo());
				listaClientesPersonalizados = clienteResponse.getListaClientes();
				model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
				return "convenio/clientesPersonalizados/listaClientesPersonalizados";
			} else {
				if (respuesta.getStatus() == 422) {
					log.info("entro en error 422");
					try {
						resultado = mapper.jsonToClass(respuesta.getBody(), Resultado.class);
						log.info("resultado: " + resultado);
					} catch (IOException e) {
						e.printStackTrace();
					}
					redirectAttributes.addFlashAttribute("mensajeError",
							" Codigo :" + resultado.getCodigo() + " descripcion: " + resultado.getDescripcion());
					return "convenio/clientesPersonalizados/listaClientesPersonalizados";
				}
			}
		} else {
			log.info("error conectar microservicio consultarWs clientesPersonalizados");
			return "redirect:/";
		}

		return "convenio/clientesPersonalizados/listaClientesPersonalizados";
	}

	@GetMapping("/search1")
	public String search1(
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

		ClienteResponse clienteResponse = new ClienteResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = clientePersonalizadoServiceApiRest.consultarWs(clienteRequest);
		log.info("responseMoneda: " + respuesta);
		log.info("respuesta.getBody(): " + respuesta.getBody());
		log.info("retorno.getStatus(): " + respuesta.getStatus());
		log.info("respuesta.isExitoso(): " + respuesta.isExitoso());

		if (respuesta.isExitoso()) {
			if (respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista clientes personalizados");
				try {
					clienteResponse = mapper.jsonToClass(respuesta.getBody(), ClienteResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				log.info("clienteResponse: " + clienteResponse);
				log.info(clienteResponse.getResultado().getCodigo());
				log.info(clienteResponse.getResultado().getCodigo());
				if (clienteResponse.getResultado().getCodigo().equals("0000")) {
					log.info("Respusta codigo 0000 si existe la modena");
					listaClientesPersonalizados = clienteResponse.getListaClientes();
					model.addAttribute("listaClientesPersonalizados", listaClientesPersonalizados);
					return "convenio/clientesPersonalizados/listaClientesPersonalizados";
				}else {
						// if(monedaResponse.getResultado().getCodigo().equals("0000")){
						if (clienteResponse.getResultado().getCodigo().equals("0001")) {
							log.info("Respusta codigo 0001 recurso no encontrado");
							resultado = clienteResponse.getResultado();
							redirectAttributes.addFlashAttribute("mensajeError",
									" Codigo :" + resultado.getCodigo() + " descripcion: " + resultado.getDescripcion());
							return "redirect:/clientesPersonalizados/index";
						}
				}
				
			} else {
				if (respuesta.getStatus() == 422) {
					log.info("entro en error 422");
					try {
						resultado = mapper.jsonToClass(respuesta.getBody(), Resultado.class);
						log.info("resultado: " + resultado);
					} catch (IOException e) {
						e.printStackTrace();
					}
					redirectAttributes.addFlashAttribute("mensajeError",
							" Codigo :" + resultado.getCodigo() + " descripcion: " + resultado.getDescripcion());
					return "redirect:/clientesPersonalizados/index";
				}
			}
		} else {
			log.info("error conectar microservicio consultarWs clientesPersonalizados");
			return "redirect:/";
		}

		return "convenio/clientesPersonalizados/listaClientesPersonalizados";
	}

	@GetMapping("/activar1/{codigoIbs}")
	public String activarWs1(@PathVariable("codigoIbs") String codigoIbs, Model model,
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

		Response response = new Response();
		ClienteResponse clienteResponse = new ClienteResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = clientePersonalizadoServiceApiRest.consultarWs(clienteRequest);
		log.info("responseMoneda: " + respuesta);
		log.info("respuesta.getBody(): " + respuesta.getBody());
		log.info("retorno.getStatus(): " + respuesta.getStatus());
		log.info("respuesta.isExitoso(): " + respuesta.isExitoso());

		if (respuesta.isExitoso()) {
			if (respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista clientes personalizados");
				try {
					clienteResponse = mapper.jsonToClass(respuesta.getBody(), ClienteResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
				log.info("clienteResponse: " + clienteResponse);
				log.info(clienteResponse.getResultado().getCodigo());
				if (clienteResponse.getResultado().getCodigo().equals("0000")) {
					log.info("Respusta codigo 0000 si existe la modena");
					clientesPersonalizadosEdit = clienteResponse.getListaClientes().get(0);
					clientesPersonalizadosEdit.setFlagActivo(true);
					// clientesPersonalizadosEdit.setCodUsuario(null);
					// clientesPersonalizadosEdit.setFechaModificacion(null);

					clienteRequest.setCliente(clientesPersonalizadosEdit);
					WSResponse respuestaActualizar = clientePersonalizadoServiceApiRest.actualizarWs(clienteRequest);
					log.info("respuestaActualizar: " + respuestaActualizar);
					log.info("respuestaActualizar.getBody(): " + respuestaActualizar.getBody());
					log.info("respuestaActualizar.getStatus(): " + respuestaActualizar.getStatus());
					log.info("respuestaActualizar.isExitoso(): " + respuestaActualizar.isExitoso());
					if (respuestaActualizar.isExitoso()) {
						if (respuestaActualizar.getStatus() == 200) {
							log.info("Respusta codigo 200 en Actualizar el limitePersonalizado por codigo");
							try {
								response = mapper.jsonToClass(respuestaActualizar.getBody(), Response.class);
								log.info("response: " + response);

							} catch (IOException e) {
								e.printStackTrace();
							}
							redirectAttributes.addFlashAttribute("mensaje",
									" Codigo :" + response.getResultado().getCodigo() + " descripcion: "
											+ response.getResultado().getDescripcion());
							return "redirect:/clientesPersonalizados/index";

						} else {
							if (respuestaActualizar.getStatus() == 422 || respuestaActualizar.getStatus() == 400) {
								log.info("Respusta codigo " + respuestaActualizar.getStatus()
										+ "en Actualizar la moneda por codigo");
								try {
									response = mapper.jsonToClass(respuestaActualizar.getBody(), Response.class);
									log.info("response: " + response);

								} catch (IOException e) {
									e.printStackTrace();
								}
								redirectAttributes.addFlashAttribute("mensajeError",
										" Codigo :" + response.getResultado().getCodigo() + " descripcion: "
												+ response.getResultado().getDescripcion());
								return "redirect:/clientesPersonalizados/index";
							}
						}
					} else {
						log.info("error conectar microservicio actualizarWs limitesPersonalizados");
						return "redirect:/";
					}
				} else {
					// if(monedaResponse.getResultado().getCodigo().equals("0000")){
					if (clienteResponse.getResultado().getCodigo().equals("0001")) {
						log.info("Respusta codigo 0001 recurso no encontrado");
						resultado = clienteResponse.getResultado();
						redirectAttributes.addFlashAttribute("mensajeError",
								" Codigo :" + resultado.getCodigo() + " descripcion: " + resultado.getDescripcion());
						return "redirect:/clientesPersonalizados/index";
					}
				}

			} else {
				if (respuesta.getStatus() == 422) {
					log.info("entro en error 422");
					try {
						resultado = mapper.jsonToClass(respuesta.getBody(), Resultado.class);
						log.info("resultado: " + resultado);
					} catch (IOException e) {
						e.printStackTrace();
					}
					redirectAttributes.addFlashAttribute("mensajeError",
							" Codigo :" + resultado.getCodigo() + " descripcion: " + resultado.getDescripcion());
					return "convenio/clientesPersonalizados/listaClientesPersonalizados";
				}
			}
		} else {
			log.info("error conectar microservicio consultarWs clientesPersonalizados");
			return "redirect:/";
		}
		return "convenio/clientesPersonalizados/listaClientesPersonalizados";
	}

	@GetMapping("/desactivar1/{codigoIbs}")
	public String desactivarWs1(@PathVariable("codigoIbs") String codigoIbs, Model model,
			RedirectAttributes redirectAttributes) {
		log.info("desactivarWs");
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

		Response response = new Response();
		ClienteResponse clienteResponse = new ClienteResponse();
		Resultado resultado = new Resultado();
		WSResponse respuesta = clientePersonalizadoServiceApiRest.consultarWs(clienteRequest);
		log.info("responseMoneda: " + respuesta);
		log.info("respuesta.getBody(): " + respuesta.getBody());
		log.info("retorno.getStatus(): " + respuesta.getStatus());
		log.info("respuesta.isExitoso(): " + respuesta.isExitoso());

		if (respuesta.isExitoso()) {
			if (respuesta.getStatus() == 200) {
				log.info("Respusta codigo 200 en buscar la lista clientes personalizados");
				try {
					clienteResponse = mapper.jsonToClass(respuesta.getBody(), ClienteResponse.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
				log.info("clienteResponse: " + clienteResponse);
				log.info(clienteResponse.getResultado().getCodigo());
				if (clienteResponse.getResultado().getCodigo().equals("0000")) {
					log.info("Respusta codigo 0000 si existe la modena");
					clientesPersonalizadosEdit = clienteResponse.getListaClientes().get(0);
					clientesPersonalizadosEdit.setFlagActivo(false);
					// clientesPersonalizadosEdit.setCodUsuario(null);
					// clientesPersonalizadosEdit.setFechaModificacion(null);

					clienteRequest.setCliente(clientesPersonalizadosEdit);
					WSResponse respuestaActualizar = clientePersonalizadoServiceApiRest.actualizarWs(clienteRequest);
					log.info("respuestaActualizar: " + respuestaActualizar);
					log.info("respuestaActualizar.getBody(): " + respuestaActualizar.getBody());
					log.info("respuestaActualizar.getStatus(): " + respuestaActualizar.getStatus());
					log.info("respuestaActualizar.isExitoso(): " + respuestaActualizar.isExitoso());
					if (respuestaActualizar.isExitoso()) {
						if (respuestaActualizar.getStatus() == 200) {
							log.info("Respusta codigo 200 en Actualizar el limitePersonalizado por codigo");
							try {
								response = mapper.jsonToClass(respuestaActualizar.getBody(), Response.class);
								log.info("response: " + response);

							} catch (IOException e) {
								e.printStackTrace();
							}
							redirectAttributes.addFlashAttribute("mensaje",
									" Codigo :" + response.getResultado().getCodigo() + " descripcion: "
											+ response.getResultado().getDescripcion());
							return "redirect:/clientesPersonalizados/index";

						} else {
							if (respuestaActualizar.getStatus() == 422 || respuestaActualizar.getStatus() == 400) {
								log.info("Respusta codigo " + respuestaActualizar.getStatus()
										+ "en Actualizar la moneda por codigo");
								try {
									response = mapper.jsonToClass(respuestaActualizar.getBody(), Response.class);
									log.info("response: " + response);

								} catch (IOException e) {
									e.printStackTrace();
								}
								redirectAttributes.addFlashAttribute("mensajeError",
										" Codigo :" + response.getResultado().getCodigo() + " descripcion: "
												+ response.getResultado().getDescripcion());
								return "redirect:/clientesPersonalizados/index";
							}
						}
					} else {
						log.info("error conectar microservicio actualizarWs limitesPersonalizados");
						return "redirect:/";
					}
				} else {
					// if(monedaResponse.getResultado().getCodigo().equals("0000")){
					if (clienteResponse.getResultado().getCodigo().equals("0001")) {
						log.info("Respusta codigo 0001 recurso no encontrado");
						resultado = clienteResponse.getResultado();
						redirectAttributes.addFlashAttribute("mensajeError",
								" Codigo :" + resultado.getCodigo() + " descripcion: " + resultado.getDescripcion());
						return "redirect:/clientesPersonalizados/index";
					}
				}

			} else {
				if (respuesta.getStatus() == 422) {
					log.info("entro en error 422");
					try {
						resultado = mapper.jsonToClass(respuesta.getBody(), Resultado.class);
						log.info("resultado: " + resultado);
					} catch (IOException e) {
						e.printStackTrace();
					}
					redirectAttributes.addFlashAttribute("mensajeError",
							" Codigo :" + resultado.getCodigo() + " descripcion: " + resultado.getDescripcion());
					return "convenio/clientesPersonalizados/listaClientesPersonalizados";
				}
			}
		} else {
			log.info("error conectar microservicio consultarWs clientesPersonalizados");
			return "redirect:/";
		}
		return "convenio/clientesPersonalizados/listaClientesPersonalizados";
	}

	

	@ModelAttribute
	public void setGenericos(Model model) {
		ClientesPersonalizados clientesPersonalizadosSearch = new ClientesPersonalizados();
		model.addAttribute("clientesPersonalizadosSearch", clientesPersonalizadosSearch);
	}
}
