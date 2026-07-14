package com.parking.proj_parking_api.web.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parking.proj_parking_api.entity.Cliente;
import com.parking.proj_parking_api.jwt.JwtUserDetails;
import com.parking.proj_parking_api.repository.projection.ClienteProjection;
import com.parking.proj_parking_api.service.ClienteService;
import com.parking.proj_parking_api.service.UsuarioService;
import com.parking.proj_parking_api.web.dto.ClienteCreateDto;
import com.parking.proj_parking_api.web.dto.ClienteResponseDto;
import com.parking.proj_parking_api.web.dto.PageableDto;
import com.parking.proj_parking_api.web.dto.UsuarioResponseDto;
import com.parking.proj_parking_api.web.dto.mapper.ClienteMapper;
import com.parking.proj_parking_api.web.dto.mapper.PageAbleMapper;
import com.parking.proj_parking_api.web.exception.ErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Clientes", description = "Contem todas as operações relativas aos recursos de um cliente.")
@RequiredArgsConstructor
@RestController
@RequestMapping ("api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final UsuarioService usuarioService;

    @Operation(summary = "Criar um novo cliente.", 
           description = "Recurso para criar um novo cliente vinculado a um usuário cadastrado. " +
                        "Requisição exige uso de um bearer token. Acesso restrito a Role='CLIENTE'",
              security = @SecurityRequirement(name = "security"), // Inserção da opção de token na documentação.
        responses = {
            @ApiResponse (responseCode = "201", description = "Recurso criado com sucesso",
                content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse (responseCode = "409", description = "Cliente CPF já possui cadastrado no sistema",
                content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse (responseCode = "422", description = "Recurso não processado por falta de dados ou dados inválidos",
                content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse (responseCode = "403", description = "Recurso não permitido ao perfil de ADMIN",
                content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse (responseCode = "500", description = "Internal Server Error",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),    
        }
    )

    @PostMapping            // Criar um Cliente.    //Só quem pode criar um cliente é o próprio usuário logado.
    @PreAuthorize("hasRole('CLIENTE')")             //Um único usuário só pode criar um único cliente.
    public ResponseEntity<ClienteResponseDto> create (@RequestBody @Valid ClienteCreateDto dto,
                                                      @AuthenticationPrincipal JwtUserDetails userDetails) {

        Cliente cliente = ClienteMapper.toCliente(dto);
        cliente.setUsuario(usuarioService.buscarPorId(userDetails.getId()));
        clienteService.salvar(cliente);                                                    
        return ResponseEntity.status(201).body(ClienteMapper.toDto(cliente));
    }

    @Operation(summary = "Localizar um cliente.", 
           description = "Recurso para localizar um cliente pelo ID. " +
        "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'",
              security = @SecurityRequirement(name = "security"), // Inserção da opção de token na documentação.
        responses = {
            @ApiResponse (responseCode = "200", description = "Recurso localizado com sucesso",
                content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse (responseCode = "404", description = "Cliente não encontrado",
                content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse (responseCode = "403", description = "Recurso não permitido ao perfil de CLIENTE",
                content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse (responseCode = "500", description = "Internal Server Error",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),    
        }
    )

    @GetMapping("/{id}")    // Buscar Cliente pelo Id.
    @PreAuthorize("hasRole('ADMIN')")     //Permissão de acesso do perfil Admin  
    public ResponseEntity<ClienteResponseDto> getById (@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }   

    @Operation(summary = "Recuperar Lista de todos clientes.", 
           description = "Requisição exige uso de um Bearer Token. Acesso restrito a Role='ADMIN' ",
              security = @SecurityRequirement(name = "security"), // Inserção da opção de token na documentação.
        parameters = {
            @Parameter(in = QUERY, name = "page",
                content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                description = "Representa a página retornada/atual"
            ),
            @Parameter(in = QUERY, name = "size",
                content = @Content(schema = @Schema(type = "integer", defaultValue = "20")),
                description = "Representa o total de elementos por página"
            ),
            @Parameter(in = QUERY, name = "sort", hidden = true,
                array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "id,asc")),
                description = "Representa a ordenação dos resultados. Multiplos critérios de ordenação são suportados. "
            ),
        },
        responses = {
            @ApiResponse (responseCode = "200", description = "Listagem gerada com sucesso!",
                content = @Content(mediaType = "application/json; charset=UTF-8", 
                    schema = @Schema(implementation = ClienteResponseDto.class))
            ),
            @ApiResponse (responseCode = "403", description = "Recurso não permitido ao perfil de CLIENTE!",
                content = @Content(mediaType = "application/json; charset=UTF-8", 
                    schema = @Schema(implementation = ErrorMessage.class))
            ),   
            @ApiResponse (responseCode = "500", description = "Internal Server Error",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
        } )

    @GetMapping                 // Listar todos os clientes.
    @PreAuthorize("hasRole('ADMIN')")     //Permissão de acesso do perfil Admin  
    public ResponseEntity<PageableDto> getAll (@Parameter(hidden = true) @PageableDefault(size = 5, sort = {"nome"}) Pageable pageable) {
        Page<ClienteProjection> clientes = clienteService.buscarTodos(pageable);
        return ResponseEntity.ok(PageAbleMapper.toDto(clientes));
    }   

    @Operation(summary = "Recuperar dados do cliente autenticado.", 
           description = "Requisição exige uso de um bearer token. Acesso restrito a Role='CLIENTE'",
              security = @SecurityRequirement(name = "security"), // Inserção da opção de token na documentação.
        responses = {
            @ApiResponse (responseCode = "200", description = "Recurso localizado com sucesso",
                content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = UsuarioResponseDto.class))
            ),            
            @ApiResponse (responseCode = "403", description = "Recurso não permitido ao perfil de ADMIN",
                content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))
            ),
            @ApiResponse (responseCode = "500", description = "Internal Server Error",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
        } )

    @GetMapping("/detalhes")    // Listar detalhes do próprio cliente.            
    @PreAuthorize("hasRole('CLIENTE')")   //Permissão de acesso do perfil Cliente    
    public ResponseEntity<ClienteResponseDto> getDetalhes (@AuthenticationPrincipal JwtUserDetails userDetails) {
        Cliente cliente = clienteService.buscarPorUsuarioId(userDetails.getId());
        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }   

}
