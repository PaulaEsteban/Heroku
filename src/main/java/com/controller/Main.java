package com.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Scope;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
	@RequestMapping("/checkRules/1")
	@ResponseBody
	public String comprobarPasiva(HttpServletResponse response,
			@RequestParam(name="texto", required=true) String texto) throws Throwable {
		Analizador analyzer=new Analizador(texto);
		return analyzer.reglaPasiva(texto);
	}
	@Scope("request")
	@RequestMapping("/checkRules/2")
	@ResponseBody
	public String comprobarSinSujeto(HttpServletResponse response,
			@RequestParam(name="texto", required=true) String texto) throws Throwable {
		Analizador analyzer=new Analizador(texto);
		return analyzer.reglaSinSujeto(texto);
	}
	@Scope("request")
	@RequestMapping("/checkRules")
	@ResponseBody
	public List<String> comprobarLecturaFacil(HttpServletResponse response,
			@RequestParam(name="texto", required=true) String texto) throws Throwable {
		Analizador analyzer=new Analizador(texto);
		String pasiva =analyzer.reglaPasiva(texto);
		String sinSujeto= analyzer.reglaSinSujeto(texto);
		List<String> listaResultados= new ArrayList<String>();
		listaResultados.add(pasiva);
		listaResultados.add(sinSujeto);
		return listaResultados;
	}
	@Scope("request")
	@RequestMapping("/rules")
	@ResponseBody
	public List<String> reglas(HttpServletResponse response) throws Throwable {
		Rule pasiva= new Rule();
		pasiva.setId(1);
		pasiva.setName("Regla - Forma Pasiva");
		pasiva.setDescription("No se permite el uso de la forma pasiva");
		pasiva.setReason(null);
		Gson gson = new Gson();
		String pasivaString = gson.toJson(pasiva);
		Rule sinSujeto= new Rule();
		sinSujeto.setId(1);
		sinSujeto.setName("Regla - Sujeto en la oraci\u00f3n");
		sinSujeto.setDescription("Las oraciones deben tener sujeto");
		sinSujeto.setReason(null);
		String sinSujetoString = gson.toJson(sinSujeto);
		List<String> listaReglas= new ArrayList<String>();
		listaReglas.add(pasivaString);
		listaReglas.add(sinSujetoString);
		return listaReglas;
	}
}
