package com.parking.proj_parking_api.jwt;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parking.proj_parking_api.web.exception.ErrorMessage;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,AuthenticationException authException) throws IOException, ServletException {
        log.info("Http Satus 401 {}", authException.getMessage());

        ErrorMessage error = new ErrorMessage(request, HttpStatus.UNAUTHORIZED, "Authentication is required");

        response.setHeader("www-authenticate", "Bearer realm='/api/v1/auth'");
        response.setContentType("application/json");    
        response.setStatus(401);

        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}
