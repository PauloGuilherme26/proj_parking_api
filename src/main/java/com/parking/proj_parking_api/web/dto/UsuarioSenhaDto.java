package com.parking.proj_parking_api.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class UsuarioSenhaDto {

@NotBlank
@Size(min = 6, max = 6)
private String senhaAtual;

@NotBlank
@Size(min = 6, max = 6)
private String novaSenha;

@NotBlank
@Size(min = 6, max = 6)
private String confirmaSenha;


}
