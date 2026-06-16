package com.parking.proj_parking_api.web.dto.mapper;

import org.modelmapper.ModelMapper;

import com.parking.proj_parking_api.entity.Vaga;
import com.parking.proj_parking_api.web.dto.VagaCreateDto;
import com.parking.proj_parking_api.web.dto.VagaResponseDto;

import lombok.*;

@NoArgsConstructor (access = AccessLevel.PRIVATE)
public class VagaMapper {

    public static Vaga toVaga (VagaCreateDto dto) {
        return new ModelMapper().map(dto, Vaga.class);
    }

    public static VagaResponseDto toDto (Vaga vaga) {
        return new ModelMapper().map(vaga, VagaResponseDto.class);
    }
}
