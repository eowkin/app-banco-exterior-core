package com.bancoexterior.app.convenio.repository.restApi;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.security.Key;

import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.model.Moneda;
import com.bancoexterior.app.convenio.services.restApi.IWSService;
import com.bancoexterior.app.convenio.services.restApi.model.WSRequest;
import com.bancoexterior.app.convenio.services.restApi.model.WSResponse;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;





import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm; 
import javax.xml.bind.DatatypeConverter;



@Slf4j
@Repository
public class MonedaRepositoryRestImpl  implements IMonedaRepositoryRest{

	@Autowired
	private IWSService wsService;
	
	
	@Override
	public List<Moneda> bucarMonedasRestApi(MonedasRequest monedasRequest){
		WSRequest wsrequest = new WSRequest();
		String print;
		WSResponse retorno;
		String monedasRequestJSON;
		
		String secret = "VuL4HVoKOVvhNZhzHUkaOTZKbLB9Kwh7";
		String iss  =  "GBpXZjE9BCjQlh9S0umaYgFfz0TW98nI";
		
		//Utils.createJWT(null, env.getProperty("microservicio.jwt.key"), null, new Date(System.currentTimeMillis()+Long.parseLong(env.getProperty("microservicio.jwt.time"))),env.getProperty("microservicio.jwt.secret"),new Date(System.currentTimeMillis())))
		String bearer2 = this.createJWT(null, iss, null, new Date(System.currentTimeMillis()+ 900000), secret, new Date(System.currentTimeMillis()));
		
		
		
		//String bearer = this.generarBearer(secret, iss);
		try {
			
			log.info("bearer: "+bearer2);
			
			//log.info("bearer: "+bearer);
			
			
			monedasRequestJSON = new Gson().toJson(monedasRequest);
			log.info("monedasRequestJSON: "+monedasRequestJSON);
		
			
			
			
			wsrequest.setBody(monedasRequestJSON);
			wsrequest.setConnectTimeout(10000);
			wsrequest.setContenType("application/json");
			wsrequest.setSocketTimeout(10000);
			
			//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
			//https://172.19.148.51:8443/api/des/V1/parametros/monedas/
			//wsrequest.setUrl("http://172.19.148.48:7108/api/des/V1/parametros/monedas/consultas");
			wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/monedas/");
			
			//retorno: WSResponse [statusText=, status=200, body={"resultado":{"codigo":"0000","descripcion":"Operacion Exitosa."},"monedas":[{"codMoneda":"EUR","descripcion":"EURO Europa","codAlterno":"222","flagActivo":true,"codUsuario":"E33333","fechaModificacion":"2021-05-07 21:24:07"}]}, exitoso=true, httpRetorno=kong.unirest.StringResponse@7451891e, httpError=null, error=null, idConstructor=1]
			log.info("antes de llamarte");
			retorno = wsService.post(wsrequest);
			log.info("retorno: "+retorno);
			log.info("retorno.getBody(): "+retorno.getBody());
			log.info("retorno.getIdConstructor(): "+retorno.getIdConstructor());
			log.info("retorno.getStatus(): "+retorno.getStatus());
			log.info("retorno.getStatusText(): "+retorno.getStatusText());
			log.info("retorno.getError(): "+retorno.getError());
			log.info("retorno.getHttpError(): "+retorno.getHttpError());
			log.info("retorno.getHttpRetorno(): "+retorno.getHttpRetorno());
			
			print = retorno.getBody();
			
			if (retorno.getStatus()==200) {
				log.info(print);
			}else {
				log.error("ERROR:{}",print);
			}
		} catch (Exception e) {
			log.error("bucarMonedasRestApi ERROR:{}",e);
			
			
		}
		return null;
	}
	
	
	public String generarBearer(String secret, String iss) {
		SignatureAlgorithm algoritmoHS256 = SignatureAlgorithm.HS256;
		byte[] secretBytes = DatatypeConverter.parseBase64Binary(secret);
		Key key = new SecretKeySpec(secretBytes, algoritmoHS256.getJcaName());
		Instant timeIni = Instant.now();
		Instant timeFin = timeIni.plusSeconds(3600 * 24);
		String s = new String();
		try {
			s = Jwts.builder().setHeaderParam("alg", "HS256").setHeaderParam("typ", "JWT").setIssuer(iss)
					.setExpiration((Date.from(Instant.ofEpochSecond(timeFin.getEpochSecond())))).claim("iss", iss)
					// .signWith(SignatureAlgorithm.HS256, secret.getBytes())
					.signWith(algoritmoHS256, key).compact();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
		
	
	//Utils.createJWT(null, env.getProperty("microservicio.jwt.key"), null, new Date(System.currentTimeMillis()+Long.parseLong(env.getProperty("microservicio.jwt.time"))),env.getProperty("microservicio.jwt.secret"),new Date(System.currentTimeMillis())))
	
	public static String createJWT(String id, String issuer, String subject, Date exp ,String secret, Date issuedat) {
		log.info("createJWT");   
		log.info(issuer);
		log.info(subject);
		log.info("exp: "+exp);
		log.info(secret);
		log.info("issuedat: "+issuedat);
		
		
        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
   
   
        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
   
        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                                    .setId(id)
                                    .setIssuedAt(issuedat)
                                    .setSubject(subject)
                                    .setIssuer(issuer)
                                    .setExpiration(exp)
                                    .signWith(signatureAlgorithm, signingKey);
           
   
        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

}
