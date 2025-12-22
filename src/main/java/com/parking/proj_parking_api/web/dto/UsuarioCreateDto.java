package com.parking.proj_parking_api.web.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class UsuarioCreateDto {

@NotBlank                       //Validação (não nulo, pelo menos caractere).
//@Email(regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$", message = "formato de e-mail está inválido") //Validação. 
@Email(message = "formato de e-mail está inválido")
private String username;

@NotBlank
@Size(min = 6, max = 6)         //Validação.
private String password;

}
