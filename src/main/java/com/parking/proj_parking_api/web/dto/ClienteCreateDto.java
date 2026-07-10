package com.parking.proj_parking_api.web.dto;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.*;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteCreateDto {

@NotBlank(message = "{NotBlank.clienteCreateDto.nome}")
@Size(min = 5, max = 100, message = "{Size.clienteCreateDto.nome}")
private String nome;

@NotBlank(message = "{NotBlank.clienteCreateDto.cpf}")
@Size(min = 11, max = 11, message = "{Size.clienteCreateDto.cpf}")
@CPF(message = "{CPF.clienteCreateDto.cpf}")    //CPF tanto com 11 como CPF com 14. O que restringiu a 11 caracteres foi a anotação Size. 
private String cpf;



}
