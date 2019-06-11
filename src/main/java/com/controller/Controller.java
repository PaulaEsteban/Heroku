package com.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
public class Controller {

  @Scope("request")
  @RequestMapping("/url2")
  @ResponseBody
  public String servicioWebEjemplo2(HttpServletResponse response,
      @RequestParam(name="json", required=true) String json) throws Throwable {
    
    return "yes";
  }
}