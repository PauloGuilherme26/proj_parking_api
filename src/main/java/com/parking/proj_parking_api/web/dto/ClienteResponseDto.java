package com.parking.proj_parking_api.web.dto;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ClienteResponseDto {

private Long id;
private String nome;
private String cpf;


}
