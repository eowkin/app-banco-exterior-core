package com.bancoexterior.app.util;

import java.time.LocalDateTime;


import org.springframework.stereotype.Component;



import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LibreriaUtil {
	
	public String obtenerIdSesion() {
		LocalDateTime ahora = LocalDateTime.now();
		log.info("año: "+ahora.getYear());
		log.info("mes: "+ahora.getMonthValue());
		log.info("dia: "+ahora.getDayOfMonth());
		log.info("dia: "+ahora.getHour());
		log.info("min: "+ahora.getMinute());
		log.info("seg: "+ahora.getSecond());
		String valorAno = "";
		valorAno = ahora.getYear()+"";
		log.info("valorAno: "+valorAno);
		
		String valorMes = "";
		if(ahora.getMonthValue() < 10) {
			valorMes = "0"+ahora.getMonthValue();
		}else {
			valorMes = ""+ahora.getMonthValue();
		}
		log.info("valorMes: "+valorMes);
		
		String valorDia = "";
		if(ahora.getDayOfMonth() < 10) {
			valorDia = "0"+ahora.getDayOfMonth();
		}else {
			valorDia = ""+ahora.getDayOfMonth();
		}
		log.info("valorDia: "+valorDia);
		
		String valorHora = "";
		if(ahora.getHour() < 10) {
			valorHora = "0"+ahora.getHour();
		}else {
			valorHora = ""+ahora.getHour();
		}
		log.info("valorHora: "+valorHora);
		
		String valorMin = "";
		if(ahora.getMinute() < 10) {
			valorMin = "0"+ahora.getMinute();
		}else {
			valorMin = ""+ahora.getMinute();
		}
		log.info("valorMin: "+valorMin);
		
		String valorSeg = "";
		if(ahora.getSecond() < 10) {
			valorSeg = "0"+ahora.getSecond();
		}else {
			valorSeg = ""+ahora.getSecond();
		}
		log.info("valorSeg: "+valorSeg);
		
	
		return valorAno+valorMes+valorDia+valorHora+valorMin+valorSeg;
	}

}
