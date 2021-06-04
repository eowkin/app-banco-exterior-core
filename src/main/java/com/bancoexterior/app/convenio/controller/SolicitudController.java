package com.bancoexterior.app.convenio.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.bancoexterior.app.convenio.apiRest.IAcumuladosServiceApiRest;
import com.bancoexterior.app.convenio.apiRest.IMovimientosApiRest;
import com.bancoexterior.app.convenio.dto.AcumuladoCompraVentaResponse;
import com.bancoexterior.app.convenio.dto.AcumuladoRequest;
import com.bancoexterior.app.convenio.dto.AcumuladoResponse;
import com.bancoexterior.app.convenio.dto.AprobarRechazarRequest;
import com.bancoexterior.app.convenio.dto.MovimientosRequest;
import com.bancoexterior.app.convenio.dto.MovimientosResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.Compra;
import com.bancoexterior.app.convenio.model.DatosConsulta;
import com.bancoexterior.app.convenio.model.DatosPaginacion;
import com.bancoexterior.app.convenio.model.Movimiento;
import com.bancoexterior.app.convenio.model.Solicitud;
import com.bancoexterior.app.convenio.model.Venta;
import com.bancoexterior.app.util.UserExcelExporter;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/solicitudes")
public class SolicitudController {
		
	@Autowired
	private IMovimientosApiRest movimientosApiRest;
	
	@Autowired
	private IAcumuladosServiceApiRest acumuladosServiceApiRest;
	
	
	
	
	
	
	
	@GetMapping("/listaSolicitudesMovimientosPorAprobarVentas/{page}")
	public String consultaMovimientoPorAprobarVenta(@PathVariable("page") int page,Model model) {
		log.info("page: "+page);
		MovimientosRequest movimientosRequest = new MovimientosRequest();
		movimientosRequest.setIdUsuario("test");
		movimientosRequest.setIdSesion("20210101121213");
		movimientosRequest.setUsuario("E66666");
		movimientosRequest.setCanal("8");
		
		List<Movimiento> listaMovimientosVenta = new ArrayList<>();
		DatosPaginacion datosPaginacionVenta = new DatosPaginacion();
		
		List<Movimiento> listaMovimientosCompra = new ArrayList<>();
		DatosPaginacion datosPaginacionCompra = new DatosPaginacion();
		try {
		
			movimientosRequest.setNumeroPagina(page);
			movimientosRequest.setTamanoPagina(5);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			filtrosVenta.setEstatus(0);
			movimientosRequest.setFiltros(filtrosVenta);
			MovimientosResponse responseVenta = movimientosApiRest.consultarMovimientosPorAprobar(movimientosRequest);
			if(responseVenta != null) {
				datosPaginacionVenta = responseVenta.getDatosPaginacion();
				log.info("datosPaginacionVenta: "+datosPaginacionVenta);
				listaMovimientosVenta = responseVenta.getMovimientos();
				log.info("listaMovimientosVenta: "+listaMovimientosVenta);
				log.info("listaMovimientosVenta.size: "+listaMovimientosVenta.size());
				model.addAttribute("listaMovimientosVenta", listaMovimientosVenta);
				model.addAttribute("datosPaginacionVenta", datosPaginacionVenta);
				
				movimientosRequest.setNumeroPagina(1);
				movimientosRequest.setTamanoPagina(5);
				Movimiento filtrosCompra = new Movimiento();
				filtrosCompra.setTipoTransaccion("C");
				filtrosCompra.setEstatus(0);
				movimientosRequest.setFiltros(filtrosCompra);
				MovimientosResponse responseCompra = movimientosApiRest.consultarMovimientosPorAprobar(movimientosRequest);
				
				if(responseCompra != null) {
					datosPaginacionCompra = responseCompra.getDatosPaginacion();
					log.info("datosPaginacionCompra: "+datosPaginacionCompra);
					listaMovimientosCompra = responseCompra.getMovimientos();
					log.info("listaMovimientosCompra: "+listaMovimientosCompra);
					log.info("listaMovimientosCompra.size: "+listaMovimientosCompra.size());
					model.addAttribute("listaMovimientosCompra", listaMovimientosCompra);
					model.addAttribute("datosPaginacionCompra", datosPaginacionCompra);
					
					return "convenio/solicitudes/listaSolicitudesMovimientosPorAprobarVenta";
					
					
					
				}else {
					datosPaginacionCompra.setTotalPaginas(0);
					model.addAttribute("listaMovimientosCompra", listaMovimientosCompra);
					model.addAttribute("datosPaginacionCompra", datosPaginacionCompra);
					return "convenio/solicitudes/listaSolicitudesMovimientosPorAporbarVenta";
				}
				
			}else {
				return "redirect:/";
			}
		} catch (CustomException e) {
			log.error("error: "+e);
			return "redirect:/";
		}
	}
	
	@GetMapping("/listaSolicitudesMovimientosPorAprobarCompra/{page}")
	public String consultaMovimientoPorAprobarCompra(@PathVariable("page") int page,Model model) {
		log.info("page: "+page);
		MovimientosRequest movimientosRequest = new MovimientosRequest();
		movimientosRequest.setIdUsuario("test");
		movimientosRequest.setIdSesion("20210101121213");
		movimientosRequest.setUsuario("E66666");
		movimientosRequest.setCanal("8");
		
		List<Movimiento> listaMovimientosVenta = new ArrayList<>();
		DatosPaginacion datosPaginacionVenta = new DatosPaginacion();
		
		List<Movimiento> listaMovimientosCompra = new ArrayList<>();
		DatosPaginacion datosPaginacionCompra = new DatosPaginacion();
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(5);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			filtrosVenta.setEstatus(0);
			movimientosRequest.setFiltros(filtrosVenta);
			MovimientosResponse responseVenta = movimientosApiRest.consultarMovimientosPorAprobar(movimientosRequest);
			if(responseVenta != null) {
				datosPaginacionVenta = responseVenta.getDatosPaginacion();
				log.info("datosPaginacionVenta: "+datosPaginacionVenta);
				listaMovimientosVenta = responseVenta.getMovimientos();
				log.info("listaMovimientosVenta: "+listaMovimientosVenta);
				log.info("listaMovimientosVenta.size: "+listaMovimientosVenta.size());
				model.addAttribute("listaMovimientosVenta", listaMovimientosVenta);
				model.addAttribute("datosPaginacionVenta", datosPaginacionVenta);
				
				movimientosRequest.setNumeroPagina(page);
				movimientosRequest.setTamanoPagina(5);
				Movimiento filtrosCompra = new Movimiento();
				filtrosCompra.setTipoTransaccion("C");
				filtrosCompra.setEstatus(0);
				movimientosRequest.setFiltros(filtrosCompra);
				MovimientosResponse responseCompra = movimientosApiRest.consultarMovimientosPorAprobar(movimientosRequest);
				
				if(responseCompra != null) {
					datosPaginacionCompra = responseCompra.getDatosPaginacion();
					log.info("datosPaginacionCompra: "+datosPaginacionCompra);
					listaMovimientosCompra = responseCompra.getMovimientos();
					log.info("listaMovimientosCompra: "+listaMovimientosCompra);
					log.info("listaMovimientosCompra.size: "+listaMovimientosCompra.size());
					model.addAttribute("listaMovimientosCompra", listaMovimientosCompra);
					model.addAttribute("datosPaginacionCompra", datosPaginacionCompra);
					
					return "convenio/solicitudes/listaSolicitudesMovimientosPorAprobarVenta";
					
					
					
				}else {
					datosPaginacionCompra.setTotalPaginas(0);
					model.addAttribute("listaMovimientosCompra", listaMovimientosCompra);
					model.addAttribute("datosPaginacionCompra", datosPaginacionCompra);
					return "convenio/solicitudes/listaSolicitudesMovimientosPorAporbarVenta";
				}
				
			}else {
				return "redirect:/";
			}
		} catch (CustomException e) {
			log.error("error: "+e);
			return "redirect:/";
		}
	}
	
	
	
	@GetMapping("/listaSolicitudesMovimientosVentas/{page}")
	public String consultaMovimientoVenta(@PathVariable("page") int page,Model model) {
		log.info("page: "+page);
		MovimientosRequest movimientosRequest = new MovimientosRequest();
		movimientosRequest.setIdUsuario("test");
		movimientosRequest.setIdSesion("20210101121213");
		movimientosRequest.setUsuario("E66666");
		movimientosRequest.setCanal("8");
		
		List<Movimiento> listaMovimientosVenta = new ArrayList<>();
		DatosPaginacion datosPaginacionVenta = new DatosPaginacion();
		
		List<Movimiento> listaMovimientosCompra = new ArrayList<>();
		DatosPaginacion datosPaginacionCompra = new DatosPaginacion();
		try {
			movimientosRequest.setNumeroPagina(page);
			movimientosRequest.setTamanoPagina(10);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			movimientosRequest.setFiltros(filtrosVenta);
			MovimientosResponse responseVenta = movimientosApiRest.consultarMovimientos(movimientosRequest);
			if(responseVenta != null) {
				datosPaginacionVenta = responseVenta.getDatosPaginacion();
				log.info("datosPaginacionVenta: "+datosPaginacionVenta);
				listaMovimientosVenta = responseVenta.getMovimientos();
				log.info("listaMovimientosVenta: "+listaMovimientosVenta);
				log.info("listaMovimientosVenta.size: "+listaMovimientosVenta.size());
				model.addAttribute("listaMovimientosVenta", listaMovimientosVenta);
				model.addAttribute("datosPaginacionVenta", datosPaginacionVenta);
				
				movimientosRequest.setNumeroPagina(1);
				movimientosRequest.setTamanoPagina(10);
				Movimiento filtrosCompra = new Movimiento();
				filtrosCompra.setTipoTransaccion("C");
				movimientosRequest.setFiltros(filtrosCompra);
				MovimientosResponse responseCompra = movimientosApiRest.consultarMovimientos(movimientosRequest);
				
				if(responseCompra != null) {
					datosPaginacionCompra = responseCompra.getDatosPaginacion();
					log.info("datosPaginacionCompra: "+datosPaginacionCompra);
					listaMovimientosCompra = responseCompra.getMovimientos();
					log.info("listaMovimientosCompra: "+listaMovimientosCompra);
					log.info("listaMovimientosCompra.size: "+listaMovimientosCompra.size());
					model.addAttribute("listaMovimientosCompra", listaMovimientosCompra);
					model.addAttribute("datosPaginacionCompra", datosPaginacionCompra);
					
					return "convenio/solicitudes/listaSolicitudesMovimientosVenta";
					
					
					
				}else {
					datosPaginacionCompra.setTotalPaginas(0);
					model.addAttribute("listaMovimientosCompra", listaMovimientosCompra);
					model.addAttribute("datosPaginacionCompra", datosPaginacionCompra);
					return "convenio/solicitudes/listaSolicitudesMovimientosVenta";
				}
				
			}else {
				return "redirect:/";
			}
		} catch (CustomException e) {
			log.error("error: "+e);
			return "redirect:/";
		}
	}
	
	
	@GetMapping("/listaSolicitudesMovimientosCompras/{page}")
	public String consultaMovimientoCompra(@PathVariable("page") int page,Model model) {
		log.info("page: "+page);
		MovimientosRequest movimientosRequest = new MovimientosRequest();
		movimientosRequest.setIdUsuario("test");
		movimientosRequest.setIdSesion("20210101121213");
		movimientosRequest.setUsuario("E66666");
		movimientosRequest.setCanal("8");
		
		List<Movimiento> listaMovimientosVenta = new ArrayList<>();
		DatosPaginacion datosPaginacionVenta = new DatosPaginacion();
		
		List<Movimiento> listaMovimientosCompra = new ArrayList<>();
		DatosPaginacion datosPaginacionCompra = new DatosPaginacion();
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(10);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			movimientosRequest.setFiltros(filtrosVenta);
			MovimientosResponse responseVenta = movimientosApiRest.consultarMovimientos(movimientosRequest);
			if(responseVenta != null) {
				datosPaginacionVenta = responseVenta.getDatosPaginacion();
				log.info("datosPaginacionVenta: "+datosPaginacionVenta);
				listaMovimientosVenta = responseVenta.getMovimientos();
				log.info("listaMovimientosVenta: "+listaMovimientosVenta);
				log.info("listaMovimientosVenta.size: "+listaMovimientosVenta.size());
				model.addAttribute("listaMovimientosVenta", listaMovimientosVenta);
				model.addAttribute("datosPaginacionVenta", datosPaginacionVenta);
				
				movimientosRequest.setNumeroPagina(page);
				movimientosRequest.setTamanoPagina(10);
				Movimiento filtrosCompra = new Movimiento();
				filtrosCompra.setTipoTransaccion("C");
				movimientosRequest.setFiltros(filtrosCompra);
				MovimientosResponse responseCompra = movimientosApiRest.consultarMovimientos(movimientosRequest);
				
				if(responseCompra != null) {
					datosPaginacionCompra = responseCompra.getDatosPaginacion();
					log.info("datosPaginacionCompra: "+datosPaginacionCompra);
					listaMovimientosCompra = responseCompra.getMovimientos();
					log.info("listaMovimientosCompra: "+listaMovimientosCompra);
					log.info("listaMovimientosCompra.size: "+listaMovimientosCompra.size());
					model.addAttribute("listaMovimientosCompra", listaMovimientosCompra);
					model.addAttribute("datosPaginacionCompra", datosPaginacionCompra);
					
					return "convenio/solicitudes/listaSolicitudesMovimientosCompra";
					
					
					
				}else {
					datosPaginacionCompra.setTotalPaginas(0);
					model.addAttribute("listaMovimientosCompra", listaMovimientosCompra);
					model.addAttribute("datosPaginacionCompra", datosPaginacionCompra);
					return "convenio/solicitudes/listaSolicitudesMovimientosCompra";
				}
				
			}else {
				return "redirect:/";
			}
		} catch (CustomException e) {
			log.error("error: "+e);
			return "redirect:/";
		}
	}
	
	@GetMapping("/procesarCompra/{codOperacion}/{page}")
	public String procesarCompra(@PathVariable("codOperacion") String codOperacion, @PathVariable("page") int page, Model model,
			RedirectAttributes redirectAttributes, Movimiento movimiento ) {
		log.info("procesarCompra");
		log.info("codOperacion: "+codOperacion);
		log.info("page: "+page);
		
		MovimientosRequest movimientosRequest = new MovimientosRequest();
		movimientosRequest.setIdUsuario("test");
		movimientosRequest.setIdSesion("20210101121213");
		movimientosRequest.setUsuario("E66666");
		movimientosRequest.setCanal("8");
		
		Movimiento movimientoProcesar = new Movimiento();
		
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(5);
			Movimiento filtros = new Movimiento();
			//filtrosVenta.setTipoTransaccion("V");
			//filtrosVenta.setEstatus(0);
			filtros.setCodOperacion(codOperacion);
			movimientosRequest.setFiltros(filtros);
			MovimientosResponse response = movimientosApiRest.consultarMovimientos(movimientosRequest);
			
			if(response.getResultado().getCodigo().equals("0000")) {
				movimientoProcesar = response.getMovimientos().get(0);
				movimientoProcesar.setPaginaActual(page);
				log.info("movimientoProcesar: "+movimientoProcesar);
				model.addAttribute("paginaActual", page);
				model.addAttribute("movimiento", movimientoProcesar);
				return "convenio/solicitudes/formSolicitud";
			}else {
				String mensajeError = response.getResultado().getCodigo() + " " + response.getResultado().getDescripcion();
				redirectAttributes.addFlashAttribute("mensajeError", mensajeError);
				return "redirect:/solicitudes/listaSolicitudesMovimientosPorAprobarCompra/"+page;
			}
			
			
			
			
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError",e.getMessage());
			return "redirect:/solicitudes/listaSolicitudesMovimientosPorAprobarCompra/"+page;
		}
	}
	
	@PostMapping("/guardarProcesarCompra")
	public String guardarProcesarCompra(Movimiento movimiento, BindingResult result, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		log.info("movimiento: "+movimiento);
		model.addAttribute("paginaActual", movimiento.getPaginaActual());
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				log.info("Ocurrio un error: " + error.getDefaultMessage());
			}
			
			
			
			return "convenio/solicitudes/formSolicitud";
		}
		
		//firstBigDecimal.compareTo(secondBigDecimal) < 0 // "<"
				//firstBigDecimal.compareTo(secondBigDecimal) > 0 // ">"    
				//firstBigDecimal.compareTo(secondBigDecimal) == 0 // "=="  
				//firstBigDecimal.compareTo(secondBigDecimal) >= 0 // ">="
		/*
		 * log.info("Comparar tamaño:" + movimiento.getNuevaTasaCliente().compareTo
		 * (movimiento.getTasaCliente())); if(movimiento.getNuevaTasaCliente().compareTo
		 * (movimiento.getTasaCliente()) < 0) { result.addError(new
		 * ObjectError("codMoneda", " La tasa de cambio es manor que la nueva tasa"));
		 * return "convenio/solicitudes/formSolicitud"; }
		 */
		
		if(!isFechaValida(movimiento.getFecha())) {
			result.addError(new ObjectError("codMoneda", " La fecha liquidacion es invalida"));
			return "convenio/solicitudes/formSolicitud";
		}
		
		AprobarRechazarRequest aprobarRechazarRequest = new AprobarRechazarRequest();
		aprobarRechazarRequest.setIdUsuario("test");
		aprobarRechazarRequest.setIdSesion("20210101121213");
		aprobarRechazarRequest.setCodUsuario("E66666");
		aprobarRechazarRequest.setCanal("8");
		aprobarRechazarRequest.setIp(request.getRemoteAddr());
		aprobarRechazarRequest.setOrigen("01");
		aprobarRechazarRequest.setCodSolicitud(movimiento.getCodOperacion());
		aprobarRechazarRequest.setTasa(movimiento.getNuevaTasaCliente());
		aprobarRechazarRequest.setFechaLiquidacion(movimiento.getFecha());
		aprobarRechazarRequest.setEstatus(1);
		
		try {
			String respuesta = movimientosApiRest.aprobarCompra(aprobarRechazarRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/solicitudes/listaSolicitudesMovimientosPorAprobarCompra/"+movimiento.getPaginaActual();
			
		} catch (CustomException e) {
			log.error("error: "+e);
			//redirectAttributes.addFlashAttribute("mensajeError",e.getMessage());
			result.addError(new ObjectError("codMoneda", " Codigo :" +e.getMessage()));
			return "convenio/solicitudes/formSolicitud";
			//return "redirect:/solicitudes/listaSolicitudesMovimientosPorAprobarCompra/"+movimiento.getPaginaActual();
			
			//model.addAttribute("mensajeError", e.getMessage());
			//return "convenio/agencia/formBuscarAgencia";
		}
	}
	
	@GetMapping("/procesarVenta/{codOperacion}/{page}")
	public String procesarVenta(@PathVariable("codOperacion") String codOperacion, @PathVariable("page") int page, Model model,
			RedirectAttributes redirectAttributes, Movimiento movimiento ) {
		log.info("procesarCompra");
		log.info("codOperacion: "+codOperacion);
		log.info("page: "+page);
		
		MovimientosRequest movimientosRequest = new MovimientosRequest();
		movimientosRequest.setIdUsuario("test");
		movimientosRequest.setIdSesion("20210101121213");
		movimientosRequest.setUsuario("E66666");
		movimientosRequest.setCanal("8");
		
		Movimiento movimientoProcesar = new Movimiento();
		
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(5);
			Movimiento filtros = new Movimiento();
			filtros.setTipoTransaccion("V");
			//filtrosVenta.setEstatus(0);
			filtros.setCodOperacion(codOperacion);
			movimientosRequest.setFiltros(filtros);
			MovimientosResponse response = movimientosApiRest.consultarMovimientos(movimientosRequest);
			
			if(response.getResultado().getCodigo().equals("0000")) {
				movimientoProcesar = response.getMovimientos().get(0);
				movimientoProcesar.setPaginaActual(page);
				log.info("movimientoProcesar: "+movimientoProcesar);
				model.addAttribute("paginaActual", page);
				model.addAttribute("movimiento", movimientoProcesar);
				return "convenio/solicitudes/formSolicitudVenta";
			}else {
				String mensajeError = response.getResultado().getCodigo() + " " + response.getResultado().getDescripcion();
				redirectAttributes.addFlashAttribute("mensajeErrorVenta", mensajeError);
				return "redirect:/solicitudes/listaSolicitudesMovimientosPorAprobarVentas/"+page;
			}
			
			
			
			
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeErrorVenta",e.getMessage());
			return "redirect:/solicitudes/listaSolicitudesMovimientosPorAprobarVentas/"+page;
		}
	}
	
	
	@PostMapping("/guardarProcesarVenta")
	public String guardarProcesarVenta(Movimiento movimiento, BindingResult result, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		log.info("movimiento: "+movimiento);
		model.addAttribute("paginaActual", movimiento.getPaginaActual());
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				log.info("Ocurrio un error: " + error.getDefaultMessage());
			}
			
			
			
			return "convenio/solicitudes/formSolicitudVenta";
		}
		
		//firstBigDecimal.compareTo(secondBigDecimal) < 0 // "<"
				//firstBigDecimal.compareTo(secondBigDecimal) > 0 // ">"    
				//firstBigDecimal.compareTo(secondBigDecimal) == 0 // "=="  
				//firstBigDecimal.compareTo(secondBigDecimal) >= 0 // ">="
		/*
		 * log.info("Comparar tamaño:" + movimiento.getNuevaTasaCliente().compareTo
		 * (movimiento.getTasaCliente())); if(movimiento.getNuevaTasaCliente().compareTo
		 * (movimiento.getTasaCliente()) < 0) { result.addError(new
		 * ObjectError("codMoneda", " La tasa de cambio es manor que la nueva tasa"));
		 * return "convenio/solicitudes/formSolicitud"; }
		 */
		
		if(!isFechaValida(movimiento.getFecha())) {
			result.addError(new ObjectError("codMoneda", " La fecha liquidacion es invalida"));
			return "convenio/solicitudes/formSolicitudVenta";
		}
		
		AprobarRechazarRequest aprobarRechazarRequest = new AprobarRechazarRequest();
		aprobarRechazarRequest.setIdUsuario("test");
		aprobarRechazarRequest.setIdSesion("20210101121213");
		aprobarRechazarRequest.setCodUsuario("E66666");
		aprobarRechazarRequest.setCanal("8");
		aprobarRechazarRequest.setIp(request.getRemoteAddr());
		aprobarRechazarRequest.setOrigen("01");
		aprobarRechazarRequest.setCodSolicitud(movimiento.getCodOperacion());
		aprobarRechazarRequest.setTasa(movimiento.getNuevaTasaCliente());
		aprobarRechazarRequest.setFechaLiquidacion(movimiento.getFecha());
		aprobarRechazarRequest.setEstatus(1);
		
		try {
			String respuesta = movimientosApiRest.aprobarVenta(aprobarRechazarRequest);
			redirectAttributes.addFlashAttribute("mensajeVenta", respuesta);
			return "redirect:/solicitudes/listaSolicitudesMovimientosPorAprobarVentas/"+movimiento.getPaginaActual();
			
		} catch (CustomException e) {
			log.error("error: "+e);
			//redirectAttributes.addFlashAttribute("mensajeError",e.getMessage());
			result.addError(new ObjectError("codMoneda", " Codigo :" +e.getMessage()));
			return "convenio/solicitudes/formSolicitudVenta";
			//return "redirect:/solicitudes/listaSolicitudesMovimientosPorAprobarCompra/"+movimiento.getPaginaActual();
			
			//model.addAttribute("mensajeError", e.getMessage());
			//return "convenio/agencia/formBuscarAgencia";
		}
	}
	
	
	@GetMapping("/rechazarCompra/{codOperacion}/{tasa}/{page}")
	public String rechazarCompra(@PathVariable("codOperacion") String codOperacion, @PathVariable("tasa") BigDecimal tasa, 
			@PathVariable("page") int page, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request ) {
		log.info("rechazarCompra");
		log.info("codOperacion: "+codOperacion);
		log.info("tasa: "+tasa);
		log.info("page: "+page);
		
		
		
		AprobarRechazarRequest aprobarRechazarRequest = new AprobarRechazarRequest();
		aprobarRechazarRequest.setIdUsuario("test");
		aprobarRechazarRequest.setIdSesion("20210101121213");
		aprobarRechazarRequest.setCodUsuario("E66666");
		aprobarRechazarRequest.setCanal("8");
		aprobarRechazarRequest.setIp(request.getRemoteAddr());
		aprobarRechazarRequest.setOrigen("01");
		aprobarRechazarRequest.setCodSolicitud(codOperacion);
		aprobarRechazarRequest.setTasa(tasa);
		aprobarRechazarRequest.setFechaLiquidacion(fecha(new Date()));
		aprobarRechazarRequest.setEstatus(2);
		
		try {
			String respuesta = movimientosApiRest.rechazarCompra(aprobarRechazarRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/solicitudes/listaSolicitudesMovimientosPorAprobarCompra/"+page;
			
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError",e.getMessage());
			return "redirect:/solicitudes/listaSolicitudesMovimientosPorAprobarCompra/"+page;
			
			//model.addAttribute("mensajeError", e.getMessage());
			//return "convenio/agencia/formBuscarAgencia";
		}
	}
	
	@GetMapping("/aprobarCompra/{codOperacion}/{tasa}/{page}")
	public String aprobarCompra(@PathVariable("codOperacion") String codOperacion, @PathVariable("tasa") BigDecimal tasa, 
			@PathVariable("page") int page, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request ) {
		log.info("aprobarCompra");
		log.info("codOperacion: "+codOperacion);
		log.info("tasa: "+tasa);
		log.info("page: "+page);
		
		AprobarRechazarRequest aprobarRechazarRequest = new AprobarRechazarRequest();
		aprobarRechazarRequest.setIdUsuario("test");
		aprobarRechazarRequest.setIdSesion("20210101121213");
		aprobarRechazarRequest.setCodUsuario("E66666");
		aprobarRechazarRequest.setCanal("8");
		aprobarRechazarRequest.setIp(request.getRemoteAddr());
		aprobarRechazarRequest.setOrigen("01");
		aprobarRechazarRequest.setCodSolicitud(codOperacion);
		aprobarRechazarRequest.setTasa(tasa);
		aprobarRechazarRequest.setFechaLiquidacion(fecha(new Date()));
		aprobarRechazarRequest.setEstatus(1);
		
		try {
			String respuesta = movimientosApiRest.aprobarCompra(aprobarRechazarRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/solicitudes/listaSolicitudesMovimientosPorAprobarCompra/"+page;
			
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError",e.getMessage());
			return "redirect:/solicitudes/listaSolicitudesMovimientosPorAprobarCompra/"+page;
			
			//model.addAttribute("mensajeError", e.getMessage());
			//return "convenio/agencia/formBuscarAgencia";
		}
	}
	
	@GetMapping("/rechazarVenta/{codOperacion}/{tasa}/{page}")
	public String rechazarVenta(@PathVariable("codOperacion") String codOperacion, @PathVariable("tasa") BigDecimal tasa, 
			@PathVariable("page") int page, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request ) {
		log.info("rechazarVenta");
		log.info("codOperacion: "+codOperacion);
		log.info("tasa: "+tasa);
		log.info("page: "+page);
		
		
		AprobarRechazarRequest aprobarRechazarRequest = new AprobarRechazarRequest();
		aprobarRechazarRequest.setIdUsuario("test");
		aprobarRechazarRequest.setIdSesion("20210101121213");
		aprobarRechazarRequest.setCodUsuario("E66666");
		aprobarRechazarRequest.setCanal("8");
		aprobarRechazarRequest.setIp(request.getRemoteAddr());
		aprobarRechazarRequest.setOrigen("01");
		aprobarRechazarRequest.setCodSolicitud(codOperacion);
		aprobarRechazarRequest.setTasa(tasa);
		aprobarRechazarRequest.setFechaLiquidacion(fecha(new Date()));
		aprobarRechazarRequest.setEstatus(2);
		
		try {
			String respuesta = movimientosApiRest.rechazarVenta(aprobarRechazarRequest);
			redirectAttributes.addFlashAttribute("mensajeVenta", respuesta);
			return "redirect:/solicitudes/listaSolicitudesMovimientosPorAprobarVentas/"+page;
			
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeErrorVenta",e.getMessage());
			return "redirect:/solicitudes/listaSolicitudesMovimientosPorAprobarVentas/"+page;
			
			//model.addAttribute("mensajeError", e.getMessage());
			//return "convenio/agencia/formBuscarAgencia";
		}
	}
	
	@GetMapping("/aprobarVenta/{codOperacion}/{tasa}/{page}")
	public String aprobarVenta(@PathVariable("codOperacion") String codOperacion, @PathVariable("tasa") BigDecimal tasa, 
			@PathVariable("page") int page, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request ) {
		log.info("aprobarVenta");
		log.info("codOperacion: "+codOperacion);
		log.info("tasa: "+tasa);
		log.info("page: "+page);
		
		AprobarRechazarRequest aprobarRechazarRequest = new AprobarRechazarRequest();
		aprobarRechazarRequest.setIdUsuario("test");
		aprobarRechazarRequest.setIdSesion("20210101121213");
		aprobarRechazarRequest.setCodUsuario("E66666");
		aprobarRechazarRequest.setCanal("8");
		aprobarRechazarRequest.setIp(request.getRemoteAddr());
		aprobarRechazarRequest.setOrigen("01");
		aprobarRechazarRequest.setCodSolicitud(codOperacion);
		aprobarRechazarRequest.setTasa(tasa);
		aprobarRechazarRequest.setFechaLiquidacion(fecha(new Date()));
		aprobarRechazarRequest.setEstatus(1);
		
		try {
			String respuesta = movimientosApiRest.aprobarVenta(aprobarRechazarRequest);
			redirectAttributes.addFlashAttribute("mensajeVenta", respuesta);
			return "redirect:/solicitudes/listaSolicitudesMovimientosPorAprobarVentas/"+page;
			
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeErrorVenta",e.getMessage());
			return "redirect:/solicitudes/listaSolicitudesMovimientosPorAprobarVentas/"+page;
			
			//model.addAttribute("mensajeError", e.getMessage());
			//return "convenio/agencia/formBuscarAgencia";
		}
	}
	
	
	
	@GetMapping("/movimientos/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
		log.info("exportToExcel");
        
		
		MovimientosRequest movimientosRequest = new MovimientosRequest();
		movimientosRequest.setIdUsuario("test");
		movimientosRequest.setIdSesion("20210101121213");
		movimientosRequest.setUsuario("E66666");
		movimientosRequest.setCanal("8");
		
		List<Movimiento> listaMovimientos = new ArrayList<>();
		
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(2147483647);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			movimientosRequest.setFiltros(filtrosVenta);
			MovimientosResponse responseVenta = movimientosApiRest.consultarMovimientos(movimientosRequest);
			if(responseVenta != null) {
				listaMovimientos = responseVenta.getMovimientos();
				response.setContentType("application/octet-stream");
		        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		        String currentDateTime = dateFormatter.format(new Date());
		         
		        String headerKey = "Content-Disposition";
		        String headerValue = "attachment; filename=movimientosventas_" + currentDateTime + ".xlsx";
		        response.setHeader(headerKey, headerValue);
		         
		        
		         
		        UserExcelExporter excelExporter = new UserExcelExporter(listaMovimientos);
		         
		        excelExporter.export(response);
			}else {
				
			}
		} catch (CustomException e) {
			log.error("error: "+e);
			
		}
		
		
		
		
		
		
		
		
		
		    
    }  
	
	
	@GetMapping("/movimientosCompras/export/excel")
    public void exportToExcelCompra(HttpServletResponse response) throws IOException {
		log.info("exportToExcel");
        
		
		MovimientosRequest movimientosRequest = new MovimientosRequest();
		movimientosRequest.setIdUsuario("test");
		movimientosRequest.setIdSesion("20210101121213");
		movimientosRequest.setUsuario("E66666");
		movimientosRequest.setCanal("8");
		
		List<Movimiento> listaMovimientos = new ArrayList<>();
		
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(2147483647);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("C");
			movimientosRequest.setFiltros(filtrosVenta);
			MovimientosResponse responseVenta = movimientosApiRest.consultarMovimientos(movimientosRequest);
			if(responseVenta != null) {
				listaMovimientos = responseVenta.getMovimientos();
				response.setContentType("application/octet-stream");
		        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		        String currentDateTime = dateFormatter.format(new Date());
		         
		        String headerKey = "Content-Disposition";
		        String headerValue = "attachment; filename=movimientoscompras_" + currentDateTime + ".xlsx";
		        response.setHeader(headerKey, headerValue);
		         
		        
		         
		        UserExcelExporter excelExporter = new UserExcelExporter(listaMovimientos);
		         
		        excelExporter.export(response);
			}else {
				
			}
		} catch (CustomException e) {
			log.error("error: "+e);
			
		}	    
    }
	
	
	
	
	
	public void acumuladosCompraVenta()  {
		AcumuladoRequest acumuladoRequest = new AcumuladoRequest();
		acumuladoRequest.setIdUsuario("test");
		acumuladoRequest.setIdSesion("20210101121213");
		acumuladoRequest.setCanal("8");
		acumuladoRequest.setTipoAcumulado("2");
		DatosConsulta datosConsulta = new DatosConsulta();
		datosConsulta.setFechaDesde("2021-05-01");
		datosConsulta.setFechaHasta(fecha(new Date()));
		acumuladoRequest.setDatosConsulta(datosConsulta);
		
		try {
			AcumuladoCompraVentaResponse acumuladoCompraVentaResponse = acumuladosServiceApiRest.consultarAcumuladosCompraVenta(acumuladoRequest);
		} catch (CustomException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public Venta acumuladosVenta(String codMoneda)  {
		AcumuladoRequest acumuladoRequest = new AcumuladoRequest();
		acumuladoRequest.setIdUsuario("test");
		acumuladoRequest.setIdSesion("20210101121213");
		acumuladoRequest.setCanal("8");
		acumuladoRequest.setTipoAcumulado("2");
		DatosConsulta datosConsulta = new DatosConsulta();
		datosConsulta.setFechaDesde(fecha(new Date()));
		datosConsulta.setFechaHasta(fecha(new Date()));
		acumuladoRequest.setDatosConsulta(datosConsulta);
		List<Venta> listaVenta = new ArrayList<>();
		Venta ventaRes = new Venta("","", new BigDecimal(0), new BigDecimal(0));
		try {
			AcumuladoCompraVentaResponse acumuladoCompraVentaResponse = acumuladosServiceApiRest.consultarAcumuladosCompraVenta(acumuladoRequest);
			if(acumuladoCompraVentaResponse.getResultado().getCodigo().equals("0000")) {
				listaVenta = acumuladoCompraVentaResponse.getAcumuladosCompraVenta().getVenta();
				for (Venta venta : listaVenta) {
					if(venta.getCodMoneda().equals(codMoneda)) {
						ventaRes = venta;
					}
				}
				
				return ventaRes;
			}else {
				return ventaRes;
			}
		} catch (CustomException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ventaRes;
		}
		
		
	}
	
	public Compra acumuladosCompra(String codMoneda)  {
		AcumuladoRequest acumuladoRequest = new AcumuladoRequest();
		acumuladoRequest.setIdUsuario("test");
		acumuladoRequest.setIdSesion("20210101121213");
		acumuladoRequest.setCanal("8");
		acumuladoRequest.setTipoAcumulado("2");
		DatosConsulta datosConsulta = new DatosConsulta();
		datosConsulta.setFechaDesde(fecha(new Date()));
		datosConsulta.setFechaHasta(fecha(new Date()));
		acumuladoRequest.setDatosConsulta(datosConsulta);
		List<Compra> listaCompra = new ArrayList<>();
		Compra compraRes = new Compra();
		try {
			AcumuladoCompraVentaResponse acumuladoCompraVentaResponse = acumuladosServiceApiRest.consultarAcumuladosCompraVenta(acumuladoRequest);
			if(acumuladoCompraVentaResponse.getResultado().getCodigo().equals("0000")) {
				listaCompra = acumuladoCompraVentaResponse.getAcumuladosCompraVenta().getCompra();
				for (Compra compra : listaCompra) {
					if(compra.getCodMoneda().equals(codMoneda)) {
						compraRes = compra;
					}
				}
				
				return compraRes;
			}else {
				return compraRes;
			}
		} catch (CustomException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return compraRes;
		}
		
		
	}
	
	public void ofertasPorAprobar()  {
		AcumuladoRequest acumuladoRequest = new AcumuladoRequest();
		acumuladoRequest.setIdUsuario("test");
		acumuladoRequest.setIdSesion("20210101121213");
		acumuladoRequest.setCanal("8");
		acumuladoRequest.setTipoAcumulado("3");
		DatosConsulta datosConsulta = new DatosConsulta();
		datosConsulta.setFechaDesde("2021-05-01");
		datosConsulta.setFechaHasta(fecha(new Date()));
		acumuladoRequest.setDatosConsulta(datosConsulta);
		
		try {
			AcumuladoResponse acumuladoResponse = acumuladosServiceApiRest.consultarAcumuladosDiariosBanco(acumuladoRequest);
		} catch (CustomException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public Venta acumuladosPorAprobarVenta(String codMoneda)  {
		AcumuladoRequest acumuladoRequest = new AcumuladoRequest();
		acumuladoRequest.setIdUsuario("test");
		acumuladoRequest.setIdSesion("20210101121213");
		acumuladoRequest.setCanal("8");
		acumuladoRequest.setTipoAcumulado("3");
		DatosConsulta datosConsulta = new DatosConsulta();
		datosConsulta.setFechaDesde("2021-01-01");
		datosConsulta.setFechaHasta(fecha(new Date()));
		acumuladoRequest.setDatosConsulta(datosConsulta);
		List<Venta> listaPorAprobarVenta = new ArrayList<>();
		Venta ventaPorAprobarRes = new Venta("","", new BigDecimal(0), new BigDecimal(0));
		try {
			AcumuladoResponse acumuladoResponse = acumuladosServiceApiRest.consultarAcumuladosDiariosBanco(acumuladoRequest);
			if(acumuladoResponse.getResultado().getCodigo().equals("0000")) {
				listaPorAprobarVenta = acumuladoResponse.getAcumuladosPorAprobar().getVenta();
				for (Venta venta : listaPorAprobarVenta) {
					if(venta.getCodMoneda().equals(codMoneda)) {
						ventaPorAprobarRes = venta;
					}
				}
				
				return ventaPorAprobarRes;
			}else {
				return ventaPorAprobarRes;
			}
		} catch (CustomException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ventaPorAprobarRes;
		}
	}
	
	public Compra acumuladosPorAprobarCompra(String codMoneda)  {
		AcumuladoRequest acumuladoRequest = new AcumuladoRequest();
		acumuladoRequest.setIdUsuario("test");
		acumuladoRequest.setIdSesion("20210101121213");
		acumuladoRequest.setCanal("8");
		acumuladoRequest.setTipoAcumulado("3");
		DatosConsulta datosConsulta = new DatosConsulta();
		datosConsulta.setFechaDesde("2021-01-01");
		datosConsulta.setFechaHasta(fecha(new Date()));
		acumuladoRequest.setDatosConsulta(datosConsulta);
		List<Compra> listaPorAprobarCompra = new ArrayList<>();
		Compra compraPorAprobarRes = new Compra("","", new BigDecimal(0), new BigDecimal(0));
		try {
			AcumuladoResponse acumuladoResponse = acumuladosServiceApiRest.consultarAcumuladosDiariosBanco(acumuladoRequest);
			if(acumuladoResponse.getResultado().getCodigo().equals("0000")) {
				listaPorAprobarCompra = acumuladoResponse.getAcumuladosPorAprobar().getCompra();
				for (Compra compra : listaPorAprobarCompra) {
					if(compra.getCodMoneda().equals(codMoneda)) {
						compraPorAprobarRes = compra;
					}
				}
				
				return compraPorAprobarRes;
			}else {
				return compraPorAprobarRes;
			}
		} catch (CustomException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return compraPorAprobarRes;
		}
	}
	
	@ModelAttribute
	public void setGenericos(Model model) {
		Movimiento movimientoSearch = new Movimiento();
		model.addAttribute("movimientoSearch", movimientoSearch);
		log.info("se ejecuta setGenericos");
		//acumuladosCompraVenta();
		//ofertasPorAprobar();
		
		Venta ventaAcumuladoUSD = acumuladosVenta("USD");
		log.info("ventaAcumuladoUSD: "+ventaAcumuladoUSD);
		model.addAttribute("ventaAcumuladoUSD", ventaAcumuladoUSD);
		Venta ventaAcumuladoEUR = acumuladosVenta("EUR");
		log.info("ventaAcumuladoEUR: "+ventaAcumuladoEUR);
		model.addAttribute("ventaAcumuladoEUR", ventaAcumuladoEUR);
		BigDecimal montoBsTotalVenta =  ventaAcumuladoEUR.getMontoBs().add(ventaAcumuladoUSD.getMontoBs());
		model.addAttribute("montoBsTotalVenta", montoBsTotalVenta);
		Compra compraAcumuladoUSD = acumuladosCompra("USD");
		log.info("compraAcumuladoUSD: "+compraAcumuladoUSD);
		model.addAttribute("compraAcumuladoUSD", compraAcumuladoUSD);
		Compra compraAcumuladoEUR = acumuladosCompra("EUR");
		log.info("compraAcumuladoEUR: "+compraAcumuladoEUR);
		model.addAttribute("compraAcumuladoEUR", compraAcumuladoEUR);
		BigDecimal montoBsTotalCompra =  compraAcumuladoEUR.getMontoBs().add(compraAcumuladoUSD.getMontoBs());
		model.addAttribute("montoBsTotalCompra", montoBsTotalCompra);
		
		//ofertasPorAprobar();
		Venta ventaPorAprobarUSD = acumuladosPorAprobarVenta("USD");
		log.info("ventaPorAprobarUSD: "+ventaPorAprobarUSD);
		model.addAttribute("ventaPorAprobarUSD", ventaPorAprobarUSD);
		Venta ventaPorAprobarEUR = acumuladosPorAprobarVenta("EUR");
		log.info("ventaPorAprobarEUR: "+ventaPorAprobarEUR);
		model.addAttribute("ventaPorAprobarEUR", ventaPorAprobarEUR);
		BigDecimal montoBsTotalPorAprobarVenta =  ventaPorAprobarEUR.getMontoBs().add(ventaPorAprobarUSD.getMontoBs());
		model.addAttribute("montoBsTotalPorAprobarVenta", montoBsTotalPorAprobarVenta);
		
		Compra compraPorAprobarUSD = acumuladosPorAprobarCompra("USD");
		log.info("compraPorAprobarUSD: "+compraPorAprobarUSD);
		model.addAttribute("compraPorAprobarUSD", compraPorAprobarUSD);
		Compra compraPorAprobarEUR = acumuladosPorAprobarCompra("EUR");
		log.info("compraPorAprobarEUR: "+compraPorAprobarEUR);
		model.addAttribute("compraPorAprobarEUR", compraPorAprobarEUR);
		BigDecimal montoBsTotalPorAprobarCompra =  compraPorAprobarEUR.getMontoBs().add(compraPorAprobarUSD.getMontoBs());
		model.addAttribute("montoBsTotalPorAprobarCompra", montoBsTotalPorAprobarCompra);
		
		//ofertasPorAprobar();
	}
	
	
	@GetMapping("/searchEstatus")
	public String searchEstatus(@ModelAttribute("movimientoSearch") Movimiento movimientoSearch, 
			Model model, RedirectAttributes redirectAttributes) {
		
		log.info("searchEstatus");
		
		MovimientosRequest movimientosRequest = new MovimientosRequest();
		movimientosRequest.setIdUsuario("test");
		movimientosRequest.setIdSesion("20210101121213");
		movimientosRequest.setUsuario("E66666");
		movimientosRequest.setCanal("8");
		
		List<Movimiento> listaMovimientosVenta = new ArrayList<>();
		DatosPaginacion datosPaginacionVenta = new DatosPaginacion();
		
		List<Movimiento> listaMovimientosCompra = new ArrayList<>();
		DatosPaginacion datosPaginacionCompra = new DatosPaginacion();
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(10);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			filtrosVenta.setEstatus(movimientoSearch.getEstatus());
			movimientosRequest.setFiltros(filtrosVenta);
			MovimientosResponse responseVenta = movimientosApiRest.consultarMovimientos(movimientosRequest);
			if(responseVenta != null) {
				datosPaginacionVenta = responseVenta.getDatosPaginacion();
				log.info("datosPaginacionVenta: "+datosPaginacionVenta);
				listaMovimientosVenta = responseVenta.getMovimientos();
				log.info("listaMovimientosVenta: "+listaMovimientosVenta);
				log.info("listaMovimientosVenta.size: "+listaMovimientosVenta.size());
				model.addAttribute("listaMovimientosVenta", listaMovimientosVenta);
				model.addAttribute("datosPaginacionVenta", datosPaginacionVenta);
				
				movimientosRequest.setNumeroPagina(1);
				movimientosRequest.setTamanoPagina(10);
				Movimiento filtrosCompra = new Movimiento();
				filtrosCompra.setTipoTransaccion("C");
				movimientosRequest.setFiltros(filtrosCompra);
				MovimientosResponse responseCompra = movimientosApiRest.consultarMovimientos(movimientosRequest);
				
				if(responseCompra != null) {
					datosPaginacionCompra = responseCompra.getDatosPaginacion();
					log.info("datosPaginacionCompra: "+datosPaginacionCompra);
					listaMovimientosCompra = responseCompra.getMovimientos();
					log.info("listaMovimientosCompra: "+listaMovimientosCompra);
					log.info("listaMovimientosCompra.size: "+listaMovimientosCompra.size());
					model.addAttribute("listaMovimientosCompra", listaMovimientosCompra);
					model.addAttribute("datosPaginacionCompra", datosPaginacionCompra);
					model.addAttribute("estatus", movimientoSearch.getEstatus());
					return "convenio/solicitudes/listaSolicitudesMovimientosVentaSearchEstatus";
					
					
					
				}else {
					datosPaginacionCompra.setTotalPaginas(0);
					model.addAttribute("listaMovimientosCompra", listaMovimientosCompra);
					model.addAttribute("datosPaginacionCompra", datosPaginacionCompra);
					return "convenio/solicitudes/listaSolicitudesMovimientosVentaVentaSearchEstatus";
				}
				
			}else {
				return "redirect:/";
			}
		} catch (CustomException e) {
			log.error("error: "+e);
			return "redirect:/";
		}
		
	}
	
	@GetMapping("/listaSolicitudesMovimientosVentas/{page}/{estatus}")
	public String consultaMovimientoVentaSearchEstatus(@PathVariable("page") int page,@PathVariable("estatus") int estatus, Model model) {
		log.info("page: "+page);
		log.info("estatus: "+estatus);
		MovimientosRequest movimientosRequest = new MovimientosRequest();
		movimientosRequest.setIdUsuario("test");
		movimientosRequest.setIdSesion("20210101121213");
		movimientosRequest.setUsuario("E66666");
		movimientosRequest.setCanal("8");
		
		List<Movimiento> listaMovimientosVenta = new ArrayList<>();
		DatosPaginacion datosPaginacionVenta = new DatosPaginacion();
		
		List<Movimiento> listaMovimientosCompra = new ArrayList<>();
		DatosPaginacion datosPaginacionCompra = new DatosPaginacion();
		try {
			movimientosRequest.setNumeroPagina(page);
			movimientosRequest.setTamanoPagina(10);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			filtrosVenta.setEstatus(estatus);
			movimientosRequest.setFiltros(filtrosVenta);
			MovimientosResponse responseVenta = movimientosApiRest.consultarMovimientos(movimientosRequest);
			if(responseVenta != null) {
				datosPaginacionVenta = responseVenta.getDatosPaginacion();
				log.info("datosPaginacionVenta: "+datosPaginacionVenta);
				listaMovimientosVenta = responseVenta.getMovimientos();
				log.info("listaMovimientosVenta: "+listaMovimientosVenta);
				log.info("listaMovimientosVenta.size: "+listaMovimientosVenta.size());
				model.addAttribute("listaMovimientosVenta", listaMovimientosVenta);
				model.addAttribute("datosPaginacionVenta", datosPaginacionVenta);
				
				movimientosRequest.setNumeroPagina(1);
				movimientosRequest.setTamanoPagina(10);
				Movimiento filtrosCompra = new Movimiento();
				filtrosCompra.setTipoTransaccion("C");
				movimientosRequest.setFiltros(filtrosCompra);
				MovimientosResponse responseCompra = movimientosApiRest.consultarMovimientos(movimientosRequest);
				
				if(responseCompra != null) {
					datosPaginacionCompra = responseCompra.getDatosPaginacion();
					log.info("datosPaginacionCompra: "+datosPaginacionCompra);
					listaMovimientosCompra = responseCompra.getMovimientos();
					log.info("listaMovimientosCompra: "+listaMovimientosCompra);
					log.info("listaMovimientosCompra.size: "+listaMovimientosCompra.size());
					model.addAttribute("listaMovimientosCompra", listaMovimientosCompra);
					model.addAttribute("datosPaginacionCompra", datosPaginacionCompra);
					model.addAttribute("estatus", estatus);
					return "convenio/solicitudes/listaSolicitudesMovimientosVentaSearchEstatus";
					
					
					
				}else {
					datosPaginacionCompra.setTotalPaginas(0);
					model.addAttribute("listaMovimientosCompra", listaMovimientosCompra);
					model.addAttribute("datosPaginacionCompra", datosPaginacionCompra);
					return "convenio/solicitudes/listaSolicitudesMovimientosVentaSearchEstatus";
				}
				
			}else {
				return "redirect:/";
			}
		} catch (CustomException e) {
			log.error("error: "+e);
			return "redirect:/";
		}
	}
	
	@GetMapping("/searchEstatusCompra")
	public String searchEstatusCompra(@ModelAttribute("movimientoSearch") Movimiento movimientoSearch, 
			Model model, RedirectAttributes redirectAttributes) {
		
		log.info("searchEstatusCompra");
		
		MovimientosRequest movimientosRequest = new MovimientosRequest();
		movimientosRequest.setIdUsuario("test");
		movimientosRequest.setIdSesion("20210101121213");
		movimientosRequest.setUsuario("E66666");
		movimientosRequest.setCanal("8");
		
		List<Movimiento> listaMovimientosVenta = new ArrayList<>();
		DatosPaginacion datosPaginacionVenta = new DatosPaginacion();
		
		List<Movimiento> listaMovimientosCompra = new ArrayList<>();
		DatosPaginacion datosPaginacionCompra = new DatosPaginacion();
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(10);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			movimientosRequest.setFiltros(filtrosVenta);
			MovimientosResponse responseVenta = movimientosApiRest.consultarMovimientos(movimientosRequest);
			if(responseVenta != null) {
				datosPaginacionVenta = responseVenta.getDatosPaginacion();
				log.info("datosPaginacionVenta: "+datosPaginacionVenta);
				listaMovimientosVenta = responseVenta.getMovimientos();
				log.info("listaMovimientosVenta: "+listaMovimientosVenta);
				log.info("listaMovimientosVenta.size: "+listaMovimientosVenta.size());
				model.addAttribute("listaMovimientosVenta", listaMovimientosVenta);
				model.addAttribute("datosPaginacionVenta", datosPaginacionVenta);
				
				movimientosRequest.setNumeroPagina(1);
				movimientosRequest.setTamanoPagina(10);
				Movimiento filtrosCompra = new Movimiento();
				filtrosCompra.setTipoTransaccion("C");
				filtrosCompra.setEstatus(movimientoSearch.getEstatus());
				movimientosRequest.setFiltros(filtrosCompra);
				MovimientosResponse responseCompra = movimientosApiRest.consultarMovimientos(movimientosRequest);
				
				if(responseCompra != null) {
					datosPaginacionCompra = responseCompra.getDatosPaginacion();
					log.info("datosPaginacionCompra: "+datosPaginacionCompra);
					listaMovimientosCompra = responseCompra.getMovimientos();
					log.info("listaMovimientosCompra: "+listaMovimientosCompra);
					log.info("listaMovimientosCompra.size: "+listaMovimientosCompra.size());
					model.addAttribute("listaMovimientosCompra", listaMovimientosCompra);
					model.addAttribute("datosPaginacionCompra", datosPaginacionCompra);
					model.addAttribute("estatus", movimientoSearch.getEstatus());
					return "convenio/solicitudes/listaSolicitudesMovimientosCompraSearchEstatus";
					
					
					
				}else {
					datosPaginacionCompra.setTotalPaginas(0);
					model.addAttribute("listaMovimientosCompra", listaMovimientosCompra);
					model.addAttribute("datosPaginacionCompra", datosPaginacionCompra);
					return "convenio/solicitudes/listaSolicitudesMovimientosCompraVentaSearchEstatus";
				}
				
			}else {
				return "redirect:/";
			}
		} catch (CustomException e) {
			log.error("error: "+e);
			return "redirect:/";
		}
		
	}
	
	@GetMapping("/listaSolicitudesMovimientosCompras/{page}/{estatus}")
	public String consultaMovimientoCompraSearchEstatus(@PathVariable("page") int page,@PathVariable("estatus") int estatus, Model model) {
		log.info("page: "+page);
		log.info("estatus: "+estatus);
		MovimientosRequest movimientosRequest = new MovimientosRequest();
		movimientosRequest.setIdUsuario("test");
		movimientosRequest.setIdSesion("20210101121213");
		movimientosRequest.setUsuario("E66666");
		movimientosRequest.setCanal("8");
		
		List<Movimiento> listaMovimientosVenta = new ArrayList<>();
		DatosPaginacion datosPaginacionVenta = new DatosPaginacion();
		
		List<Movimiento> listaMovimientosCompra = new ArrayList<>();
		DatosPaginacion datosPaginacionCompra = new DatosPaginacion();
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(10);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			movimientosRequest.setFiltros(filtrosVenta);
			MovimientosResponse responseVenta = movimientosApiRest.consultarMovimientos(movimientosRequest);
			if(responseVenta != null) {
				datosPaginacionVenta = responseVenta.getDatosPaginacion();
				log.info("datosPaginacionVenta: "+datosPaginacionVenta);
				listaMovimientosVenta = responseVenta.getMovimientos();
				log.info("listaMovimientosVenta: "+listaMovimientosVenta);
				log.info("listaMovimientosVenta.size: "+listaMovimientosVenta.size());
				model.addAttribute("listaMovimientosVenta", listaMovimientosVenta);
				model.addAttribute("datosPaginacionVenta", datosPaginacionVenta);
				
				movimientosRequest.setNumeroPagina(page);
				movimientosRequest.setTamanoPagina(10);
				Movimiento filtrosCompra = new Movimiento();
				filtrosCompra.setTipoTransaccion("C");
				filtrosCompra.setEstatus(estatus);
				movimientosRequest.setFiltros(filtrosCompra);
				MovimientosResponse responseCompra = movimientosApiRest.consultarMovimientos(movimientosRequest);
				
				if(responseCompra != null) {
					datosPaginacionCompra = responseCompra.getDatosPaginacion();
					log.info("datosPaginacionCompra: "+datosPaginacionCompra);
					listaMovimientosCompra = responseCompra.getMovimientos();
					log.info("listaMovimientosCompra: "+listaMovimientosCompra);
					log.info("listaMovimientosCompra.size: "+listaMovimientosCompra.size());
					model.addAttribute("listaMovimientosCompra", listaMovimientosCompra);
					model.addAttribute("datosPaginacionCompra", datosPaginacionCompra);
					model.addAttribute("estatus", estatus);
					return "convenio/solicitudes/listaSolicitudesMovimientosCompraSearchEstatus";
					
					
					
				}else {
					datosPaginacionCompra.setTotalPaginas(0);
					model.addAttribute("listaMovimientosCompra", listaMovimientosCompra);
					model.addAttribute("datosPaginacionCompra", datosPaginacionCompra);
					return "convenio/solicitudes/listaSolicitudesMovimientosCompraSearchEstatus";
				}
				
			}else {
				return "redirect:/";
			}
		} catch (CustomException e) {
			log.error("error: "+e);
			return "redirect:/";
		}
	}
	
	public String fecha(Date fecha) {
		
		//SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(fecha); 
        String strDateFormat = "yyyy-MM-dd"; // El formato de fecha está especificado  
        SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat); // La cadena de formato de fecha se pasa como un argumento al objeto 
        System.out.println(objSDF.format(fecha)); // El
        
        return objSDF.format(fecha);
	}
	
	
	public boolean isFechaValida(String fechaLiquidacion) {
		
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		//SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		
        try {
        	
        	
        	Date fechaDate1 = formato.parse(fechaLiquidacion);
        	Date fechaDate2 = formato.parse(fecha(new Date()));
        	
        	if ( fechaDate1.before(fechaDate2) ){
        	    log.info("La fechaLiquidacion es menor que la actual");
        		return false;
        	}else{
        	     if ( fechaDate2.before(fechaDate1) ){
        	    	 //resultado= "La Fecha 1 es Mayor ";
        	    	 log.info("La fechaActual es menor que la fechaLiquidacion");
        	    	 return true;
        	     }else{
        	    	 log.info("La fechaActual es igual que la fechaLiquidacion");
        	    	 return true;
        	     } 
        	}
        } 
        catch (ParseException ex) 
        {
            System.out.println(ex);
        }
        
        return false;
	}
	
	@GetMapping("/listaSolicitudesMovimientosVentas")
	public String consultaMovimientoVenta1(Model model) {
		MovimientosRequest movimientosRequest = new MovimientosRequest();
		movimientosRequest.setIdUsuario("test");
		movimientosRequest.setIdSesion("20210101121213");
		movimientosRequest.setUsuario("E66666");
		movimientosRequest.setCanal("8");
		movimientosRequest.setNumeroPagina(1);
		movimientosRequest.setTamanoPagina(5);
		Movimiento filtros = new Movimiento();
		//filtros.setEstatus(0);
		filtros.setTipoTransaccion("V");
		movimientosRequest.setFiltros(filtros);
		
		try {
			MovimientosResponse response = movimientosApiRest.consultarMovimientos(movimientosRequest);
			if(response != null) {
				DatosPaginacion datosPaginacion = response.getDatosPaginacion();
				log.info("datosPaginacion: "+datosPaginacion);
				List<Movimiento> listaMovimientos = response.getMovimientos();
				log.info("listaMovimientos: "+listaMovimientos);
				log.info("listaMovimientos.size: "+listaMovimientos.size());
				
				
				//List<Solicitud> listaSolicitud = solicitudService.bucarPorAprobar();
				
				model.addAttribute("listaMovimientos", listaMovimientos);
				model.addAttribute("datosPaginacion", datosPaginacion);
				return "convenio/solicitudes/listaSolicitudesMovimientosVenta";
			}else {
				return "redirect:/";
			}
		} catch (CustomException e) {
			log.error("error: "+e);
			return "redirect:/";
		}
	}
	
	///borrar
	
	

}
