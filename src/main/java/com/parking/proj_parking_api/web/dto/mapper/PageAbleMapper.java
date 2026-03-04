package com.parking.proj_parking_api.web.dto.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import com.parking.proj_parking_api.web.dto.PageableDto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor (access = AccessLevel.PRIVATE)
public class PageAbleMapper {

    public static PageableDto toDto(Page page) {
        return new ModelMapper().map(page, PageableDto.class);
    }

}
