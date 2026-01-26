package com.parking.proj_parking_api.web.dto;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteCreateDto {

@NotBlank
@Size(min = 5, max = 100)
private String nome;

@NotBlank
@Size(min = 11, max = 11)
@CPF                        //CPF tanto com 11 como CPF com 14. O que restringiu a 11 caracteres foi a anotação Size. 
private String cpf;



}
