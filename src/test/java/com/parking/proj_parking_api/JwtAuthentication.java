package com.parking.proj_parking_api;

import java.util.function.Consumer;

import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.parking.proj_parking_api.jwt.JwtToken;
import com.parking.proj_parking_api.web.dto.UsuarioLoginDto;

public class JwtAuthentication {    // Preparando a operação de autenticação para a destravar os testes

    public static Consumer <HttpHeaders> getHeaderAuthorization(WebTestClient client, String username, String password) {
        String token = client
                .post()
                .uri("/api/v1/auth")
                .bodyValue(new UsuarioLoginDto(username, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody().getToken();
        return Headers -> Headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

    }

}
