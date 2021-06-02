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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bancoexterior.app.convenio.apiRest.IMovimientosApiRest;
import com.bancoexterior.app.convenio.dto.AprobarRechazarRequest;
import com.bancoexterior.app.convenio.dto.MovimientosRequest;
import com.bancoexterior.app.convenio.dto.MovimientosResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.DatosPaginacion;
import com.bancoexterior.app.convenio.model.Movimiento;
import com.bancoexterior.app.convenio.model.Solicitud;
import com.bancoexterior.app.util.UserExcelExporter;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/solicitudes")
public class SolicitudController {
		
	@Autowired
	private IMovimientosApiRest movimientosApiRest;
	
	
	
	
	
	
	
	
	
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
	
	@GetMapping("/procesarCompra/{codOperacion}")
	public String procesarCompra(@PathVariable("codOperacion") String codOperacion, @PathVariable("tasa") BigDecimal tasa, 
			@PathVariable("page") int page, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request ) {
		log.info("procesarCompra");
		log.info("codOperacion: "+codOperacion);
		
		MovimientosRequest movimientosRequest = new MovimientosRequest();
		movimientosRequest.setIdUsuario("test");
		movimientosRequest.setIdSesion("20210101121213");
		movimientosRequest.setUsuario("E66666");
		movimientosRequest.setCanal("8");
		
		
		try {
			movimientosRequest.setNumeroPagina(page);
			movimientosRequest.setTamanoPagina(5);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			filtrosVenta.setEstatus(0);
			movimientosRequest.setFiltros(filtrosVenta);
			MovimientosResponse responseVenta = movimientosApiRest.consultarMovimientosPorAprobar(movimientosRequest);
			return "redirect:/solicitudes/listaSolicitudesMovimientosPorAprobarCompra/"+page;
			
			
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError",e.getMessage());
			return "redirect:/solicitudes/listaSolicitudesMovimientosPorAprobarCompra/"+page;
			
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
	
	
	@ModelAttribute
	public void setGenericos(Model model) {
		Movimiento movimientoSearch = new Movimiento();
		model.addAttribute("movimientoSearch", movimientoSearch);
		
	}
	
	public String fecha(Date fecha) {
		
		//SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(fecha); 
        String strDateFormat = "yyyy-MM-dd"; // El formato de fecha está especificado  
        SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat); // La cadena de formato de fecha se pasa como un argumento al objeto 
        System.out.println(objSDF.format(fecha)); // El
        
        return objSDF.format(fecha);
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
