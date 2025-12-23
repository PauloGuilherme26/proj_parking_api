package com.parking.proj_parking_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SpringDocOpenApiConfig {// teste de branch
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                                        .title("Rest API - Spring Park")
                                        .description("API para gestão de estacionamento de veículos")
                                        .version("v1")
                                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                                        .contact(new Contact().name("João Guilherme").email("joaoguilherme26@hotmail.com"))        
                                    );
    }
}
