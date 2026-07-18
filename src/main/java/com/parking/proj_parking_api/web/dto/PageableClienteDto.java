package com.parking.proj_parking_api.web.dto;

import java.util.List;
import com.parking.proj_parking_api.repository.projection.ClienteProjection;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

public class PageableClienteDto extends PageableDto<ClienteProjection>{

    @ArraySchema(schema = @Schema(implementation = ClienteProjection.class))
    @Override
    public List<?> getContent() {
        
        return super.getContent();
    }
}
