package com.parking.proj_parking_api.web.dto.mapper;

import org.modelmapper.ModelMapper;

import com.parking.proj_parking_api.entity.Cliente;
import com.parking.proj_parking_api.web.dto.ClienteCreateDto;
import com.parking.proj_parking_api.web.dto.ClienteResponseDto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteMapper {

public static Cliente toCliente(ClienteCreateDto dto)  {
        return new ModelMapper().map(dto, Cliente.class); //dto-Origem e Cliente.class-Destino
    }

public static ClienteResponseDto toDto(Cliente cliente)  {
        return new ModelMapper().map(cliente, ClienteResponseDto.class); 
    }


}
