package com.parking.proj_parking_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.parking.proj_parking_api.web.dto.EstacionamentoCreateDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/estacionamentos/estacionamentos-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/estacionamentos/estacionamentos-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class EstacionamentoIntegTest {

    @Autowired
    WebTestClient testClient;

    @Test
    public void criarCheckin_ComDadosValidos_RetornarCreatedAndLocation() {
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("WER-1111")
                .marca("FIAT")
                .modelo("PALIO 1.0")
                .cor("AZUL")
                .clienteCpf("09191773016")               
                .build();

        testClient  
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com.br", "123456"))
                .bodyValue(createDto)

                .exchange()                             //A partir do "exchange()" é o que se espera após a requisição.
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody()
                .jsonPath("placa").isEqualTo("WER-1111")
                .jsonPath("marca").isEqualTo("FIAT")
                .jsonPath("modelo").isEqualTo("PALIO 1.0")
                .jsonPath("cor").isEqualTo("AZUL")
                .jsonPath("clienteCpf").isEqualTo("09191773016")
                .jsonPath("recibo").exists()
                .jsonPath("dataEntrada").exists()
                .jsonPath("vagaCodigo").exists();                            
    }

    @Test
    public void criarCheckin_ComRoleCliente_RetornarErrorMessageComStatus403() {
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("WER-1111")
                .marca("FIAT")
                .modelo("PALIO 1.0")
                .cor("AZUL")
                .clienteCpf("09191773016")               
                .build();

        testClient  
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com.br", "123456"))
                .bodyValue(createDto)                                                // bia é cliente

                .exchange()                             //A partir do "exchange()" é o que se espera após a requisição.
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in")
                .jsonPath("method").isEqualTo("POST");                              
    }

    @Test
    public void criarCheckin_ComDadosInvalidos_RetornarErrorMessageComStatus422() {
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("WEEER-AAAA")
                .marca("FIAT")
                .modelo("PALIO 1.0")
                .cor("")
                .clienteCpf("09191773016-123@456$789")               
                .build();

        testClient  
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com.br", "123456"))
                .bodyValue(createDto)                                                

                .exchange()                             //A partir do "exchange()" é o que se espera após a requisição.
                .expectStatus().isEqualTo(422)
                .expectBody()
                .jsonPath("status").isEqualTo("422")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in")
                .jsonPath("method").isEqualTo("POST");                              
    }

    @Test
    public void criarCheckin_ComCpfInexistente_RetornarErrorMessageComStatus404() {
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("WER-1111")
                .marca("FIAT")
                .modelo("PALIO 1.0")
                .cor("AZUL")
                .clienteCpf("33838667000")  //CPF é válido (gerador de CPF), mas não esta na base de dados.
                .build();

        testClient  
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com.br", "123456"))
                .bodyValue(createDto)                                                

                .exchange()                             //A partir do "exchange()" é o que se espera após a requisição.
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in")
                .jsonPath("method").isEqualTo("POST");                              
    }

    @Sql(scripts = "/sql/estacionamentos/estacionamento-insert-vagas-ocupadas.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/estacionamentos/estacionamento-delete-vagas-ocupadas.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)    
    @Test
    public void criarCheckin_ComVagasOcupadas_RetornarErrorMessageComStatus404() {
        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
                .placa("WER-1111")
                .marca("FIAT")
                .modelo("PALIO 1.0")
                .cor("AZUL")
                .clienteCpf("09191773016")  
                .build();

        testClient  
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com.br", "123456"))
                .bodyValue(createDto)                                                

                .exchange()                             //A partir do "exchange()" é o que se espera após a requisição.
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in")
                .jsonPath("method").isEqualTo("POST");                              
    }

    @Test
    public void buscarCheckin_ComPerfilAdmin_RetornarDadosComStatus200() {
                        
        testClient  
                .get()
                .uri("/api/v1/estacionamentos/check-in/{recibo}", "20230313-101300")                
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com.br", "123456"))
                                                             
                .exchange()                             //A partir do "exchange()" é o que se espera após a requisição.
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("placa").isEqualTo("FIT-1020")
                .jsonPath("marca").isEqualTo("FIAT")
                .jsonPath("modelo").isEqualTo("PALIO")
                .jsonPath("cor").isEqualTo("VERDE")
                .jsonPath("clienteCpf").isEqualTo("98401203015")
                .jsonPath("recibo").isEqualTo("20230313-101300")
                .jsonPath("dataEntrada").isEqualTo("2023-03-13 10:15:00")
                .jsonPath("vagaCodigo").isEqualTo("A-01");               
    }

    @Test
    public void buscarCheckin_ComPerfilCliente_RetornarDadosComStatus200() {
                        
        testClient  
                .get()
                .uri("/api/v1/estacionamentos/check-in/{recibo}", "20230313-101300")                
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bob@email.com.br", "123456"))
                                                             
                .exchange()                             //A partir do "exchange()" é o que se espera após a requisição.
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("placa").isEqualTo("FIT-1020")
                .jsonPath("marca").isEqualTo("FIAT")
                .jsonPath("modelo").isEqualTo("PALIO")
                .jsonPath("cor").isEqualTo("VERDE")
                .jsonPath("clienteCpf").isEqualTo("98401203015")
                .jsonPath("recibo").isEqualTo("20230313-101300")
                .jsonPath("dataEntrada").isEqualTo("2023-03-13 10:15:00")
                .jsonPath("vagaCodigo").isEqualTo("A-01");               
    }

    @Test
    public void buscarCheckin_ComReciboInexistente_RetornarErrorMessageComStatus404() {
                        
        testClient  
                .get()
                .uri("/api/v1/estacionamentos/check-in/{recibo}", "20230313-999999")         
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bob@email.com.br", "123456"))
                                                             
                .exchange()                             //A partir do "exchange()" é o que se espera após a requisição.
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in/20230313-999999")
                .jsonPath("method").isEqualTo("GET");     
    }

    @Test
    public void criarCheckOut_ComReciboExistente_RetornarSucesso_ComStatus200() {
                        
        testClient
                .put()
                .uri("/api/v1/estacionamentos/check-out/{recibo}", "20230313-101300")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com.br", "123456"))
                                                             
                .exchange()                             //A partir do "exchange()" é o que se espera após a requisição.
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("placa").isEqualTo("FIT-1020")
                .jsonPath("marca").isEqualTo("FIAT")
                .jsonPath("modelo").isEqualTo("PALIO")
                .jsonPath("cor").isEqualTo("VERDE")
                .jsonPath("dataEntrada").isEqualTo("2023-03-13 10:15:00")
                .jsonPath("clienteCpf").isEqualTo("98401203015")
                .jsonPath("vagaCodigo").isEqualTo("A-01")
                .jsonPath("recibo").isEqualTo("20230313-101300")               
                .jsonPath("dataSaida").exists()
                .jsonPath("valor").exists()
                .jsonPath("desconto").exists();                
    }                    

    @Test
    public void criarCheckOut_ComReciboInexistente_RetornarErrorMessageComStatus404() {
                        
        testClient  
                .put()
                .uri("/api/v1/estacionamentos/check-out/{recibo}", "20230313-000000")                
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@email.com.br", "123456"))
                                                             
                .exchange()                             //A partir do "exchange()" é o que se espera após a requisição.
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-out/20230313-000000")
                .jsonPath("method").isEqualTo("PUT");                
    }      
    
    @Test
    public void criarCheckOut_ComPerfilCliente_RetornarErrorMessageComStatus403() {
                        
        testClient  
                .put()
                .uri("/api/v1/estacionamentos/check-out/{recibo}", "20230313-101300")                
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "bia@email.com.br", "123456"))
                                                             
                .exchange()                             //A partir do "exchange()" é o que se espera após a requisição.
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-out/20230313-101300")
                .jsonPath("method").isEqualTo("PUT");                
    }     
}                           

                                