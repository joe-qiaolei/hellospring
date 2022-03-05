package com.example.hellospring;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

  @RequestMapping("/")
  public String index() {
      String version = System.getenv("VERSION");
      return "Greetings from Azure Spring Cloud! THIS IS THE " +version+ " DEPLOYMENT";
  }

}