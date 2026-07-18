package com.parking.proj_parking_api.web.dto;

import jakarta.validation.constraints.Email;
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

public class UsuarioLoginDto {

    @NotBlank(message = "{NotBlank.usuarioLoginDto.username}")      //Validação (não nulo, pelo menos caractere).
    @Email(message = "{Email.usuarioLoginDto.username}", regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    private String username;

    @NotBlank(message = "{NotBlank.usuarioLoginDto.password}")
    @Size(min = 6, max = 6, message = "{Size.usuarioLoginDto.password}")         //Validação.
    private String password;

}
