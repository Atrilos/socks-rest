package com.atrilos.socksrest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition
@SpringBootApplication
public class SocksRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocksRestApplication.class, args);
    }

}
