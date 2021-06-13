package com.bancoexterior.app.convenio.controller;



import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.bancoexterior.app.convenio.apiRest.ITasaServiceApiRest;
import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.dto.TasaRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.Moneda;
import com.bancoexterior.app.convenio.model.Tasa;
import com.bancoexterior.app.util.LibreriaUtil;

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
	private LibreriaUtil libreriaUtil; 
	
	@Value("${des.canal}")
    private String canal;	
	
	@GetMapping("/index")
	public String index(Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a index listaTasasWs");
		
		List<Tasa> listaTasas = new ArrayList<>();
		
		TasaRequest tasaRequest = getTasaRequest();
		Tasa tasa = new Tasa();
		tasaRequest.setTasa(tasa);
	
		try {
			listaTasas = tasaServiceApiRest.listaTasas(tasaRequest);
			for (Tasa tasa2 : listaTasas) {
				if(tasa2.getFechaModificacion() != null) {
					String[] arrOfStr = tasa2.getFechaModificacion().split(" ", 2);
					tasa2.setFechaModificacion(arrOfStr[0]);
				}
			}
			model.addAttribute("listaTasas", listaTasas);
			return "convenio/tasa/listaTasas";
		} catch (CustomException e) {
			
			log.error("error: "+e);
			model.addAttribute("mensajeError", e.getMessage());
			model.addAttribute("listaTasas", listaTasas);
			return "convenio/tasa/listaTasas";
			
		}
		
		
		
	}	
	
	@GetMapping("/edit/{codMonedaOrigen}/{codMonedaDestino}/{tipoOperacion}")
	public String editarWs(@PathVariable("codMonedaOrigen") String codMonedaOrigen, 
			@PathVariable("codMonedaDestino") String codMonedaDestino, @PathVariable("tipoOperacion") Integer tipoOperacion,
			Tasa tasa, Model model, RedirectAttributes redirectAttributes) {
		log.info("editarWs");
		log.info("codMonedaOrigen: "+codMonedaOrigen);
		log.info("codMonedaDestino: "+codMonedaDestino);
		log.info("tipoOperacion: "+tipoOperacion);
		
		
		Tasa tasaEdit = new Tasa();
		
		
		
		TasaRequest tasaRequest = getTasaRequest();
		Tasa tasaBuscar = new Tasa();
		tasaBuscar.setCodMonedaOrigen(codMonedaOrigen);
		tasaBuscar.setCodMonedaDestino(codMonedaDestino);
		tasaBuscar.setTipoOperacion(tipoOperacion);
		tasaRequest.setTasa(tasaBuscar);
		
		try {
			tasaEdit = tasaServiceApiRest.buscarTasa(tasaRequest);
			if(tasaEdit != null) {
				model.addAttribute("tasa", tasaEdit);
            	return "convenio/tasa/formTasaEdit";
			}else {
				redirectAttributes.addFlashAttribute("mensajeError", " Codigo : 0001 descripcion: Operacion Exitosa.La consulta no arrojo resultado.");
				return "redirect:/tasas/index";
			}
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/tasas/index";
		}
		
		
		
	}
	
	
	@PostMapping("/guardar")
	public String guardarWs(Tasa tasa, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		log.info("guardarWs");
		log.info("tasa: "+tasa);
		List<String> listaError = new ArrayList<>();
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				log.info("Ocurrio un error: " + error.getDefaultMessage());
				if(error.getCode().equals("typeMismatch")) {
					listaError.add("Los valores de los montos debe ser numerico");
				}
			}
			model.addAttribute("listaError", listaError);
			return "convenio/tasa/formTasaEdit";
		}
		
		TasaRequest tasaRequest = getTasaRequest();
		Tasa tasaEdit = new Tasa();
		tasaEdit.setCodMonedaOrigen(tasa.getCodMonedaOrigen());
		tasaEdit.setCodMonedaDestino(tasa.getCodMonedaDestino());
		tasaEdit.setTipoOperacion(tasa.getTipoOperacion());
		tasaEdit.setMontoTasaCompra(tasa.getMontoTasaCompra());
		tasaEdit.setMontoTasaVenta(tasa.getMontoTasaVenta());
		tasaRequest.setTasa(tasaEdit);
		
		try {
			
			String respuesta = tasaServiceApiRest.actualizar(tasaRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/tasas/index";
		} catch (CustomException e) {
			log.error("error: "+e);
			result.addError(new ObjectError("codMoneda", " Codigo :" +e.getMessage()));
			listaError.add(e.getMessage());
			model.addAttribute("listaError", listaError);
			return "convenio/tasa/formTasaEdit";
		}
		
		
		
	}
	
	@GetMapping("/formTasa")
	public String formTasa(Tasa tasa, Model model, RedirectAttributes redirectAttributes) {
		
		List<Moneda> listaMonedas = new ArrayList<>();
		
		MonedasRequest monedasRequest = getMonedasRequest();
		Moneda moneda = new Moneda();
		moneda.setFlagActivo(true);
		monedasRequest.setMoneda(moneda);
		
		try {
			listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
			model.addAttribute("listaMonedas", listaMonedas);
			return "convenio/tasa/formTasa";
		} catch (CustomException e) {
			log.error("error: "+e);
			redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
			return "redirect:/tasas/index";
		}
		
		
		
	}	
	
	@PostMapping("/save")
	public String saveWs(Tasa tasa, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		log.info("save");
		log.info("tasa: "+tasa);
		List<String> listaError = new ArrayList<>();
		List<Moneda> listaMonedas = new ArrayList<>();
		MonedasRequest monedasRequest = getMonedasRequest();
		Moneda moneda = new Moneda();
		moneda.setFlagActivo(true);
		monedasRequest.setMoneda(moneda);
		
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				log.info("Ocurrio un error: " + error.getDefaultMessage());
				if(error.getCode().equals("typeMismatch")) {
					listaError.add("Los valores de los montos debe ser numerico");
				}
			}
			try {
				listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
				model.addAttribute("listaMonedas", listaMonedas);
				model.addAttribute("listaError", listaError);
				return "convenio/tasa/formTasa";
			} catch (CustomException e) {
				log.error("error: "+e);
				result.addError(new ObjectError("codMoneda", " Codigo :" +e.getMessage()));
				model.addAttribute("listaError", e.getMessage());
				return "convenio/tasa/formTasa";
			}
		}
		
		TasaRequest tasaRequest = getTasaRequest();
		tasaRequest.setTasa(tasa);
		
		try {
			String respuesta = tasaServiceApiRest.crear(tasaRequest);
			redirectAttributes.addFlashAttribute("mensaje", respuesta);
			return "redirect:/tasas/index";
		} catch (CustomException e) {
			log.error("error: "+e);
			try {
				listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
				model.addAttribute("listaMonedas", listaMonedas);
				result.addError(new ObjectError("codMoneda", " Codigo :" +e.getMessage()));
				listaError.add(e.getMessage());
				model.addAttribute("listaError", listaError);
				return "convenio/tasa/formTasa";
			} catch (CustomException e1) {
				log.error("error: "+e1);
				result.addError(new ObjectError("codMoneda", " Codigo :" +e1.getMessage()));
				listaError.add(e1.getMessage());
				model.addAttribute("listaError", listaError);
				return "convenio/tasa/formTasa";
			}
			
		}
		
		
	}	
	
	public TasaRequest getTasaRequest() {
		TasaRequest tasaRequest = new TasaRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		tasaRequest.setIdUsuario(userName);
		tasaRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		tasaRequest.setCodUsuario(userName);
		tasaRequest.setCanal(canal);
		return tasaRequest;
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
		String uri = request.getRequestURI();
		log.info("uri: "+uri);
		String[] arrUri = uri.split("/");

		arrUri[0] = "Home";
		model.addAttribute("arrUri", arrUri);
	}
	
	
	
	
}
