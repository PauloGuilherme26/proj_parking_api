package com.parking.proj_parking_api.web.controller;

import com.parking.proj_parking_api.entity.Usuario;
import com.parking.proj_parking_api.service.UsuarioService;
import com.parking.proj_parking_api.web.dto.UsuarioCreateDto;
import com.parking.proj_parking_api.web.dto.UsuarioResponseDto;
import com.parking.proj_parking_api.web.dto.UsuarioSenhaDto;
import com.parking.proj_parking_api.web.dto.mapper.UsuarioMapper;
import com.parking.proj_parking_api.web.exception.ErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Usuarios", description = "Contem todas as operações relativas aos recursos para cadastro, edição e leitura de um usuário.")
@RestController
@RequestMapping("api/v1/usuarios")  //api/v1/usuarios - é uma URI: Identificador/Endereço para localizar o recurso.
@RequiredArgsConstructor            //Injeção de dependências do UsuárioService "via construtor"
public class UsuarioController {

    private final UsuarioService usuarioService; // - Ponto de Injeção de dependências "via construtor" (Agregação)
    // Funciona como se fosse atributo/variável usuarioService do tipo classe UsuarioService. 
    // Tem a finalidade de utilizar os recursos da classe UsuarioService.
    
    @Operation(summary = "Criar um novo usuário.", description = "Recurso para criar um novo usuário.",
        responses = {
            @ApiResponse (responseCode = "201", description = "Recurso criado com sucesso",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse (responseCode = "409", description = "Usuário e-mail já cadastrado no sistema",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
             @ApiResponse (responseCode = "422", description = "Recurso não processado por dados de entrada inválidos",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
        }
    )
    @PostMapping            // Criar um usuário.
    public ResponseEntity<UsuarioResponseDto> create (@Valid @RequestBody UsuarioCreateDto createDto) {
        Usuario user = usuarioService.salvar(UsuarioMapper.toUsuario(createDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
    }                                               //@Valid - Deve ser validado!

    @Operation(summary = "Recuperar um usuário pelo Id.", description = "Recuperar um usuário pelo Id.",
        responses = {
            @ApiResponse (responseCode = "200", description = "Recurso recuperado com sucesso!",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
            @ApiResponse (responseCode = "404", description = "Recurso não encontrado!",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
        }
    )
    @GetMapping("/{id}")    // Buscar usuário pelo Id.
    @PreAuthorize("hasRole('ADMIN')")                                             //Permissão de autorização do perfil Admin
    public ResponseEntity<UsuarioResponseDto> getById (@PathVariable long id) {
        Usuario user = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(UsuarioMapper.toDto(user));
    }   

    @Operation(summary = "Atualização de senha.", description = "Atualização de senha.",
        responses = {
            @ApiResponse (responseCode = "204", description = "Senha atualizada com sucesso!",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
            @ApiResponse (responseCode = "400", description = "Senha não confere!",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse (responseCode = "404", description = "Recurso não encontrado!",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse (responseCode = "422", description = "Campos inválidos ou mal formatados!",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))    
                
        }
    )
    //ResponseEntity<UsuarioResponseDto> - Corpo de resposta (atributos da classe UsuarioResponseDto) apos execução do método.
            //id
            //username
            //role
    //@RequestBody UsuarioSenhaDto - Corpo de envio (atributos da classe UsuarioSenhaDto) antes da execução do método.
            //senhaAtual
            //novaSenha
            //confirmaSenha

    @PatchMapping("/{id}")  // Alteração de Senha do usuário.   //@PathVariable - Torna o "Id" um valor variável.
    //public ResponseEntity <UsuarioResponseDto> updatePassord (@PathVariable long id, @Valid @RequestBody UsuarioSenhaDto dto) {
    public ResponseEntity      <Void>        updatePassord (@PathVariable long id, @Valid @RequestBody UsuarioSenhaDto dto) {
            Usuario user = usuarioService.editarSenha (
                id, 
                dto.getSenhaAtual(),
                dto.getNovaSenha(),             //Existem duas opções para o retorno: Status 200 ou Status 204(sem reorno)
                dto.getConfirmaSenha());

        //return ResponseEntity.ok(UsuarioMapper.toDto(user));  // Instrução de retorno - Status 200 Ok
        return ResponseEntity.noContent().build();              // Instrução de retorno - Status 204 No Content
    }   

    @Operation(summary = "Listagem de todos os usuários.", description = "Listagem de todos os usuários.",
        responses = {
            @ApiResponse (responseCode = "200", description = "Listagem gerada com sucesso!",
                content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDto.class))))              
        }
    )
    @GetMapping            // Listar todos os usuário.
    public ResponseEntity<List<UsuarioResponseDto>> getALL () {
        List<Usuario> users = usuarioService.buscarTodos();
        return ResponseEntity.ok(UsuarioMapper.toListDto(users));
    }
}
