package com.parking.proj_parking_api.web.dto;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
public class PageableDto {

    private List content = new ArrayList<>();

    private boolean first;

    private boolean last;

    @JsonProperty("page")   // muda o nome 'number' para 'page'.
    private int number;

    private int size;

    @JsonProperty("pageElements")  // muda o nome 'numberOfElements' para 'pageElements'.
    private int numberOfElements;
    
    private int totalPages;

    private int totalElements;
}
