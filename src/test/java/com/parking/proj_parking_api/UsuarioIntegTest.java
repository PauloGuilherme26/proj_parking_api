package com.parking.proj_parking_api;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.parking.proj_parking_api.web.dto.UsuarioCreateDto;
import com.parking.proj_parking_api.web.dto.UsuarioResponseDto;
import com.parking.proj_parking_api.web.dto.UsuarioSenhaDto;
import com.parking.proj_parking_api.web.exception.ErrorMessage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

public class UsuarioIntegTest {

   @Autowired
    WebTestClient testClient;
   
    @Test
    public void createUsuario_ComUsernameEPasswordValidos_RetornarUsuarioCriadoComStatus201() {
        UsuarioResponseDto responseBody = testClient  
                    .post()
                    .uri("/api/v1/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new UsuarioCreateDto("tody@email.com", "123456"))
                    
                    .exchange()     //A partir do "exchange()" é o que se espera após a requisição.
                    .expectStatus().isCreated()
                    .expectBody(UsuarioResponseDto.class)
                    .returnResult().getResponseBody();
        
        //Teste de confirmação se a requisição esta retornando o que se espera.
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();                                     //UsuarioResponseDto
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();                                    //Id
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("tody@email.com");    //Username  
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");               //Role
    }

    @Test
    public void createUsuario_ComUsernameInvalido_RetornarErrorMessageStatus422() {
        ErrorMessage responseBody = testClient  
                    .post()
                    .uri("/api/v1/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new UsuarioCreateDto("", "123456"))
                    .exchange()
                    .expectStatus().isEqualTo(422)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient  
                    .post()
                    .uri("/api/v1/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new UsuarioCreateDto("tody@", "123456"))
                    .exchange()
                    .expectStatus().isEqualTo(422)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient  
                    .post()
                    .uri("/api/v1/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new UsuarioCreateDto("tody@email.", "123456"))
                    .exchange()
                    .expectStatus().isEqualTo(422)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUsuario_ComPasswordInvalido_RetornarErrorMessageStatus422() {
        ErrorMessage responseBody = testClient  
                    .post()
                    .uri("/api/v1/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new UsuarioCreateDto("tody@email.com", ""))
                    .exchange()
                    .expectStatus().isEqualTo(422)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient  
                    .post()
                    .uri("/api/v1/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new UsuarioCreateDto("tody@email", "123"))
                    .exchange()
                    .expectStatus().isEqualTo(422)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient  
                    .post()
                    .uri("/api/v1/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new UsuarioCreateDto("tody@email", "12345678"))
                    .exchange()
                    .expectStatus().isEqualTo(422)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void createUsuario_ComuUsernameRepetido_RetornarErrorMessageStatus409() {
        ErrorMessage responseBody = testClient  
                    .post()
                    .uri("/api/v1/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new UsuarioCreateDto("ana@email.com", "123456")) //ana@email.com já existe no banco
                    .exchange()
                    .expectStatus().isEqualTo(409)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);

    }

    @Test
    public void buscarUsuario_ComIdExistente_RetornarUsuarioComStatus200() {
        UsuarioResponseDto responseBody = testClient  
                    .get()
                    .uri("/api/v1/usuarios/100")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(UsuarioResponseDto.class)
                    .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();                                           
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(100);                        //Id
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("ana@email.com");      //Username
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("ADMIN");                  //Role

        responseBody = testClient  
                    .get()
                    .uri("/api/v1/usuarios/101")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(UsuarioResponseDto.class)
                    .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();                                           
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(101);                       
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("bia@email.com");     
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE"); 

        responseBody = testClient  
                    .get()
                    .uri("/api/v1/usuarios/101")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(UsuarioResponseDto.class)
                    .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();                                           
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(101);                       
        org.assertj.core.api.Assertions.assertThat(responseBody.getUsername()).isEqualTo("bia@email.com");     
        org.assertj.core.api.Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE");
    }

    @Test
    public void buscarUsuario_ComIdinexistente_RetornarErrorMessageComStatus404() {
        ErrorMessage responseBody = testClient  
                    .get()
                    .uri("/api/v1/usuarios/0")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }

    @Test
    public void buscarUsuario_ComUsuarioClienteBuscandoOutroCliente_RetornarErrorMessageComStatus403() {
        ErrorMessage responseBody = testClient  
                    .get()
                    .uri("/api/v1/usuarios/102")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
                    .exchange()
                    .expectStatus().isForbidden()
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void editarSenha_ComDadosValidos_RetornarStatus204() {
            testClient  
                    .patch()
                    .uri("/api/v1/usuarios/100")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new UsuarioSenhaDto("123456", "123456", "123456"))
                    .exchange()
                    .expectStatus().isNoContent();                  
    
            testClient  
                    .patch()
                    .uri("/api/v1/usuarios/101")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new UsuarioSenhaDto("123456", "123456", "123456"))
                    .exchange()
                    .expectStatus().isNoContent();                  
    }

    @Test
    public void editarSenha_ComUsuariosDiferentes_RetornarErrorMessageComStatus403() {
        ErrorMessage responseBody = testClient  
                    .patch()
                    .uri("/api/v1/usuarios/102")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new UsuarioSenhaDto("123456", "123456", "123456"))
                    .exchange()
                    .expectStatus().isForbidden()
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

        responseBody = testClient  
                    .patch()
                    .uri("/api/v1/usuarios/102")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new UsuarioSenhaDto("123456", "123456", "123456"))
                    .exchange()
                    .expectStatus().isForbidden()
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }

    @Test
    public void editarSenha_ComCamposFormatoInvalidos_RetornarErrorMessageComStatus422() {
        ErrorMessage responseBody = testClient  
                    .patch()
                    .uri("/api/v1/usuarios/100")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new UsuarioSenhaDto("", "", ""))
                    .exchange()
                    .expectStatus().isEqualTo(422)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient  
                    .patch()
                    .uri("/api/v1/usuarios/100")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new UsuarioSenhaDto("123", "123", "123"))
                    .exchange()
                    .expectStatus().isEqualTo(422)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient  
                    .patch()
                    .uri("/api/v1/usuarios/100")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new UsuarioSenhaDto("12345678", "12345678", "12345678"))
                    .exchange()
                    .expectStatus().isEqualTo(422)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void editarSenha_ComSenhasInvalidas_RetornarErrorMessageComStatus400() {
        ErrorMessage responseBody = testClient  
                    .patch()
                    .uri("/api/v1/usuarios/100")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new UsuarioSenhaDto("123456", "123456", "000000"))
                    .exchange()
                    .expectStatus().isEqualTo(400)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);

        responseBody = testClient  
                    .patch()
                    .uri("/api/v1/usuarios/100")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new UsuarioSenhaDto("000000", "123456", "123456"))
                    .exchange()
                    .expectStatus().isEqualTo(400)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);
    }

    @Test
    public void buscarTodosUsuarios_SemQualquerParametro_RetornarListaUsuarioComStatus200() {
        List<UsuarioResponseDto> responseBody = testClient  
                    .get()
                    .uri("/api/v1/usuarios")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com", "123456"))
                    .exchange()
                    .expectStatus().isOk()
                    .expectBodyList(UsuarioResponseDto.class)   //expectBodyList
                    .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();                                           
        org.assertj.core.api.Assertions.assertThat(responseBody.size()).isEqualTo(3);   //Quantidade de usuarios da lista                     
    }

    @Test
    public void buscarTodosUsuarios_ComUsuarioSemPermisao_RetornarErroMessageComStatus403() {
        ErrorMessage responseBody = testClient  
                    .get()
                    .uri("/api/v1/usuarios")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
                    .exchange()
                    .expectStatus().isForbidden()
                    .expectBody(ErrorMessage.class)             //expectBody
                    .returnResult().getResponseBody();

        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();                                           
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);                       
    }

}
