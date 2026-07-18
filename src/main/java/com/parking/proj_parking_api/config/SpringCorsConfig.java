package com.parking.proj_parking_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SpringCorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {          // Acesso a API pela aplicação cliente
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("*"));              //Listas de URL a serem liberadas

        configuration.setAllowedMethods(
            Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        configuration.setAllowedHeaders(
            Arrays.asList("Authorization", "Content-Type", "Accept-Language"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //source.registerCorsConfiguration("/v1/usuarios/**", configuration);  // ( /v1/usuarios/** ) => todos os recursos de usuarios
          source.registerCorsConfiguration("/**", configuration);              // (             /** ) => todos os recursos da aplicação.
        
        return source;
    }
}
