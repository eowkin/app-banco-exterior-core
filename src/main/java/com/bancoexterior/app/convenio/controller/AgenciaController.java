package com.bancoexterior.app.convenio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bancoexterior.app.convenio.model.Agencia;
import com.bancoexterior.app.convenio.services.IAgenciaService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/agencias")
public class AgenciaController {

	
	@Autowired
	private IAgenciaService agenciaService;
	
	@GetMapping("/index")
	public String index(Model model) {
		List<Agencia> listaAgencias = agenciaService.buscarTodas();
		for (Agencia agencia : listaAgencias) {
			log.info("agencia: "+agencia);
		}
		model.addAttribute("listaAgencias", listaAgencias);
		return "convenio/agencia/listaAgencias";
	}
	
	@GetMapping("/formAgencia")
	public String fromAgencia(Agencia agencia,  Model model) {
		
		return "convenio/agencia/formAgencia";
	}
}
