package com.parking.proj_parking_api.web.dto.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import com.parking.proj_parking_api.web.dto.PageableDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor (access = AccessLevel.PRIVATE)
public class PageAbleMapper<T> {

    @SuppressWarnings("unchecked")  //Esconde o aviso de Warnings
    public static <T> PageableDto<T> toDto(Page<T> page) {
        return new ModelMapper().map(page, PageableDto.class);
    }
}

