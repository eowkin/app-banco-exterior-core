package com.bancoexterior.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bancoexterior.app.model.Solicitud;
import com.bancoexterior.app.services.ISolicitudService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/solicitudes")
public class SolicitudController {
	
	@Autowired
	private ISolicitudService solicitudService;
	
	
	@GetMapping("/listaSolicitudesPorAprobar")
	public String porAprobar(Model model) {
		
		List<Solicitud> listaSolicitud = solicitudService.bucarPorAprobar();
		model.addAttribute("listaSolicitud", listaSolicitud);
		return "convenio/solicitudes/listaSolicitudesPorAprobar";
	}
	
	@GetMapping("/listaSolicitudesMovimientos")
	public String porMovimiestos(Model model) {
		
		List<Solicitud> listaSolicitud = solicitudService.buscarTodas();
		model.addAttribute("listaSolicitud", listaSolicitud);
		return "convenio/solicitudes/listaSolicitudesMovimientos";
	}

}
