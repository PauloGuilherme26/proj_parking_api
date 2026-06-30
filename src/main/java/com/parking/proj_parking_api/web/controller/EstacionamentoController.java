package com.parking.proj_parking_api.web.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.parking.proj_parking_api.entity.ClienteVaga;
import com.parking.proj_parking_api.repository.projection.ClienteVagaProjection;
import com.parking.proj_parking_api.service.ClienteVagaService;
import com.parking.proj_parking_api.service.EstacionamentoService;
import com.parking.proj_parking_api.web.dto.EstacionamentoCreateDto;
import com.parking.proj_parking_api.web.dto.EstacionamentoResponseDto;
import com.parking.proj_parking_api.web.dto.PageableDto;
import com.parking.proj_parking_api.web.dto.mapper.ClienteVagaMapper;
import com.parking.proj_parking_api.web.dto.mapper.PageAbleMapper;
import com.parking.proj_parking_api.web.exception.ErrorMessage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.headers.Header;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Estacionamentos", description = "Operações de registro de entrada e saída de um veículo do estacionamento.")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/estacionamentos")
public class EstacionamentoController {

    private final EstacionamentoService estacionamentoService;
    private final ClienteVagaService clienteVagaService;

    @Operation(summary = "Operação de check-in.", description = "Recurso para dar entrada de um veículo no estacionamento. " +
            "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            responses = {       
                    @ApiResponse (responseCode = "201", description = "Recurso criado com sucesso",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "URL de acesso ao Recurso criado"),
                            content = @Content(mediaType = "application/json;charset=UTF-8", 
                            schema = @Schema(implementation = EstacionamentoResponseDto.class))),

                    @ApiResponse (responseCode = "404", description = "Causas possíveis: <br/>" + 
                            "- CPF do cliente não cadastrado no sistema; <br/>" +
                            "- Nenhuma vaga livre localizada;",
                            content = @Content(mediaType = "application/json;charset=UTF-8", 
                            schema = @Schema(implementation = ErrorMessage.class))),

                    @ApiResponse (responseCode = "422", description = "Recurso não processado por falta de dados ou dados inválidos",
                            content = @Content(mediaType = "application/json;charset=UTF-8", 
                            schema = @Schema(implementation = ErrorMessage.class))),

                    @ApiResponse (responseCode = "403", description = "Recurso não permitido ao perfil de CLIENTE",
                            content = @Content(mediaType = "application/json;charset=UTF-8", 
                            schema = @Schema(implementation = ErrorMessage.class))),    
            })

    @PostMapping("/check-in")            // Criar um Checkin.
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstacionamentoResponseDto> checkin (@RequestBody @Valid EstacionamentoCreateDto dto) {
        ClienteVaga clienteVaga = ClienteVagaMapper.toClienteVaga(dto);
        estacionamentoService.checkIn(clienteVaga);
        EstacionamentoResponseDto responseDto = ClienteVagaMapper.toDto(clienteVaga);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{recibo}")
                .buildAndExpand(clienteVaga.getRecibo())
                .toUri();
        return ResponseEntity.created(location).body(responseDto);
    }

    @GetMapping("/check-in/{recibo}")   //Localizar recibo
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    public ResponseEntity <EstacionamentoResponseDto> getByRecibo(@PathVariable String recibo) {
        ClienteVaga clienteVaga = clienteVagaService.buscarPorRecibo(recibo);
        EstacionamentoResponseDto dto = ClienteVagaMapper.toDto(clienteVaga);
        return ResponseEntity.ok(dto);
    }    
    
    @Operation(summary = "Operação de check-out.", description = "Recurso para dar saída de um veículo do estacionamento. " +
            "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "recibo", description = "Número do recibo gerado pelo check-in")
            },
            responses = {       
                    @ApiResponse (responseCode = "200", description = "Recurso atualizado com sucesso",
                            content = @Content(mediaType = "application/json;charset=UTF-8", 
                            schema = @Schema(implementation = EstacionamentoResponseDto.class))),

                    @ApiResponse (responseCode = "404", description = "Causas possíveis: <br/>" + 
                            "- Número do recibo inexistente; <br/>" +
                            "- O veículo já passou pelo check-out.",
                            content = @Content(mediaType = "application/json;charset=UTF-8", 
                            schema = @Schema(implementation = ErrorMessage.class))),                   

                    @ApiResponse (responseCode = "403", description = "Recurso não permitido ao perfil de CLIENTE",
                            content = @Content(mediaType = "application/json;charset=UTF-8", 
                            schema = @Schema(implementation = ErrorMessage.class))),    
            })

    @PutMapping("/check-out/{recibo}")   
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity <EstacionamentoResponseDto> checkout(@PathVariable String recibo) {
        ClienteVaga clienteVaga = estacionamentoService.checkOut(recibo);
        EstacionamentoResponseDto dto = ClienteVagaMapper.toDto(clienteVaga);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Localizar os registros de estacionamentos do cliente pelo CPF.", 
               description = "LOcalizar os registros de estacionamentos do cliente pelo CPF. " +
            "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "cpf", description = "Número do cpf referente ao cliente a ser consultado.", 
                               required = true
                        ),
                    @Parameter(in = QUERY, name = "size", description = "Representa a página retornada.",
                               content = @Content(schema = @Schema(type = "interger", defaultValue = "0"))
                        ),
                    @Parameter(in = QUERY, name = "page", description = "Representa o total de elementos por página.",
                               content = @Content(schema = @Schema(type = "interger", defaultValue = "5"))
                        ),
                    @Parameter(in = QUERY, name = "sort", description = "Campo padrão de ordenação 'dataEntrada,asc'.",
                               array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "dataEntrada,asc")),
                               hidden = true
                        ) },
                        
            responses = {       
                    @ApiResponse (responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = "application/json;charset=UTF-8", 
                            schema = @Schema(implementation = PageableDto.class))),
                    
                    @ApiResponse (responseCode = "403", description = "Recurso não permitido ao perfil de CLIENTE",
                            content = @Content(mediaType = "application/json;charset=UTF-8", 
                            schema = @Schema(implementation = ErrorMessage.class))),    
            } )

    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity <PageableDto> getAllEstacionamentosPorCpf ( 
        @PathVariable 
        String cpf, 
        @PageableDefault(size = 5, sort = "dataEntrada", direction = Sort.Direction.ASC) 
        Pageable pageable ) {
        
        Page <ClienteVagaProjection> projection = clienteVagaService.buscarTodosPorClienteCpf(cpf, pageable);
        PageableDto dto = PageAbleMapper.toDto(projection);
        return ResponseEntity.ok(dto);        
    }
}

                        