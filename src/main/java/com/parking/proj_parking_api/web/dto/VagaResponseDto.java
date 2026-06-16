package com.parking.proj_parking_api.web.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VagaResponseDto {

    private Long id;

    private String codigo;

    private String status;

}
