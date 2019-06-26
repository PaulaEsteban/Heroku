package com.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
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
	//@RequestMapping("/rules")
	@RequestMapping(method = RequestMethod.GET, value = "/rules/{id_rule}")
	@ResponseBody
	public String reglaConcreta(HttpServletResponse response,@PathVariable("id_rule") Integer id) throws IOException {
		String regla="";// new ArrayList<String>();
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
		}
		return regla;
	}
	@Scope("request")
	@RequestMapping("/rules")
	//@RequestMapping(method = RequestMethod.GET, value = "/rules/{id_rule}")
	@ResponseBody
	public List<String> reglas(HttpServletResponse response) throws IOException {
		List<String> listaReglas= new ArrayList<String>();
		Gson gson = new Gson();

		Rule pasiva= new Rule();
		pasiva.setId(1);
		pasiva.setName("Regla - Forma Pasiva");
		pasiva.setDescription("No se permite el uso de la forma pasiva");
		pasiva.setReason(null);
		String pasivaString = gson.toJson(pasiva);
		listaReglas.add(pasivaString);

		Rule sinSujeto= new Rule();
		sinSujeto.setId(2);
		sinSujeto.setName("Regla - Sujeto en la oraci\u00f3n");
		sinSujeto.setDescription("Las oraciones deben tener sujeto");
		sinSujeto.setReason(null);
		String sinSujetoString = gson.toJson(sinSujeto);
		listaReglas.add(sinSujetoString);

		return listaReglas;
	}



}
