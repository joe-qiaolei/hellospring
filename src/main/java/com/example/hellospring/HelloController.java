package com.example.hellospring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

    private final HelloService helloService;

    @Autowired
    public HelloController(HelloService helloService) {
        this.helloService = helloService;
    }

    @RequestMapping("/")
    public String index() {
        String version = System.getenv("VERSION");
        helloService.sayHello(version);
        return "Greetings from Azure Spring Cloud team! THIS IS THE " + version + " DEPLOYMENT!";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void save() {

    }

}