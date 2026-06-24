package com.parking.proj_parking_api.web.dto.mapper;

import org.modelmapper.ModelMapper;
import com.parking.proj_parking_api.entity.ClienteVaga;
import com.parking.proj_parking_api.web.dto.EstacionamentoCreateDto;
import com.parking.proj_parking_api.web.dto.EstacionamentoResponseDto;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteVagaMapper {

    public static ClienteVaga toClienteVaga (EstacionamentoCreateDto dto){
        return new ModelMapper().map(dto, ClienteVaga.class);
    }
    
    public static EstacionamentoResponseDto toDto (ClienteVaga clienteVaga){
        return new ModelMapper().map(clienteVaga, EstacionamentoResponseDto.class);
    }
}
