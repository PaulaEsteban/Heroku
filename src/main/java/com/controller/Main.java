package com.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.parser.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.analizador.Analizador;
import com.analizador.Rule;
import com.google.gson.Gson;

@Controller
@SpringBootApplication
public class Main {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Main.class, args);
	}

	@RequestMapping("/")
	String index() {
		return "index";
	}
	@Scope("request")
	@RequestMapping("/analyzer/checkRules/{id_rule}")
	@ResponseBody
	public ResponseEntity<String> comprobarReglaConcreta(HttpServletResponse response,@PathVariable("id_rule") Integer id,
			@RequestParam(name="texto", required=true) String texto) {
		Analizador analyzer=new Analizador(texto);
		if(id==1){
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json;charset=UTF-8");
			try {
				return new ResponseEntity<>(analyzer.reglaPasiva(texto), headers, HttpStatus.OK);
			} catch (IOException | ParseException e) {
				return new ResponseEntity<>("Se ha producido un error en el proceso de validación", HttpStatus.INTERNAL_SERVER_ERROR);
			}	
		}else if(id==2){
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "application/json;charset=UTF-8");
			try {
				return new ResponseEntity<>(analyzer.reglaSinSujeto(texto), headers, HttpStatus.OK);
			} catch (IOException | ParseException e) {
				return new ResponseEntity<>("Se ha producido un error en el proceso de validación", HttpStatus.INTERNAL_SERVER_ERROR);
			}	
		}else{
			String regla="No se ha encontrado la regla que solicitaba";
			return new ResponseEntity<>(regla, HttpStatus.NOT_FOUND);
		}
	}
	@Scope("request")
	@RequestMapping("/analyzer/checkRules")
	@ResponseBody
	public ResponseEntity<String> comprobarLecturaFacil(HttpServletResponse response,
			@RequestParam(name="texto", required=true) String texto) {
		Analizador analyzer=new Analizador(texto);
		String pasiva="";
		try {
			pasiva = analyzer.reglaPasiva(texto);
		} catch (IOException | ParseException e) {
			return new ResponseEntity<>("Se ha producido un error en el proceso de validación", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		String sinSujeto="";
		try {
			sinSujeto = analyzer.reglaSinSujeto(texto);
		} catch (IOException | ParseException e) {
			return new ResponseEntity<>("Se ha producido un error en el proceso de validación", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		String listaJSON= "["+pasiva+","+sinSujeto+"]";
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json;charset=UTF-8");

		return new ResponseEntity<>(listaJSON, headers, HttpStatus.OK);
		
	}
	@Scope("request")
	@RequestMapping(method = RequestMethod.GET, value = "/analyzer/rules/{id_rule}")
	@ResponseBody
	public ResponseEntity<String> reglaConcreta(HttpServletResponse response,@PathVariable("id_rule") Integer id) throws IOException {
		String regla="";
		Gson gson = new Gson();
		if(id==1){
			Rule pasiva= new Rule();
			pasiva.setId(1);
			pasiva.setName("Regla - Forma Pasiva");
			pasiva.setDescription("No se permite el uso de la forma pasiva");
			pasiva.setReason(null);
			regla = gson.toJson(pasiva);
		}else if(id==2){
			Rule sinSujeto= new Rule();
			sinSujeto.setId(2);
			sinSujeto.setName("Regla - Sujeto en la oraci\u00f3n");
			sinSujeto.setDescription("Las oraciones deben tener sujeto");
			sinSujeto.setReason(null);
			regla = gson.toJson(sinSujeto);
		}else{
			regla="No se ha encontrado la regla que solicitaba";
			return new ResponseEntity<>(regla, HttpStatus.NOT_FOUND);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json;charset=UTF-8");

		return new ResponseEntity<>(regla, headers, HttpStatus.OK);
	}
	@Scope("request")
	@RequestMapping("/analyzer/rules")
	//@RequestMapping(method = RequestMethod.GET, value = "/rules/{id_rule}")
	@ResponseBody
	public String reglas(HttpServletResponse response) {
		
		Gson gson = new Gson();
		String lista ="[";
		Rule pasiva= new Rule();
		pasiva.setId(1);
		pasiva.setName("Regla - Forma Pasiva");
		pasiva.setDescription("No se permite el uso de la forma pasiva");
		pasiva.setReason(null);
		String pasivaString = gson.toJson(pasiva);
		lista+=pasivaString+",";
		

		Rule sinSujeto= new Rule();
		sinSujeto.setId(2);
		sinSujeto.setName("Regla - Sujeto en la oraci\u00f3n");
		sinSujeto.setDescription("Las oraciones deben tener sujeto");
		sinSujeto.setReason(null);
		String sinSujetoString = gson.toJson(sinSujeto);
		lista+=sinSujetoString+"]";
		

		return lista;
	}



}
