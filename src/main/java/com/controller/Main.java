package com.controller;

import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Scope;
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

  @RequestMapping("/")
  String index() {
    return "index";
  }
  
  @Scope("request")
  @RequestMapping("/checkRules/1")
  @ResponseBody
  public String comprobarPasiva(HttpServletResponse response,
      @RequestParam(name="texto", required=true) String texto) throws Throwable {
    //https://analizadorlecturafacil.herokuapp.com/analyzer?texto="holaaa" ASI ES COMO SE HACE LA LLAMADA
    //https://analizadorlecturafacil.herokuapp.com/analyzer?texto="holaaa"&regla="1" PARA MANDAR MÁS DE UN 1 
	  Analizador analyzer=new Analizador(texto);
	  String resultado= analyzer.reglaPasiva(texto);	  
	  return resultado;
  }
}
