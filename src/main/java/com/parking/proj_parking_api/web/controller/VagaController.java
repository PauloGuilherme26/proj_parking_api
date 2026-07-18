package com.parking.proj_parking_api.web.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.parking.proj_parking_api.entity.Vaga;
import com.parking.proj_parking_api.service.VagaService;
import com.parking.proj_parking_api.web.dto.VagaCreateDto;
import com.parking.proj_parking_api.web.dto.VagaResponseDto;
import com.parking.proj_parking_api.web.dto.mapper.VagaMapper;
import com.parking.proj_parking_api.web.exception.ErrorMessage;

import org.springframework.http.HttpHeaders;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

@Tag(name = "Vagas", description = "Contem todas as operações relativas ao recurso de um vagas.")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/vagas")
@Validated
public class VagaController {

    private final VagaService vagaService;

    @Operation(summary = "Criar uma nova vaga.", description = "Recurso para criar uma nova vaga. " +
                        "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'",              
        responses = {
            @ApiResponse (responseCode = "201", description = "Recurso criado com sucesso",
                    headers = @Header(name = HttpHeaders.LOCATION, description = "URL do Recurso criado")),
            @ApiResponse (responseCode = "409", description = "Vaga já cadastrada.",
                    content = @Content(mediaType = "application/json;charset=UTF-8", 
                    schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse (responseCode = "422", description = "Recurso não processado por falta de dados ou dados inválidos",
                    content = @Content(mediaType = "application/json;charset=UTF-8", 
                    schema = @Schema(implementation = ErrorMessage.class))),       
            @ApiResponse (responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = ErrorMessage.class))),             
        }
    )

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity <Void> create(@RequestBody @Valid VagaCreateDto dto) {
        Vaga vaga = VagaMapper.toVaga(dto);
        vagaService.salvar(vaga);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{codigo}")
                .buildAndExpand(vaga.getCodigo())
                .toUri();
        return ResponseEntity.created(location).build();          
    }
    
    @Operation(summary = "Localizar uma vaga.", description = "Requisição para retornar uma vaga pelo seu código." + 
        "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'",
        responses = {
            @ApiResponse (responseCode = "200", description = "Recurso criado com sucesso!",
                content = @Content(mediaType = "application/json;charset=UTF-8", 
                schema = @Schema(implementation = VagaResponseDto.class))),
            @ApiResponse (responseCode = "404", description = "Vaga não localizada!",
                content = @Content(mediaType = "application/json;charset=UTF-8", 
                schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse (responseCode = "500", description = "Internal Server Error",
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = ErrorMessage.class))),
        }
    )

    @GetMapping("/{codigo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity <VagaResponseDto> getByCodigo(@PathVariable 
                                                        @Size(min = 4, max = 4, message = "Código deve ter 4 caracteres")
                                                        @Pattern(regexp = "^[A-Z]-\\d{2}$", message = "O código deve ter o formato 'X-00'")
                                                        @NotBlank(message = "Código deve ser informado")
                                                        String codigo) {
                                                            
        Vaga vaga = vagaService.buscarPorCodigo(codigo);
        return ResponseEntity.ok(VagaMapper.toDto(vaga));          
    }             
}
