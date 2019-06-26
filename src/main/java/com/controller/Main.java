package com.controller;

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

@Controller
@SpringBootApplication
public class Main {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Main.class, args);
	}

//	@RequestMapping("/")
//	String index() {
//		return "index";
//	}

	@Scope("request")
	@RequestMapping("/checkRules/1")
	@ResponseBody
	public String comprobarPasiva(HttpServletResponse response,
			@RequestParam(name="texto", required=true) String texto) throws Throwable {
		Analizador analyzer=new Analizador(texto);
		return analyzer.reglaPasiva(texto);
	}
}
