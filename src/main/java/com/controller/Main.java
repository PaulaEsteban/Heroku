package com.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
  @RequestMapping("/analyzer")
  @ResponseBody
  public String servicioWebEjemplo1(HttpServletResponse response,
      @RequestParam(name="texto", required=true) String texto) throws Throwable {
    //https://analizadorlecturafacil.herokuapp.com/analyzer?texto="holaaa" ASI ES COMO SE HACE LA LLAMADA
    return texto;
  }
}
