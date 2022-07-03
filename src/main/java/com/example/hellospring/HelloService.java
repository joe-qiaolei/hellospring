package com.example.hellospring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloService.class);

    public void sayHello(String who){
        LOGGER.info("Hello " + who + " " + System.getenv("VERSION"));
    }

    public void someOtherMethod() {}
}
