package com.parking.proj_parking_api.web.dto;

import java.util.List;

import com.parking.proj_parking_api.repository.projection.ClienteVagaProjection;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

public class PageableClienteVagaDto extends PageableDto<ClienteVagaProjection>{

    @ArraySchema(schema = @Schema(implementation = ClienteVagaProjection.class))
    @Override
    public List<?> getContent() {
        
        return super.getContent();
    }    
}
