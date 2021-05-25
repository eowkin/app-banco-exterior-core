package com.bancoexterior.app.convenio.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bancoexterior.app.convenio.apiRest.IAgenciaServiceApiRest;
import com.bancoexterior.app.convenio.dto.AgenciaRequest;
import com.bancoexterior.app.convenio.dto.AgenciaResponse;
import com.bancoexterior.app.convenio.dto.ClienteResponse;
import com.bancoexterior.app.convenio.model.Agencia;
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
		}
		
		return "convenio/agencia/listaAgencias";
	}
	
	@GetMapping("/formAgencia")
	public String fromAgencia(Agencia agencia,  Model model) {
		
		return "convenio/agencia/formAgencia";
	}
}
