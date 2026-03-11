package com.parking.proj_parking_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.parking.proj_parking_api.web.dto.ClienteCreateDto;
import com.parking.proj_parking_api.web.dto.ClienteResponseDto;
import com.parking.proj_parking_api.web.dto.PageableDto;
import com.parking.proj_parking_api.web.exception.ErrorMessage;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/clientes/clientes-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clientes/clientes-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

public class ClienteIntegTest {

   @Autowired
    WebTestClient testClient;

    @Test
    public void criarCliente_ComDadosValidos_RetornarClienteComStatus201() {
        ClienteResponseDto responseBody = testClient  
                    .post()
                    .uri("/api/v1/clientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@email.com", "123456"))
                    .bodyValue(new ClienteCreateDto("Tobias Ferreira", "48946631040"))
                    
                    .exchange()                             //A partir do "exchange()" é o que se espera após a requisição.
                    .expectStatus().isCreated()
                    .expectBody(ClienteResponseDto.class)
                    .returnResult().getResponseBody();
        
        //Teste de confirmação se a requisição esta retornando o que se espera.
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();                                     
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();                                    
        org.assertj.core.api.Assertions.assertThat(responseBody.getNome()).isEqualTo("Tobias Ferreira");      
        org.assertj.core.api.Assertions.assertThat(responseBody.getCpf()).isEqualTo("48946631040");               
    }

    @Test
    public void criarCliente_ComCpfJaCadastrado_RetornarErrorMessageStatus409() {
        ErrorMessage responseBody = testClient  
                    .post()
                    .uri("/api/v1/clientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@email.com", "123456"))
                    .bodyValue(new ClienteCreateDto("Tobias Ferreira", "89471349028")) //É o CPF do Roberto Gomes no BD
                    
                    .exchange()                             //A partir do "exchange()" é o que se espera após a requisição.
                    .expectStatus().isEqualTo(409)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();
        
        //Teste de confirmação se a requisição esta retornando o que se espera.
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();                                     
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);                                  
    }

    @Test
    public void criarCliente_ComDadosInvalidos_RetornarErrorMessageStatus422() {
        ErrorMessage responseBody = testClient  
                    .post()
                    .uri("/api/v1/clientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@email.com", "123456"))
                    .bodyValue(new ClienteCreateDto(" ", " ")) 
                    
                    .exchange()                             //A partir do "exchange()" é o que se espera após a requisição.
                    .expectStatus().isEqualTo(422)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();  
        
        //Teste de confirmação se a requisição esta retornando o que se espera.
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();                                     
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);   
        
        responseBody = testClient  
                    .post()
                    .uri("/api/v1/clientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@email.com", "123456"))
                    .bodyValue(new ClienteCreateDto("bobb", "00000000000")) 
                    
                    .exchange()                             //A partir do "exchange()" é o que se espera após a requisição.
                    .expectStatus().isEqualTo(422)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();  
        
        //Teste de confirmação se a requisição esta retornando o que se espera.
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();                                     
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

        responseBody = testClient  
                    .post()
                    .uri("/api/v1/clientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@email.com", "123456"))
                    .bodyValue(new ClienteCreateDto("bobb", "489.466.310-40")) 
                    
                    .exchange()                             //A partir do "exchange()" é o que se espera após a requisição.
                    .expectStatus().isEqualTo(422)
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();  
        
        //Teste de confirmação se a requisição esta retornando o que se espera.
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();                                     
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void criarCliente_ComUsuarioNaoPermitido_RetornarErrorMessageStatus403() {
        ErrorMessage responseBody = testClient  
                    .post()
                    .uri("/api/v1/clientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joao@email.com", "123456"))
                    .bodyValue(new ClienteCreateDto("Tobias Ferreira", "48946631040"))  //joao é Admin
                    
                    .exchange()                             //A partir do "exchange()" é o que se espera após a requisição.
                    .expectStatus().isForbidden()
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();
        
        //Teste de confirmação se a requisição esta retornando o que se espera.
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();                                     
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);                                  
    }

    @Test
    public void buscarCliente_ComIdExistentePeloAdmin_RetornarClienteComStatus200() {
        ClienteResponseDto responseBody = testClient  
                    .get()
                    .uri("/api/v1/clientes/10")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joao@email.com", "123456"))
                                        
                    .exchange()                             //A partir do "exchange()" é o que se espera após a requisição.
                    .expectStatus().isOk()
                    .expectBody(ClienteResponseDto.class)
                    .returnResult().getResponseBody();
        
        //Teste de confirmação se a requisição esta retornando o que se espera.
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();   
        org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(10);     
    }

    @Test
    public void buscarCliente_ComIdInexistentePeloAdmin_RetornarErrorMessageComStatus404() {
        ErrorMessage responseBody = testClient  
                    .get()
                    .uri("/api/v1/clientes/0")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joao@email.com", "123456"))
                                        
                    .exchange()                            
                    .expectStatus().isNotFound()
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();
        
        //Teste de confirmação se a requisição esta retornando o que se espera.
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();   
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);     
    }

    @Test
    public void buscarCliente_ComIdExistentePeloAdmin_RetornarErrorMessageComStatus403() {
        ErrorMessage responseBody = testClient  
                    .get()
                    .uri("/api/v1/clientes/0")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
                                        
                    .exchange()                            
                    .expectStatus().isForbidden()
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();
        
        //Teste de confirmação se a requisição esta retornando o que se espera.
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();   
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);     
    }

    @Test
    public void buscarClientes_ComPaginacaoPeloAdmin_RetornarClientesComStatus200() {
        PageableDto responseBody = testClient  
                    .get()
                    .uri("/api/v1/clientes")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joao@email.com", "123456"))
                                                                                            //joao é Admin
                    .exchange()                            
                    .expectStatus().isOk()
                    .expectBody(PageableDto.class)
                    .returnResult().getResponseBody();
        
        //Teste de confirmação se a requisição esta retornando o que se espera.
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();   
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(2);   
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);  
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalElements()).isEqualTo(2);
        
        responseBody = testClient  
                    .get()
                    .uri("/api/v1/clientes?size=1&page=1")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joao@email.com", "123456"))
                                                                                            //joao é Admin
                    .exchange()                            
                    .expectStatus().isOk()
                    .expectBody(PageableDto.class)
                    .returnResult().getResponseBody();
        
        //Teste de confirmação se a requisição esta retornando o que se espera.
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();   
        org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);   
        org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(1);  
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2);
        org.assertj.core.api.Assertions.assertThat(responseBody.getTotalElements()).isEqualTo(2);
    }

    @Test
    public void buscarClientes_ComPaginacaoPeloCliente_RetornarErrorMessageComStatus403() {
        ErrorMessage responseBody = testClient  
                    .get()
                    .uri("/api/v1/clientes")
                    .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com", "123456"))
                                                                                            
                    .exchange()                            
                    .expectStatus().isForbidden()
                    .expectBody(ErrorMessage.class)
                    .returnResult().getResponseBody();
        
        //Teste de confirmação se a requisição esta retornando o que se espera.
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();   
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);  
    }


} 
