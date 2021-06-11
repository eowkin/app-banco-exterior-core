package com.bancoexterior.app.convenio.controller;



import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
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

import com.bancoexterior.app.convenio.apiRest.IMonedaServiceApiRest;

import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.Moneda;
import com.bancoexterior.app.util.LibreriaUtil;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/monedas")
public class MonedaController {

	@Autowired
	private IMonedaServiceApiRest monedaServiceApiRest;
	
	
	@Autowired
	private LibreriaUtil libreriaUtil; 
	
	@Value("${des.canal}")
    private String canal;	

	
	@GetMapping("/index")
	public String indexWs(Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a index listaMonedasWs");
		
		
		
		MonedasRequest monedasRequest = getMonedasRequest();
		Moneda moneda = new Moneda();
		//moneda.setCodMoneda("EUR1");
		monedasRequest.setMoneda(moneda);
		List<Moneda> listMonedas = new ArrayList<>();
		try {
			listMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
			
			for (Moneda moneda2 : listMonedas) {
				//log.info(moneda2.getFechaModificacion());
				if(moneda2.getFechaModificacion() != null) {
					String[] arrOfStr = moneda2.getFechaModificacion().split(" ", 2);
					moneda2.setFechaModificacion(arrOfStr[0]);
				}
			}
			
			model.addAttribute("listMonedas", listMonedas);
	    	return "convenio/moneda/listaMonedas";
		} catch (CustomException e) {
			log.error("error: "+e);
			model.addAttribute("mensajeError", e.getMessage());
			model.addAttribute("listMonedas", listMonedas);
			return "convenio/moneda/listaMonedas";
			//return "redirect:/";
		}
		
		 
	}
	
	@GetMapping("/activar/{codMoneda}")
	public String activarWs(@PathVariable("codMoneda") String codMoneda, Moneda moneda, Model model, RedirectAttributes redirectAttributes) {
		log.info("activarWs");
		log.info(codMoneda);
		Moneda monedaEdit = new Moneda();
		MonedasRequest monedasRequest = getMonedasRequest();
		Moneda monedaBuscar = new Moneda();
		monedaBuscar.setCodMoneda(codMoneda);
		monedasRequest.setMoneda(monedaBuscar);
		
		try {
			
			monedaEdit = monedaServiceApiRest.buscarMoneda(monedasRequest);
			monedaEdit.setFlagActivo(true);
			monedasRequest.setMoneda(monedaEdit);
			String respuesta = monedaServiceApiRest.actualizar(monedasRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/monedas/index";
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/monedas/index";
		}
	}	
	
	@GetMapping("/desactivar/{codMoneda}")
	public String desactivarWs(@PathVariable("codMoneda") String codMoneda, Moneda moneda, Model model, RedirectAttributes redirectAttributes) {
		log.info("desactivarWs");
		log.info(codMoneda);
		Moneda monedaEdit = new Moneda();
		MonedasRequest monedasRequest = getMonedasRequest();
		Moneda monedaBuscar = new Moneda();
		monedaBuscar.setCodMoneda(codMoneda);
		monedasRequest.setMoneda(monedaBuscar);
		
		try {
			
			monedaEdit = monedaServiceApiRest.buscarMoneda(monedasRequest);
			monedaEdit.setFlagActivo(false);
			monedasRequest.setMoneda(monedaEdit);
			String respuesta = monedaServiceApiRest.actualizar(monedasRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/monedas/index";
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/monedas/index";
		}
	}
	
	@GetMapping("/edit/{codMoneda}")
	public String editarWs(@PathVariable("codMoneda") String codMoneda, Moneda moneda, Model model, RedirectAttributes redirectAttributes) {
		log.info("editarWs");
		log.info(codMoneda);
		Moneda monedaEdit = new Moneda();
		MonedasRequest monedasRequest = getMonedasRequest();
		Moneda monedaBuscar = new Moneda();
		monedaBuscar.setCodMoneda(codMoneda);
		monedasRequest.setMoneda(monedaBuscar);
		try {
			monedaEdit = monedaServiceApiRest.buscarMoneda(monedasRequest);
			if(monedaEdit != null) {
				log.info("monedaEdit: "+monedaEdit);
				model.addAttribute("moneda", monedaEdit);
        		return "convenio/moneda/formMonedaEdit";
			}else {
				redirectAttributes.addFlashAttribute("mensajeError", "Operacion Exitosa.La consulta no arrojo resultado.");
        		return "redirect:/monedas/index";
			}
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/monedas/index";
		}
	
	}	
	
	@PostMapping("/guardar")
	public String guardarWs(Moneda moneda, BindingResult result,  RedirectAttributes redirectAttributes) {
		
		log.info("guardar");
		log.info("moneda: "+moneda);
			
		MonedasRequest monedasRequest = getMonedasRequest();
		moneda.setCodUsuario("E66666");
		monedasRequest.setMoneda(moneda);
		
		
		try {
			String respuesta = monedaServiceApiRest.actualizar(monedasRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/monedas/index";
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "convenio/moneda/formMonedaEdit";
		}
		
		
		
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
		
		MonedasRequest monedasRequest = getMonedasRequest();
		moneda.setFlagActivo(true);
		monedasRequest.setMoneda(moneda);
		
		
		try {
			String respuesta = monedaServiceApiRest.crear(monedasRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/monedas/index";
		} catch (CustomException e) {
			log.error("error: "+e);
			//redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			result.addError(new ObjectError("codMoneda", e.getMessage()));
			return "convenio/moneda/formMoneda";
		}
		
	}
	
	@GetMapping("/formMoneda")
	public String formMoneda(Moneda moneda, Model model) {
		
		return "convenio/moneda/formMoneda";
	}	
	
	
	
	@GetMapping("/searchCodigo")
	public String searchCodigo(@ModelAttribute("monedaSearch") Moneda monedaSearch,
			Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a searchCodigo");
		
		
		
		MonedasRequest monedasRequest = getMonedasRequest();
		Moneda moneda = new Moneda();
		if(!monedaSearch.getCodMoneda().equals("")) {
			moneda.setCodMoneda(monedaSearch.getCodMoneda().toUpperCase());
		}
		monedasRequest.setMoneda(moneda);
		List<Moneda> listMonedas = new ArrayList<>();
		try {
			listMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
			log.info("listMonedas.isEmpty(): "+listMonedas.isEmpty());
			
			if(!listMonedas.isEmpty()) {
				for (Moneda moneda2 : listMonedas) {
					//log.info(moneda2.getFechaModificacion());
					if(moneda2.getFechaModificacion() != null) {
						String[] arrOfStr = moneda2.getFechaModificacion().split(" ", 2);
						moneda2.setFechaModificacion(arrOfStr[0]);
					}
				}
				
				model.addAttribute("listMonedas", listMonedas);
		    	return "convenio/moneda/listaMonedas";
			}else {
				
				model.addAttribute("listMonedas", listMonedas);
				model.addAttribute("mensajeError", "Operacion Exitosa.La consulta no arrojo resultado.");
		    	return "convenio/moneda/listaMonedas";
			}
			
			
		} catch (CustomException e) {
			log.error("error: "+e);
			model.addAttribute("mensajeError", e.getMessage());
			model.addAttribute("listMonedas", listMonedas);
			return "convenio/moneda/listaMonedas";
			//return "redirect:/";
		}
		
	}
	
	
	public MonedasRequest getMonedasRequest() {
		MonedasRequest monedasRequest = new MonedasRequest();
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		monedasRequest.setIdUsuario(userName);
		monedasRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		monedasRequest.setCodUsuario(userName);
		monedasRequest.setCanal(canal);
		return monedasRequest;
	}
	
	
	
	
	@ModelAttribute
	public void setGenericos(Model model, HttpServletRequest request) {
		Moneda monedaSearch = new Moneda();
		model.addAttribute("monedaSearch", monedaSearch);
		String uri = request.getRequestURI();
		log.info("uri: "+uri);
		String[] arrUri = uri.split("/");

		arrUri[0] = "Home";
		for (String string : arrUri) {
			log.info("string: "+string);
		}
		model.addAttribute("arrUri", arrUri);
		
		
		
	}
	
	
	/*
	 * @InitBinder public void initBinder(WebDataBinder webDataBinder) {
	 * SimpleDateFormat dataFormat = new SimpleDateFormat("dd-MM-yyyy");
	 * webDataBinder.registerCustomEditor(Date.class, new
	 * CustomDateEditor(dataFormat, false)); }
	 */
	

	
}
